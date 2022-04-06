package games;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import main.API;
import preferences.StripperQuotePreferences;


/**
 * 
 */

/**
 * @author neilh
 *
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
			
			if(index>StripperQuotePreferences.COLUMNS) {
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
		StripperQuotePreferences.ROWS = rows;
	}

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