import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

public class StripperQuote {
	private int totalLength;
	private ArrayList<String> logicalChars;
	private ArrayList<String> baseChars;
	private API api = new API();
	private int rows;
	private int columns;
	private int cell_width;
	private int cell_height; 
	private static int STARTING_X = 40;
	private static int STARTING_Y = 80;
	
	public StripperQuote(String quote) throws UnsupportedEncodingException, SQLException {
		totalLength = api.getLength(quote);
		logicalChars = api.getLogicalChars(quote);
		baseChars = api.getBaseChars(quote);
		cell_width = 20;
		cell_height = 20;
	}
	
	public ArrayList<String> getLogicalChars() {
		return logicalChars;
	}
	
	public ArrayList<String> getBaseChars() {
		return baseChars;
	}
	
	public int getLength() {
		return totalLength;
	}
	
	public int getBaseLength() {
		return baseChars.size();
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
