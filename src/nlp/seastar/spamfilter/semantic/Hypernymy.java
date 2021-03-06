package nlp.seastar.spamfilter.semantic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import nlp.seastar.spamfilter.data.Data;
import nlp.seastar.spamfilter.data.Tester;
import nlp.seastar.spamfilter.data.Trainer;
import nlp.seastar.spamfilter.email.Email;
import nlp.seastar.spamfilter.email.Word;
import nlp.seastar.spamfilter.naive_bayes.Baseline;

public class Hypernymy extends SemanticFeatures implements Trainer, Tester {
	public Hypernymy() {
		super();
	}

	@Override
	public void train(Data data) {
		new Baseline().train(data);
	}

	@Override
	public String test(Data data) {
		StringBuilder builder = new StringBuilder();
		try {
			wordNetDictionary.open();
			wordNetDictionary.load(true);
			File testDirectory = new File(data.getTestingDirectory());
			File[] directories = testDirectory.listFiles();
			HashMap<String, Word> dictionary = data.getDictionary();
			for (File directory : directories) {
				int testHamCount = 0;
				int testSpamCount = 0;
				File directoryPath = new File(
						data.getTestingDirectory() + "\\" + directory.getName());
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
							getHypernyms(word, dictionary);
							setAverageCount(word);
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	private void getHypernyms(Word word, HashMap<String, Word> dictionary) {
		nyms = new ArrayList<Word>();
		POS[] poss = POS.values();
		for (POS pos : poss) {
			IIndexWord idxWord = wordNetDictionary.getIndexWord(word.getWord(), pos);
			if (idxWord != null) {
				IWordID wordID = idxWord.getWordIDs().get(0);
				IWord iWord = wordNetDictionary.getWord(wordID);
				ISynset synset = iWord.getSynset();
				List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
				List<IWord> words;
				for (ISynsetID synsetID : hypernyms) {
					words = wordNetDictionary.getSynset(synsetID).getWords();
					for (Iterator<IWord> iterator = words.iterator(); iterator.hasNext();) {
						String hypernym = iterator.next().getLemma().trim();
						if (dictionary.containsKey(hypernym)) {
							Word newWord = dictionary.get(hypernym);
							nyms.add(newWord);
						}
					}
				}
			}
		}
	}
	// public static void main(String[] args) {
	// Data data = new Data(args[0], args[1]);
	// Hypernymy hypernymy = new Hypernymy();
	// hypernymy.train(data);
	// hypernymy.test(data);
	// }
}
