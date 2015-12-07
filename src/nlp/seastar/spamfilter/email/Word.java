package nlp.seastar.spamfilter.email;

import nlp.seastar.spamfilter.data.Data;

public class Word {
	private String word;
	private int hamCount;
	private int spamCount;
	private double hamProbability;
	private double spamProbability;

	public Word() {
		this.setHamCount(0);
		this.setSpamCount(0);
		this.setHamProbability(0.0);
		this.setSpamProbability(0.0);
	}

	public Word(boolean isHam, String word) {
		this.word = word;
		if (isHam) {
			this.hamCount = 1;
			this.spamCount = 0;
		} else {
			this.spamCount = 1;
			this.hamCount = 0;
		}
	}

	public Word(String word) {
		super();
		this.setWord(word);
		this.setHamCount(0);
		this.setSpamCount(0);
		this.setHamProbability(0.0);
		this.setSpamProbability(0.0);
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

	public void incrementSpamCount() {
		spamCount++;
	}

	public void incrementHamCount() {
		hamCount++;
	}

	public void setSpamCount(int spamCount) {
		this.spamCount = spamCount;
	}

	public double getHamProbability() {
		return hamProbability;
	}

	public void setHamProbability(double hamProbability) {
		this.hamProbability = hamProbability;
	}

	public double getSpamProbability() {
		return spamProbability;
	}

	public void setSpamProbability(double spamProbability) {
		this.spamProbability = spamProbability;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Word [word=" + word + ", hamCount=" + hamCount + ", spamCount="
				+ spamCount + "]";
	}

	public boolean isSpecialCharacter() {
		if (word.length() == 1 && !word.equalsIgnoreCase("a")
				&& !word.equalsIgnoreCase("i"))
			return true;
		return false;
	}

	public void computeProbability(Data data) {
		hamProbability = (double) (hamCount + 1)
				/ (data.getNumOfHamWords() + data.getDictionary()
						.size());
		
		spamProbability = (double) (spamCount + 1)
				/ (data.getNumOfSpamWords() + data.getDictionary()
						.size());
	}
}
