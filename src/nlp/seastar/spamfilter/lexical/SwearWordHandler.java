package nlp.seastar.spamfilter.lexical;

import java.io.File;

import nlp.seastar.spamfilter.data.Data;
import nlp.seastar.spamfilter.data.Tester;
import nlp.seastar.spamfilter.data.Trainer;
import nlp.seastar.spamfilter.email.Email;
import nlp.seastar.spamfilter.email.Word;
import nlp.seastar.spamfilter.naive_bayes.Baseline;

public class SwearWordHandler implements Trainer, Tester {
	private LexicalFeatures lexical;

	public SwearWordHandler() {
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
								if (lexical.isSwearWord(word.getWord().trim())) {
									word.incrementHamCount();
								}
							} else {
								data.incrementSpamWords();
								word.incrementSpamCount();
								if (!lexical.isSwearWord(word.getWord().trim())) {
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
		return new Baseline().test(data);
	}
	// public static void main(String[] args) {
	// Data data = new Data(args[0], args[1]);
	// SwearWordHandler swear = new SwearWordHandler();
	// swear.train(data);
	// swear.test(data);
	// }
}
