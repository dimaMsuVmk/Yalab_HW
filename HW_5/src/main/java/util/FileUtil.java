package util;

import messagefilter.DataProcessor;

import java.io.*;

public class FileUtil {
    public static void main(String[] args) {
        transfer("filter.txt","fileOfWordsFilter.txt");
        //correctWord("Fuck");
    }

    public static void transfer(String fromFile,String toFile) {
        try(BufferedReader fromReader = new BufferedReader(new FileReader(fromFile));
            BufferedWriter toWriter = new BufferedWriter(new FileWriter(toFile))){
            String line = null;
            while ((line = fromReader.readLine()) != null){
                String[] arr = line.split(", |,");
                for(String word : arr){
                    toWriter.write(word);
                    toWriter.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static String correctWord(String line){
//        String regex = "[(,.!?()-:;'\" )]+";
//        String regexPunctuations = "[^(,.!?()-:;'\" )]+";
//        String[] arrWord = line.split(regex);
//        String[] arrPunctuations = line.split(regexPunctuations);
//        for(String word : arrPunctuations){
//            System.out.println(word);
//        }
//        System.out.println("-----");
//        for(String word : arrWord){
//            System.out.println(word);
//        }
//        System.out.println("-----");
//        StringBuilder sb = new StringBuilder("");
//        //если line начинается НЕ с буквеного символа
//        if(arrWord[0].equals("")) {
//            for (int i = 0; i < arrPunctuations.length; i++) {
//                sb.append(arrPunctuations[i]);
//                if (i + 1 < arrWord.length) sb.append(arrWord[i + 1]);
//            }
//        } //если line начинается с БУКВЕНОГО символа
//        else {
//            for (int i = 0; i < arrWord.length; i++) {
//                sb.append(arrWord[i]);
//                if(i + 1 < arrPunctuations.length) sb.append(arrPunctuations[i + 1]);
//            }
//        }
//
//        System.out.println(sb);
//
//        return line;
//    }
}
