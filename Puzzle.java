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
	protected static String PPT_FILE_NAME;
	protected static String PUZZLE_TITLE;
	private static int STARTING_X = 40;
	private static int STARTING_Y = 120;
	private static boolean SHOW_LABELS = false;
	private static boolean SHOW_BORDERS = true;


	public Puzzle(String quote) throws SQLException, IOException {
		length = api.getLength(quote);
		splitQuote(toList(quote));
		//System.out.println(length);
		buildGrid(quoteParts);
	}
	
	public void generatePPT() throws SQLException, IOException {
		GeneratePPT ppt = new GeneratePPT(this);
		ppt.genPowerPoint();
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
	
	public ArrayList<String> getQuotes() {
		return quoteParts;
	}

	public int getCellWidth() {
		return cell_width;
	}

	public int getCellHeight() {
		return cell_height;
	}
	public String getPPT_FILE_NAME() {
		return PPT_FILE_NAME;
	}


	public String getPUZZLE_TITLE() {
		return PUZZLE_TITLE;
	}

	public int getSTARTING_X() {
		return STARTING_X;
	}


	public int getSTARTING_Y() {
		return STARTING_Y;
	}


	public boolean isSHOW_LABELS() {
		return SHOW_LABELS;
	}


	public boolean isSHOW_BORDERS() {
		return SHOW_BORDERS;
	}
	
	public abstract ArrayList<String> toList(String quote);
	public abstract double getCellCount();
	public abstract void splitQuote(List<String> list);
	public abstract void buildGrid(List<String> list);
}
