package nlp.seastar.spamfilter.email;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import nlp.seastar.spamfilter.lexical.LexicalFeatures;

/**
 * @author Ashwini
 * @author Sagar
 */
public class Email {
	private ArrayList<String> ngrams;
	private ArrayList<String> tokens;
	private double hamProbability;
	private double spamProbability;

	public Email() {
		this.ngrams = new ArrayList<String>();
		this.tokens = new ArrayList<String>();
		this.setHamProbability(0.0);
		this.setSpamProbability(0.0);
	}

	public ArrayList<String> getEmailNgrams() {
		return ngrams;
	}

	public ArrayList<String> getEmailTokens() {
		return tokens;
	}

	public void setEmailParameters(int ngramCount, File file) {
		computeTokens(file);
		computeNgrams(ngramCount);
	}

	public double getHamProbability() {
		return hamProbability;
	}

	public void setHamProbability(double hamProbability) {
		this.hamProbability = hamProbability;
	}

	public double getSpamProbability() {
		return spamProbability;
	}

	public void setSpamProbability(double spamProbability) {
		this.spamProbability = spamProbability;
	}

	public void adjustProbability(double hamProbability, double spamProbability) {
		this.hamProbability += Math.log(hamProbability);
		this.spamProbability += Math.log(spamProbability);
	}

	public void computeNgrams(int ngramCount) {
		if (ngramCount > 1) {
			for (int i = 0; i < tokens.size(); i++) {
				if ((i + ngramCount) <= tokens.size()) {
					StringBuilder ngram = new StringBuilder();
					for (int j = i; j < (i + ngramCount); j++) {
						ngram.append(tokens.get(j));
					}
					ngrams.add(ngram.toString());
				}
			}
		} else {
			ngrams = tokens;
		}
	}

	public void computeTokens(File file) {
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String words[] = (scanner.nextLine().split(" "));
				for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
					if (words[wordIndex].length() > 1) {
						tokens.add(words[wordIndex]);
					}
				}
			}
			scanner.close();
		} catch (Exception e) {
			System.out.println("Please check the file " + file);
			e.printStackTrace();
		}
	}

	public ArrayList<String> removeStopWords(LexicalFeatures lexical) {
		for (int i = 0; i < tokens.size(); i++) {
			if (lexical.isStopWord(tokens.get(i)))
				tokens.remove(i);
		}
		return tokens;
	}
	
	public void handleSwearWords(LexicalFeatures lexical) {
		
	}

	public void setEmail(ArrayList<String> words) {
		this.tokens = words;
	}

	public void addToken(String word) {
		this.tokens.add(word);
	}

	public boolean isHam() {
		return (hamProbability > spamProbability ? true : false);
	}
}
