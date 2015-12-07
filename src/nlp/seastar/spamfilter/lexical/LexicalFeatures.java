package nlp.seastar.spamfilter.lexical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LexicalFeatures {
	private List<String> stopWords;
	private static final String STOP_WORDS_FILE_NAME = "stopwords.txt";
	private List<String> swearWords;
	private static final String SWEAR_WORDS_FILE_NAME = "swearwords.txt";
	private List<String> spamPhrases;
	private static final String SPAM_PHRASES_FILE_NAME = "spamwords.txt";

	public LexicalFeatures() {
		this.stopWords = new ArrayList<String>();
		getStopWords();
		this.swearWords = new ArrayList<String>();
		getSwearWords();
		this.spamPhrases = new ArrayList<String>();
		getSpamPhrases();
	}

	/**
	 * Takes the stopwords file and generates a list of stop words
	 */
	public void getStopWords() {
		File file = new File(STOP_WORDS_FILE_NAME);
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				stopWords.add(line.trim());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Please check the " + STOP_WORDS_FILE_NAME
					+ " file.");
		}
	}

	public boolean isStopWord(String word) {
		if (stopWords != null && !stopWords.isEmpty()
				&& stopWords.contains(word))
			return true;
		return false;
	}

	public void getSwearWords() {
		File file = new File(SWEAR_WORDS_FILE_NAME);
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				swearWords.add(line.trim());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Please check the " + SWEAR_WORDS_FILE_NAME
					+ " file.");
		}
	}

	public boolean isSwearWord(String word) {
		if (swearWords != null && !swearWords.isEmpty()
				&& swearWords.contains(word))
			return true;
		return false;
	}

	public void getSpamPhrases() {
		File file = new File(SPAM_PHRASES_FILE_NAME);
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				spamPhrases.add(line.trim());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Please check the " + SPAM_PHRASES_FILE_NAME
					+ " file.");
		}

	}

	public boolean isSpamPhrase(String line) {
		if (spamPhrases != null && !spamPhrases.isEmpty()) {
			for (String spamWord : spamPhrases) {
				if (line.contains(spamWord))
					return true;
			}
		}
		return false;
	}
}
