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
	
	private String[][] bankGrid;
	private ArrayList<String> letterBank = new ArrayList<String>();
	private ArrayList<String> logicalChars;
	private int cell_width;
	private int cell_height;
	private int rows = 1;
	private int columns = 20;
	private static int STARTING_X = 40;
	private static int STARTING_Y = 80;
	private int length;
	private int wordCount = 1;
	private API api = new API();
	
	public ScrambleQuote(String quote) throws SQLException, IOException {
		
		length = api.getLength(quote);
		logicalChars = api.getLogicalChars(quote);
		createLetterBank();
		generateWordCount();
		
		cell_width = 30;
		cell_height = 20;
	}
	
	

	private void createLetterBank() throws UnsupportedEncodingException, SQLException {
		int index = 0;
		for(int i = 0; i<logicalChars.size(); i++) {
			if(isValid(logicalChars.get(i).charAt(0))) {
				letterBank.add(logicalChars.get(i));
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
	
	private void generateWordCount() {
		for(int i = 0; i<length; i++) {
			if(logicalChars.get(i).equals(" "))
				wordCount++;
		}
	}
	
	public int getWordCount() {
		return wordCount;
	}

	public String[][] getBankGrid() {
		return bankGrid;
	}
	
	public ArrayList<String> getLogicalChars(){
		return logicalChars;
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
