import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SplitQuote extends Puzzle {
	
	public SplitQuote(String quote) throws SQLException, IOException {
		super(quote);
		PPT_FILE_NAME = "SplitQuote.ppt";
		PUZZLE_TITLE = "Split Quote";
	}
	
	public ArrayList<String> toList(String quote) {
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i<quote.length(); i++) {
			result.add(Character.toString(quote.charAt(i)));
		}
		return result;
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
	
	public void splitQuote(List<String> list) {
		
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
	
	public void buildGrid(List<String> list) {
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

}
