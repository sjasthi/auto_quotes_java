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
public class StripperQuote {
	
	private String[][] bankGrid;
	private String[][] puzzleGrid;
	private ArrayList<String> baseChars;
	private ArrayList<String> logicalChars = new ArrayList<String>();
	private int cell_width = 40;
	private int cell_height = 30;
	private int rows = 1;
	private int columns = 16;
	private static int STARTING_X = 40;
	private static int STARTING_Y = 80;
	private API api = new API();
	private boolean hardMode;
	private static double GRID_FONT_SIZE = Preferences.GRID_FONT_SIZE + 6.0;
	
	public StripperQuote(String quote) throws SQLException, IOException {
		
		generateLogicalChars(quote);
		createPuzzleGrid();
		createLetterBank(quote);
		
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

	private void createLetterBank(String quote) throws UnsupportedEncodingException, SQLException {
		baseChars = api.getBaseChars(quote);
		
		if(hardMode)
			Collections.shuffle(baseChars);
		
		int index = 0;
		bankGrid = new String[rows][columns];
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<columns; j++) {
				if(index<baseChars.size()) {
					bankGrid[i][j] = baseChars.get(index);
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
