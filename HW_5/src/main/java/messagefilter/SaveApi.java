package messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class SaveApi {
    @Autowired
    private ConnectionFactory connectionFactory;
    private String queueIn = "input";
    private String queueOut = "output";
    private String exhangeName = "exc";
    private Channel inputChannel, outputChannel;
    public SaveApi(@Autowired @Qualifier("channelInput") Channel inputChannel,
                   @Autowired @Qualifier("channelOutput") Channel outputChannel) {
        this.inputChannel = inputChannel;
        this.outputChannel = outputChannel;
    }
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

    public void saveInput(String line)  {
        try {
            inputChannel.basicPublish(exhangeName,"in",null,line.getBytes(StandardCharsets.UTF_8));
            Thread.sleep(1000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveOutput(String line)  {
        try {
            outputChannel.basicPublish(exhangeName,"out",null,line.getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
