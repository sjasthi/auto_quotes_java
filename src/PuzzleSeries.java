import java.awt.Color;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.poi.hslf.usermodel.HSLFLine;
import org.apache.poi.hslf.usermodel.HSLFPictureData;
import org.apache.poi.hslf.usermodel.HSLFPictureShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTable;
import org.apache.poi.hslf.usermodel.HSLFTableCell;
import org.apache.poi.hslf.usermodel.HSLFTextBox;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.sl.usermodel.TableCell.BorderEdge;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.util.IOUtils;


public class PuzzleSeries {
	
	private static String PUZZLE_TITLE;
	private final int numberOfPuzzles = Preferences.PUZZLE_COUNT;
	private static String PPT_FILE_NAME = Preferences.PPT_FILE_NAME;
	private static boolean hasBoarders = Preferences.HAS_BOARDERS;
	private static String FONT_NAME = Preferences.FONT_NAME;
	private static double GRID_FONT_SIZE = Preferences.GRID_FONT_SIZE;
	private static double TITLE_FONT_SIZE = Preferences.TITLE_FONT_SIZE;
	private static Color fillColor = Preferences.FILL_COLOR;
	private static Color textColor = Preferences.TEXT_COLOR;
	private static Color titleColor = Preferences.TITLE_COLOR;
	private static Color gridColor = Preferences.GRID_COLOR;
	private static Color slideNumberColor = Preferences.SLIDE_NUMBER_COLOR;
	private File quotes = new File("Test.txt");
	
	public PuzzleSeries(String puzzleType) throws SQLException, IOException {
		System.out.println("Loading...");
		PUZZLE_TITLE = puzzleType;
	}
	
	public void splitQuotePPT() throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();
		
		for(int n = 0; n<numberOfPuzzles; n++) {
			
			String quote = scan.nextLine();
			SplitQuote puzzle = new SplitQuote(quote);
			String[][] grid = puzzle.getPuzzleGrid();
			
			HSLFSlide slide = ppt.createSlide();
			String title_name = PUZZLE_TITLE;
			createTitle(slide, title_name);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(puzzle.getRows(), puzzle.getColumns()); 
			//getLabels(slide, puzzle.getRows(), puzzle.getColumns()); 
		
			for (int i = 0; i < puzzle.getColumns(); i++) {
				for (int j = 0; j < puzzle.getRows(); j++) {
					String char_string = String.valueOf(grid[j][i]);
					HSLFTableCell cell1 = table.getCell(j, i);
					
					cell1.setText(char_string);

					setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(FONT_NAME);
					rt1.setFontSize(GRID_FONT_SIZE);
					rt1.setFontColor(textColor);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int i = 0; i < puzzle.getColumns(); i++) {
				table.setColumnWidth(i, puzzle.getCellWidth());
			}

			for (int i = 0; i < puzzle.getRows(); i++) {
				table.setRowHeight(i, puzzle.getCellHeight());
			}

			table.moveTo(puzzle.getSTARTING_X(), puzzle.getSTARTING_Y());

			createSlideNumber(slide, puzzle_slide_no);
			puzzle_slide_no++;
		}
		
		splitQuoteSolutions(ppt);
		
		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle is created: " + ppt_file_name);
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		System.out.println("Done.");

		ppt.close();
		scan.close();
		
	}
	
	private void splitQuoteSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);

		int puzzle_slide_no = 1;
		
		for(int n = 0; n<numberOfPuzzles; n++) {
			
			String quote = scan.nextLine();
			SplitQuote puzzle = new SplitQuote(quote);
			String[][] grid = puzzle.getSolutionGrid();
			
			HSLFSlide slide = ppt.createSlide();
			String title_name = "Split Quote Solution";
			createTitle(slide, title_name);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(puzzle.getRows(), puzzle.getColumns()); 
			//getLabels(slide, puzzle.getRows(), puzzle.getColumns()); 
		
			for (int i = 0; i < puzzle.getColumns(); i++) {
				for (int j = 0; j < puzzle.getRows(); j++) {
					String char_string = String.valueOf(grid[j][i]);
					HSLFTableCell cell1 = table.getCell(j, i);
					
					cell1.setText(char_string);

					setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(FONT_NAME);
					rt1.setFontSize(GRID_FONT_SIZE);
					rt1.setFontColor(textColor);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int i = 0; i < puzzle.getColumns(); i++) {
				table.setColumnWidth(i, puzzle.getCellWidth());
			}

			for (int i = 0; i < puzzle.getRows(); i++) {
				table.setRowHeight(i, puzzle.getCellHeight());
			}

			table.moveTo(puzzle.getSTARTING_X(), puzzle.getSTARTING_Y());

			createSlideNumber(slide, puzzle_slide_no);
			puzzle_slide_no++;
		}
	}

	private void dropSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		int puzzle_slide_no = 1;
		
		for(int n = 0; n<numberOfPuzzles; n++) {
			String quote = scan.nextLine();
			DropQuote puzzle = new DropQuote(quote);			
			HSLFSlide slide = ppt.createSlide();
			String title_name = "Drop Quote Solution";
			createTitle(slide, title_name);
			createLogo(ppt, slide);
			
			HSLFTable table = slide.createTable(puzzle.getRows(), puzzle.getColumns());
			String[][] grid = puzzle.getPuzzleGrid();
			for (int i = 0; i < puzzle.getColumns(); i++) {
				for (int j = 0; j < puzzle.getRows(); j++) {
					String char_string = String.valueOf(grid[j][i]);
					if(char_string.equals(" ")) {
						HSLFTableCell cell1 = table.getCell(j, i);
						cell1.setText(" ");
						cell1.setFillColor(fillColor);
						setBorders(cell1);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontColor(textColor);
						rt1.setFontFamily(FONT_NAME);
						rt1.setFontSize(GRID_FONT_SIZE);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					} else {
						HSLFTableCell cell1 = table.getCell(j, i);

						cell1.setText(char_string);

						setBorders(cell1);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(FONT_NAME);
						rt1.setFontSize(GRID_FONT_SIZE);
						rt1.setFontColor(textColor);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					}
				}
			}

			for (int i = 0; i < puzzle.getColumns(); i++) {
				table.setColumnWidth(i, puzzle.getCellWidth());
			}

			for (int i = 0; i < puzzle.getRows(); i++) {
				table.setRowHeight(i, puzzle.getCellHeight());
			}

			table.moveTo(puzzle.getSTARTING_X(), puzzle.getSTARTING_Y());

			
			createSlideNumber(slide, puzzle_slide_no);
			puzzle_slide_no++;
		}
	}
	
	public void dropQuotePPT() throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();
		
		for(int n = 0; n<numberOfPuzzles; n++) {
			
			String quote = scan.nextLine();
			DropQuote puzzle = new DropQuote(quote);
			String[][] grid = puzzle.getScrambleGrid();
			
			HSLFSlide slide = ppt.createSlide();
			String title_name = PUZZLE_TITLE;
			createTitle(slide, title_name);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(puzzle.getRows(), puzzle.getColumns()); 
			
			for (int i = 0; i < puzzle.getColumns(); i++) {
				for (int j = 0; j < puzzle.getRows(); j++) {
					String char_string = String.valueOf(grid[j][i]);
					HSLFTableCell cell1 = table.getCell(j, i);
					
					cell1.setText(char_string);

					setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(FONT_NAME);
					rt1.setFontColor(textColor);
					rt1.setFontSize(GRID_FONT_SIZE);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int i = 0; i < puzzle.getColumns(); i++) {
				table.setColumnWidth(i, puzzle.getCellWidth());
			}

			for (int i = 0; i < puzzle.getRows(); i++) {
				table.setRowHeight(i, puzzle.getCellHeight());
			}

			table.moveTo(puzzle.getSTARTING_X(), puzzle.getSTARTING_Y());

			HSLFTable table2 = slide.createTable(puzzle.getRows(), puzzle.getColumns());
			grid = puzzle.getPuzzleGrid();
			for (int i = 0; i < puzzle.getColumns(); i++) {
				for (int j = 0; j < puzzle.getRows(); j++) {
					String char_string = String.valueOf(grid[j][i]);
					if(char_string.equals(" ")) {
						HSLFTableCell cell1 = table2.getCell(j, i);
						
						cell1.setText(" ");
						cell1.setFillColor(fillColor);

						setBorders(cell1);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(FONT_NAME);
						rt1.setFontSize(GRID_FONT_SIZE);
						rt1.setFontColor(textColor);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					} else {
						HSLFTableCell cell1 = table2.getCell(j, i);

						cell1.setText(" ");

						setBorders(cell1);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(FONT_NAME);
						rt1.setFontSize(GRID_FONT_SIZE);
						rt1.setFontColor(textColor);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					}
				}
			}

			for (int i = 0; i < puzzle.getColumns(); i++) {
				table2.setColumnWidth(i, puzzle.getCellWidth());
			}

			for (int i = 0; i < puzzle.getRows(); i++) {
				table2.setRowHeight(i, puzzle.getCellHeight());
			}

			table2.moveTo(puzzle.getSTARTING_X(), puzzle.getSTARTING_Y()*4);

			
			createSlideNumber(slide, puzzle_slide_no);
			puzzle_slide_no++;
		}
		
		dropSolutions(ppt);
		
		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle is created: " + ppt_file_name);
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		System.out.println("Done.");

		ppt.close();
		scan.close();
		
	}
	
	public void stripperQuote() throws IOException, SQLException {
		
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();
		
		
		for(int n = 0; n<numberOfPuzzles; n++) {

			String quote = scan.nextLine();
			StripperQuote puzzle = new StripperQuote(quote);
		
			HSLFSlide slide = ppt.createSlide();
			String title_name = PUZZLE_TITLE;
			createTitle(slide, title_name);
			createLogo(ppt, slide);



			HSLFTable table = slide.createTable(1, puzzle.getLength());
			for(int a = 0; a<puzzle.getBaseLength(); a++) {
				String char_string = puzzle.getBaseChars().get(a);
				HSLFTableCell cell1 = table.getCell(0, a);

				cell1.setText(char_string);

				setBorders(cell1);
				HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
				rt1.setFontFamily(FONT_NAME);
				rt1.setFontSize(GRID_FONT_SIZE);
				rt1.setFontColor(textColor);
				cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
				cell1.setHorizontalCentered(true);
			}
			for (int b = 0; b < puzzle.getBaseLength(); b++) {
				table.setColumnWidth(b, puzzle.getCellWidth());
			}


			table.setRowHeight(0, puzzle.getCellHeight());


			table.moveTo(puzzle.getSTARTING_X(), puzzle.getSTARTING_Y());


			createSlideNumber(slide, puzzle_slide_no);
			puzzle_slide_no++;
		}

		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle is created: " + ppt_file_name);
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		System.out.println("Done.");

		ppt.close();
		scan.close();
		
	}

	public void scrambleQuotePPT() throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();
		
		for(int n = 0; n<numberOfPuzzles; n++) {
			
			String quote = scan.nextLine();
			ScrambleQuote puzzle = new ScrambleQuote(quote);
			int yOffSet = 0;
			int xOffSet = 0;
			
			HSLFSlide slide = ppt.createSlide();
			String title_name = PUZZLE_TITLE;
			createTitle(slide, title_name);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(puzzle.getRows(), puzzle.getColumns());
			String[][] grid = puzzle.getBankGrid();
			for (int i = 0; i < puzzle.getRows(); i++) {
				for (int j = 0; j < puzzle.getColumns(); j++) {
					String char_string = String.valueOf(grid[i][j]);
					HSLFTableCell cell1 = table.getCell(i, j);

					cell1.setText(char_string);

					//setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(FONT_NAME);
					rt1.setFontSize(GRID_FONT_SIZE);
					rt1.setFontColor(textColor);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
				for (int b = 0; b < puzzle.getColumns(); b++) {
					table.setColumnWidth(b, puzzle.getCellWidth());
				}

				for (int b = 0; b < puzzle.getRows(); b++) {
					table.setRowHeight(b, puzzle.getCellHeight());
				}

				table.moveTo(puzzle.getSTARTING_X(), puzzle.getSTARTING_Y() + yOffSet);
			}
			
			yOffSet += (45*puzzle.getRows());
			
			ArrayList<String> word;
			ArrayList<String> logicalChars = puzzle.getLogicalChars();
			int index = 0;
			int nextWordLength;
			word = new ArrayList<String>();
			while(!logicalChars.get(index).equals(" ") && index<logicalChars.size()) {
				word.add(logicalChars.get(index));
				index++;
			}
			index++;
			int currentWordLength = word.size();
			
			for(int i = 0; i<puzzle.getWordCount(); i++) {
				table = slide.createTable(1, word.size());
				for(int a = 0; a<word.size(); a++) {
					String char_string = word.get(a);
					HSLFTableCell cell1 = table.getCell(0, a);
					
					if(hasPunctuation(char_string))
						cell1.setText(char_string);
					else
						cell1.setText(" ");

					setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(FONT_NAME);
					rt1.setFontSize(GRID_FONT_SIZE);
					rt1.setFontColor(textColor);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
				for (int b = 0; b < word.size(); b++) {
					table.setColumnWidth(b, puzzle.getCellWidth());
				}


				table.setRowHeight(0, puzzle.getCellHeight());
				

				table.moveTo(puzzle.getSTARTING_X() + xOffSet, puzzle.getSTARTING_Y() + yOffSet);
				
				
				word = new ArrayList<String>();
				if(index<logicalChars.size()) {
					while(!logicalChars.get(index).equals(" ")) {
						word.add(logicalChars.get(index));
						index++;
						if(index==logicalChars.size())
							break;
					}
					index++;
				}
				
				
				
				if((i+1)<puzzle.getWordCount()) {
					nextWordLength = word.size(); 
					
					if(currentWordLength + nextWordLength > 20) {
							yOffSet += 45;
							xOffSet = 0;
							currentWordLength = nextWordLength;
						} else {
							xOffSet = ((currentWordLength * 32) + (i*5));
							currentWordLength += nextWordLength;
						}
				} else {
					yOffSet += 45;
				}
				
			}
			

			createSlideNumber(slide, puzzle_slide_no);
			puzzle_slide_no++;
		}
		
		scrambleQuoteSolutions(ppt);
		
		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle is created: " + ppt_file_name);
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		System.out.println("Done.");

		ppt.close();
		scan.close();
		
	}
	
		private void scrambleQuoteSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		int puzzle_slide_no = 1;
		
		for(int n = 0; n<numberOfPuzzles; n++) {
			String quote = scan.nextLine();
			ScrambleQuote puzzle = new ScrambleQuote(quote);
			
			int yOffSet = 30;
			int xOffSet = 0;
			
			HSLFSlide slide = ppt.createSlide();
			String title_name = "Scramble Quote Solution";
			createTitle(slide, title_name);
			createLogo(ppt, slide);

			HSLFTable table;
			
			ArrayList<String> word;
			ArrayList<String> logicalChars = puzzle.getLogicalChars();
			int index = 0;
			int nextWordLength;
			word = new ArrayList<String>();
			while(!logicalChars.get(index).equals(" ") && index<logicalChars.size()) {
				word.add(logicalChars.get(index));
				index++;
			}
			index++;
			int currentWordLength = word.size();
			
			for(int i = 0; i<puzzle.getWordCount(); i++) {
				table = slide.createTable(1, word.size());
				for(int a = 0; a<word.size(); a++) {
					String char_string = word.get(a);
					HSLFTableCell cell1 = table.getCell(0, a);
					
					cell1.setText(char_string);

					setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(FONT_NAME);
					rt1.setFontSize(GRID_FONT_SIZE);
					rt1.setFontColor(textColor);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
				for (int b = 0; b < word.size(); b++) {
					table.setColumnWidth(b, puzzle.getCellWidth());
				}


				table.setRowHeight(0, puzzle.getCellHeight());
				

				table.moveTo(puzzle.getSTARTING_X() + xOffSet, puzzle.getSTARTING_Y() + yOffSet);
				
				
				word = new ArrayList<String>();
				if(index<logicalChars.size()) {
					while(!logicalChars.get(index).equals(" ")) {
						word.add(logicalChars.get(index));
						index++;
						if(index==logicalChars.size())
							break;
					}
					index++;
				}
				
				
				
				if((i+1)<puzzle.getWordCount()) {
					nextWordLength = word.size(); 
					
					if(currentWordLength + nextWordLength > 20) {
							yOffSet += 45;
							xOffSet = 0;
							currentWordLength = nextWordLength;
						} else {
							xOffSet = ((currentWordLength * 32) + (i*5));
							currentWordLength += nextWordLength;
						}
				} else {
					yOffSet += 45;
				}
				
			}
			

			createSlideNumber(slide, puzzle_slide_no);
			puzzle_slide_no++;
		}
		
	}
	
	private boolean hasPunctuation(String a) {
		ArrayList<String> punctuation = new ArrayList<String>();
		punctuation.add(".");
		punctuation.add("?");
		punctuation.add("!");
		punctuation.add(",");
		if(punctuation.contains(a))
			return true;
		else
			return false;
	}

	public static void createTitle(HSLFSlide slide, String puzzleName) {
		HSLFTextBox title = slide.createTextBox();
		HSLFTextParagraph p1 = title.getTextParagraphs().get(0);
		p1.setTextAlign(TextAlign.CENTER);
		HSLFTextRun run = p1.getTextRuns().get(0);
		run.setFontColor(titleColor);
		run.setText(puzzleName.toUpperCase());
		run.setFontFamily(FONT_NAME);
		run.setFontSize(TITLE_FONT_SIZE);
		title.setAnchor(new Rectangle(240, 10, 400, 200));
	}
	
	public static void createSlideNumber(HSLFSlide slide, int slide_num) {
		HSLFTextBox slide_number = slide.createTextBox();
		HSLFTextParagraph p = slide_number.getTextParagraphs().get(0);
		p.setTextAlign(TextAlign.CENTER);
		HSLFTextRun r = p.getTextRuns().get(0);
		r.setText("" + slide_num + "");
		r.setFontFamily(FONT_NAME);
		r.setFontSize(30.);
		r.setFontColor(slideNumberColor);
		slide_number.setAnchor(new Rectangle(220, 10, 50, 30));

		createLine(slide, 220, 5, 50, 0); // top line
		createLine(slide, 270, 5, 0, 50); // right line
		createLine(slide, 220, 55, 50, 0); // bottom line
		createLine(slide, 220, 5, 0, 50); // left line
	}

	public static void createLine(HSLFSlide slide, int x, int y, int width, int height) {
		HSLFLine line = new HSLFLine();
		line.setAnchor(new Rectangle(x, y, width, height));
		line.setLineColor(slideNumberColor);
		slide.addShape(line);
	}
	
	public static void createLogo(HSLFSlideShow ppt, HSLFSlide slide) throws IOException {
		byte[] picture = IOUtils.toByteArray(new FileInputStream(new File("logo.png")));
		HSLFPictureData pd = ppt.addPicture(picture, HSLFPictureData.PictureType.PNG);
		HSLFPictureShape pic_shape = slide.createPicture(pd);
		pic_shape.setAnchor(new Rectangle(0, 0, 174, 65));
	}
	
	public static void setBorders(HSLFTableCell cell) {
		if(hasBoarders) {
			cell.setBorderColor(BorderEdge.bottom, gridColor);
			cell.setBorderColor(BorderEdge.top, gridColor);
			cell.setBorderColor(BorderEdge.right, gridColor);
			cell.setBorderColor(BorderEdge.left, gridColor);
		}
	}
}
