package one;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static String mypathname = "D:\\Projects\\workspace_java\\Threads\\textfiles";

    public static void main(String[] args) throws InterruptedException {

        File folder = new File(mypathname);
        List<String> newFileList;


        DictionaryClass dictionary = new DictionaryClass();
        dictionary.start();


        while (true) {
            dictionary.myDictionary.clear();
            System.out.println("szukanie");
            newFileList = newFiles(folder);

            System.out.println(newFileList);

            ExecutorService filesExecutor = Executors.newFixedThreadPool(2);

            for (String singleFile : newFileList){

                filesExecutor.submit(() -> {

                    List<String> fileDictionary = fileReader(singleFile);

                    for (String fileword : fileDictionary){
                        Word convertedfileword = Wordgenerator(singleFile, fileword);
                        dictionary.myDictionary.add(convertedfileword);
                    }

                    System.out.println(Thread.currentThread().getName());

                });
            }
            Thread.sleep(4000);
        }

    }

    private static List<String> newFiles(File folder) {
        ArrayList<String> newFileList = new ArrayList<String>();
        //ArrayList<String> fileDictionary = new ArrayList<String>();

        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().endsWith(".txt")) {
                newFileList.add(fileEntry.getName());
            }
            else if (fileEntry.isDirectory()) {
                String subfolder = mypathname + "\\" + fileEntry.getName();
                File foldera = new File(subfolder);
                for (File fileEntrya : foldera.listFiles()) {
                    if (fileEntrya.getName().endsWith(".txt")) {
                        newFileList.add(fileEntry.getName() + "\\" +fileEntrya.getName());
                    }
                }
            }
        }

        return newFileList;
    }

    private static List<String> fileReader(String fileEntry) {
        List<String> fileWordList = null;

           String fullpath = (mypathname + "\\" + fileEntry);

            Scanner s = null;
            try {
                s = new Scanner(new File(fullpath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            fileWordList = new ArrayList<String>();
            while (s.hasNext()) {
                fileWordList.add(s.next());
            }
            s.close();


        return fileWordList;
    }

    private static Word Wordgenerator(String file, String value) {
        Word thisword = new Word();
        thisword.wordvalue = value;
        thisword.sourceFile = file;
        return thisword;
    }
}
