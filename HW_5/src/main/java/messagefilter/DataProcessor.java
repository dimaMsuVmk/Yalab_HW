package messagefilter;

import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class DataProcessor {
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private SaveApi saveApi;
    @Autowired
    private WordService wordService;
    private String queueIn = "input";
    private String queueOut = "output";
    private final String exhangeName = "exc";
    private Channel inputChannel, outputChannel;
    public DataProcessor(@Autowired @Qualifier("channelInput") Channel inputChannel,
                         @Autowired @Qualifier("channelOutput") Channel outputChannel){
        this.inputChannel = inputChannel;
        this.outputChannel = outputChannel;
    }

    public void inputConsume(){
        try {
            Consumer consumer = new DefaultConsumer(inputChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String line = new String(body, StandardCharsets.UTF_8);
                    if ("in".equals(envelope.getRoutingKey())) {
                        System.out.println("Получено из очереди input: " + line);
                        //обработка слова
                        String newLine = wordService.correctLine(line);
                        saveApi.saveOutput(newLine);
                    }
                }
            };
            inputChannel.basicConsume(queueIn, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void outputConsume(){
        try {
            Consumer consumer = new DefaultConsumer(outputChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String line = new String(body, StandardCharsets.UTF_8);
                    if ("out".equals(envelope.getRoutingKey())) {
                        System.out.println("Отправлено в очередь output: " + line);
                    }
                }
            };
            outputChannel.basicConsume(queueOut, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void processQueue(){
//        try (Connection connection = connectionFactory.newConnection();
//             Channel channelInput = getChannelInput(connection);
//             Channel channelOutput = getChannelOutput(connection)) {
//
//            while (!Thread.currentThread().isInterrupted()) {
//
//                GetResponse responseInput = channelInput.basicGet(queueIn, true);
//                if (responseInput != null) {
//                    Envelope envelope = responseInput.getEnvelope();
//                    if ("in".equals(envelope.getRoutingKey())) {
//                        String line = new String(responseInput.getBody());
//                        System.out.println("Получено из очереди input: " + line);
//                        //обработка слова
//                        String newLine = wordService.correctLine(line);
//                        saveApi.saveOutput(newLine);
//                    }
//                }
//                ////////////////////////////////////////////////
//                GetResponse responseOutput = channelOutput.basicGet(queueOut, true);
//                if (responseOutput != null) {
//                    Envelope envelope = responseOutput.getEnvelope();
//                    if ("out".equals(envelope.getRoutingKey())) {
//                        String line = new String(responseOutput.getBody());
//                        System.out.println("Отправлено в очередь output: " + line);
//                    }
//                }
//            }
//
//        } catch (IOException | TimeoutException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private Channel getChannelInput(Connection connection) throws IOException {
//        Channel channel = connection.createChannel();
//        channel.exchangeDeclare(exhangeName, BuiltinExchangeType.TOPIC);
//        channel.queueDeclare(queueIn, true, false, false, null);
//        channel.queueBind(queueIn, exhangeName, "in");
//        return channel;
//    }
//    private Channel getChannelOutput(Connection connection) throws IOException {
//        Channel channel = connection.createChannel();
//        channel.exchangeDeclare(exhangeName, BuiltinExchangeType.TOPIC);
//        channel.queueDeclare(queueOut, true, false, false, null);
//        channel.queueBind(queueOut, exhangeName, "out");
//        return channel;
//    }
}
