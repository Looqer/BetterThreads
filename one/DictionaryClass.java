package one;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DictionaryClass extends Thread {

    List<Word> myDictionary = new ArrayList<>();
    int duplicaterate, onlyinonefile;

    @Override
    public void run() {


        while (true) {


            for(Word everyword : myDictionary) System.out.println(everyword.sourceFile + " : " + everyword.wordvalue);

            System.out.println("Slow w slowniku: " + myDictionary.size());

            Set<String> hashsetFiles = new HashSet<String>();
            Set<String> hashsetUnique = new HashSet<String>();
            Set<String> hashsetNUnique = new HashSet<String>();

            duplicaterate = 0;
            onlyinonefile = 0;
            for(Word wordex : myDictionary){
                duplicaterate = 0;
                hashsetFiles.add(wordex.sourceFile);
                //System.out.println("ogarniamy slowo " + wordex.wordvalue + " z " + wordex.sourceFile);
                for (Word wordexa : myDictionary){
                    //System.out.println("ze slowem " + wordexa.wordvalue + " z " + wordexa.sourceFile);
                    if(!wordex.sourceFile.equals(wordexa.sourceFile)){
                        if(wordex.wordvalue.equals(wordexa.wordvalue)){
                            duplicaterate++;
                        }
                    }
                }
                if(duplicaterate == 0){
                    hashsetUnique.add(wordex.wordvalue);
                }
                else{
                    hashsetNUnique.add(wordex.wordvalue);
                }
            }
            System.out.println("Plikow w slowniku: " + hashsetFiles.size());
            hashsetFiles.clear();
            System.out.println("Slow co sa tylko w jednym pliku: " + hashsetUnique.size());
            System.out.println("Slow co sa w wiecej niz jednym pliku: " + hashsetNUnique.size());

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
