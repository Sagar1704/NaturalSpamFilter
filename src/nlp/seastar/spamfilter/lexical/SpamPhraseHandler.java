package nlp.seastar.spamfilter.lexical;

import java.io.File;
import java.util.HashMap;

import nlp.seastar.spamfilter.data.Data;
import nlp.seastar.spamfilter.data.Tester;
import nlp.seastar.spamfilter.data.Trainer;
import nlp.seastar.spamfilter.email.Email;
import nlp.seastar.spamfilter.email.Word;

public class SpamPhraseHandler implements Trainer, Tester {
	private LexicalFeatures lexical;

	public SpamPhraseHandler() {
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
				File directoryPath = new File(
						data.getTrainingDirectory() + "\\" + directory.getName());
				File[] files = directoryPath.listFiles();
				for (File file : files) {
					Email email = new Email();
					email.setEmailParameters(1, file);
					if (isHam)
						data.incrementHamEmails();
					else
						data.incrementSpamEmails();
					for (String token : email.getEmailNgrams()) {
						Word word = new Word(isHam, token);
						if (!word.isSpecialCharacter()) {
							if (data.getDictionary().containsKey(token))
								word = data.getDictionary(token);
							if (isHam) {
								data.incrementHamWords();
								word.incrementHamCount();
								if (lexical.isSpamPhrase(word.getWord().trim())) {
									word.incrementHamCount();
								}
							} else {
								data.incrementSpamWords();
								word.incrementSpamCount();
								if (!lexical.isSpamPhrase(word.getWord().trim())) {
									word.incrementSpamCount();
								}
							}
							data.addToDictionary(token, word);
						}
					}
				}
			}
			data.computePriorProbabilities();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String test(Data data) {
		StringBuilder builder = new StringBuilder();
		File testDirectory = new File(data.getTestingDirectory());
		File[] directories = testDirectory.listFiles();
		HashMap<String, Word> dictionary = data.getDictionary();
		for (File directory : directories) {
			int testHamCount = 0;
			int testSpamCount = 0;
			File directoryPath = new File(data.getTestingDirectory() + "\\" + directory.getName());
			File[] files = directoryPath.listFiles();
			builder.append("\nTest result for directory: " + directory.getName());
			for (File file : files) {
				Email email = new Email();
				email.setEmailParameters(1, file);
				for (String token : email.getEmailNgrams()) {
					Word word = new Word(token);
					if (!word.isSpecialCharacter()) {
						if (dictionary.containsKey(word.getWord().trim())) {
							word = dictionary.get(word.getWord().trim());
						}
						if (lexical.isSpamPhrase(word.getWord()))
							word.incrementSpamCount();
						else
							word.incrementHamCount();
						word.computeProbability(data);
						email.adjustProbability(word.getHamProbability(),
								word.getSpamProbability());
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
	// public static void main(String[] args) {
	// Data data = new Data(args[0], args[1]);
	// SpamPhraseHandler spamHandler = new SpamPhraseHandler();
	// spamHandler.train(data);
	// spamHandler.test(data);
	// }
}
