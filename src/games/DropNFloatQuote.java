package games;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import main.API;
import preferences.DropNFloatQuotePreferences;

public class DropNFloatQuote {
	private String[][] floatGrid;
	private String[][] dropGrid;
	private String[][] floatScrambleGrid;
	private String[][] dropScrambleGrid;

	private String[][] finalScrambleGrid;
	private API api = new API();
	
	ArrayList<String> logicalChars1;
	ArrayList<String> logicalChars2;
	
	public DropNFloatQuote(String quote1, String quote2) throws UnsupportedEncodingException, SQLException {
		initializeCellCount(quote1, quote2);
		
		floatGrid = new String[DropNFloatQuotePreferences.FLOAT_ROWS][DropNFloatQuotePreferences.COLUMNS];
		dropGrid = new String[DropNFloatQuotePreferences.DROP_ROWS][DropNFloatQuotePreferences.COLUMNS];
		finalScrambleGrid = new String[DropNFloatQuotePreferences.FLOAT_ROWS + DropNFloatQuotePreferences.DROP_ROWS][DropNFloatQuotePreferences.COLUMNS];
		floatScrambleGrid = new String[DropNFloatQuotePreferences.FLOAT_ROWS][DropNFloatQuotePreferences.COLUMNS];
		dropScrambleGrid = new String[DropNFloatQuotePreferences.DROP_ROWS][DropNFloatQuotePreferences.COLUMNS];
		
		buildFloatGrid();
		buildDropGrid();
		
		buildScrambleGrids();
		buildFinalScrambleGrid();
	}
	
	private ArrayList<String> removePunctuation(String quote) throws UnsupportedEncodingException, SQLException {
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
			if(punctuation.contains(tempList.get(i))) {
				tempList.remove(i);
			}
		}
		
		return tempList;
	}
	
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
	
	private void buildFinalScrambleGrid() {
		int index;
		for(int i = 0; i<DropNFloatQuotePreferences.COLUMNS; i++) {
			index = 0;
			for(int j = 0; j<DropNFloatQuotePreferences.FLOAT_ROWS; j++) {
				finalScrambleGrid[j][i] = floatScrambleGrid[j][i];
				index++;
			}
			for(int j = 0; j<DropNFloatQuotePreferences.DROP_ROWS; j++) {
				finalScrambleGrid[j+index][i] = dropScrambleGrid[j][i];
			}
		}
	}
	
	private void buildScrambleGrids() throws UnsupportedEncodingException, SQLException {
		for(int i = 0; i<DropNFloatQuotePreferences.COLUMNS; i++) {
			randomizeFloatColumn(i);
			floatColumn(i);
			randomizeDropColumn(i);
			dropColumn(i);
		}
	}
	
	private void randomizeFloatColumn(int column) {
		ArrayList<String> columnChars = new ArrayList<String>();

		for(int i = 0; i<DropNFloatQuotePreferences.FLOAT_ROWS; i++) {
			String currentChar = floatGrid[i][column];
			columnChars.add(currentChar);
		}
		
		Collections.shuffle(columnChars);
		
		for(int i = 0; i<DropNFloatQuotePreferences.FLOAT_ROWS; i++) {
			floatScrambleGrid[i][column] = columnChars.get(i);
		}	
	}
	
	private void randomizeDropColumn(int column) {
		ArrayList<String> columnChars = new ArrayList<String>();
		for(int i = 0; i<DropNFloatQuotePreferences.DROP_ROWS; i++) {
			String currentChar = dropGrid[i][column];
			columnChars.add(currentChar);
		}
		
		Collections.shuffle(columnChars);
		
		for(int i = 0; i<DropNFloatQuotePreferences.DROP_ROWS; i++) {
			dropScrambleGrid[i][column] = columnChars.get(i);
		}	
	}
	
	private void dropColumn(int column) {
		int cell1, cell2;
		while(!dropColumnInOrder(column)) {
			cell1 = DropNFloatQuotePreferences.DROP_ROWS-1;
			cell2 = 0;
			while(!dropScrambleGrid[cell1][column].equals(" ")) {
				cell1--;
			}
			while(dropScrambleGrid[cell2][column].equals(" ")) {
				cell2++;
			}
			swapCell(column, cell1, cell2, dropScrambleGrid);
		}
	}
	
	private void floatColumn(int column) {
		int cell1, cell2;
		while(!floatColumnInOrder(column)) {
			cell1 = 0;
			cell2 = DropNFloatQuotePreferences.FLOAT_ROWS-1;
			while(!floatScrambleGrid[cell1][column].equals(" ")) {
				cell1++;
			}
			while(floatScrambleGrid[cell2][column].equals(" ")) {
				cell2--;
			}
			swapCell(column, cell1, cell2, floatScrambleGrid);
		}
	}
	
	private boolean floatColumnInOrder(int column) {
		boolean result = true;
		
		String currentCell = floatScrambleGrid[0][column];
		for(int i = 2; i<=DropNFloatQuotePreferences.FLOAT_ROWS; i++) {
			String nextCell = floatScrambleGrid[i-1][column];
			if(currentCell.equals(" ")) {
				if(!nextCell.equals(" "))
					result = false;
			}
			currentCell = nextCell;
		}
		return result;
	}
	
	private boolean dropColumnInOrder(int column) {
		boolean result = true;
		
		String currentCell = dropScrambleGrid[DropNFloatQuotePreferences.DROP_ROWS-1][column];
		for(int i = 2; i<=DropNFloatQuotePreferences.DROP_ROWS; i++) {
			String nextCell = dropScrambleGrid[DropNFloatQuotePreferences.DROP_ROWS-i][column];
			if(currentCell.equals(" ")) {
				if(!nextCell.equals(" "))
					result = false;
			}
			currentCell = nextCell;
		}
		return result;
	}

	private void swapCell(int column, int row1, int row2, String[][] scrambleGrid) {
		String temp = scrambleGrid[row1][column];
		scrambleGrid[row1][column] = scrambleGrid[row2][column];
		scrambleGrid[row2][column] = temp;
	}
	
	private void initializeCellCount(String quote1, String quote2) throws UnsupportedEncodingException, SQLException {
		logicalChars1 = removePunctuation(quote1);
		logicalChars2 = removePunctuation(quote2);
		DropNFloatQuotePreferences.FLOAT_LENGTH = logicalChars1.size();
		DropNFloatQuotePreferences.DROP_LENGTH = logicalChars2.size();

		if(DropNFloatQuotePreferences.FLOAT_LENGTH>48) {
			DropNFloatQuotePreferences.FLOAT_CELL_COUNT = 60.0;
			DropNFloatQuotePreferences.FLOAT_ROWS = 6;
			DropNFloatQuotePreferences.COLUMNS = 10;
			DropNFloatQuotePreferences.FLOAT_CELL_WIDTH = 60;
			
			DropNFloatQuotePreferences.DROP_ROWS = 1;
			while(DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS<DropNFloatQuotePreferences.DROP_LENGTH) {
				DropNFloatQuotePreferences.DROP_ROWS++;
			}
			DropNFloatQuotePreferences.DROP_CELL_COUNT = DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS;
			DropNFloatQuotePreferences.DROP_CELL_WIDTH = 60;
			
		} else if(DropNFloatQuotePreferences.FLOAT_LENGTH>42) {
			DropNFloatQuotePreferences.FLOAT_CELL_COUNT = 48.0;
			DropNFloatQuotePreferences.FLOAT_ROWS = 6;
			DropNFloatQuotePreferences.COLUMNS = 8;
			DropNFloatQuotePreferences.FLOAT_CELL_WIDTH = 80;
			
			DropNFloatQuotePreferences.DROP_ROWS = 1;
			while(DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS<DropNFloatQuotePreferences.DROP_LENGTH) {
				DropNFloatQuotePreferences.DROP_ROWS++;
			}
			DropNFloatQuotePreferences.DROP_CELL_COUNT = DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS;
			DropNFloatQuotePreferences.DROP_CELL_WIDTH = 80;
			
		} else if(DropNFloatQuotePreferences.FLOAT_LENGTH>30) {
			DropNFloatQuotePreferences.FLOAT_CELL_COUNT = 42.0;
			DropNFloatQuotePreferences.FLOAT_ROWS = 6;
			DropNFloatQuotePreferences.COLUMNS = 7;
			DropNFloatQuotePreferences.FLOAT_CELL_WIDTH = 90;
			
			DropNFloatQuotePreferences.DROP_ROWS = 1;
			while(DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS<DropNFloatQuotePreferences.DROP_LENGTH) {
				DropNFloatQuotePreferences.DROP_ROWS++;
			}
			DropNFloatQuotePreferences.DROP_CELL_COUNT = DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS;
			DropNFloatQuotePreferences.DROP_CELL_WIDTH = 90;
			
		} else if(DropNFloatQuotePreferences.FLOAT_LENGTH>24) {
			DropNFloatQuotePreferences.FLOAT_CELL_COUNT = 30.0;
			DropNFloatQuotePreferences.FLOAT_ROWS = 5;
			DropNFloatQuotePreferences.COLUMNS = 6;
			DropNFloatQuotePreferences.FLOAT_CELL_WIDTH = 100;
			
			DropNFloatQuotePreferences.DROP_ROWS = 1;
			while(DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS<DropNFloatQuotePreferences.DROP_LENGTH) {
				DropNFloatQuotePreferences.DROP_ROWS++;
			}
			DropNFloatQuotePreferences.DROP_CELL_COUNT = DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS;
			DropNFloatQuotePreferences.DROP_CELL_WIDTH = 100;
			
		} else if(DropNFloatQuotePreferences.FLOAT_LENGTH>15) {
			DropNFloatQuotePreferences.FLOAT_CELL_COUNT = 24.0;
			DropNFloatQuotePreferences.FLOAT_ROWS = 4;
			DropNFloatQuotePreferences.COLUMNS = 6;
			DropNFloatQuotePreferences.FLOAT_CELL_WIDTH = 100;
			
			DropNFloatQuotePreferences.DROP_ROWS = 1;
			while(DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS<DropNFloatQuotePreferences.DROP_LENGTH) {
				DropNFloatQuotePreferences.DROP_ROWS++;
			}
			DropNFloatQuotePreferences.DROP_CELL_COUNT = DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS;
			DropNFloatQuotePreferences.DROP_CELL_WIDTH = 100;
			
		} else {
			DropNFloatQuotePreferences.FLOAT_CELL_COUNT = 2.0;
			DropNFloatQuotePreferences.FLOAT_ROWS = 1;
			DropNFloatQuotePreferences.COLUMNS = 2;
			DropNFloatQuotePreferences.FLOAT_CELL_WIDTH = 280;
			
			DropNFloatQuotePreferences.DROP_ROWS = 1;
			while(DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS<DropNFloatQuotePreferences.DROP_LENGTH) {
				DropNFloatQuotePreferences.DROP_ROWS++;
			}
			DropNFloatQuotePreferences.DROP_CELL_COUNT = DropNFloatQuotePreferences.DROP_ROWS*DropNFloatQuotePreferences.COLUMNS;
			DropNFloatQuotePreferences.DROP_CELL_WIDTH = 280;
		}
			
	}
	
	public String[][] getFloatGrid() {
		return floatGrid;
	}
	
	public String[][] getDropGrid() {
		return dropGrid;
	}
	
	public String[][] getFinalScrambleGrid() {
		return finalScrambleGrid;
	}
}
