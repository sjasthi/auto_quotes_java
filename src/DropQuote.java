import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author neilh
 *
 */
public class DropQuote {

	class Preferences {
		public final static String PPT_FILE_NAME = "Puzzles.ppt";
		public final static String TITLE = "Drop Quote";
		public final static String FONT_NAME = "NATS";
		public final static double GRID_FONT_SIZE = 18.0;
		public final static double TITLE_FONT_SIZE = 24.0;
		public static double CELL_COUNT;
		public final static boolean HAS_SPACES = true;
		public final static boolean HAS_PUNCTUATION = true;
		public final static boolean HAS_BOARDERS = true;
		public final static Color FILL_COLOR = Color.BLUE; 
		public final static Color TEXT_COLOR = Color.BLUE;
		public final static Color TITLE_COLOR = Color.BLUE;
		public final static Color GRID_COLOR = Color.BLUE;
		public final static Color SLIDE_NUMBER_COLOR = Color.BLUE;
		public final static int PUZZLE_COUNT = 5;
		public final static int STARTING_X = 40;
		public final static int STARTING_Y = 80;
		public static int LENGTH;
		public static int ROWS;
		public static int COLUMNS;
		public static int CELL_WIDTH;
		public static int CELL_HEIGHT;
	}
	
	private API api = new API();
	private String[][] scrambleGrid;
	private String[][] puzzleGrid;
	 
	
	private ArrayList<String> wordList = new ArrayList<String>();
	private ArrayList<String> letterBank = new ArrayList<String>();
	private ArrayList<String> logicalChars = new ArrayList<String>();
	
	public DropQuote(String quote) throws SQLException, IOException {	
		
		initializeCellCount(quote);
		createWordList();
		createLetterBank();
		
		scrambleGrid = new String[Preferences.ROWS][Preferences.COLUMNS];
		puzzleGrid = new String[Preferences.ROWS][Preferences.COLUMNS];
		
		buildPuzzleGrid();
		buildScrambleGrid();
	}
	
	private void createWordList() {
		for(int i = 0; i<Preferences.CELL_COUNT; i++) {
			if(i<Preferences.LENGTH) {
				wordList.add(logicalChars.get(i));
			} else {
				wordList.add(" ");
			}	
		}
	}
	
	public ArrayList<String> getWordList() {
		return wordList;
	}
	
	private void initializeCellCount(String quote) throws UnsupportedEncodingException, SQLException {
		removePunctuation(quote);
		Preferences.LENGTH = logicalChars.size();
		Preferences.CELL_COUNT = 0.0;
		if(Preferences.LENGTH>48) {
			Preferences.CELL_COUNT = 60.0;
			Preferences.ROWS = 6;
			Preferences.COLUMNS = 10;
			Preferences.CELL_WIDTH = 60;
			Preferences.CELL_HEIGHT = 35;
		} else if(Preferences.LENGTH>42) {
			Preferences.CELL_COUNT = 48.0;
			Preferences.ROWS = 6;
			Preferences.COLUMNS = 8;
			Preferences.CELL_WIDTH = 80;
			Preferences.CELL_HEIGHT = 35;
		} else if(Preferences.LENGTH>30) {
			Preferences.CELL_COUNT = 42.0;
			Preferences.ROWS = 6;
			Preferences.COLUMNS = 7;
			Preferences.CELL_WIDTH = 90;
			Preferences.CELL_HEIGHT = 35;
		} else if(Preferences.LENGTH>24) {
			Preferences.CELL_COUNT = 30.0;
			Preferences.ROWS = 5;
			Preferences.COLUMNS = 6;
			Preferences.CELL_WIDTH = 100;
			Preferences.CELL_HEIGHT = 50;
		} else if(Preferences.LENGTH>15) {
			Preferences.CELL_COUNT = 24.0;
			Preferences.ROWS = 4;
			Preferences.COLUMNS = 6;
			Preferences.CELL_WIDTH = 100;
			Preferences.CELL_HEIGHT = 60;
		} else {
			Preferences.CELL_COUNT = 2.0;
			Preferences.ROWS = 1;
			Preferences.COLUMNS = 2;
			Preferences.CELL_WIDTH = 280;
			Preferences.CELL_HEIGHT = 180;
		}
	}
	

	private void buildScrambleGrid() throws UnsupportedEncodingException, SQLException {
		for(int i = 0; i<Preferences.COLUMNS; i++) {
			randomizeColumn(i);
			dropColumn(i);
		}
	}
	
	private void randomizeColumn(int column) {
		ArrayList<String> columnChars = new ArrayList<String>();
		for(int i = 0; i<Preferences.ROWS; i++) {
			String currentChar = puzzleGrid[i][column];
			columnChars.add(currentChar);
		}
		
		Collections.shuffle(columnChars);
		
		for(int i = 0; i<Preferences.ROWS; i++) {
			scrambleGrid[i][column] = columnChars.get(i);
		}
		
	}
	
	private void removePunctuation(String quote) throws UnsupportedEncodingException, SQLException {
		ArrayList<String> tempList = api.getLogicalChars(quote);
		ArrayList<String> punctuation = new ArrayList<String>();
		punctuation.add("'");
		punctuation.add(".");
		punctuation.add("?");
		punctuation.add("!");
		punctuation.add(",");
		punctuation.add("@");
		punctuation.add("#");
		punctuation.add("$");
		punctuation.add("%");
		punctuation.add("<");
		punctuation.add(">");
		punctuation.add("^");
		punctuation.add("&");
		punctuation.add("`");
		punctuation.add("~");
		
		for(int i = 0; i<tempList.size(); i++) {
			if(!punctuation.contains(tempList.get(i))) {
				logicalChars.add(tempList.get(i));
			}
		}
	}

	private void buildPuzzleGrid() {
		int index = 0;
		for(int i = 0; i<Preferences.ROWS; i++) {
			for(int j = 0; j<Preferences.COLUMNS; j++) {
				puzzleGrid[i][j] = wordList.get(index);
				index++;
			}
		}
	}
	
	public void createLetterBank() throws UnsupportedEncodingException, SQLException {
		int index = 0;
		for(int i = 0; i<logicalChars.size(); i++) {
			letterBank.add(logicalChars.get(i));
			index++;
		}
		for(int i = index; i<Preferences.CELL_COUNT; i++) {
			letterBank.add(" ");
		}
		Collections.shuffle(letterBank);
	}
	
	private void dropColumn(int column) {
		int cell1, cell2;
		while(!columnInOrder(column)) {
			cell1 = Preferences.ROWS-1;
			cell2 = 0;
			while(!scrambleGrid[cell1][column].equals(" ")) {
				cell1--;
			}
			while(scrambleGrid[cell2][column].equals(" ")) {
				cell2++;
			}
			swapCell(column, cell1, cell2);
		}
	}
	
	private boolean columnInOrder(int column) {
		boolean result = true;
		
		String currentCell = scrambleGrid[Preferences.ROWS-1][column];
		for(int i = 2; i<=Preferences.ROWS; i++) {
			String nextCell = scrambleGrid[Preferences.ROWS-i][column];
			if(currentCell.equals(" ")) {
				if(!nextCell.equals(" "))
					result = false;
			}
			currentCell = nextCell;
		}
		return result;
	}

	private void swapCell(int column, int row1, int row2) {
		String temp = scrambleGrid[row1][column];
		scrambleGrid[row1][column] = scrambleGrid[row2][column];
		scrambleGrid[row2][column] = temp;
	}
	
	public String[][] getScrambleGrid() {
		return scrambleGrid;
	}
	
	public String[][] getPuzzleGrid() {
		return puzzleGrid;
	}
	
}
