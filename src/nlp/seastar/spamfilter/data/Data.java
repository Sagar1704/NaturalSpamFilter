package nlp.seastar.spamfilter.data;

import java.util.HashMap;
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
	private String trainDirectoryName;
	private String testDirectoryName;
	private HashMap<String, Word> dictionary;

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

	public String getTrainDirectoryName() {
		return trainDirectoryName;
	}

	public void setTrainDirectoryName(String trainDirectoryName) {
		this.trainDirectoryName = trainDirectoryName;
	}

	public String getTestDirectoryName() {
		return testDirectoryName;
	}

	public void setTestDirectoryName(String testDirectoryName) {
		this.testDirectoryName = testDirectoryName;
	}
}
