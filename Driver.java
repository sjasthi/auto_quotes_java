import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Neil Haggerty This is the main driver class. Run this to run the whole program.        
 */

public class Driver {

	public static void main(String[] args) throws SQLException, IOException {
		Puzzle puzzle1 = new SplitQuote("Hello my name is Neil Haggerty");
		//Puzzle puzzle1 = new SplitQuote("కళ్ళముందు చీకటుంటే కలత దేనికి మిణుగురంత ఆశ చాలు నడపడానికి");
		
		puzzle1.generatePPT();
		
	}
}
