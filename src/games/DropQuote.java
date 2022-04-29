package games;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import main.API;
import preferences.DropQuotePreferences;

/**
 * @author neilh
 * This class receives a quote and generates a puzzle grid and solution grid of the type drop quote
 */
public class DropQuote {
	private API api = new API();
	private String[][] scrambleGrid;
	private String[][] puzzleGrid; 
	private ArrayList<String> letterBank = new ArrayList<String>();
	private ArrayList<String> logicalChars = new ArrayList<String>();
	
	public DropQuote(String quote) throws SQLException, IOException {	
		
		initializeCellCount(quote);
		createLetterBank();
		
		scrambleGrid = new String[DropQuotePreferences.ROWS][DropQuotePreferences.COLUMNS];
		puzzleGrid = new String[DropQuotePreferences.ROWS][DropQuotePreferences.COLUMNS];
		
		buildPuzzleGrid();
		buildScrambleGrid();
	}
	
	//method initializes variables based on the length of the quote
	private void initializeCellCount(String quote) throws UnsupportedEncodingException, SQLException {
		removePunctuation(quote);
		DropQuotePreferences.CELL_COUNT = 0.0;
		DropQuotePreferences.LENGTH = logicalChars.size();
		if(DropQuotePreferences.LENGTH>42) {
			DropQuotePreferences.CELL_COUNT = 48.0;
			DropQuotePreferences.ROWS = 6;
			DropQuotePreferences.COLUMNS = 8;
			DropQuotePreferences.CELL_WIDTH = 80;
			DropQuotePreferences.CELL_HEIGHT = 35;
		} else if(DropQuotePreferences.LENGTH>30) {
			DropQuotePreferences.CELL_COUNT = 42.0;
			DropQuotePreferences.ROWS = 6;
			DropQuotePreferences.COLUMNS = 7;
			DropQuotePreferences.CELL_WIDTH = 90;
			DropQuotePreferences.CELL_HEIGHT = 35;
		} else if(DropQuotePreferences.LENGTH>24) {
			DropQuotePreferences.CELL_COUNT = 30.0;
			DropQuotePreferences.ROWS = 5;
			DropQuotePreferences.COLUMNS = 6;
			DropQuotePreferences.CELL_WIDTH = 100;
			DropQuotePreferences.CELL_HEIGHT = 40;
		} else if(DropQuotePreferences.LENGTH>15) {
			DropQuotePreferences.CELL_COUNT = 24.0;
			DropQuotePreferences.ROWS = 4;
			DropQuotePreferences.COLUMNS = 6;
			DropQuotePreferences.CELL_WIDTH = 100;
			DropQuotePreferences.CELL_HEIGHT = 50;
		} else {
			DropQuotePreferences.CELL_COUNT = 10.0;
			DropQuotePreferences.ROWS = 2;
			DropQuotePreferences.COLUMNS = 5;
			DropQuotePreferences.CELL_WIDTH = 120;
			DropQuotePreferences.CELL_HEIGHT = 100;
		}
	}
	
	//method that builds the grid for the puzzles clue
	private void buildScrambleGrid() throws UnsupportedEncodingException, SQLException {
		for(int i = 0; i<DropQuotePreferences.COLUMNS; i++) {
			randomizeColumn(i);
			dropColumn(i);
		}
	}
	
	//given a column, this method randomizes the elements of that column
	private void randomizeColumn(int column) {
		ArrayList<String> columnChars = new ArrayList<String>();
		for(int i = 0; i<DropQuotePreferences.ROWS; i++) {
			String currentChar = puzzleGrid[i][column];
			columnChars.add(currentChar);
		}
		
		Collections.shuffle(columnChars);
		
		for(int i = 0; i<DropQuotePreferences.ROWS; i++) {
			scrambleGrid[i][column] = columnChars.get(i);
		}	
	}
	
	//method removes punctuation from quote
	private void removePunctuation(String quote) throws UnsupportedEncodingException, SQLException {
		String newQuote = quote.trim();
		ArrayList<String> tempList = api.getLogicalChars(newQuote);
		
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

	//method builds the grid for the puzzle
	private void buildPuzzleGrid() {
		int index = 0;
		for(int i = 0; i<DropQuotePreferences.ROWS; i++) {
			for(int j = 0; j<DropQuotePreferences.COLUMNS; j++) {
				if(index<DropQuotePreferences.LENGTH) {					
					puzzleGrid[i][j] = logicalChars.get(index);
					index++;
				} else {
					puzzleGrid[i][j] = " ";
				}
			}
		}
	}
	
	//method creates an arraylist of the characters for letter bank(clue of the puzzle)
	public void createLetterBank() throws UnsupportedEncodingException, SQLException {
		int index = 0;
		for(int i = 0; i<logicalChars.size(); i++) {
			letterBank.add(logicalChars.get(i));
			index++;
		}
		for(int i = index; i<DropQuotePreferences.CELL_COUNT; i++) {
			letterBank.add(" ");
		}
		Collections.shuffle(letterBank);
	}
	
	//given a column, this method slides all the elements to the bottom of the column, leaving the spaces at the top
	private void dropColumn(int column) {
		int cell1, cell2;
		while(!columnInOrder(column)) {
			cell1 = DropQuotePreferences.ROWS-1;
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
	
	//method checks if the elements of a column are in the desired arrangement(all of them at the bottom) and returns true or false
	private boolean columnInOrder(int column) {
		boolean result = true;
		
		String currentCell = scrambleGrid[DropQuotePreferences.ROWS-1][column];
		for(int i = 2; i<=DropQuotePreferences.ROWS; i++) {
			String nextCell = scrambleGrid[DropQuotePreferences.ROWS-i][column];
			if(currentCell.equals(" ")) {
				if(!nextCell.equals(" "))
					result = false;
			}
			currentCell = nextCell;
		}
		return result;
	}

	//given two cells of a column, this method swaps the elements of those cells
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
