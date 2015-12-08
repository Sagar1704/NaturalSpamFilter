package nlp.seastar.spamfilter.syntactic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class HeadWordCalculator {
	Properties props;
	static String content;
	StanfordCoreNLP pipeline;

	public HeadWordCalculator() {
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		pipeline = new StanfordCoreNLP(props);
	}

	@SuppressWarnings("resource")
	public ArrayList<String> getHeadWords(File file) {
		ArrayList<String> headWords = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(file);
			StringBuilder content = new StringBuilder();
			int i = 0;
			while (scanner.hasNextLine() && i < 10) {
				content.append(scanner.nextLine());
				i++;
			}
			Annotation document = new Annotation(content.toString());
			pipeline.annotate(document);
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			for (CoreMap sentence : sentences) {
				SemanticGraph dependencies1 = sentence.get(CollapsedDependenciesAnnotation.class);
				String head = dependencies1.getRoots().toString();
				if (head.length() > 2)
					headWords.add(head.substring(1, head.indexOf("/")));
				// add substring in headWords
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return headWords;
	}
	// public static void main(String[] args) {
	// HeadWordCalculator h = new HeadWordCalculator();
	// ArrayList<String> headWords = h.getHeadWords(new File("test.txt"));
	// System.out.println(headWords.toString());
	// }
}
