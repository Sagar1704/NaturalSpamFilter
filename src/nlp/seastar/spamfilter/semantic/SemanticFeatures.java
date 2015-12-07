package nlp.seastar.spamfilter.semantic;

import java.io.File;
import java.util.ArrayList;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import nlp.seastar.spamfilter.email.Word;

public class SemanticFeatures {
	private static final String WORDNET_DATABASE_DIR_VALUE = "dict";
	protected IRAMDictionary wordNetDictionary;
	protected ArrayList<Word> nyms;
	
	public SemanticFeatures() {
		this.wordNetDictionary = new RAMDictionary(new File(WORDNET_DATABASE_DIR_VALUE),
				ILoadPolicy.NO_LOAD);
	}
	
	protected void setAverageCount(Word word) {
		int hamCount = 0;
		int spamCount = 0;
		for (Word synonym : nyms) {
			hamCount += synonym.getHamCount();
			spamCount += synonym.getSpamCount();
		}
		if (nyms.size() != 0) {
			word.setHamCount(hamCount / nyms.size());
			word.setSpamCount(spamCount / nyms.size());
		}
	}
}
