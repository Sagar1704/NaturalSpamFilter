package nlp.seastar.spamfilter.naive_bayes;

import java.io.File;
import java.util.ArrayList;

import nlp.seastar.spamfilter.data.Data;
import nlp.seastar.spamfilter.data.Tester;
import nlp.seastar.spamfilter.data.Trainer;
import nlp.seastar.spamfilter.email.Word;
import nlp.seastar.spamfilter.syntactic.POSTagger;

public class BayesWithPOSTagger implements Trainer, Tester {
	private POSTagger syntactic;
	private int rulesPerTag;

	public BayesWithPOSTagger() {
		syntactic = new POSTagger();
		rulesPerTag = 5;
	}

	public BayesWithPOSTagger(int rulesPerTag) {
		super();
		this.rulesPerTag = rulesPerTag;
	}

	@Override
	public String test(Data data) {
		StringBuilder builder = new StringBuilder();
		File testDirectory = new File(data.getTestingDirectory());
		File[] directories = testDirectory.listFiles();
		for (File directory : directories) {
			int testHamCount = 0;
			int testSpamCount = 0;
			File directoryPath = new File(data.getTestingDirectory() + "\\" + directory.getName());
			File[] files = directoryPath.listFiles();
			builder.append("\nTest result for directory: " + directory.getName());
			for (File file : files) {
				double hamProbability = 0.0;
				double spamProbability = 0.0;
				ArrayList<String> testFileTags = (ArrayList<String>) syntactic.lemmatize(file);
				ArrayList<String> testRuleList = (ArrayList<String>) syntactic
						.makePOSRulelList(testFileTags, rulesPerTag);
				for (int i = 0; i < testRuleList.size(); i++) {
					int spamRuleCount = 0;
					int hamRuleCount = 0;
					double ruleSpamProbability = 0.0;
					double ruleHamProbability = 0.0;
					if (data.getDictionary().containsKey(testRuleList.get(i))) {
						Word word = data.getDictionary().get(testRuleList.get(i));
						spamRuleCount = word.getSpamCount();
						hamRuleCount = word.getHamCount();
					}
					ruleSpamProbability = (double) (spamRuleCount + 1)
							/ (data.getNumOfSpamWords() + data.getDictionary().size());
					ruleHamProbability = (double) (hamRuleCount + 1)
							/ (data.getNumOfHamWords() + data.getDictionary().size());
					hamProbability += Math.log(ruleHamProbability);
					spamProbability += Math.log(ruleSpamProbability);
				}
				hamProbability += Math.log(data.getPriorHamProbability());
				spamProbability += Math.log(data.getPriorSpamProbablity());
				if (hamProbability < spamProbability)
					testSpamCount++;
				else
					testHamCount++;
			}
			builder.append("\n\n Classified " + testSpamCount + " as Spam\n Classified  "
					+ testHamCount + " as Ham\n Accuracy = ");
			if (directory.getName().equals("spam"))
				builder.append(
						(double) (testSpamCount * 100) / (testSpamCount + testHamCount) + " %");
			else
				builder.append(
						(double) (testHamCount * 100) / (testSpamCount + testHamCount) + " %");
		}
		return builder.toString();
	}

	@Override
	public void train(Data data) {
		File trainDirectory = new File(data.getTrainingDirectory());
		File[] directories = trainDirectory.listFiles();
		boolean isHam = false;
		try {
			for (File directory : directories) {
				if (directory.getName().equals("ham"))
					isHam = true;
				else
					isHam = false;
				File directoryPath = new File(
						data.getTrainingDirectory() + "\\" + directory.getName());
				File[] files = directoryPath.listFiles();
				for (File file : files) {
					if (isHam)
						data.incrementHamEmails();
					else
						data.incrementSpamEmails();
					ArrayList<String> tags = (ArrayList<String>) syntactic.lemmatize(file);
					ArrayList<String> ruleList = (ArrayList<String>) syntactic
							.makePOSRulelList(tags, rulesPerTag);
					for (int i = 0; i < ruleList.size(); i++) {
						if (isHam)
							data.incrementHamWords();
						else
							data.incrementSpamWords();
						String rule = ruleList.get(i);
						if (data.getDictionary().containsKey(rule)) {
							Word word = data.getDictionary().get(rule);
							if (isHam)
								word.incrementHamCount();
							else
								word.incrementSpamCount();
						} else {
							Word word = new Word(isHam, rule);
							data.getDictionary().put(rule, word);
						}
					}
				}
			}
			data.computePriorProbabilities();
			data.verify();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String trainingDirectory = args[0];
		String testDirectory = args[1];
		try {
			System.out.print("\n\n#### NAIVE BAYES CLASSIFIER ####\n\n");
			BayesWithPOSTagger bayes = new BayesWithPOSTagger();
			Data data = new Data(trainingDirectory, testDirectory);
			bayes.train(data);
			bayes.test(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
