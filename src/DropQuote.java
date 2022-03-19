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
public class DropQuote {
	
	private API api = new API();
	private String[][] scrambleGrid;
	private String[][] puzzleGrid;
	private double cellCount;
	private int length;
	private int rows;
	private int columns;
	private int cell_width;
	private int cell_height; 
	private static int STARTING_X = 40;
	private static int STARTING_Y = 80;
	private ArrayList<String> wordList = new ArrayList<String>();
	private ArrayList<String> letterBank = new ArrayList<String>();
	private ArrayList<String> logicalChars = new ArrayList<String>();
	private static double GRID_FONT_SIZE = Preferences.GRID_FONT_SIZE;

	public DropQuote(String quote) throws SQLException, IOException {	
		
		getCellCount(quote);
		createWordList();
		createLetterBank();
		
		scrambleGrid = new String[rows][columns];
		puzzleGrid = new String[rows][columns];
		
		buildPuzzleGrid();
		buildScrambleGrid();
	}
	
	private void createWordList() {
		for(int i = 0; i<cellCount; i++) {
			if(i<length) {
				wordList.add(logicalChars.get(i));
			} else {
				wordList.add(" ");
			}	
		}
	}
	
	public ArrayList<String> getWordList() {
		return wordList;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	private void getCellCount(String quote) throws UnsupportedEncodingException, SQLException {
		removePunctuation(quote);
		length = logicalChars.size();
		cellCount = 0.0;
		if(length>48) {
			cellCount = 60.0;
			rows = 6;
			columns = 10;
			cell_width = 60;
			cell_height = 25;
		} else if(length>42) {
			cellCount = 48.0;
			rows = 6;
			columns = 8;
			cell_width = 80;
			cell_height = 25;
		} else if(length>30) {
			cellCount = 42.0;
			rows = 6;
			columns = 7;
			cell_width = 90;
			cell_height = 25;
		} else if(length>24) {
			cellCount = 30.0;
			rows = 5;
			columns = 6;
			cell_width = 100;
			cell_height = 40;
		} else if(length>15) {
			cellCount = 24.0;
			rows = 4;
			columns = 6;
			cell_width = 100;
			cell_height = 50;
		} else {
			cellCount = 2.0;
			rows = 1;
			columns = 2;
			cell_width = 280;
			cell_height = 180;
		}
	}
	

	private void buildScrambleGrid() throws UnsupportedEncodingException, SQLException {
		for(int i = 0; i<columns; i++) {
			randomizeColumn(i);
			dropColumn(i);
		}
	}
	
	private void randomizeColumn(int column) {
		ArrayList<String> columnChars = new ArrayList<String>();
		for(int i = 0; i<rows; i++) {
			String currentChar = puzzleGrid[i][column];
			columnChars.add(currentChar);
		}
		
		Collections.shuffle(columnChars);
		
		for(int i = 0; i<rows; i++) {
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
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<columns; j++) {
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
		for(int i = index; i<cellCount; i++) {
			letterBank.add(" ");
		}
		Collections.shuffle(letterBank);
	}
	
	private void dropColumn(int column) {
		int cell1, cell2;
		while(!columnInOrder(column)) {
			cell1 = rows-1;
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
		
		String currentCell = scrambleGrid[rows-1][column];
		for(int i = 2; i<=rows; i++) {
			String nextCell = scrambleGrid[rows-i][column];
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
	
	public String[][] getScrambleGrid() {
		return scrambleGrid;
	}
	
	public String[][] getPuzzleGrid() {
		return puzzleGrid;
	}
	
	public double getGRID_FONT_SIZE() {
		return GRID_FONT_SIZE;
	}

}
