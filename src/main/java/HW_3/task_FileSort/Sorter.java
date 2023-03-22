package HW_3.task_FileSort;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sorter {
    private static int partOfName;

    public Sorter() {
        partOfName = 1;
    }

    public static File sortFile(File dataFile) throws IOException {
        //колличество чисел в файле
        //375_000_000 чисел будем хранить в 512 файлах
        int sizeOfFile = 375_000_000 / 512;
        List<File> listOfFiles = new ArrayList<>();

        int i = 0;// "i+1" чисел уже разместили по мелким файлам
        File fileBuf;
        PrintWriter pw = null;

        //разместим числа в разных файлах,файлы храним в list
        try(Scanner sc = new Scanner(new FileInputStream(dataFile))) {
            while (sc.hasNext()) {
                if (i % sizeOfFile == 0) {
                    if(pw != null) {
                        pw.flush();
                        pw.close();
                    }
                    fileBuf = new File("sortFile//f" + partOfName++ + ".txt");
                    pw = new PrintWriter(fileBuf);
                    listOfFiles.add(fileBuf);
                }
                pw.println(sc.nextLong());
                i++;// "i+1" чисел уже считали с большого файла
            }
            pw.flush();
        }
        //сортируем содержимое файлов
        for (int j = 0; j < listOfFiles.size(); j++) {
            //копируем файл в массив
            Long[] arrSort = copyFileToArray(listOfFiles.get(j));
            //сортировка массива
            mergeSort(arrSort,arrSort.length);
            try(PrintWriter printWriter = new PrintWriter(new FileOutputStream(listOfFiles.get(j),false))) {
                for (int k = 0; k < arrSort.length; k++) {
                    printWriter.println(arrSort[k].longValue());
                }
            }
        }
        //сливаем в один

        return mergeAllFiles(listOfFiles);
    }
//////////////////////////////////////////////////
    private static File mergeAllFiles(List<File> listOfFiles){
        while(listOfFiles.size() != 1) {
            List<File> NEW_listOfFiles = new ArrayList<>();
            for (int j = 0; j < listOfFiles.size(); j = j + 2) {
                if (j + 1 < listOfFiles.size()) {
                    NEW_listOfFiles.add(mergeTwoFiles(listOfFiles.get(j), listOfFiles.get(j + 1)));
                } else {
                    NEW_listOfFiles.add(listOfFiles.get(j));
                }
            }
            listOfFiles = NEW_listOfFiles;
        }
        return listOfFiles.get(0);
    }
//////////////////////////////////////////////////
    private static File mergeTwoFiles(File f1,File f2){
        File out = new File("sortFile//" + partOfName++ + ".txt");
        try(Scanner sc1 = new Scanner(f1);
            Scanner sc2 = new Scanner(f2);
            PrintWriter pw = new PrintWriter(out)) {
            long left = sc1.nextLong();
            long right = sc2.nextLong();
            while (sc1.hasNext()&&sc2.hasNext()){
                if(left < right){
                    pw.println(left);
                    left = sc1.nextLong();
                } else {
                    pw.println(right);
                    right = sc2.nextLong();
                }
            }
            if(left < right) pw.println(left); else pw.println(right);

            while (sc1.hasNext()){
                pw.println(sc1.nextLong());
            }
            while (sc2.hasNext()){
                pw.println(sc2.nextLong());
            }
            pw.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return out;
    }
/////////////////////////////////////////////////

    //копируем файл в массив
    private static Long[] copyFileToArray(File dataFile){
        List<Long> list = new ArrayList<>();
        try(Scanner sc = new Scanner(new FileInputStream(dataFile))) {
            while (sc.hasNext()) {
                list.add(sc.nextLong());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Long[] arr = new Long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
///////////////////////////////////////////////
    private static void mergeSort(Long[] a, int n) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        Long[] l = new Long[mid];
        Long[] r = new Long[n - mid];

        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        mergeSort(l, mid);
        mergeSort(r, n - mid);

        merge(a, l, r, mid, n - mid);
    }
    private static void merge(
            Long[] a, Long[] l, Long[] r, int left, int right) {

        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l[i] <= r[j]) {
                a[k++] = l[i++];
            }
            else {
                a[k++] = r[j++];
            }
        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
    }

}
