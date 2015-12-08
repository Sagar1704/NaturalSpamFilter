package nlp.seastar.spamfilter.data;

import java.util.HashMap;

import nlp.seastar.spamfilter.email.Email;
import nlp.seastar.spamfilter.email.Word;

/**
 * @author Sagar
 */
public class Data {
	private int numOfSpamEmails;
	private int numOfHamEmails;
	private int numOfSpamWords;
	private int numOfHamWords;
	private double priorSpamProbablity;
	private double priorHamProbability;
	private String trainingDirectory;
	private String testingDirectory;
	private HashMap<String, Word> dictionary;

	public Data(String trainingDirectory, String testDirectory) {
		this.setTrainingDirectory(trainingDirectory);
		this.setTestingDirectory(testDirectory);
		this.setNumOfSpamEmails(0);
		this.setNumOfHamEmails(0);
		this.setNumOfHamWords(0);
		this.setNumOfSpamWords(0);
		this.setPriorHamProbability(0.0);
		this.setPriorSpamProbablity(0.0);
		this.dictionary = new HashMap<String, Word>();
	}

	public String getTrainingDirectory() {
		return trainingDirectory;
	}

	public void setTrainingDirectory(String trainingDirectory) {
		this.trainingDirectory = trainingDirectory;
	}

	public void incrementHamEmails() {
		numOfHamEmails++;
	}

	public void incrementSpamEmails() {
		numOfSpamEmails++;
	}

	public void incrementHamWords() {
		numOfHamWords++;
	}

	public void incrementSpamWords() {
		numOfSpamWords++;
	}

	public String getTestingDirectory() {
		return testingDirectory;
	}

	public void setTestingDirectory(String testingDirectory) {
		this.testingDirectory = testingDirectory;
	}

	public HashMap<String, Word> getDictionary() {
		return dictionary;
	}

	public Word getDictionary(String key) {
		return dictionary.get(key);
	}

	public void addToDictionary(String key, Word value) {
		this.dictionary.put(key, value);
	}

	public int getNumOfSpamEmails() {
		return numOfSpamEmails;
	}

	public void setNumOfSpamEmails(int numOfSpamEmails) {
		this.numOfSpamEmails = numOfSpamEmails;
	}

	public int getNumOfHamEmails() {
		return numOfHamEmails;
	}

	public void setNumOfHamEmails(int numOfHamEmails) {
		this.numOfHamEmails = numOfHamEmails;
	}

	public int getNumOfSpamWords() {
		return numOfSpamWords;
	}

	public void setNumOfSpamWords(int numOfSpamWords) {
		this.numOfSpamWords = numOfSpamWords;
	}

	public int getNumOfHamWords() {
		return numOfHamWords;
	}

	public void setNumOfHamWords(int numOfHamWords) {
		this.numOfHamWords = numOfHamWords;
	}

	public double getPriorSpamProbablity() {
		return priorSpamProbablity;
	}

	public void setPriorSpamProbablity(double priorSpamProbablity) {
		this.priorSpamProbablity = priorSpamProbablity;
	}

	public double getPriorHamProbability() {
		return priorHamProbability;
	}

	public void setPriorHamProbability(double priorHamProbability) {
		this.priorHamProbability = priorHamProbability;
	}

	public void addEntriesToMap(Email email, boolean isHam) {
		for (int i = 0; i < email.getEmailNgrams().size(); i++) {
			Word word = new Word(isHam, email.getEmailNgrams().get(i).trim());
			if (dictionary.containsKey(word.getWord())) {
				word = dictionary.get(word.getWord());
				if (isHam)
					word.incrementHamCount();
				else
					word.incrementSpamCount();
			} else {
				dictionary.put(word.getWord().trim(), word);
			}
			if (isHam)
				numOfHamWords++;
			else
				numOfSpamWords++;
		}
	}

	public void computePriorProbabilities() {
		priorHamProbability = (double) numOfHamEmails / (numOfSpamEmails + numOfHamEmails);
		priorSpamProbablity = (double) numOfSpamEmails / (numOfSpamEmails + numOfHamEmails);
	}

	public void verify() {
		System.out.println(numOfHamEmails);
		System.out.println(numOfSpamEmails);
		System.out.println(numOfHamWords);
		System.out.println(numOfSpamWords);
	}
}
