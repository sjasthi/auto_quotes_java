import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * @author Neil Haggerty This is the main driver class. Run this to run the whole program.        
 */

public class Driver {
	
	private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /IM ";

	public static void main(String[] args) throws Exception {
		String input;
		Scanner scan = new Scanner(System.in);
		boolean loopExit = true;
		String serviceName = "POWERPNT.EXE";
		
		
		while(loopExit) {
			System.out.println("1: Stripper Quote\n2: Drop Quote\n3: Split Quote\n4: Scramle Quote\n5: Exit");
			System.out.println("Enter Selection: ");
			input = scan.nextLine();
			
			
			if(input.equals("1")) {
				isRunning(serviceName);
				PuzzleSeries s1 = new PuzzleSeries("Stripper Quote");
				s1.stripperQuote();
			} else if(input.equals("2")) {
				isRunning(serviceName);
				PuzzleSeries s1 = new PuzzleSeries("Drop Quote");
				s1.dropQuotePPT();
			} else if(input.equals("3")) {
				isRunning(serviceName);
				PuzzleSeries s1 = new PuzzleSeries("Split Quote");
				s1.splitQuotePPT();
			} else if(input.equals("4")) {
				isRunning(serviceName);
				PuzzleSeries s1 = new PuzzleSeries("Scramble Quote");
				s1.scrambleQuotePPT();
			} else if(input.equals("5")) {
				loopExit = false;
				System.out.println("Goodbye");
			} else {
				System.out.println("Not a valid selection.\n");
			}
		}
	}
	
	public static void isRunning(String serviceName) throws Exception {
		boolean result = false;
		
        Process p = Runtime.getRuntime().exec(TASKLIST);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.contains(serviceName)) {
                result = true;
            }
        }
        
        if(result)
        	Runtime.getRuntime().exec(KILL + serviceName);
        	
    }   
}

