package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String pathToFile = args[0];
        File file = new File (pathToFile);
        String text = "";
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                text += scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found at " + pathToFile);
        }
        int words = countWords(text);
        int sentences = countSentences(text);
        int characters = countCharacters(text);
        int syllables = countSyllables(text);
        int polysyllables = countPolysyllables(text);
        System.out.println("The text is: \n" + text + "\n");
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        String action = scanner.next().toUpperCase();
        switch (action) {
            case "ARI":
                ariScore(characters,words,sentences);
                break;
            case "FK":
                fkScore(words,sentences,syllables);
                break;
            case "SMOG":
                smogScore(polysyllables,sentences);
                break;
            case "CL":
                clScore(characters,words,sentences);
                break;
            case "ALL":
                ariScore(characters,words,sentences);
                fkScore(words,sentences,syllables);
                smogScore(polysyllables,sentences);
                clScore(characters,words,sentences);
                break;
            default:
                System.out.println("Error: wrong input");
                break;
        }
    }

    public static int countWords(String text) {
        String[] words = text.split(" ");
        return words.length;
    }

    public static int countSentences(String text) {
        String[] sentences = text.split("[.!?]");
        return sentences.length;
    }
    public static int countCharacters(String text) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c != ' ') {
                count++;
            }
        }
        return count;
    }

    public static int countSyllablesInWord(String word) {
        char[] chars = word.toCharArray();
        int syllables = 0;
        int count = 0;
        //System.out.println("Word: " + s + " Word length: " + word.length);
        for (int i = 0; i < chars.length; i++) {
            //System.out.print("    Character: " + word[i] + " i: " + i);
            if (isVowel(chars[i])) {
                if (i > 0) {
                    if (isVowel(chars[count-1])) {

                    } else if (i == chars.length-1 &&
                            String.valueOf(chars[i]).matches("e")) {
                        //System.out.print(" dodge");
                    } else if (i == chars.length - 2 &&
                            String.valueOf(chars[i]).matches("e")
                            && isEnd(chars[chars.length-1])) {
                        //System.out.print(" dodge");
                    }else {
                        syllables++;
                        //System.out.print(" syllables++");
                    }
                } else {
                    syllables++;
                    //System.out.print(" syllables++");
                }
            }
            //System.out.println();
            count++;
        }
        if (syllables == 0) {
            syllables = 1;
        }
        return syllables;
    }

    public static int countSyllables(String text) {
        String[] words = text.split(" ");
        int sumOfSyllables = 0;
        for (String word : words) {
            sumOfSyllables += countSyllablesInWord(word);
        }
        return sumOfSyllables;
    }

    public static int countPolysyllables(String text) {
        String[] words = text.split(" ");
        int sumOfPolysyllables = 0;
        for (String word : words) {
            double syllables = countSyllablesInWord(word);
            if (syllables > 2.0) {
                sumOfPolysyllables++;
            }
        }
        return sumOfPolysyllables;
    }

    public static void ariScore(int characters, int words, int sentences) {
        double score = 4.71 * ((double) characters/(double) words) +
                0.5 * ((double) words/(double) sentences) - 21.43;
        score = Math.floor(score * 100.0) / 100.0;
        int roundedScore = (int)Math.ceil(score);
        String age = "";
        if (roundedScore > 0 && roundedScore < 14) {
            age = String.valueOf(roundedScore+5);
        } else if (roundedScore == 14) {
            age = "18-22";
        } else {
            age = "error";
        }
        System.out.println("Automated Readability Index: " + score
        + "(about " + age + "-year-olds).");
    }

    public static void fkScore(int words, int sentences, int syllables) {
        double score = 0.39 * ((double) words / (double) sentences)
                + 11.8 * ((double) syllables / (double) words) - 15.59;
        score = Math.floor(score * 100.0) / 100.0;
        int age = (int) Math.round(score + 6.0);
        System.out.println("Flesch–Kincaid readability tests: " + score
        + " (about " + age + "-year-olds).");
    }

    public static void smogScore(int polysyllables, int sentences) {
        double score = 1.043 * Math.sqrt(polysyllables * (30 / (double) sentences)) + 3.1291;
        score = Math.floor(score * 100.0) / 100.0;
        int age = (int) Math.round(score + 6.0);
        System.out.println("Simple Measure of Gobbledygook: " + score
        + " (about " + age + "-year-olds).");
    }

    public static void clScore(int characters, int words, int sentences) {
        double L = (double) characters * 100.0 / (double) words;
        double S = (double) sentences * 100.0 / (double) words;
        double score = 0.0588 * L - 0.296 * S - 15.8;
        score = Math.floor(score * 100.0) / 100.0;
        int age = (int) Math.round(score + 6.0);
        System.out.println("Coleman–Liau index: " + score
                + " (about " + age + "-year-olds).");
    }
    public static boolean isVowel(char c) {
        return String.valueOf(c).matches("[aeiouyAEIOUY]");
    }

    public static boolean isEnd(char c) {
        return String.valueOf(c).matches("[.!?]");
    }
}
