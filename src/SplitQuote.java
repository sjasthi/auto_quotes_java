import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class SplitQuote {
	class Preferences {
		public final static String FONT_NAME = "NATS";
		public final static String TITLE = "Split Quote";
		public final static double GRID_FONT_SIZE = 20.0;
		public final static double TITLE_FONT_SIZE = 24.0;
		public static double CELL_COUNT;
		public final static Color FILL_COLOR = Color.RED; 
		public final static Color TEXT_COLOR = Color.RED;
		public final static Color TITLE_COLOR = Color.RED;
		public final static Color GRID_COLOR = Color.RED;
		public final static Color SLIDE_NUMBER_COLOR = Color.RED;
		public final static boolean HAS_BOARDERS = true;
		public final static boolean HAS_SPACES = true;
		public final static boolean HAS_PUNCTUATION = true;
		public final static int STARTING_X = 40;
		public final static int STARTING_Y = 120;
		public static int LENGTH;
		public static int ROWS;
		public static int COLUMNS;
		public static int CELL_WIDTH;
		public static int CELL_HEIGHT;
	}
	
	private API api = new API();
	private ArrayList<String> logicalChars;
	private ArrayList<String> quoteParts = new ArrayList<String>();
	private String[][] solutionGrid;
	private String[][] puzzleGrid;
	
	
	public SplitQuote(String quote) throws SQLException, IOException {
		checkParams(quote);
		splitQuote(logicalChars);
		buildSolutionGrid();
		buildPuzzleGrid();
	}
	
	public void checkParams(String quote) throws UnsupportedEncodingException, SQLException {
		if(Preferences.HAS_SPACES) {
			if(Preferences.HAS_PUNCTUATION) {
				Preferences.LENGTH = api.getLength(quote);
				logicalChars = api.getLogicalChars(quote);
			} else {
				Preferences.LENGTH = api.getLength(removePunctuation(quote));
				logicalChars = api.getLogicalChars(removePunctuation(quote));
			}
		} else {
			if(Preferences.HAS_PUNCTUATION) {
				Preferences.LENGTH = api.getLength(removeSpaces(quote));
				logicalChars = api.getLogicalChars(removeSpaces(quote));
			} else {
				Preferences.LENGTH = api.getLength(removeSpaces(removePunctuation(quote)));
				logicalChars = api.getLogicalChars(removeSpaces(removePunctuation(quote)));
			}
		}
	}
	
	public double getCellCount() {
		Preferences.CELL_COUNT = 0.0;
		if(Preferences.LENGTH>45) {
			Preferences.CELL_COUNT = 20.0;
			Preferences.ROWS = 4;
			Preferences.COLUMNS = 5;
			Preferences.CELL_WIDTH = 125;
			Preferences.CELL_HEIGHT = 90;
		} else if(Preferences.LENGTH>36) {
			Preferences.CELL_COUNT = 15.0;
			Preferences.ROWS = 3;
			Preferences.COLUMNS = 5;
			Preferences.CELL_WIDTH = 125;
			Preferences.CELL_HEIGHT = 100;
		} else if(Preferences.LENGTH>27) {
			Preferences.CELL_COUNT = 12.0;
			Preferences.ROWS = 3;
			Preferences.COLUMNS = 4;
			Preferences.CELL_WIDTH = 160;
			Preferences.CELL_HEIGHT = 100;
		} else if(Preferences.LENGTH>12) {
			Preferences.CELL_COUNT = 9.0;
			Preferences.ROWS = 3;
			Preferences.COLUMNS = 3;
			Preferences.CELL_WIDTH = 180;
			Preferences.CELL_HEIGHT = 120;
		} else if(Preferences.LENGTH>6) {
			Preferences.CELL_COUNT = 4.0;
			Preferences.ROWS = 2;
			Preferences.COLUMNS = 2;
			Preferences.CELL_WIDTH = 280;
			Preferences.CELL_HEIGHT = 180;
		} else {
			Preferences.CELL_COUNT = 2.0;
			Preferences.ROWS = 1;
			Preferences.COLUMNS = 2;
			Preferences.CELL_WIDTH = 280;
			Preferences.CELL_HEIGHT = 180;
		}
		return Preferences.CELL_COUNT;
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
	
	private String removePunctuation(String quote) {
		String newString = "";
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
		
		int count = quote.length();
		for(int i = 0; i<count; i++) {
			if(!(punctuation.contains(String.valueOf(quote.charAt(i)))))
				newString = newString + String.valueOf(quote.charAt(i));
		}
		return newString;
	}
	
	public void splitQuote(ArrayList<String> list) {		
		double chunks = Math.ceil(Preferences.LENGTH/getCellCount());
		int countLeft = Preferences.LENGTH;
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
	
	public void buildSolutionGrid() {
		getCellCount();
		solutionGrid = new String[Preferences.ROWS][Preferences.COLUMNS];
		int count = 0;
		for(int i = 0; i<Preferences.ROWS; i++) {
			for(int n = 0; n<Preferences.COLUMNS; n++) {
				solutionGrid[i][n] = quoteParts.get(count);
				count++;
			}
		}
	}
	
	public void buildPuzzleGrid() {
		puzzleGrid = new String[Preferences.ROWS][Preferences.COLUMNS];
		Collections.shuffle(quoteParts);
		int count = 0;
		for(int i = 0; i<Preferences.ROWS; i++) {
			for(int n = 0; n<Preferences.COLUMNS; n++) {
				puzzleGrid[i][n] = quoteParts.get(count);
				count++;
			}
		}
	}
	
	public String[][] getSolutionGrid() {
		return solutionGrid;
	}
	
	public String[][] getPuzzleGrid() {
		return puzzleGrid;
	}
}
