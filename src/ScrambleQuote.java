import java.awt.Color;
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
	
	class Preferences {
		public final static String PPT_FILE_NAME = "Puzzles.ppt";
		public final static String FONT_NAME = "NATS";
		public final static String TITLE = "Scramble Quote";
		public final static double GRID_FONT_SIZE = 12.0;
		public final static double TITLE_FONT_SIZE = 24.0;
		public final static boolean HAS_SPACES = true;
		public final static boolean HAS_PUNCTUATION = true;
		public final static boolean HAS_BOARDERS = true;
		public final static Color FILL_COLOR = Color.BLUE; 
		public final static Color TEXT_COLOR = Color.BLUE;
		public final static Color TITLE_COLOR = Color.BLUE;
		public final static Color GRID_COLOR = Color.BLUE;
		public final static Color SLIDE_NUMBER_COLOR = Color.BLUE;
		public final static int PUZZLE_COUNT = 5;
		public final static int CELL_WIDTH = 40;
		public final static int CELL_HEIGHT = 30;
		public final static int STARTING_X = 40;
		public final static int STARTING_Y = 80;
		public final static int COLUMNS = 16;
		public static int ROWS;

	}
	
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
			
			if(index>Preferences.COLUMNS) {
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
		
		Preferences.ROWS = rows;
	}

	private void createLetterBank() throws UnsupportedEncodingException, SQLException {
		for(int i = 0; i<logicalChars.size(); i++) {
			if(isValid(logicalChars.get(i).charAt(0))) {
				letterBank.add(logicalChars.get(i));
			}
		}
		
		Collections.shuffle(letterBank);
		
		int index = 0;
		bankGrid = new String[Preferences.ROWS][Preferences.COLUMNS];
		for(int i = 0; i<Preferences.ROWS; i++) {
			for(int j = 0; j<Preferences.COLUMNS; j++) {
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
