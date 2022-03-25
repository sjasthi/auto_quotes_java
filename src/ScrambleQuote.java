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
		public final static double GRID_FONT_SIZE = 12.0;
		public final static double TITLE_FONT_SIZE = 24.0;
		public final static boolean hasSpaces = true;
		public final static boolean hasPunctuation = true;
		public static final int PUZZLE_COUNT = 5;
		public static final Color FILL_COLOR = Color.BLUE; 
		public static final Color TEXT_COLOR = Color.BLUE;
		public static final Color TITLE_COLOR = Color.BLUE;
		public static final Color GRID_COLOR = Color.BLUE;
		public static final Color SLIDE_NUMBER_COLOR = Color.BLUE;
		public static final boolean HAS_BOARDERS = true;
	}
	
	private String[][] bankGrid;
	private String[][] puzzleGrid;
	private ArrayList<String> letterBank = new ArrayList<String>();
	private ArrayList<String> logicalChars = new ArrayList<String>();
	private int cell_width = 40;
	private int cell_height = 30;
	private int rows = 1;
	private int columns = 16;
	private static int STARTING_X = 40;
	private static int STARTING_Y = 80;
	private API api = new API();
	private static double GRID_FONT_SIZE = Preferences.GRID_FONT_SIZE + 6.0;
	
	public ScrambleQuote(String quote) throws SQLException, IOException {
		
		generateLogicalChars(quote);
		createPuzzleGrid();
		createLetterBank();
		
	}

	private void generateLogicalChars(String quote) throws UnsupportedEncodingException, SQLException {
		
		ArrayList<String> tempList = api.getLogicalChars(quote);
		String prevChar = tempList.get(0);
		logicalChars.add(prevChar);
		for(int i = 1; i<tempList.size(); i++) {
			if(!(tempList.get(i).equals(prevChar) && tempList.get(i).equals(" "))) {
				logicalChars.add(tempList.get(i));
				
			}
			prevChar = tempList.get(i);
		}
		
		while(logicalChars.get(0).equals(" ")) {
			logicalChars.remove(0);
		}
		
		while(logicalChars.get(logicalChars.size()-1).equals(" ")) {
			logicalChars.remove(logicalChars.size()-1);
		}
	}

	private void createLetterBank() throws UnsupportedEncodingException, SQLException {
		for(int i = 0; i<logicalChars.size(); i++) {
			if(isValid(logicalChars.get(i).charAt(0))) {
				letterBank.add(logicalChars.get(i));
			}
		}
		
		Collections.shuffle(letterBank);
		
		int index = 0;
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
	
	private void createPuzzleGrid() {
		int index = 0;
		for(int i = 0; i<logicalChars.size(); i++) {
			index++;
			if(index>columns) {
				rows++;
				index = 0;
			}
		}
		
		index = 0;
		puzzleGrid = new String[rows][columns];
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<columns; j++) {
				if(index<logicalChars.size()) {
					puzzleGrid[i][j] = logicalChars.get(index);
					index++;
				} else {
					puzzleGrid[i][j] = " ";
				}
			}
		}
	} 

	public String[][] getBankGrid() {
		return bankGrid;
	}
	
	public String[][] getPuzzleGrid() {
		return puzzleGrid;
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
	
	public double getGRID_FONT_SIZE() {
		return GRID_FONT_SIZE;
	}

}
