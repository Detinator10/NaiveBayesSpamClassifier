// Do not submit with package statements if you are using eclipse.
// Only use what is provided in the standard libraries.

import java.io.*;
import java.util.*;

public class NaiveBayes {

    HashMap<String,Double> spamWords = new HashMap();
    HashMap<String,Double> hamWords = new HashMap();

    double emailIsSpamProb;
    double emailIsHamProb;
    int numOfSpam;
    int numOfHam;

    public void train(File[] hams, File[] spams) throws IOException {
        int index = 0;
        HashSet<String>[] wordsInSpamFile = new HashSet[spams.length];
        HashSet<String>[] wordsInHamFile = new HashSet[hams.length];

        // For each spam file, add its word to the overall dictionary, as well as the set of words for this specific file
        for(File file : spams) {
            Scanner f = new Scanner(file);
            wordsInSpamFile[index] = new HashSet<String>();
            while(f.hasNext()) {
                String word = f.next();
                spamWords.put(word,0.0);
                wordsInSpamFile[index].add(word);
            }
            f.close();
            index++;
        }

        index = 0;
        // For each ham file, add its word to the overall dictionary, as well as the set of words for this specific file
        for(File file : hams) {
            Scanner f = new Scanner(file);
            wordsInHamFile[index] = new HashSet<String>();

            while(f.hasNext()) {
                String word = f.next();
                hamWords.put(word,0.0);
                wordsInHamFile[index].add(word);
            }
            f.close();
            index++;
        }

        // Calculate probability for each word in spamWords set
        for(String spamWord : spamWords.keySet()) {
            //Keep track of number of files the word occurs in
            for (HashSet<String> spamFile : wordsInSpamFile) {
                if (spamFile.contains(spamWord)) {
                    spamWords.put(spamWord, spamWords.get(spamWord) + 1);
                }
            }
            // Adjusts occurrences counter to reflect smoothed probability
            spamWords.put(spamWord, (spamWords.get(spamWord)+1) / (wordsInSpamFile.length + 2));

        }

        // Calculate probability for each word in spamWords set
        for(String hamWord : hamWords.keySet()) {
            //Keep track of number of files the word occurs in
            for (HashSet<String> hamFile : wordsInHamFile) {
                if (hamFile.contains(hamWord)) {
                    hamWords.put(hamWord, hamWords.get(hamWord) +1);
                }
            }
            // Adjusts occurrences counter to reflect smoothed probability
            hamWords.put(hamWord, (hamWords.get(hamWord)+1) / (wordsInHamFile.length + 2));
        }
        emailIsHamProb = (double)wordsInHamFile.length / (wordsInHamFile.length + wordsInSpamFile.length);
        emailIsSpamProb = (double)wordsInSpamFile.length / (wordsInHamFile.length + wordsInSpamFile.length);
        numOfHam = wordsInHamFile.length;
        numOfSpam = wordsInSpamFile.length;
    }

    public void classify(File[] emails, Set<File> spams, Set<File> hams) throws IOException {
        for(File file : emails) {
            HashSet<String> wordsInEmail = new HashSet();
            Scanner emailScanner = new Scanner(file);
            // Finds and adds all unique words in the email that also appear in spamwords or hamwords to a set
            while (emailScanner.hasNext()) {
                String word = emailScanner.next();
                if(spamWords.keySet().contains(word) || hamWords.keySet().contains(word)) {
                    wordsInEmail.add(word);
                }
            }

            double spamSide = 0.0;
            double hamSide = 0.0;

            // Goes through each acceptable word from the email and calculates a sum of the logarithms of the probabilities
            //      to avoid underflow problem
            for(String w : wordsInEmail) {
                if(spamWords.keySet().contains(w)) {
                    spamSide += Math.log(spamWords.get(w));
                } else {
                    spamSide += Math.log(1.0 / (numOfSpam + 2.0));
                }
                if(hamWords.keySet().contains(w)) {
                    hamSide += Math.log(hamWords.get(w));
                } else {
                    hamSide += Math.log(1.0 / (numOfHam + 2.0));
                }
            }
            spamSide += Math.log(emailIsSpamProb);
            hamSide += Math.log(emailIsHamProb);

            if(spamSide > hamSide) {
                spams.add(file);
            } else {
                hams.add(file);
            }
        }
    }


// Returns a set of all the tokens (words) in an email
    public static HashSet<String> tokenSet(File filename) throws IOException {
        HashSet<String> tokens = new HashSet<String>();
        Scanner filescan = new Scanner(filename);
        filescan.next(); // Ignoring "Subject"
        while(filescan.hasNextLine() && filescan.hasNext()) {
            tokens.add(filescan.next());
        }
        filescan.close();
        return tokens;
    }
}
