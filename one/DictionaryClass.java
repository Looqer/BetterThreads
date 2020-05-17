package one;

import java.util.*;

public class DictionaryClass extends Thread {

    List<Word> myDictionary = new ArrayList<>();
    int duplicaterate, onlyinonefile, frequency;

    @Override
    public void run() {


        while (true) {


            for(Word everyword : myDictionary) System.out.println(everyword.sourceFile + " : " + everyword.wordvalue);

            System.out.println("Slow w slowniku: " + myDictionary.size());

            Set<String> hashsetFiles = new HashSet<String>();
            Set<String> hashsetUnique = new HashSet<String>();
            Set<String> hashsetNUnique = new HashSet<String>();
            Word[] frequencyRank = new Word[5];

            duplicaterate = 0;
            onlyinonefile = 0;
            frequency = 0;
            for(Word wordex : myDictionary){
                duplicaterate = frequency = 0;
                hashsetFiles.add(wordex.sourceFile);
                //System.out.println("ogarniamy slowo " + wordex.wordvalue + " z " + wordex.sourceFile);
                for (Word wordexa : myDictionary){
                    //System.out.println("ze slowem " + wordexa.wordvalue + " z " + wordexa.sourceFile);
                    if(wordex.wordvalue.equals(wordexa.wordvalue)){
                        if(!wordex.sourceFile.equals(wordexa.sourceFile)){
                            duplicaterate++;
                        }
                        frequency++;
                    }
                }
                if(duplicaterate == 0){
                    hashsetUnique.add(wordex.wordvalue);
                }
                else{
                    hashsetNUnique.add(wordex.wordvalue);
                }

                wordex.occurences = frequency;
                if ((frequencyRank.equals(null))){
                    frequencyRank[0] = wordex;
                }

                if ((wordex.occurences > frequencyRank[0].occurences)){
                    frequencyRank[0] = wordex;
                }
                for(int i = 0; i < 4; i++){
                    if (frequencyRank[i].occurences > frequencyRank[i+1].occurences){
                        frequencyRank[i+1] = frequencyRank[i];
                    }
                }
            }

            System.out.println("rankng " + Arrays.toString(frequencyRank));

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