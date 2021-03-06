package one;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class DictionaryClass extends Thread {

    List<Word> myDictionary = Collections.synchronizedList(new ArrayList<>());
    int duplicaterate, onlyinonefile, frequency, dictionarythreadsleeptime;

    @Override
    synchronized public void run() {
        Reader readerprop = null;
        try {
            readerprop = new FileReader("src\\one\\config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties Props = new Properties();
        try {
            Props.load(readerprop);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dictionarythreadsleeptime = Integer.parseInt(Props.getProperty("dictionarythreadsleeptime"));

        while (true) {
            System.out.println("Slow w slowniku: " + myDictionary.size());

            Set<String> hashsetFiles = new HashSet<String>();
            Set<String> hashsetUnique = new HashSet<String>();
            Set<String> hashsetNUnique = new HashSet<String>();
            Word[] frequencyRank = new Word[5];

            for(int i=0; i< 5; i++){
                frequencyRank[i] = new Word();
                frequencyRank[i].wordvalue = "";
                frequencyRank[i].sourceFile = "";
                frequencyRank[i].globaloccurences = 0;
            }

            duplicaterate = 0;
            onlyinonefile = 0;
            frequency = 0;
            for(Word wordex : myDictionary){
                duplicaterate = frequency = 0;
                hashsetFiles.add(wordex.sourceFile);
                for (Word wordexa : myDictionary){
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
            System.out.println("Top 5 najczestszych wyrazow w calym slowniku: ");
            System.out.println("1." + frequencyRank[4].wordvalue + " 2." + frequencyRank[3].wordvalue + " 3." + frequencyRank[2].wordvalue+ " 4." + frequencyRank[1].wordvalue+ " 5." + frequencyRank[0].wordvalue);

            System.out.println("Plikow w slowniku: " + hashsetFiles.size());
            hashsetFiles.clear();
            System.out.println("Slow ktore sa tylko w jednym pliku: " + hashsetUnique.size());
            System.out.println("Slow ktore sa w wiecej niz jednym pliku: " + hashsetNUnique.size());

            try {
                Thread.sleep(dictionarythreadsleeptime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}