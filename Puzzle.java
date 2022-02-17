import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Haggerty This class generates the puzzle and creates it into a grid
 *         
 */

public abstract class Puzzle {
	private API api = new API();
	protected ArrayList<String> quoteParts = new ArrayList<String>();
	protected double cellCount;
	protected String[][] grid;
	protected int length;
	protected int rows;
	protected int columns;
	protected int cell_width;
	protected int cell_height; 
	private static int STARTING_X = 40;
	private static int STARTING_Y = 120;


	public Puzzle(String quote) throws SQLException, IOException {
		length = api.getLength(quote);
	}	
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public ArrayList<String> getQuotes() {
		return quoteParts;
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
	
	public String[][] getGrid() {
		return grid;
	}
	
	public ArrayList<String> toList(String quote) {
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i<quote.length(); i++) {
			result.add(Character.toString(quote.charAt(i)));
		}
		return result;
	}

	protected abstract ArrayList<String> buildGrid();
	protected abstract ArrayList<String> getLetterBank();
	
}
