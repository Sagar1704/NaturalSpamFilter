package nlp.seastar.spamfilter.email;

import java.util.ArrayList;

public class Email {
	private ArrayList<Word> words;

	public ArrayList<Word> getEmail() {
		return words;
	}

	public void setEmail(ArrayList<Word> words) {
		this.words = words;
	}
	
	public void setEmailWord(Word word) {
		this.words.add(word);
	}
}
