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
        OperationDb operations = new OperationDb(dataSource);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            ObjectMapper mapper = new ObjectMapper();

            while (!Thread.currentThread().isInterrupted()) {

                GetResponse response = channel.basicGet(queue, true);
                if (response != null) {
                    Envelope envelope = response.getEnvelope();

                    if ("save".equals(envelope.getRoutingKey())) {
                        String objectJson = new String(response.getBody());
                        Person person = mapper.readValue(objectJson, Person.class);
                        operations.save(person);
                    } else if ("delete".equals(envelope.getRoutingKey())) {
                        Long id = Long.parseLong(new String(response.getBody()));
                        operations.delete(id);
                    }
                }

            }
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
