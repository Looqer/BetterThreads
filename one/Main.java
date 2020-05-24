package one;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;


public class Main {


    static String mypathname;
    static int mainthreadsleeptime, threadspool;


    public static void main(String[] args) throws InterruptedException, IOException {

        Reader readerprop = new FileReader("src\\one\\config.properties");
        Properties Props = new Properties();
        Props.load(readerprop);
        mypathname = Props.getProperty("filespath");
        mainthreadsleeptime = Integer.parseInt(Props.getProperty("mainthreadsleeptime"));
        threadspool = Integer.parseInt(Props.getProperty("threadspool"));
        File folder = new File(mypathname);
        List<String> newFileList;


        DictionaryClass dictionary = new DictionaryClass();
        dictionary.start(); //start watku slownika

        //ograniczenie ilosci watkow dla obslugi plikow
        ExecutorService filesExecutor = Executors.newFixedThreadPool(threadspool);


        while (true) {

            newFileList = newFiles(folder, mypathname);

            for (String singleFile : newFileList){
                filesExecutor.submit(() -> {
                    for( Word allwords : dictionary.myDictionary){
                        if(allwords.sourceFile.equals(singleFile)){
                            dictionary.myDictionary.remove(allwords);
                        }
                    }

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
                    System.out.println(singleFile + "--> Najdluzszy ciag znanych wyrazow: " + maxKnownWordsArray);

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
                        wordlengtharray[1][word.wordletters-1] = wordlengtharray[1][word.wordletters-1]+1;
                        int frequency = 0;
                        for (Word worda : thisFileDictionary){
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

                    System.out.println(singleFile + "--> Top 5 najczestszych wyrazow");
                    System.out.println("1." + fileFrequencyRank[4].wordvalue + " 2." + fileFrequencyRank[3].wordvalue + " 3." + fileFrequencyRank[2].wordvalue+ " 4." + fileFrequencyRank[1].wordvalue+ " 5." + fileFrequencyRank[0].wordvalue);
                    System.out.println(singleFile + "--> Statystyka dlugosci wyrazow: ");
                    for( int j = 0; j < wordlengtharray[0].length; j++){
                        System.out.println(wordlengtharray[0][j] + "-literowych: " + wordlengtharray[1][j]);
                    }
                });
            }
            Thread.sleep(mainthreadsleeptime);
        }
    }

    private static List<String> newFiles(File folder, String correctpath) {
        ArrayList<String> newFileList = new ArrayList<String>();

        for (File fileEntry : folder.listFiles()) {

            if (fileEntry.isDirectory()) {
                String subfolder = mypathname + "\\" + fileEntry.getName();
                File subfolderfile = new File(subfolder);
                newFileList.addAll(newFiles(subfolderfile, subfolder));
            }

            else if ((System.currentTimeMillis() - (fileEntry.lastModified())) <= mainthreadsleeptime) {
                if (fileEntry.getName().endsWith(".txt")) {
                    if (fileEntry.getName().equals("koniec.txt")){
                        System.exit(0);
                    }
                    newFileList.add(correctpath + "\\" + fileEntry.getName());
                }
            }
        }
        return newFileList;
    }

    private static List<String> fileReader(String fileEntry) {
        List<String> fileWordList;

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
}
