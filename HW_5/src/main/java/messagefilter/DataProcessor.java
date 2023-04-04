package messagefilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Component
public class DataProcessor {
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private SaveApi saveApi;
    private String queueIn = "input";
    private String queueOut = "output";
    private final String exhangeName = "exc";

    public void processQueue(){
        try (Connection connection = connectionFactory.newConnection();
             Channel channelInput = getChannelInput(connection);
             Channel channelOutput = getChannelOutput(connection)) {
            ObjectMapper mapper = new ObjectMapper();

            while (!Thread.currentThread().isInterrupted()) {

                GetResponse responseInput = channelInput.basicGet(queueIn, true);
                if (responseInput != null) {
                    Envelope envelope = responseInput.getEnvelope();
                    if ("in".equals(envelope.getRoutingKey())) {
                        String line = new String(responseInput.getBody());
                        System.out.println("Получено из очереди input: " + line);
                        //обработка слова
                        String newLine = correctLine(line);
                        saveApi.saveOutput(newLine);
                    }
                }
                ////////////////////////////////////////////////
                GetResponse responseOutput = channelOutput.basicGet(queueOut, true);
                if (responseOutput != null) {
                    Envelope envelope = responseOutput.getEnvelope();
                    if ("out".equals(envelope.getRoutingKey())) {
                        String line = new String(responseOutput.getBody());
                        System.out.println("Отправлено в очередь output: " + line);
                    }
                }
            }

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private String correctLine(String line){
        String regex = "[(,.!?()-:;'\" )]+";
        String regexPunctuations = "[^(,.!?()-:;'\" )]+";
        String[] arrWord = line.split(regex);
        String[] arrPunctuations = line.split(regexPunctuations);
        //проверяем слова из БД
        for (int i = 0; i < arrWord.length; i++) {
            //цензурим слово
            if(isExistInDb(arrWord[i])) arrWord[i] = replaceWord(arrWord[i]);
        }
        //составляем новую строку с ЦЕНЗУРОЙ
        StringBuilder sb = new StringBuilder("");
        //если line начинается НЕ с буквеного символа
        if(arrWord[0].equals("")) {
            for (int i = 0; i < arrPunctuations.length; i++) {
                sb.append(arrPunctuations[i]);
                if (i + 1 < arrWord.length) sb.append(arrWord[i + 1]);
            }
        } //если line начинается с БУКВЕНОГО символа
        else {
            for (int i = 0; i < arrWord.length; i++) {
                sb.append(arrWord[i]);
                if(i + 1 < arrPunctuations.length) sb.append(arrPunctuations[i + 1]);
            }
        }
        return sb.toString();
    }

    private boolean isExistInDb(String word){
        boolean isExist = false;
        String select = "SELECT * FROM filtertable WHERE word = ?";
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select)) {
            preparedStatement.setString(1,word.toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            isExist = resultSet.next();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    private String replaceWord(String word){
        if(word.length() <= 2) return word;
        char[] arr = word.toCharArray();
        for (int i = 1; i < arr.length - 1; i++) {
            arr[i] = '*';
        }
        return new String(arr);
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
}
