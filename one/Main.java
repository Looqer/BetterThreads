package one;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        File folder = new File("D:\\Projects\\workspace_java\\Threads\\textfiles");

        DictionaryClass tojest = new DictionaryClass();
        tojest.start();

        ExecutorService filesExecutor = Executors.newFixedThreadPool(2);
        filesExecutor.submit(()-> {
                    while (true) {
                        tojest.myDictionary.add(newFiles(folder));
                        System.out.println(Thread.currentThread().getName());
                        Thread.sleep(4000);
                    }
                }
        );
    }

    private static List<String> newFiles(File folder){
        ArrayList<String> newFileList = new ArrayList<String>();

        for (File fileEntry : folder.listFiles())
            if(fileEntry.getName().endsWith(".txt")){
                newFileList.add(fileEntry.getName());
            }

        return newFileList;
    }
}
