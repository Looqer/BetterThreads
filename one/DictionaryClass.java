package one;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DictionaryClass extends Thread {

    List<Word> myDictionary = new ArrayList<>();

    @Override
    public void run() {


        while (true) {


            int duplicaterate = 0, onlyinonefile = 0;
            //for(Word everyword : myDictionary) System.out.println(everyword.sourceFile + " : " + everyword.wordvalue);

            System.out.println("Slow w slowniku: " + myDictionary.size());

            Set<String> hashset = new HashSet<String>();
            hashset.clear();
            for(Word wordex : myDictionary){
                duplicaterate = 0;
                onlyinonefile = 0;
                hashset.add(wordex.sourceFile);

                for(Word wordexa : myDictionary){
                    if((wordex.sourceFile != wordexa.sourceFile) & (wordex.wordvalue == wordexa.wordvalue)){
                        duplicaterate++;
                    }
                }
                if (duplicaterate == 0){
                    onlyinonefile++;
                }
            }
            System.out.println("Plikow w slowniku: " +hashset.size());
            System.out.println("Slow co sa tylko w jednym pliku: " + onlyinonefile);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
