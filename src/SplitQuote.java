import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class SplitQuote {
	
	private API api = new API();
	private double cellCount;
	private int length;
	private int rows;
	private int columns;
	private int cell_width;
	private int cell_height;
	private ArrayList<String> logicalChars;
	private ArrayList<String> quoteParts = new ArrayList<String>();
	private String[][] grid;
	private static int STARTING_X = 40;
	private static int STARTING_Y = 120;
	private boolean hasSpaces = false;
	
	public SplitQuote(String quote) throws SQLException, IOException {
		if(hasSpaces) {
			length = api.getLength(quote);
			logicalChars = api.getLogicalChars(quote);
		} else {
			length = api.getLength(removeSpaces(quote));
			logicalChars = api.getLogicalChars(removeSpaces(quote));
		}
		splitQuote(logicalChars);
		buildGrid();
	}	
	
	public double getCellCount() {
		cellCount = 0.0;
		if(length>45) {
			cellCount = 20.0;
			rows = 4;
			columns = 5;
			cell_width = 125;
			cell_height = 90;
		} else if(length>36) {
			cellCount = 15.0;
			rows = 3;
			columns = 5;
			cell_width = 125;
			cell_height = 100;
		} else if(length>27) {
			cellCount = 12.0;
			rows = 3;
			columns = 4;
			cell_width = 160;
			cell_height = 100;
		} else if(length>12) {
			cellCount = 9.0;
			rows = 3;
			columns = 3;
			cell_width = 180;
			cell_height = 120;
		} else if(length>6) {
			cellCount = 4.0;
			rows = 2;
			columns = 2;
			cell_width = 280;
			cell_height = 180;
		} else {
			cellCount = 2.0;
			rows = 1;
			columns = 2;
			cell_width = 280;
			cell_height = 180;
		}
		return cellCount;
	}
	
	private String removeSpaces(String quote) {
		String newString = "";
		int count = quote.length();
		for(int i = 0; i<count; i++) {
			if(!(quote.charAt(i) == ' '))
				newString = newString + String.valueOf(quote.charAt(i));
		}
		return newString;
	}	
	
	public void splitQuote(ArrayList<String> list) {		
		double chunks = Math.ceil(length/getCellCount());
		int countLeft = length;
		double index = getCellCount();
		while(countLeft>index) {
			String aChunk = "";
			for(int i = 0; i<chunks; i++) {
				aChunk = aChunk + list.remove(0);
				countLeft--;
			}
			quoteParts.add(aChunk);
			index--;
			chunks = Math.ceil(countLeft/index);
		}
		
		for(int i = 0; i<index; i++) {
			quoteParts.add(list.remove(0));
		}	
	}
	
	public void buildGrid() {
		getCellCount();
		grid = new String[rows][columns];
		Collections.shuffle(quoteParts);
		int count = 0;
		for(int i = 0; i<rows; i++) {
			for(int n = 0; n<columns; n++) {
				grid[i][n] = quoteParts.get(count);
				count++;
			}
		}
	}
	
	public String[][] getGrid() {
		return grid;
	}

	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
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

}
