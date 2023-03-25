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
        int sizeOfFile = 375_000_000/512;//столько чисел в одном файле
        List<File> listOfFiles = new ArrayList<>();

        int i = 0;// "i+1" чисел уже разместили по мелким файлам
        File fileBuf;
        BufferedWriter bw = null;

        //разместим числа в разных файлах,файлы храним в list
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)))) {
            while (br.ready()) {
                if (i % sizeOfFile == 0) {
                    if (bw != null) {
                        bw.flush();
                        bw.close();
                    }
                    fileBuf = new File("sortFile//f" + partOfName++ + ".txt");
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileBuf)));
                    listOfFiles.add(fileBuf);
                }
                bw.write(Long.parseLong(br.readLine()) + "\n");
                i++;// "i+1" чисел уже считали с большого файла
            }
            bw.flush();
        }
        //сортируем содержимое файлов
        for (int j = 0; j < listOfFiles.size(); j++) {
            //копируем файл в массив
            Long[] arrSort = copyFileToArray(listOfFiles.get(j));
            //сортировка массива
            mergeSort(arrSort, arrSort.length);
            try (BufferedWriter printWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(listOfFiles.get(j))))) {
                for (int k = 0; k < arrSort.length; k++) {
                    printWriter.write(arrSort[k] + "\n");
                }
            }
        }
        //сливаем в один

        return mergeAllFiles(listOfFiles);
    }

    /////////////////////////////////////////////////
    private static File mergeAllFiles(List<File> listOfFiles) throws IOException {
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
////////////////////////////////////////////////////////
//        //Отсортированый рез-т можно увидеть в файле "data\\dataCopy.txt"
//        try(Scanner sc = new Scanner(listOfFiles.get(0));
//            PrintWriter pw = new PrintWriter("data\\dataCopy.txt")){
//            while (sc.hasNext()){
//                pw.println(sc.nextLong());
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//////////////////////////////////////////////////////////////

        return listOfFiles.get(0);
    }

/////////////////////////////////////////////////////////
    private static File mergeTwoFiles(File f1, File f2) throws IOException {
        File out = new File("sortFile//" + partOfName++ + ".txt");
        try (
                BufferedReader br1 = new BufferedReader(new FileReader(f1));
                BufferedReader br2 = new BufferedReader(new FileReader(f2));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out)))) {
            String l1=br1.readLine(),l2=br2.readLine();
            long left=Long.parseLong(l1),right=Long.parseLong(l2);

            while (true) {
                if (left < right) {
                    bw.write(left + "\n");
                    if((l1 = br1.readLine()) == null) {
                        bw.write(Long.parseLong(l2) + "\n");
                        while ((l2 = br2.readLine()) != null) {
                            bw.write(Long.parseLong(l2) + "\n");
                        }
                        break;
                    }
                    left=Long.parseLong(l1);
                } else {
                    bw.write(right + "\n");
                    if((l2 = br2.readLine()) == null) {
                        bw.write(Long.parseLong(l1) + "\n");
                        while ((l1 = br1.readLine()) != null) {
                            bw.write(Long.parseLong(l1) + "\n");
                        }
                        break;
                    }
                    right=Long.parseLong(l2);
                }
            }
            bw.flush();
        }
        return out;
    }
/////////////////////////////////////////////////

    //копируем файл в массив
    private static Long[] copyFileToArray(File dataFile) {
        List<Long> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)))) {
            while (br.ready()) {
                list.add(Long.parseLong(br.readLine()));//
            }
        } catch (IOException e) {
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
            } else {
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


