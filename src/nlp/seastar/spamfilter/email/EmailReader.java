package nlp.seastar.spamfilter.email;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Ashwini
 * @author Sagar
 *
 */
public class EmailReader {
	public Email readEmail(boolean isHam, File emailFile) {
		Scanner scanner;
		Email email = new Email();
		try {
			scanner = new Scanner(emailFile);
			while (scanner.hasNextLine()) {
				for (String token : (scanner.nextLine()).split(" ")) {
					Word word = new Word(isHam, token);
					email.setEmailWord(word);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Please check the email file.");
		}
		return email;
	}
}
