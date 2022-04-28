package games;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import main.API;
import preferences.StripperQuotePreferences;

/**
 * @author neilh
 * This class receives a quote and generates a puzzle grid and solution grid of the type stripper quote
 */

public class StripperQuote {
	
	private String[][] bankGrid;
	private ArrayList<String> baseChars;
	private ArrayList<String> logicalChars = new ArrayList<String>();
	private API api = new API();
	
	public StripperQuote(String quote) throws SQLException, IOException {
		
		generateLogicalChars(quote);
		createLetterBank(quote);
	}

	//method creates the logicalChars arraylist as well as initializing important variables such as row count...
	private void generateLogicalChars(String quote) throws UnsupportedEncodingException, SQLException {
		int index = 0;
		int rows = 1;
		
		String newQuote = quote.trim();
		StripperQuotePreferences.LENGTH = api.getLength(newQuote);
		ArrayList<String> tempList = api.getLogicalChars(newQuote);
		
		//removing duplicate spaces
		String prevChar = tempList.get(0);
		logicalChars.add(prevChar);
		for(int i = 1; i<tempList.size(); i++) {
			if(!(tempList.get(i).equals(prevChar) && tempList.get(i).equals(" "))) {
				logicalChars.add(tempList.get(i));
				index++;
			}
			
			if(index>=StripperQuotePreferences.COLUMNS) {
				rows++;
				index = 0;
			}
			prevChar = tempList.get(i);
		}
		
		StripperQuotePreferences.ROWS = rows;
	}

	//method creates the arraylist used for the clue portion of the puzzle
	private void createLetterBank(String quote) throws UnsupportedEncodingException, SQLException {
		baseChars = api.getBaseChars(quote);
		
		if(StripperQuotePreferences.EXPERT_MODE)
			Collections.shuffle(baseChars);
		
		int index = 0;
		bankGrid = new String[StripperQuotePreferences.ROWS][StripperQuotePreferences.COLUMNS];
		for(int i = 0; i<StripperQuotePreferences.ROWS; i++) {
			for(int j = 0; j<StripperQuotePreferences.COLUMNS; j++) {
				if(index<baseChars.size()) {
					bankGrid[i][j] = baseChars.get(index);
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
}
