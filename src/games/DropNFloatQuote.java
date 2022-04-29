package games;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import main.API;
import preferences.DropNFloatQuotePreferences;

/**
 * @author Neil Haggerty This class generates the grids for the drop n float puzzle.        
 */

public class DropNFloatQuote {
	private String[][] floatGrid;
	private String[][] dropGrid;
	private String[][] scrambleGrid;
	private API api = new API();
	
	ArrayList<String> logicalChars1;
	ArrayList<String> logicalChars2;
	
	public DropNFloatQuote(String quote1, String quote2) throws UnsupportedEncodingException, SQLException {
		initializeCellCount(quote1.trim(), quote2.trim());
		
		floatGrid = new String[DropNFloatQuotePreferences.FLOAT_ROWS][DropNFloatQuotePreferences.COLUMNS];
		dropGrid = new String[DropNFloatQuotePreferences.DROP_ROWS][DropNFloatQuotePreferences.COLUMNS];
		scrambleGrid = new String[DropNFloatQuotePreferences.FLOAT_ROWS + DropNFloatQuotePreferences.DROP_ROWS][DropNFloatQuotePreferences.COLUMNS];
		
		buildFloatGrid();
		buildDropGrid();
		buildScrambleGrid();
		finalizeRows();
	}
	
	//this method removes any empty rows from the final scrambled grid
	private void finalizeRows() {
		int totalRowCount = DropNFloatQuotePreferences.DROP_ROWS + DropNFloatQuotePreferences.FLOAT_ROWS;
		int rows = totalRowCount;
		for(int i = 0; i<totalRowCount; i++) {
			if(rowIsEmpty(i)) {
				rows--;
			}
		}
		String[][] newScrambleGrid = new String[rows][DropNFloatQuotePreferences.COLUMNS];
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<DropNFloatQuotePreferences.COLUMNS; j++) {
				newScrambleGrid[i][j] = scrambleGrid[i][j];
			}
		}
		DropNFloatQuotePreferences.TOTAL_ROWS = rows;
		scrambleGrid = newScrambleGrid;
	}

	//method checks if a given row of a grid is empty and returns true or false
	private boolean rowIsEmpty(int row) {
		boolean result = true;
		for(int i = 0; i<DropNFloatQuotePreferences.COLUMNS; i++) {
			if(!scrambleGrid[row][i].equals(" "))
				result = false;
		}
		return result;
	}

	//method removes punctuation and returns the updated ArrayList
	private ArrayList<String> removePunctuation(String quote) throws UnsupportedEncodingException, SQLException {
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
			if(punctuation.contains(tempList.get(i))) {
				tempList.remove(i);
			}
		}
		
		return tempList;
	}
	
	//method creates the puzzle grid displayed at the top of the slide
	private void buildFloatGrid() {
		int index = 0;
		for(int i = 0; i<DropNFloatQuotePreferences.FLOAT_ROWS; i++) {
			for(int j = 0; j<DropNFloatQuotePreferences.COLUMNS; j++) {
				if(index<DropNFloatQuotePreferences.FLOAT_LENGTH) {					
					floatGrid[i][j] = logicalChars1.get(index);
					index++;
				} else {
					floatGrid[i][j] = " ";
				}
			}
		}
	}
	
	//method creates the puzzle grid displayed at the bottom of the slide
	private void buildDropGrid() {
		int index = 0;
		for(int i = 0; i<DropNFloatQuotePreferences.DROP_ROWS; i++) {
			for(int j = 0; j<DropNFloatQuotePreferences.COLUMNS; j++) {
				if(index<DropNFloatQuotePreferences.DROP_LENGTH) {					
					dropGrid[i][j] = logicalChars2.get(index);
					index++;
				} else {
					dropGrid[i][j] = " ";
				}
			}
		}
	}
	
	//method creates the clue grid displayed in the center of the slide
	private void buildScrambleGrid() {
		int totalRowCount = DropNFloatQuotePreferences.DROP_ROWS + DropNFloatQuotePreferences.FLOAT_ROWS;
		
		for(int i = 0; i<DropNFloatQuotePreferences.FLOAT_ROWS; i++) {
			for(int j = 0; j<DropNFloatQuotePreferences.COLUMNS; j++) {
				scrambleGrid[i][j] = floatGrid[i][j];
			}
		}
		
		for(int i = DropNFloatQuotePreferences.FLOAT_ROWS; i<totalRowCount; i++) {
			for(int j = 0; j<DropNFloatQuotePreferences.COLUMNS; j++) {
				scrambleGrid[i][j] = dropGrid[i-DropNFloatQuotePreferences.FLOAT_ROWS][j];
			}
		}
		
		randomizeColumns();
		sortColumns();
	}
	
	//method randomizes the elements of a given column
	private void randomizeColumns() {
		int totalRowCount = DropNFloatQuotePreferences.DROP_ROWS + DropNFloatQuotePreferences.FLOAT_ROWS;
		ArrayList<String> currentColumn;
		for(int i = 0; i<DropNFloatQuotePreferences.COLUMNS; i++) {
			currentColumn = new ArrayList<String>();
			for(int j = 0; j<totalRowCount; j++) {
				currentColumn.add(scrambleGrid[j][i]);
			}
			Collections.shuffle(currentColumn);
			for(int j = 0; j<totalRowCount; j++) {
				scrambleGrid[j][i] = currentColumn.remove(0);
			}
		}
	}
	
	//method that arranges the elements of the clue grid in the desired way
	private void sortColumns() {
		int spacesCount;
		int half;
		for(int i = 0; i<DropNFloatQuotePreferences.COLUMNS; i++) {
			spacesCount = getSpacesCount(i);		
			for(int j = 0; j<spacesCount; j++) {
				floatColumn(i);
			}
			half = Math.round(spacesCount/2);
			for(int j = 0; j<half; j++) {
				moveToTop(i);
			}
		}
	}
	
	//method takes an empty cell and moves it to the top of a column
	private void moveToTop(int column) {
		int cell1;
		int cell2;
		int index;
		index = 0;
		while(scrambleGrid[index][column].equals(" ")) {
			index++;
		}
		
		cell1 = index;
		while(!scrambleGrid[index][column].equals(" ")) {
			index++;
		}
		cell2 = index;
		swapCell(column, cell1, cell2);
	}
	
	//method moves all non empty cells to the top of the grid, and all spaces to the bottom
	private void floatColumn(int column) {
		int totalRowCount = DropNFloatQuotePreferences.DROP_ROWS + DropNFloatQuotePreferences.FLOAT_ROWS;
		int cell1, cell2;
		while(!floatColumnInOrder(column)) {
			cell1 = 0;
			cell2 = totalRowCount-1;
			while(!scrambleGrid[cell1][column].equals(" ")) {
				cell1++;
			}
			while(scrambleGrid[cell2][column].equals(" ")) {
				cell2--;
			}
			swapCell(column, cell1, cell2);
		}
	}
	
	//method checks if a given column is in the correct format and returns true or false
	private boolean floatColumnInOrder(int column) {
		boolean result = true;
		int totalRowCount = DropNFloatQuotePreferences.DROP_ROWS + DropNFloatQuotePreferences.FLOAT_ROWS;
		String currentCell = scrambleGrid[0][column];
		for(int i = 2; i<=totalRowCount; i++) {
			String nextCell = scrambleGrid[i-1][column];
			if(currentCell.equals(" ")) {
				if(!nextCell.equals(" "))
					result = false;
			}
			currentCell = nextCell;
		}
		return result;
	}
	
	//method counts the number of spaces in a given column and returns that number
	private int getSpacesCount(int i) {
		int spacesCount = 0;
		int totalRowCount = DropNFloatQuotePreferences.DROP_ROWS + DropNFloatQuotePreferences.FLOAT_ROWS;
		for(int j = 0; j<totalRowCount; j++) {
			if(scrambleGrid[j][i].equals(" "))
				spacesCount++;
		}
		return spacesCount;
	}

	//given two cells in a column, this method swaps the values of those cells
	private void swapCell(int column, int row1, int row2) {
		String temp = scrambleGrid[row1][column];
		scrambleGrid[row1][column] = scrambleGrid[row2][column];
		scrambleGrid[row2][column] = temp;
	}
	
	//this method initializes the variables based on the length of the quotes
	private void initializeCellCount(String quote1, String quote2) throws UnsupportedEncodingException, SQLException {
		logicalChars1 = removePunctuation(quote1);
		logicalChars2 = removePunctuation(quote2);
		DropNFloatQuotePreferences.FLOAT_LENGTH = logicalChars1.size();
		DropNFloatQuotePreferences.DROP_LENGTH = logicalChars2.size();

		if(DropNFloatQuotePreferences.FLOAT_LENGTH>24) {
			DropNFloatQuotePreferences.FLOAT_CELL_COUNT = 30.0;
			DropNFloatQuotePreferences.FLOAT_ROWS = 5;
			DropNFloatQuotePreferences.COLUMNS = 6;
			DropNFloatQuotePreferences.FLOAT_CELL_WIDTH = 105;
			
			DropNFloatQuotePreferences.DROP_ROWS = 1;
			while(DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS<DropNFloatQuotePreferences.DROP_LENGTH) {
				DropNFloatQuotePreferences.DROP_ROWS++;
			}
			DropNFloatQuotePreferences.DROP_CELL_COUNT = DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS;
			DropNFloatQuotePreferences.DROP_CELL_WIDTH = 105;
			
		} else if(DropNFloatQuotePreferences.FLOAT_LENGTH>15) {
			DropNFloatQuotePreferences.FLOAT_CELL_COUNT = 24.0;
			DropNFloatQuotePreferences.FLOAT_ROWS = 4;
			DropNFloatQuotePreferences.COLUMNS = 6;
			DropNFloatQuotePreferences.FLOAT_CELL_WIDTH = 105;
			
			DropNFloatQuotePreferences.DROP_ROWS = 1;
			while(DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS<DropNFloatQuotePreferences.DROP_LENGTH) {
				DropNFloatQuotePreferences.DROP_ROWS++;
			}
			DropNFloatQuotePreferences.DROP_CELL_COUNT = DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS;
			DropNFloatQuotePreferences.DROP_CELL_WIDTH = 105;
			
		} else if(DropNFloatQuotePreferences.FLOAT_LENGTH>9) {
			DropNFloatQuotePreferences.FLOAT_CELL_COUNT = 15.0;
			DropNFloatQuotePreferences.FLOAT_ROWS = 3;
			DropNFloatQuotePreferences.COLUMNS = 5;
			DropNFloatQuotePreferences.FLOAT_CELL_WIDTH = 130;
			
			DropNFloatQuotePreferences.DROP_ROWS = 1;
			while(DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS<DropNFloatQuotePreferences.DROP_LENGTH) {
				DropNFloatQuotePreferences.DROP_ROWS++;
			}
			DropNFloatQuotePreferences.DROP_CELL_COUNT = DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS;
			DropNFloatQuotePreferences.DROP_CELL_WIDTH = 130;
			
		} else {
			DropNFloatQuotePreferences.FLOAT_CELL_COUNT = 9.0;
			DropNFloatQuotePreferences.FLOAT_ROWS = 3;
			DropNFloatQuotePreferences.COLUMNS = 3;
			DropNFloatQuotePreferences.FLOAT_CELL_WIDTH = 210;
			
			DropNFloatQuotePreferences.DROP_ROWS = 1;
			while(DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS<DropNFloatQuotePreferences.DROP_LENGTH) {
				DropNFloatQuotePreferences.DROP_ROWS++;
			}
			DropNFloatQuotePreferences.DROP_CELL_COUNT = DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS;
			DropNFloatQuotePreferences.DROP_CELL_WIDTH = 210;
		}		
	}
	
	public String[][] getFloatGrid() {
		return floatGrid;
	}
	
	public String[][] getDropGrid() {
		return dropGrid;
	}
	
	public String[][] getFinalScrambleGrid() {
		return scrambleGrid;
	}
}
