package nlp.seastar.spamfilter.email;

/**
 * Word with counters of occurrence in ham and spam emails.
 * 
 * @author Ashwini
 * @author Sagar
 */
public class Word {
	private String word;
	private int hamCount;
	private int spamCount;

	public Word() {
		this.setHamCount(0);
		this.setSpamCount(0);
	}

	public Word(boolean isHam, String word) {
		this.setWord(word);
		if (isHam) {
			this.setHamCount(1);
			this.setSpamCount(0);
		} else {
			this.setSpamCount(1);
			this.setHamCount(0);
		}
	}

	public Word(String word, int hamCount, int spamCount) {
		super();
		this.setWord(word);
		this.setHamCount(hamCount);
		this.setSpamCount(spamCount);
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getHamCount() {
		return hamCount;
	}

	public void setHamCount(int hamCount) {
		this.hamCount = hamCount;
	}

	public int getSpamCount() {
		return spamCount;
	}

	public void setSpamCount(int spamCount) {
		this.spamCount = spamCount;
	}
}
