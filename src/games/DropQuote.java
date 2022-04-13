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
 *
 */
public class DropQuote {

	
	
	private API api = new API();
	private String[][] scrambleGrid;
	private String[][] puzzleGrid;
	 
	
	//private ArrayList<String> wordList = new ArrayList<String>();
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
	
	private void initializeCellCount(String quote) throws UnsupportedEncodingException, SQLException {
		removePunctuation(quote);
		DropQuotePreferences.LENGTH = logicalChars.size();
		DropQuotePreferences.CELL_COUNT = 0.0;
		if(DropQuotePreferences.LENGTH>48) {
			DropQuotePreferences.CELL_COUNT = 60.0;
			DropQuotePreferences.ROWS = 6;
			DropQuotePreferences.COLUMNS = 10;
			DropQuotePreferences.CELL_WIDTH = 60;
			DropQuotePreferences.CELL_HEIGHT = 35;
		} else if(DropQuotePreferences.LENGTH>42) {
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
			DropQuotePreferences.CELL_HEIGHT = 50;
		} else if(DropQuotePreferences.LENGTH>15) {
			DropQuotePreferences.CELL_COUNT = 24.0;
			DropQuotePreferences.ROWS = 4;
			DropQuotePreferences.COLUMNS = 6;
			DropQuotePreferences.CELL_WIDTH = 100;
			DropQuotePreferences.CELL_HEIGHT = 60;
		} else {
			DropQuotePreferences.CELL_COUNT = 2.0;
			DropQuotePreferences.ROWS = 1;
			DropQuotePreferences.COLUMNS = 2;
			DropQuotePreferences.CELL_WIDTH = 280;
			DropQuotePreferences.CELL_HEIGHT = 180;
		}
	}
	

	private void buildScrambleGrid() throws UnsupportedEncodingException, SQLException {
		for(int i = 0; i<DropQuotePreferences.COLUMNS; i++) {
			randomizeColumn(i);
			dropColumn(i);
		}
	}
	
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
