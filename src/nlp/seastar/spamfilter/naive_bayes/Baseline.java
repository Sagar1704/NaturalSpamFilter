package nlp.seastar.spamfilter.naive_bayes;

import java.io.File;
import java.util.HashMap;

import nlp.seastar.spamfilter.data.Data;
import nlp.seastar.spamfilter.data.Tester;
import nlp.seastar.spamfilter.data.Trainer;
import nlp.seastar.spamfilter.email.Email;
import nlp.seastar.spamfilter.email.Word;
import nlp.seastar.spamfilter.lexical.LexicalFeatures;

public class Baseline implements Trainer, Tester {
	private int ngramCount;
	private boolean removeStopWords;
	private LexicalFeatures lexical;

	public Baseline() {
		ngramCount = 1;
		removeStopWords = false;
	}

	public Baseline(int ngramCount, boolean removeStopWords) {
		this.ngramCount = ngramCount;
		this.removeStopWords = removeStopWords;
		if (removeStopWords)
			this.lexical = new LexicalFeatures();
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
				File directoryPath = new File(data.getTrainingDirectory()
						+ "\\" + directory.getName());
				File[] files = directoryPath.listFiles();
				for (File file : files) {
					Email email = new Email();
					email.setEmailParameters(ngramCount, file);
					if (isHam)
						data.increamentHamEmails();
					else
						data.increamentSpamEmails();
					if (removeStopWords) {
						email.removeStopWords(lexical);
					}
					data.addEntriesToMap(email, isHam);
				}
			}
			data.computePriorProbabilities();
			data.verify();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void test(Data data) {
		try {
			File testDirectory = new File(data.getTestingDirectory());
			File[] directories = testDirectory.listFiles();
			HashMap<String, Word> dictionary = data.getDictionary();
			for (File directory : directories) {
				int testHamCount = 0;
				int testSpamCount = 0;
				File directoryPath = new File(data.getTestingDirectory() + "\\"
						+ directory.getName());
				File[] files = directoryPath.listFiles();
				System.out.print("\nTest result for directory: "
						+ directory.getName());
				for (File file : files) {
					Email email = new Email();
					email.computeTokens(file);
					email.computeNgrams(ngramCount);
					for (String token : email.getEmailNgrams()) {
						Word word = new Word(token);
						if (!word.isSpecialCharacter()) {
							if (dictionary.containsKey(word.getWord().trim())) {
								word = dictionary.get(word.getWord().trim());
							}
							if (lexical == null
									|| !lexical.isStopWord(word.getWord())) {
								word.computeProbability(data);
								email.adjustProbability(
										word.getHamProbability(),
										word.getSpamProbability());
							}
						}
					}
					email.adjustProbability(data.getPriorHamProbability(),
							data.getPriorSpamProbablity());
					if (email.isHam()) {
						testHamCount++;
					} else {
						testSpamCount++;
					}

				}
				System.out.print("\n\n Classified " + testSpamCount
						+ " as Spam\n Classified  " + testHamCount
						+ " as Ham\n Accuracy = ");
				if (directory.getName().equals("spam"))
					System.out.print((double) (testSpamCount * 100)
							/ (testSpamCount + testHamCount) + " %");
				else
					System.out.print((double) (testHamCount * 100)
							/ (testSpamCount + testHamCount) + " %");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String trainingDirectory = args[0];
		String testDirectory = args[1];
		try {
			System.out.print("\n\n#### NAIVE BAYES CLASSIFIER ####\n\n");
			Baseline bayes = new Baseline(2, true);
			Data data = new Data(trainingDirectory, testDirectory);
			bayes.train(data);
			bayes.test(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
