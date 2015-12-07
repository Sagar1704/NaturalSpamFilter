package nlp.seastar.spamfilter.lexical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import nlp.seastar.spamfilter.data.Data;
import nlp.seastar.spamfilter.data.Trainer;

public class StopWordRemover implements Trainer {
	private List<String> stopWords;
	private static final String STOP_WORDS_FILE_NAME = "stopwords.txt";

	@Override
	public void train(Data data) {
		getStopWords();
	}

	/**
	 * Takes the stopwords file and generates a list of stop words
	 * 
	 * @throws FileNotFoundException
	 */
	private void getStopWords() {
		stopWords = new ArrayList<String>();
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
			System.out.println("Check the " + STOP_WORDS_FILE_NAME + " file.");
		}
	}
	
	/**
	 * Check if the word is frequently used stop word
	 * 
	 * @param word
	 * @return
	 */
	private boolean isStopWord(String word) {
		if (stopWords != null && !stopWords.isEmpty() && stopWords.contains(word))
			return true;
		return false;
	}
}
