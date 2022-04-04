package games;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import main.API;
import preferences.FloatQuotePreferences;

public class FloatQuote {
	
	
	private API api = new API();
	private String[][] scrambleGrid;
	private String[][] puzzleGrid;
	 
	
	private ArrayList<String> wordList = new ArrayList<String>();
	private ArrayList<String> letterBank = new ArrayList<String>();
	private ArrayList<String> logicalChars = new ArrayList<String>();
	
	public FloatQuote(String quote) throws SQLException, IOException {	
		
		initializeCellCount(quote);
		createWordList();
		createLetterBank();
		
		scrambleGrid = new String[FloatQuotePreferences.ROWS][FloatQuotePreferences.COLUMNS];
		puzzleGrid = new String[FloatQuotePreferences.ROWS][FloatQuotePreferences.COLUMNS];
		
		buildPuzzleGrid();
		buildScrambleGrid();
	}
	
	private void createWordList() {
		for(int i = 0; i<FloatQuotePreferences.CELL_COUNT; i++) {
			if(i<FloatQuotePreferences.LENGTH) {
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
		FloatQuotePreferences.LENGTH = logicalChars.size();
		FloatQuotePreferences.CELL_COUNT = 0.0;
		if(FloatQuotePreferences.LENGTH>48) {
			FloatQuotePreferences.CELL_COUNT = 60.0;
			FloatQuotePreferences.ROWS = 6;
			FloatQuotePreferences.COLUMNS = 10;
			FloatQuotePreferences.CELL_WIDTH = 60;
			FloatQuotePreferences.CELL_HEIGHT = 35;
		} else if(FloatQuotePreferences.LENGTH>42) {
			FloatQuotePreferences.CELL_COUNT = 48.0;
			FloatQuotePreferences.ROWS = 6;
			FloatQuotePreferences.COLUMNS = 8;
			FloatQuotePreferences.CELL_WIDTH = 80;
			FloatQuotePreferences.CELL_HEIGHT = 35;
		} else if(FloatQuotePreferences.LENGTH>30) {
			FloatQuotePreferences.CELL_COUNT = 42.0;
			FloatQuotePreferences.ROWS = 6;
			FloatQuotePreferences.COLUMNS = 7;
			FloatQuotePreferences.CELL_WIDTH = 90;
			FloatQuotePreferences.CELL_HEIGHT = 35;
		} else if(FloatQuotePreferences.LENGTH>24) {
			FloatQuotePreferences.CELL_COUNT = 30.0;
			FloatQuotePreferences.ROWS = 5;
			FloatQuotePreferences.COLUMNS = 6;
			FloatQuotePreferences.CELL_WIDTH = 100;
			FloatQuotePreferences.CELL_HEIGHT = 50;
		} else if(FloatQuotePreferences.LENGTH>15) {
			FloatQuotePreferences.CELL_COUNT = 24.0;
			FloatQuotePreferences.ROWS = 4;
			FloatQuotePreferences.COLUMNS = 6;
			FloatQuotePreferences.CELL_WIDTH = 100;
			FloatQuotePreferences.CELL_HEIGHT = 60;
		} else {
			FloatQuotePreferences.CELL_COUNT = 2.0;
			FloatQuotePreferences.ROWS = 1;
			FloatQuotePreferences.COLUMNS = 2;
			FloatQuotePreferences.CELL_WIDTH = 280;
			FloatQuotePreferences.CELL_HEIGHT = 180;
		}
	}
	

	private void buildScrambleGrid() throws UnsupportedEncodingException, SQLException {
		for(int i = 0; i<FloatQuotePreferences.COLUMNS; i++) {
			randomizeColumn(i);
			dropColumn(i);
		}
	}
	
	private void randomizeColumn(int column) {
		ArrayList<String> columnChars = new ArrayList<String>();
		for(int i = 0; i<FloatQuotePreferences.ROWS; i++) {
			String currentChar = puzzleGrid[i][column];
			columnChars.add(currentChar);
		}
		
		Collections.shuffle(columnChars);
		
		for(int i = 0; i<FloatQuotePreferences.ROWS; i++) {
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
		for(int i = 0; i<FloatQuotePreferences.ROWS; i++) {
			for(int j = 0; j<FloatQuotePreferences.COLUMNS; j++) {
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
		for(int i = index; i<FloatQuotePreferences.CELL_COUNT; i++) {
			letterBank.add(" ");
		}
		Collections.shuffle(letterBank);
	}
	
	private void dropColumn(int column) {
		int cell1, cell2;
		while(!columnInOrder(column)) {
			cell1 = 0;
			cell2 = FloatQuotePreferences.ROWS-1;
			while(!scrambleGrid[cell1][column].equals(" ")) {
				cell1++;
			}
			while(scrambleGrid[cell2][column].equals(" ")) {
				cell2--;
			}
			swapCell(column, cell1, cell2);
		}
	}
	
	private boolean columnInOrder(int column) {
		boolean result = true;
		
		String currentCell = scrambleGrid[0][column];
		for(int i = 2; i<=FloatQuotePreferences.ROWS; i++) {
			String nextCell = scrambleGrid[i-1][column];
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
