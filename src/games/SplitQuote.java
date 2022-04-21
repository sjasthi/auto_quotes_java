package games;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import main.API;
import preferences.SplitQuotePreferences;


public class SplitQuote {
	
	
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
		SplitQuotePreferences.CELL_COUNT = Math.round(SplitQuotePreferences.LENGTH/SplitQuotePreferences.CHUNK_SIZE);
		int remainderSize = SplitQuotePreferences.LENGTH%SplitQuotePreferences.CHUNK_SIZE;
		
		if(remainderSize==0) {
			for(int i = 0; i<SplitQuotePreferences.CELL_COUNT; i++) {
				currentChunk = "";
				for(int n = 0; n<SplitQuotePreferences.CHUNK_SIZE; n++) {
					currentChunk += tempArray.remove(0);
				}
				chunks.add(currentChunk);
			}
		} else {
			for(int i = 0; i<SplitQuotePreferences.CELL_COUNT; i++) {
				currentChunk = "";
				for(int n = 0; n<SplitQuotePreferences.CHUNK_SIZE; n++) {
					currentChunk += tempArray.remove(0);
				}
				chunks.add(currentChunk);
			}
			currentChunk = "";
			for(int i = 0; i<remainderSize; i++) {
				currentChunk += tempArray.remove(0);
			}
			chunks.add(currentChunk);
			SplitQuotePreferences.CELL_COUNT++;
		}
	}


	public void buildSolutionGrid() {
		int rows = (int) Math.round(SplitQuotePreferences.CELL_COUNT/SplitQuotePreferences.COLUMNS);
		double remainder = SplitQuotePreferences.CELL_COUNT%SplitQuotePreferences.COLUMNS;
		if(remainder!=0) 
			rows++;
			
		SplitQuotePreferences.ROWS = rows;
		
		solutionGrid = new String[(int) SplitQuotePreferences.ROWS][SplitQuotePreferences.COLUMNS];
		puzzleGrid = new String[(int) SplitQuotePreferences.ROWS][SplitQuotePreferences.COLUMNS];
		
		int index = 0;
		for(int i = 0; i<SplitQuotePreferences.ROWS; i++) {
			for(int n = 0; n<SplitQuotePreferences.COLUMNS; n++) {
				if(index<chunks.size()) {
					solutionGrid[i][n] = chunks.get(index);
					index++;
				} else {
					solutionGrid[i][n] = "";
				}
			}
		}
		
		/*
		for(int i = 0; i<SplitQuotePreferences.ROWS; i++) {
			for(int n = 0; n<SplitQuotePreferences.COLUMNS; n++) {
				System.out.print(solutionGrid[i][n]);
				
			}
			System.out.print("\n");
		}
		*/
		
	}
	
	public void buildPuzzleGrid() {
		Collections.shuffle(chunks);
		int index = 0;
		for(int i = 0; i<SplitQuotePreferences.ROWS; i++) {
			for(int n = 0; n<SplitQuotePreferences.COLUMNS; n++) {
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
		String newQuote = quote.trim();
		
		if(SplitQuotePreferences.HAS_SPACES) {
			if(SplitQuotePreferences.HAS_PUNCTUATION) {			
				logicalChars = api.getLogicalChars(newQuote);
			} else {
				logicalChars = api.getLogicalChars(removePunctuation(newQuote));
			}
		} else {
			if(SplitQuotePreferences.HAS_PUNCTUATION) {
				logicalChars = api.getLogicalChars(removeSpaces(newQuote));
			} else {
				logicalChars = api.getLogicalChars(removeSpaces(removePunctuation(newQuote)));
			}
		}
		
		SplitQuotePreferences.LENGTH = api.getLength(newQuote);
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
