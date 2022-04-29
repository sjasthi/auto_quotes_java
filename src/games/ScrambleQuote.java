package games;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import main.API;
import preferences.ScrambleQuotePreferences;

/**
 * @author neilh
 * This class receives a quote and generates a puzzle grid and solution grid of the type scramble quote
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

	//this method assembles the logicalChars arraylist as well as initializing important variables such as row count...
	private void generateLogicalChars(String quote) throws UnsupportedEncodingException, SQLException {
		int index = 0;
		int rows = 1;
		
		String newQuote = quote.trim();
		ScrambleQuotePreferences.LENGTH = api.getLength(newQuote);
		ArrayList<String> tempList = api.getLogicalChars(newQuote);
		String prevChar = tempList.get(0);
		logicalChars.add(prevChar);
		for(int i = 1; i<tempList.size(); i++) {
			
			if(!(tempList.get(i).equals(prevChar) && tempList.get(i).equals(" "))) {
				logicalChars.add(tempList.get(i));
				index++;
			}
			
			if(index>=ScrambleQuotePreferences.COLUMNS) {
				rows++;
				index = 0;
			}
			prevChar = tempList.get(i);
		}
			
		ScrambleQuotePreferences.ROWS = rows;
	}

	//this method assembles the arraylist for the clue part of the puzzle
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

	//method checks if a given char is a space/punctuation mark or not and returns true or false
	private boolean isValid(char a) {
		if(a==' ' || a=='!' || a=='.' || a==',' || a=='?' || a==';')
			return false;
		else
			return true;
	}

}
