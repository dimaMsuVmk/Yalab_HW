package eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DataProcessor {
    @Autowired
    private OperationDb operations;
    @Autowired
    private ConnectionFactory connectionFactory;
    private final String queue = "queue";
    private final String exhangeName = "exc";

    public void process() throws Exception {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = getChannel(connection)) {
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

