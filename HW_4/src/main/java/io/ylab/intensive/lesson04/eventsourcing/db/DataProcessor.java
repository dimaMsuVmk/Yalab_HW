package io.ylab.intensive.lesson04.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class DataProcessor {
    private final String queue = "queue";
    private final String exhangeName = "exc";
    public void process(DataSource dataSource,ConnectionFactory connectionFactory) {
        OperationDb operations = new OperationDb(dataSource);
        //Channel channel = null;
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = getChannel(connection)) {
            ObjectMapper mapper = new ObjectMapper();
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    if ("save".equals(envelope.getRoutingKey())) {
                        String objectJson = new String(body);
                        Person person = mapper.readValue(objectJson, Person.class);
                        try {
                            operations.save(person);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else if ("delete".equals(envelope.getRoutingKey())) {
                        Long id = Long.parseLong(new String(body));
                        try {
                            operations.delete(id);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            };
            channel.basicConsume(queue, true, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private Channel getChannel(Connection connection) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exhangeName, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(queue, true, false, false, null);
        channel.queueBind(queue, exhangeName, "save");
        channel.queueBind(queue, exhangeName, "delete");
        return channel;
    }
}
