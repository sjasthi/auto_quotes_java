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
		public final static int CHUNK_SIZE = 3;
		public final static int CELL_WIDTH = 120;
		public final static int CELL_HEIGHT = 100;
		public final static int COLUMNS = 5;
		public static double CELL_COUNT;
		public static int ROWS;
		public static int LENGTH;
	}
	
	private API api = new API();
	private ArrayList<String> logicalChars;
	private ArrayList<String> chunks = new ArrayList<String>();
	private String[][] solutionGrid;
	private String[][] puzzleGrid;
	
	
	public SplitQuote(String quote) throws SQLException, IOException {
		checkParams(quote);
		createChunks();

		buildSolutionGrid();
		buildPuzzleGrid();
	}
	
	
	
	private void createChunks() {
		ArrayList<String> tempArray = logicalChars;
		String currentChunk;
		Preferences.CELL_COUNT = Math.round(Preferences.LENGTH/Preferences.CHUNK_SIZE);
		int remainderSize = Preferences.LENGTH%Preferences.CHUNK_SIZE;
		
		if(remainderSize==0) {
			for(int i = 0; i<Preferences.CELL_COUNT; i++) {
				currentChunk = "";
				for(int n = 0; n<Preferences.CHUNK_SIZE; n++) {
					currentChunk += tempArray.remove(0);
				}
				chunks.add(currentChunk);
			}
		} else {
			for(int i = 0; i<Preferences.CELL_COUNT; i++) {
				currentChunk = "";
				for(int n = 0; n<Preferences.CHUNK_SIZE; n++) {
					currentChunk += tempArray.remove(0);
				}
				chunks.add(currentChunk);
			}
			currentChunk = "";
			for(int i = 0; i<remainderSize; i++) {
				currentChunk += tempArray.remove(0);
			}
			chunks.add(currentChunk);
		}
		
	}


	public void buildSolutionGrid() {
		int rows = (int) Math.round(Preferences.CELL_COUNT/Preferences.COLUMNS);
		double remainder = Preferences.CELL_COUNT%Preferences.COLUMNS;
		if(remainder!=0) 
			rows++;
			
		Preferences.ROWS = rows;
		
		solutionGrid = new String[(int) Preferences.ROWS][Preferences.COLUMNS];
		puzzleGrid = new String[(int) Preferences.ROWS][Preferences.COLUMNS];
		
		int index = 0;
		for(int i = 0; i<Preferences.ROWS; i++) {
			for(int n = 0; n<Preferences.COLUMNS; n++) {
				if(index<chunks.size()) {
					solutionGrid[i][n] = chunks.get(index);
					index++;
				} else {
					solutionGrid[i][n] = "";
				}
			}
		}
		
		/*
		for(int i = 0; i<Preferences.ROWS; i++) {
			for(int n = 0; n<Preferences.COLUMNS; n++) {
				System.out.print(solutionGrid[i][n]);
				
			}
			System.out.print("\n");
		}
		*/
		
	}
	
	public void buildPuzzleGrid() {
		Collections.shuffle(chunks);
		int index = 0;
		for(int i = 0; i<Preferences.ROWS; i++) {
			for(int n = 0; n<Preferences.COLUMNS; n++) {
				if(index<chunks.size()) {
					puzzleGrid[i][n] = chunks.get(index);
					index++;
				} else {
					puzzleGrid[i][n] = "";
				}
			}
		}
	}

	public void checkParams(String quote) throws UnsupportedEncodingException, SQLException {
		if(Preferences.HAS_SPACES) {
			if(Preferences.HAS_PUNCTUATION) {
				logicalChars = api.getLogicalChars(quote);
			} else {
				logicalChars = api.getLogicalChars(removePunctuation(quote));
			}
		} else {
			if(Preferences.HAS_PUNCTUATION) {
				logicalChars = api.getLogicalChars(removeSpaces(quote));
			} else {
				logicalChars = api.getLogicalChars(removeSpaces(removePunctuation(quote)));
			}
		}
		
		while(logicalChars.get(0).equals(" ")) {
			logicalChars.remove(0);
		}
		
		while(logicalChars.get(logicalChars.size()-1).equals(" ")) {
			logicalChars.remove(logicalChars.size()-1);
		}
		
		Preferences.LENGTH = logicalChars.size();
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
	
	
	
	
	
	public String[][] getSolutionGrid() {
		return solutionGrid;
	}
	
	public String[][] getPuzzleGrid() {
		return puzzleGrid;
	}
}
