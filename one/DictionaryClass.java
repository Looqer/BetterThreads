package one;

import java.util.ArrayList;
import java.util.List;

public class DictionaryClass extends Thread {

    List<List<String>> myDictionary = new ArrayList<>();

    @Override
    public void run() {


        while (true) {

            System.out.println(myDictionary);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
