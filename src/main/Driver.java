package main;
import java.util.Scanner;


/**
 * @author Neil Haggerty This is the main driver class. Run this to run the whole program.        
 */

public class Driver {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		String input;
		Scanner scan = new Scanner(System.in);
		boolean loopExit = true;
		
		while(loopExit) {
			System.out.println("1: Stripper Quote\n2: Scramble Quote\n3: Split Quote\n4: Drop Quote\n5: Float Quote\n6: Exit");
			System.out.println("Enter Selection: ");
			input = scan.nextLine();
			
			
			if(input.equals("1")) {
				PPTGenerator s1 = new PPTGenerator();
				s1.createStripperQuote();
			} else if(input.equals("2")) {
				PPTGenerator s1 = new PPTGenerator();
				s1.createScrambleQuote();
			} else if(input.equals("3")) {
				PPTGenerator s1 = new PPTGenerator();
				s1.createSplitQuote();
			} else if(input.equals("4")) {
				PPTGenerator s1 = new PPTGenerator();
				s1.createDropQuote();
			} else if(input.equals("5")) {
				PPTGenerator s1 = new PPTGenerator();
				s1.createFloatQuote();;
			} else if(input.equals("6")) {
				loopExit = false;
				System.out.println("Goodbye"); 
			} else {
				System.out.println("Not a valid selection.\n");
			}
		}
	}
}

