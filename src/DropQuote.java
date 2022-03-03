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
	private ArrayList<String> logicalChars;

	public DropQuote(String quote) throws SQLException, IOException {
		length = api.getLength(quote);
		logicalChars = api.getLogicalChars(quote);
		
		getCellCount();
		createWordList();
		createLetterBank();
		
		scrambleGrid = new String[rows][columns];
		puzzleGrid = new String[rows][columns];
		
		buildScrambleGrid();
		buildPuzzleGrid();
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
	
	private void getCellCount() {
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
		int index = 0;
		for(int i = 0; i<columns; i++) {
			for(int j = 0; j<rows; j++) {
				scrambleGrid[j][i] = letterBank.get(index);
				index++;
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

}
