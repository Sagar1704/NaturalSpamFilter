package nlp.seastar.spamfilter.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import nlp.seastar.spamfilter.data.Data;
import nlp.seastar.spamfilter.lexical.SpamPhraseHandler;
import nlp.seastar.spamfilter.lexical.SwearWordHandler;
import nlp.seastar.spamfilter.naive_bayes.Baseline;
import nlp.seastar.spamfilter.naive_bayes.BayesWithPOSTagger;
import nlp.seastar.spamfilter.semantic.Hypernymy;
import nlp.seastar.spamfilter.semantic.Synonymy;

public class SpamFilterUI {
	protected Shell shell;
	private Text txtTrainingFolder;
	private Text txtTestingFolder;
	private Text textResult;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SpamFilterUI window = new SpamFilterUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(~SWT.RESIZE);
		shell.setSize(944, 477);
		shell.setText("Natural Spam Filter");
		Group grpBaseline = new Group(shell, SWT.BORDER | SWT.SHADOW_ETCHED_IN);
		grpBaseline.setFont(SWTResourceManager.getFont("Segoe UI", 20, SWT.BOLD));
		grpBaseline.setText("Baseline");
		grpBaseline.setBounds(10, 41, 230, 171);
		Button btnCheckButton = new Button(grpBaseline, SWT.CHECK);
		btnCheckButton.setEnabled(false);
		btnCheckButton.setSelection(true);
		btnCheckButton.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCheckButton.setBounds(10, 79, 168, 32);
		btnCheckButton.setText("Checked");
		Label lblTrain = new Label(shell, SWT.NONE);
		lblTrain.setForeground(SWTResourceManager.getColor(47, 79, 79));
		lblTrain.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblTrain.setBounds(10, 10, 52, 25);
		lblTrain.setText("Train");
		Label lblTest = new Label(shell, SWT.NONE);
		lblTest.setForeground(SWTResourceManager.getColor(123, 104, 238));
		lblTest.setText("Test");
		lblTest.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblTest.setBounds(246, 10, 52, 25);
		txtTrainingFolder = new Text(shell, SWT.BORDER);
		txtTrainingFolder.setToolTipText("Training folder");
		txtTrainingFolder.setBounds(68, 14, 172, 21);
		txtTestingFolder = new Text(shell, SWT.BORDER);
		txtTestingFolder.setToolTipText("Testing folder");
		txtTestingFolder.setBounds(304, 14, 172, 21);
		Group grpLexical = new Group(shell, SWT.BORDER | SWT.SHADOW_ETCHED_OUT);
		grpLexical.setText("Lexical");
		grpLexical.setFont(SWTResourceManager.getFont("Segoe UI", 20, SWT.BOLD));
		grpLexical.setBounds(246, 41, 230, 171);
		Button btnStopWords = new Button(grpLexical, SWT.CHECK);
		btnStopWords.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnStopWords.setText("Stop Words");
		btnStopWords.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnStopWords.setBounds(10, 53, 168, 32);
		Button btnSwearWords = new Button(grpLexical, SWT.CHECK);
		btnSwearWords.setText("Swear Words");
		btnSwearWords.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnSwearWords.setBounds(10, 91, 168, 32);
		Button btnSpamPhrases = new Button(grpLexical, SWT.CHECK);
		btnSpamPhrases.setText("Spam Phrases");
		btnSpamPhrases.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnSpamPhrases.setBounds(10, 129, 168, 32);
		Group grpSyntactic = new Group(shell, SWT.BORDER | SWT.SHADOW_IN);
		grpSyntactic.setText("Syntactic");
		grpSyntactic.setFont(SWTResourceManager.getFont("Segoe UI", 20, SWT.BOLD));
		grpSyntactic.setBounds(10, 218, 230, 171);
		Button btnPosTagger = new Button(grpSyntactic, SWT.CHECK);
		btnPosTagger.setText("POS Tagger");
		btnPosTagger.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnPosTagger.setBounds(10, 85, 168, 32);
		Button btnBigram = new Button(grpSyntactic, SWT.CHECK);
		btnBigram.setText("Bigram");
		btnBigram.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnBigram.setBounds(10, 47, 168, 32);
		Button btnHeadWord = new Button(grpSyntactic, SWT.BORDER | SWT.FLAT | SWT.CHECK);
		btnHeadWord.setEnabled(false);
		btnHeadWord.setForeground(SWTResourceManager.getColor(255, 0, 0));
		btnHeadWord.setText("Head Word");
		btnHeadWord.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnHeadWord.setBounds(10, 124, 168, 32);
		Group grpSemantic = new Group(shell, SWT.BORDER | SWT.SHADOW_OUT);
		grpSemantic.setText("Semantic");
		grpSemantic.setFont(SWTResourceManager.getFont("Segoe UI", 20, SWT.BOLD));
		grpSemantic.setBounds(246, 218, 230, 171);
		Button btnSynonymy = new Button(grpSemantic, SWT.CHECK);
		btnSynonymy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnSynonymy.setText("Synonymy");
		btnSynonymy.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnSynonymy.setBounds(10, 56, 168, 32);
		Button btnHypernymy = new Button(grpSemantic, SWT.CHECK);
		btnHypernymy.setText("Hypernymy");
		btnHypernymy.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnHypernymy.setBounds(10, 108, 168, 32);
		Button btnCompute = new Button(shell, SWT.NONE);
		btnCompute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textResult.setText("");
				String trainingDirectory = "training";
				if (!txtTrainingFolder.getText().toString().trim().equals("")) {
					trainingDirectory = txtTrainingFolder.getText().toString();
				}
				String testingDirectory = "testing";
				if (!txtTestingFolder.getText().toString().trim().equals("")) {
					testingDirectory = txtTestingFolder.getText().toString();
				}
				StringBuilder builder = new StringBuilder();
				builder.append("#### NAIVE BAYES CLASSIFIER ####");
				Data data = new Data(trainingDirectory, testingDirectory);
				Baseline baseline = new Baseline(1, false);
				baseline.train(data);
				builder.append(baseline.test(data));
				textResult.setText(builder.toString());
				if (btnStopWords.getSelection()) {
					builder.append("\n\n#### STOP WORDS REMOVED ####");
					baseline = new Baseline(1, true);
					baseline.train(data);
					builder.append(baseline.test(data));
					textResult.setText(builder.toString());
				}
				if (btnSwearWords.getSelection()) {
					builder.append("\n\n#### SWEAR WORDS HANDLED ####");
					SwearWordHandler swearHandler = new SwearWordHandler();
					swearHandler.train(data);
					builder.append(swearHandler.test(data));
					textResult.setText(builder.toString());
				}
				if (btnSpamPhrases.getSelection()) {
					builder.append("\n\n#### SPAM PHRASES HANDLED ####");
					SpamPhraseHandler spamHandler = new SpamPhraseHandler();
					spamHandler.train(data);
					builder.append(spamHandler.test(data));
					textResult.setText(builder.toString());
				}
				if (btnBigram.getSelection()) {
					builder.append("\n\n#### COMPUTING BIGRAM ####");
					baseline = new Baseline(2, true);
					baseline.train(data);
					builder.append(baseline.test(data));
					textResult.setText(builder.toString());
				}
				if (btnPosTagger.getSelection()) {
					builder.append("\n\n#### Combining POS Tags ####");
					BayesWithPOSTagger posTagger = new BayesWithPOSTagger();
					posTagger.train(data);
					builder.append(posTagger.test(data));
					textResult.setText(builder.toString());
				}
				if (btnSynonymy.getSelection()) {
					builder.append("\n\n#### SYNONYM HANDLING ####");
					Synonymy synonymy = new Synonymy();
					synonymy.train(data);
					builder.append(synonymy.test(data));
					textResult.setText(builder.toString());
				}
				if (btnHypernymy.getSelection()) {
					builder.append("\n\n#### HYPERNYM HANDLING ####");
					Hypernymy hypernymy = new Hypernymy();
					hypernymy.train(data);
					builder.append(hypernymy.test(data));
					textResult.setText(builder.toString());
				}
			}
		});
		btnCompute.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		btnCompute.setBounds(651, 8, 121, 31);
		btnCompute.setText("Compute");
		textResult = new Text(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		textResult.setToolTipText("Result");
		textResult.setEditable(false);
		textResult.setBounds(500, 47, 411, 340);
	}
}
