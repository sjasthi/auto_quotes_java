import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
public class ScrambleQuote {
	private String[] words;
	private String[][] bankGrid;
	private ArrayList<String> letterBank = new ArrayList<String>();
	private ArrayList<String> blankWords = new ArrayList<String>();
	private int cell_width;
	private int cell_height;
	private int rows = 1;
	private int columns = 14;
	private static int STARTING_X = 40;
	private static int STARTING_Y = 40;
	private int length;
	private API api = new API();
	
	public ScrambleQuote(String quote) throws SQLException, IOException {
		
		length = api.getLength(quote);
		createLetterBank(quote);
		createBlankWords(quote);
		
		cell_width = 30;
		cell_height = 20;
	}
	
		
	private void createLetterBank(String quote) throws UnsupportedEncodingException, SQLException {
		ArrayList<String> tempList = api.getLogicalChars(quote);
		int index = 0;
		for(int i = 0; i<tempList.size(); i++) {
			if(isValid(tempList.get(i).charAt(0))) {
				letterBank.add(tempList.get(i));
				index++;
			}
			if(index>columns) {
				rows++;
				index = 0;
			}
		}
		
		Collections.shuffle(letterBank);
		
		index = 0;
		bankGrid = new String[rows][columns];
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<columns; j++) {
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

	public void createBlankWords(String quote) throws UnsupportedEncodingException, SQLException {
		ArrayList<String> tempList = api.getLogicalChars(quote);
		String tempString = "";
		for(int i = 0; i<tempList.size(); i++) {
			tempString += tempList.get(i);
		}
		
		words = tempString.split(" ");
		for(int i = 0; i<words.length; i++) {
			String segment = "";
			for(int n = 0; n<words[i].length(); n++) {
				if(isValid(words[i].charAt(n))) {
					segment = segment + " ";
				} else {
					segment = segment + String.valueOf(words[i].charAt(n));
				}
				
			}
			blankWords.add(segment);
		}
	}
	
	public ArrayList<String> getBlankWords() {
		return blankWords;
	}
	
	public ArrayList<String> getLetterBank(){
		return letterBank;
	}
	
	public int getCellWidth() {
		return cell_width;
	}

	public int getCellHeight() {
		return cell_height;
	}

	public int getSTARTING_X() {
		return STARTING_X;
	}


	public int getSTARTING_Y() {
		return STARTING_Y;
	}
	
	public int getLength() {
		return length;
	}

	private boolean isValid(char a) {
		if(a==' ' || a=='!' || a=='.' || a==',' || a=='?')
			return false;
		else
			return true;
	}


	public int getColumns() {
		return columns;
	}
	
	public int getRows() {
		return rows;
	}
	
	

}
