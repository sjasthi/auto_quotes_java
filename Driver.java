import java.io.IOException;
import java.sql.SQLException;


/**
 * @author Neil Haggerty This is the main driver class. Run this to run the whole program.        
 */

public class Driver {

	public static void main(String[] args) throws SQLException, IOException {
		
		//PuzzleSeries s1 = new PuzzleSeries("Split Quote", 5);
		//s1.splitQuotePPT();
		
		PuzzleSeries s1 = new PuzzleSeries("Scramble Quote", 5);
		s1.scrambleQuotePPT();
		
		//PuzzleSeries s1 = new PuzzleSeries("Drop Quote", 5);
		//s1.dropQuotePPT();
		
		//PuzzleSeries s1 = new PuzzleSeries("Stripper Quote", 1);
		//s1.stripperQuote();
	}
}
