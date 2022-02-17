import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * 
 */

/**
 * @author neilh
 *
 */
public class ScrambleQuote extends Puzzle {
	private String quote;
	private String[] words;
	private ArrayList<String> letterBank = new ArrayList<String>();

	public ScrambleQuote(String quote) throws SQLException, IOException {
		super(quote);
		this.quote = quote;
		getWords();
		createLetterBank();
		cell_width = 40;
		cell_height = 20;
	}
	
	public void getWords() {
		words = quote.split(" ");
	}
	
	public ArrayList<String> buildGrid() {
		//System.out.println(letterBank + "\n");
		ArrayList<String> blankWords = new ArrayList<String>();
		for(int i = 0; i<words.length; i++) {
			String segment = "";
			for(int n = 0; n<words[i].length(); n++) {
				//segment = segment + "[";
				if(isValid(words[i].charAt(n))) {
					segment = segment + " ";
				} else {
					segment = segment + String.valueOf(words[i].charAt(n));
				}
				//segment = segment + "]";
			}
			//segment = segment + "|";
			//System.out.print(segment);
			blankWords.add(segment);
		}
		return blankWords;
	}
	
	private void createLetterBank() {
		for(int i = 0; i<quote.length(); i++) {
			if(isValid(quote.charAt(i))) {
				letterBank.add(String.valueOf(quote.charAt(i)));
			}
		}
		Collections.shuffle(letterBank);
	}
	
	public ArrayList<String> getLetterBank(){
		return letterBank;
	}

	private boolean isValid(char a) {
		if(a==' ' || a=='!' || a=='.' || a==',' || a=='?')
			return false;
		else
			return true;
	}
	
	

}
