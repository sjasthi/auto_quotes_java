import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * @author Neil Haggerty This is the main driver class. Run this to run the whole program.        
 */

public class Driver {
	
	private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /IM ";

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		String input;
		Scanner scan = new Scanner(System.in);
		boolean loopExit = true;
		String serviceName = "POWERPNT.EXE";
		
		
		while(loopExit) {
			System.out.println("1: Stripper Quote\n2: Drop Quote\n3: Split Quote\n4: Scramble Quote\n5: Exit");
			System.out.println("Enter Selection: ");
			input = scan.nextLine();
			
			
			if(input.equals("1")) {
				isRunning(serviceName);
				PPTGenerator s1 = new PPTGenerator("Stripper Quote");
				s1.createStripperQuote();
			} else if(input.equals("2")) {
				isRunning(serviceName);
				PPTGenerator s1 = new PPTGenerator("Drop Quote");
				s1.createDropQuote();
			} else if(input.equals("3")) {
				isRunning(serviceName);
				PPTGenerator s1 = new PPTGenerator("Split Quote");
				s1.createSplitQuote();
			} else if(input.equals("4")) {
				isRunning(serviceName);
				PPTGenerator s1 = new PPTGenerator("Scramble Quote");
				s1.createScrambleQuote();
			} else if(input.equals("5")) {
				isRunning(serviceName);
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

