package messagefilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordService {
    @Autowired
    private WordRepository repository;

    public String correctLine(String line){
        String regex = "[(,.!?()-:;'\" )]+";
        String regexPunctuations = "[^(,.!?()-:;'\" )]+";
        String[] arrWord = line.split(regex);
        String[] arrPunctuations = line.split(regexPunctuations);
        //проверяем слова из БД
        for (int i = 0; i < arrWord.length; i++) {
            //цензурим слово
            if(repository.isExistInDb(arrWord[i])) arrWord[i] = replaceWord(arrWord[i]);
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

    public String replaceWord(String word){
        if(word.length() <= 2) return word;
        char[] arr = word.toCharArray();
        for (int i = 1; i < arr.length - 1; i++) {
            arr[i] = '*';
        }
        return new String(arr);
    }

}
