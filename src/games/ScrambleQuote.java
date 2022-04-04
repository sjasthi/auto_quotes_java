package games;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import main.API;
import preferences.ScrambleQuotePreferences;


/**
 * 
 */

/**
 * @author neilh
 *
 */
public class ScrambleQuote {
	
	private String[][] bankGrid;
	private ArrayList<String> letterBank = new ArrayList<String>();
	private ArrayList<String> logicalChars = new ArrayList<String>();
	private API api = new API();
	
	public ScrambleQuote(String quote) throws SQLException, IOException {
		
		generateLogicalChars(quote);
		createLetterBank();
		
	}

	private void generateLogicalChars(String quote) throws UnsupportedEncodingException, SQLException {
		int index = 0;
		int rows = 1;
		ArrayList<String> tempList = api.getLogicalChars(quote);
		String prevChar = tempList.get(0);
		logicalChars.add(prevChar);
		for(int i = 1; i<tempList.size(); i++) {
			
			if(!(tempList.get(i).equals(prevChar) && tempList.get(i).equals(" "))) {
				logicalChars.add(tempList.get(i));
				index++;
			}
			
			if(index>ScrambleQuotePreferences.COLUMNS) {
				rows++;
				index = 0;
			}
			prevChar = tempList.get(i);
		}
		
		while(logicalChars.get(0).equals(" ")) {
			logicalChars.remove(0);
		}
		
		while(logicalChars.get(logicalChars.size()-1).equals(" ")) {
			logicalChars.remove(logicalChars.size()-1);
		}
		
		ScrambleQuotePreferences.ROWS = rows;
	}

	private void createLetterBank() throws UnsupportedEncodingException, SQLException {
		for(int i = 0; i<logicalChars.size(); i++) {
			if(isValid(logicalChars.get(i).charAt(0))) {
				letterBank.add(logicalChars.get(i));
			}
		}
		
		Collections.shuffle(letterBank);
		
		int index = 0;
		bankGrid = new String[ScrambleQuotePreferences.ROWS][ScrambleQuotePreferences.COLUMNS];
		for(int i = 0; i<ScrambleQuotePreferences.ROWS; i++) {
			for(int j = 0; j<ScrambleQuotePreferences.COLUMNS; j++) {
				if(index<letterBank.size()) {
					bankGrid[i][j] = letterBank.get(index);
					index++;
				} else {
					bankGrid[i][j] = " ";
				}
				
			}
		}
	}
	

	public String[][] getBankGrid() {
		return bankGrid;
	}
	
	
	public ArrayList<String> getLogicalChars(){
		return logicalChars;
	}

	private boolean isValid(char a) {
		if(a==' ' || a=='!' || a=='.' || a==',' || a=='?')
			return false;
		else
			return true;
	}

}
