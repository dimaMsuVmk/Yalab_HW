package messagefilter;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
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
    public SaveApi() {
    }
    private Channel getChannelInput(Connection connection) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exhangeName, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(queueIn, true, false, false, null);
        channel.queueBind(queueIn, exhangeName, "in");
        return channel;
    }
    private Channel getChannelOutput(Connection connection) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exhangeName, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(queueOut, true, false, false, null);
        channel.queueBind(queueOut, exhangeName, "out");
        return channel;
    }

    public void saveInput(String line)  {
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = getChannelInput(connection)) {
            channel.basicPublish(exhangeName,"in",null,line.getBytes(StandardCharsets.UTF_8));

        } catch (IOException | TimeoutException e) {
            System.out.println(e.getMessage());
        }
    }
    public void saveOutput(String line)  {
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = getChannelOutput(connection)) {
            channel.basicPublish(exhangeName,"out",null,line.getBytes(StandardCharsets.UTF_8));

        } catch (IOException | TimeoutException e) {
            System.out.println(e.getMessage());
        }
    }
}
