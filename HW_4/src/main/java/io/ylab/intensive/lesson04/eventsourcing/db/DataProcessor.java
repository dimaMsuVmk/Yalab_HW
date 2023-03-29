package io.ylab.intensive.lesson04.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class DataProcessor {
    private String queue = "queue";
    private String exhangeName = "exc";
    public void process(DataSource dataSource,ConnectionFactory connectionFactory) throws Exception {
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = getChannel(connection)) {

            OperationDb operationDb = new OperationDb(dataSource);
            ObjectMapper mapper = new ObjectMapper();
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        if ("save".equals(envelope.getRoutingKey())) {
                            String objectJson = new String(body, StandardCharsets.UTF_8);
                            Person person = mapper.readValue(objectJson, Person.class);
                            operationDb.save(person);
                        } else if ("delete".equals(envelope.getRoutingKey())) {
                            Long id = Long.parseLong(new String(body, StandardCharsets.UTF_8));
                            operationDb.delete(id);
                        }
                    }catch (Exception e){}
                }
            };
            channel.basicConsume(queue,true,consumer);
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
