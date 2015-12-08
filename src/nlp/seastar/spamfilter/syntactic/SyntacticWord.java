package nlp.seastar.spamfilter.syntactic;

public class SyntacticWord {
	private String word;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	private int spamCountAsHead;
	private int hamCountAsHead;
	private double weight;
	
	public int getSpamCountAsHead() {
		return spamCountAsHead;
	}
	public void setSpamCountAsHead(int spamCountAsHead) {
		this.spamCountAsHead = spamCountAsHead;
	}
	public int getHamCountAsHead() {
		return hamCountAsHead;
	}
	public void setHamCountAsHead(int hamCountAsHead) {
		this.hamCountAsHead = hamCountAsHead;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public void incrementSpamCountAsHead(){
		this.spamCountAsHead ++;
	}
	public void incrementHamCountAsHead(){
		this.hamCountAsHead ++;
	}
	public SyntacticWord(int countAsHead, double weight) {
		super();
		this.spamCountAsHead = countAsHead;
		this.hamCountAsHead = countAsHead;
		this.weight = weight;
	}
	public SyntacticWord(){
		this.hamCountAsHead = 0;
		this.spamCountAsHead = 0;
		this.weight =0.0;
	}
	
}
