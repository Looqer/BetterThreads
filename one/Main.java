package one;

import javax.xml.xpath.XPath;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.io.IOException;


public class Main {


    static String mypathname = "D:\\Projects\\workspace_java\\Threads\\textfiles";


    public static void main(String[] args) throws InterruptedException, IOException {


        File folder = new File(mypathname);
        List<String> newFileList;

        DictionaryClass dictionary = new DictionaryClass();
        dictionary.start();

        ExecutorService filesExecutor = Executors.newFixedThreadPool(3);


        while (true) {
            //dictionary.myDictionary.clear();
            System.out.println("szukanie");
            newFileList = newFiles(folder, mypathname);

            System.out.println(newFileList);

            for (String singleFile : newFileList){
                filesExecutor.submit(() -> {
                    for( Word allwords : dictionary.myDictionary){
                        if(allwords.sourceFile.equals(singleFile)){
                            dictionary.myDictionary.remove(allwords);
                        }
                    }
                    System.out.println("start watku: ----------------- " + Thread.currentThread().getName());

                    List<String> fileDictionary = fileReader(singleFile);
                    List<Word> thisFileDictionary = new ArrayList<>();
                    List<String> knownWordsArray = new ArrayList<>();
                    List<String> maxKnownWordsArray = new ArrayList<>();
                    int  maxknownratio = 0;
                    boolean known;

                    for (String fileword : fileDictionary){
                        Word convertedfileword = Wordgenerator(singleFile, fileword);
                        thisFileDictionary.add(convertedfileword);
                    }

                    int longestword = 0;
                    for ( Word eachword : thisFileDictionary){

                        if (eachword.wordletters > longestword){
                            longestword = eachword.wordletters;
                        }
                        known = false;
                        for( Word knowndictionary : dictionary.myDictionary){

                            if (eachword.wordvalue.equals(knowndictionary.wordvalue)) {
                                known = true;
                            }
                        }
                        if(known == true){
                            knownWordsArray.add(eachword.wordvalue);
                        }
                        else if(known == false){
                            if(knownWordsArray.size() > maxknownratio){
                                maxknownratio = knownWordsArray.size();
                                maxKnownWordsArray.clear();
                                maxKnownWordsArray.addAll(knownWordsArray);
                            }
                            knownWordsArray.clear();
                        }
                    }

                    System.out.println("znane: " + maxKnownWordsArray);

                    for (Word addword : thisFileDictionary){
                        dictionary.myDictionary.add(addword);
                    }

                    int[][] wordlengtharray = new int[2][longestword];

                    for(int i = 0 ; i < longestword; i++ ){
                        wordlengtharray[0][i] = i+1;
                    }

                    Word[] fileFrequencyRank = new Word[5];

                    for(int i=0; i< 5; i++){
                        fileFrequencyRank[i] = new Word();
                        fileFrequencyRank[i].wordvalue = "";
                        fileFrequencyRank[i].sourceFile = "";
                        fileFrequencyRank[i].fileoccurences = 0;
                    }


                    for ( Word word : thisFileDictionary){
                        //System.out.println("ogarniamy slowo " + word.wordvalue + " z " + word.sourceFile);
                        wordlengtharray[1][word.wordletters-1] = wordlengtharray[1][word.wordletters-1]+1;
                        int frequency = 0;
                        for (Word worda : thisFileDictionary){
                            //System.out.println("ze slowo " + worda.wordvalue + " z " + worda.sourceFile);
                            if (word.wordvalue.equals(worda.wordvalue)){
                                frequency++;
                            }
                        }
                        word.fileoccurences = frequency;

                        if(!(fileFrequencyRank[0].wordvalue.equals(word.wordvalue) || fileFrequencyRank[1].wordvalue.equals(word.wordvalue) || fileFrequencyRank[2].wordvalue.equals(word.wordvalue) || fileFrequencyRank[3].wordvalue.equals(word.wordvalue) || fileFrequencyRank[4].wordvalue.equals(word.wordvalue))) {
                            if ((word.fileoccurences > fileFrequencyRank[0].fileoccurences)) {
                                fileFrequencyRank[0] = word;
                            }
                            for (int i = 0; i < 4; i++) {

                                if (fileFrequencyRank[i].fileoccurences > fileFrequencyRank[i + 1].fileoccurences) {
                                    Word temp;
                                    temp = fileFrequencyRank[i + 1];
                                    fileFrequencyRank[i + 1] = fileFrequencyRank[i];
                                    fileFrequencyRank[i] = temp;
                                }
                            }
                        }
                    }

                    System.out.println("rankong 5 " + fileFrequencyRank[0].wordvalue + " " + fileFrequencyRank[0].fileoccurences);
                    System.out.println("rankong 4 " + fileFrequencyRank[1].wordvalue + " " + fileFrequencyRank[1].fileoccurences);
                    System.out.println("rankong 3 " + fileFrequencyRank[2].wordvalue + " " + fileFrequencyRank[2].fileoccurences);
                    System.out.println("rankong 2 " + fileFrequencyRank[3].wordvalue + " " + fileFrequencyRank[3].fileoccurences);
                    System.out.println("rankong 1 " + fileFrequencyRank[4].wordvalue + " " + fileFrequencyRank[4].fileoccurences);



                    System.out.println(Arrays.toString(wordlengtharray[0]));
                    System.out.println(Arrays.toString(wordlengtharray[1]));

                    System.out.println(Thread.currentThread().getName());
                    System.out.println("koniec watku: ----------------- " + Thread.currentThread().getName());

                });

            }
            Thread.sleep(4000);

        }

    }

    private static List<String> newFiles(File folder, String correctpath) {
        ArrayList<String> newFileList = new ArrayList<String>();
        //ArrayList<String> fileDictionary = new ArrayList<String>();


        for (File fileEntry : folder.listFiles()) {

            if (fileEntry.isDirectory()) {
                String subfolder = mypathname + "\\" + fileEntry.getName();
                File subfolderfile = new File(subfolder);
                newFileList.addAll(newFiles(subfolderfile, subfolder));
            }

            else if ((System.currentTimeMillis() - (fileEntry.lastModified())) <= 4000) {
                if (fileEntry.getName().endsWith(".txt")) {
                    if (fileEntry.getName().equals("koniec.txt")){
                        System.exit(0);
                    }
                    System.out.println("d: " + fileEntry.getName());
                    newFileList.add(correctpath + "\\" + fileEntry.getName());
                }
            }


        }
        return newFileList;
    }

    private static List<String> fileReader(String fileEntry) {
        List<String> fileWordList = null;

            Scanner s = null;
            try {
                s = new Scanner(new File(fileEntry));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            fileWordList = new ArrayList<>();
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
        thisword.wordletters = thisword.wordvalue.length();
        return thisword;
    }

    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(fis);
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            fis.close();
        }
        return prop;
    }
}
