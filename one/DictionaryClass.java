package one;

import java.util.*;

public class DictionaryClass extends Thread {

    List<Word> myDictionary = new ArrayList<>();
    int duplicaterate, onlyinonefile, frequency;

    @Override
    synchronized public void run() {


        while (true) {


            for(Word everyword : myDictionary) System.out.println(everyword.sourceFile + " : " + everyword.wordvalue);

            System.out.println("Slow w slowniku: " + myDictionary.size());

            Set<String> hashsetFiles = new HashSet<String>();
            Set<String> hashsetUnique = new HashSet<String>();
            Set<String> hashsetNUnique = new HashSet<String>();
            Word[] frequencyRank = new Word[5];

            for(int i=0; i< 5; i++){
                frequencyRank[i] = new Word();
                frequencyRank[i].wordvalue = "t";
                frequencyRank[i].sourceFile = "t";
                frequencyRank[i].globaloccurences = 0;
            }

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

                wordex.globaloccurences = frequency;

                if(!(frequencyRank[0].wordvalue.equals(wordex.wordvalue) || frequencyRank[1].wordvalue.equals(wordex.wordvalue) || frequencyRank[2].wordvalue.equals(wordex.wordvalue) || frequencyRank[3].wordvalue.equals(wordex.wordvalue) || frequencyRank[4].wordvalue.equals(wordex.wordvalue))){

                    if ((wordex.globaloccurences > frequencyRank[0].globaloccurences)) {
                        frequencyRank[0] = wordex;
                    }
                    for (int i = 0; i < 4; i++) {
                        if (frequencyRank[i].globaloccurences > frequencyRank[i + 1].globaloccurences) {
                            Word temp;
                            temp = frequencyRank[i + 1];
                            frequencyRank[i + 1] = frequencyRank[i];
                            frequencyRank[i] = temp;
                        }
                    }
                }
            }

            System.out.println("rankng 5 " + frequencyRank[0].wordvalue + " " + frequencyRank[0].globaloccurences);
            System.out.println("rankng 4 " + frequencyRank[1].wordvalue + " " + frequencyRank[1].globaloccurences);
            System.out.println("rankng 3 " + frequencyRank[2].wordvalue + " " + frequencyRank[2].globaloccurences);
            System.out.println("rankng 2 " + frequencyRank[3].wordvalue + " " + frequencyRank[3].globaloccurences);
            System.out.println("rankng 1 " + frequencyRank[4].wordvalue + " " + frequencyRank[4].globaloccurences);

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