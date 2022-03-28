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


public class PPTGenerator {

	private File quotes = new File("Test.txt");
	private static String PPT_FILE_NAME = "Puzzles.ppt";
	private static int NUMBER_OF_PUZZLES = 5;
	
	public PPTGenerator(String puzzleType) throws SQLException, IOException {
		System.out.println("Loading...");
	}
	
	public void createSplitQuote() throws SQLException, IOException {

		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();
		
		for(int n = 0; n<NUMBER_OF_PUZZLES ; n++) {
			
			String quote = scan.nextLine();
			SplitQuote puzzle = new SplitQuote(quote);
			String[][] grid = puzzle.getPuzzleGrid();
			
			HSLFSlide slide = ppt.createSlide();
			createTitle(slide, SplitQuote.Preferences.TITLE, SplitQuote.Preferences.FONT_NAME, SplitQuote.Preferences.TITLE_FONT_SIZE, SplitQuote.Preferences.TITLE_COLOR);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(SplitQuote.Preferences.ROWS, SplitQuote.Preferences.COLUMNS); 
			//getLabels(slide, puzzle.getRows(), puzzle.getColumns()); 
		
			for (int i = 0; i<SplitQuote.Preferences.COLUMNS; i++) {
				for (int j = 0; j<SplitQuote.Preferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					HSLFTableCell cell1 = table.getCell(j, i);
					
					cell1.setText(char_string);

					if(SplitQuote.Preferences.HAS_BOARDERS)
						setBorders(cell1, SplitQuote.Preferences.SLIDE_NUMBER_COLOR);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(SplitQuote.Preferences.FONT_NAME);
					rt1.setFontSize(SplitQuote.Preferences.GRID_FONT_SIZE);
					rt1.setFontColor(SplitQuote.Preferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int i = 0; i<SplitQuote.Preferences.COLUMNS; i++) {
				table.setColumnWidth(i, SplitQuote.Preferences.CELL_WIDTH);
			}

			for (int i = 0; i<SplitQuote.Preferences.ROWS; i++) {
				table.setRowHeight(i, SplitQuote.Preferences.CELL_HEIGHT);
			}

			table.moveTo(SplitQuote.Preferences.STARTING_X, SplitQuote.Preferences.STARTING_Y);

			createSlideNumber(slide, puzzle_slide_no, SplitQuote.Preferences.FONT_NAME, SplitQuote.Preferences.SLIDE_NUMBER_COLOR);
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
	
	@SuppressWarnings("resource")
	private void splitQuoteSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);

		int puzzle_slide_no = 1;
		
		for(int n = 0; n<NUMBER_OF_PUZZLES; n++) {
			
			String quote = scan.nextLine();
			SplitQuote puzzle = new SplitQuote(quote);
			String[][] grid = puzzle.getSolutionGrid();
			
			HSLFSlide slide = ppt.createSlide();
			String title_name = "Split Quote Solution";
			createTitle(slide, title_name, SplitQuote.Preferences.FONT_NAME, SplitQuote.Preferences.TITLE_FONT_SIZE, SplitQuote.Preferences.TITLE_COLOR);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(SplitQuote.Preferences.ROWS, SplitQuote.Preferences.COLUMNS); 
			//getLabels(slide, puzzle.getRows(), puzzle.getColumns()); 
		
			for (int i = 0; i<SplitQuote.Preferences.COLUMNS; i++) {
				for (int j = 0; j<SplitQuote.Preferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					HSLFTableCell cell1 = table.getCell(j, i);
					
					cell1.setText(char_string);

					if(SplitQuote.Preferences.HAS_BOARDERS)
						setBorders(cell1, SplitQuote.Preferences.SLIDE_NUMBER_COLOR);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(SplitQuote.Preferences.FONT_NAME);
					rt1.setFontSize(SplitQuote.Preferences.GRID_FONT_SIZE);
					rt1.setFontColor(SplitQuote.Preferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int i = 0; i<SplitQuote.Preferences.COLUMNS; i++) {
				table.setColumnWidth(i, SplitQuote.Preferences.CELL_WIDTH);
			}

			for (int i = 0; i<SplitQuote.Preferences.ROWS; i++) {
				table.setRowHeight(i, SplitQuote.Preferences.CELL_HEIGHT);
			}

			table.moveTo(SplitQuote.Preferences.STARTING_X, SplitQuote.Preferences.STARTING_Y);
			
			createSlideNumber(slide, puzzle_slide_no, SplitQuote.Preferences.FONT_NAME, SplitQuote.Preferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}
	}
	
	public void createDropQuote() throws SQLException, IOException {
		
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();
		
		//Repeat all this for each puzzle
		for(int n = 0; n<NUMBER_OF_PUZZLES; n++) {
			
			String quote = scan.nextLine();
			DropQuote puzzle = new DropQuote(quote);
			String[][] grid = puzzle.getScrambleGrid();
			
			HSLFSlide slide = ppt.createSlide();
			createTitle(slide, DropQuote.Preferences.TITLE, DropQuote.Preferences.FONT_NAME, DropQuote.Preferences.TITLE_FONT_SIZE, DropQuote.Preferences.TITLE_COLOR);
			createLogo(ppt, slide);

			//creating the clue at top of slide 
			HSLFTable table = slide.createTable(DropQuote.Preferences.ROWS, DropQuote.Preferences.COLUMNS); 
			for (int i = 0; i<DropQuote.Preferences.COLUMNS; i++) {
				for (int j = 0; j<DropQuote.Preferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					HSLFTableCell cell1 = table.getCell(j, i);
					
					cell1.setText(char_string);

					if(DropQuote.Preferences.HAS_BOARDERS)
						setBorders(cell1, DropQuote.Preferences.GRID_COLOR);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(DropQuote.Preferences.FONT_NAME);
					rt1.setFontColor(DropQuote.Preferences.TEXT_COLOR);
					rt1.setFontSize(DropQuote.Preferences.GRID_FONT_SIZE);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int i = 0; i<DropQuote.Preferences.COLUMNS; i++) {
				table.setColumnWidth(i, DropQuote.Preferences.CELL_WIDTH);
			}

			for (int i = 0; i<DropQuote.Preferences.ROWS; i++) {
				table.setRowHeight(i, DropQuote.Preferences.CELL_HEIGHT);
			}

			table.moveTo(DropQuote.Preferences.STARTING_X, DropQuote.Preferences.STARTING_Y);

			//creating the puzzle on the slide
			HSLFTable table2 = slide.createTable(DropQuote.Preferences.ROWS, DropQuote.Preferences.COLUMNS);
			grid = puzzle.getPuzzleGrid();
			for (int i = 0; i<DropQuote.Preferences.COLUMNS; i++) {
				for (int j = 0; j<DropQuote.Preferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					if(char_string.equals(" ")) {
						HSLFTableCell cell1 = table2.getCell(j, i);
						
						cell1.setText(" ");
						cell1.setFillColor(DropQuote.Preferences.FILL_COLOR);

						if(DropQuote.Preferences.HAS_BOARDERS)
							setBorders(cell1, DropQuote.Preferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(DropQuote.Preferences.FONT_NAME);
						rt1.setFontSize(DropQuote.Preferences.GRID_FONT_SIZE);
						rt1.setFontColor(DropQuote.Preferences.TEXT_COLOR);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					} else {
						HSLFTableCell cell1 = table2.getCell(j, i);

						cell1.setText(" ");

						if(DropQuote.Preferences.HAS_BOARDERS)
							setBorders(cell1, DropQuote.Preferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(DropQuote.Preferences.FONT_NAME);
						rt1.setFontSize(DropQuote.Preferences.GRID_FONT_SIZE);
						rt1.setFontColor(DropQuote.Preferences.TEXT_COLOR);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					}
				}
			}

			for (int i = 0; i<DropQuote.Preferences.COLUMNS; i++) {
				table2.setColumnWidth(i, DropQuote.Preferences.CELL_WIDTH);
			}

			for (int i = 0; i<DropQuote.Preferences.ROWS; i++) {
				table2.setRowHeight(i, DropQuote.Preferences.CELL_HEIGHT);
			}

			table2.moveTo(DropQuote.Preferences.STARTING_X, DropQuote.Preferences.STARTING_Y*4);

			
			createSlideNumber(slide, puzzle_slide_no, DropQuote.Preferences.FONT_NAME, DropQuote.Preferences.SLIDE_NUMBER_COLOR);
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

	@SuppressWarnings("resource")
	private void dropSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		int puzzle_slide_no = 1;
		
		for(int n = 0; n<NUMBER_OF_PUZZLES; n++) {
			String quote = scan.nextLine();
			DropQuote puzzle = new DropQuote(quote);			
			HSLFSlide slide = ppt.createSlide();
			String title_name = "Drop Quote Solution";
			createTitle(slide, title_name, DropQuote.Preferences.FONT_NAME, DropQuote.Preferences.TITLE_FONT_SIZE, DropQuote.Preferences.TITLE_COLOR);
			createLogo(ppt, slide);
			
			//creating the solution on the slide
			HSLFTable table = slide.createTable(DropQuote.Preferences.ROWS, DropQuote.Preferences.COLUMNS);
			String[][] grid = puzzle.getPuzzleGrid();
			for (int i = 0; i<DropQuote.Preferences.COLUMNS; i++) {
				for (int j = 0; j<DropQuote.Preferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					
					//Fill in spaces
					if(char_string.equals(" ")) {
						HSLFTableCell cell1 = table.getCell(j, i);
						cell1.setText(" ");
						cell1.setFillColor(DropQuote.Preferences.FILL_COLOR);
						if(DropQuote.Preferences.HAS_BOARDERS)
							setBorders(cell1, DropQuote.Preferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontColor(DropQuote.Preferences.TEXT_COLOR);
						rt1.setFontFamily(DropQuote.Preferences.FONT_NAME);
						rt1.setFontSize(DropQuote.Preferences.GRID_FONT_SIZE);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					} else {
						HSLFTableCell cell1 = table.getCell(j, i);

						cell1.setText(char_string);

						if(DropQuote.Preferences.HAS_BOARDERS)
							setBorders(cell1, DropQuote.Preferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(DropQuote.Preferences.FONT_NAME);
						rt1.setFontSize(DropQuote.Preferences.GRID_FONT_SIZE);
						rt1.setFontColor(DropQuote.Preferences.TEXT_COLOR);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					}
				}
			}

			for (int i = 0; i<DropQuote.Preferences.COLUMNS; i++) {
				table.setColumnWidth(i, DropQuote.Preferences.CELL_WIDTH);
			}

			for (int i = 0; i<DropQuote.Preferences.ROWS; i++) {
				table.setRowHeight(i, DropQuote.Preferences.CELL_HEIGHT);
			}

			table.moveTo(DropQuote.Preferences.STARTING_X, DropQuote.Preferences.STARTING_Y);

			
			createSlideNumber(slide, puzzle_slide_no, DropQuote.Preferences.FONT_NAME, DropQuote.Preferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}
	}
	
	
	
	public void createStripperQuote() throws IOException, SQLException {
		
		String char_string;
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		int yOffSet;
		HSLFSlideShow ppt = new HSLFSlideShow();
		
		for(int n = 0; n<NUMBER_OF_PUZZLES; n++) {
			
			String quote = scan.nextLine();
			StripperQuote puzzle = new StripperQuote(quote);
			yOffSet = 0;
			
			HSLFSlide slide = ppt.createSlide();
			createTitle(slide, StripperQuote.Preferences.TITLE, StripperQuote.Preferences.FONT_NAME, StripperQuote.Preferences.TITLE_FONT_SIZE, StripperQuote.Preferences.SLIDE_NUMBER_COLOR);
			createLogo(ppt, slide);

			//Creating the clue at top of slide
			HSLFTable table = slide.createTable(StripperQuote.Preferences.ROWS, StripperQuote.Preferences.COLUMNS);
			String[][] grid = puzzle.getBankGrid();
			for (int i = 0; i<StripperQuote.Preferences.ROWS; i++) {
				for (int j = 0; j<StripperQuote.Preferences.COLUMNS; j++) {
					char_string = String.valueOf(grid[i][j]);
					HSLFTableCell cell1 = table.getCell(i, j);

					cell1.setText(char_string);

					//setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(StripperQuote.Preferences.FONT_NAME);
					rt1.setFontSize(StripperQuote.Preferences.GRID_FONT_SIZE);
					rt1.setFontColor(StripperQuote.Preferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}
			
			for (int b = 0; b<StripperQuote.Preferences.COLUMNS; b++) {
				table.setColumnWidth(b, StripperQuote.Preferences.CELL_WIDTH);
			}

			for (int b = 0; b<StripperQuote.Preferences.ROWS; b++) {
				table.setRowHeight(b, StripperQuote.Preferences.CELL_HEIGHT);
			}

			table.moveTo(StripperQuote.Preferences.STARTING_X, StripperQuote.Preferences.STARTING_Y + yOffSet);
			
			yOffSet += (45*StripperQuote.Preferences.ROWS);
			
			//Creating the puzzle at bottom of slide
			int index = 0;
			for(int i = 0; i<StripperQuote.Preferences.ROWS; i++) {
				
				table = slide.createTable(1, StripperQuote.Preferences.COLUMNS);
				for (int j = 0; j < StripperQuote.Preferences.COLUMNS; j++) {
					if(index<puzzle.getLogicalChars().size()) {
						char_string = puzzle.getLogicalChars().get(index);
					} else {
						char_string = " ";
					}
					
					HSLFTableCell cell1 = table.getCell(0, j);

					if(!char_string.equals(" ")) {
						setBorders(cell1, StripperQuote.Preferences.GRID_COLOR);
						if(hasPunctuation(char_string))
							cell1.setText(char_string);
						else
							cell1.setText(" ");
					}
						
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(StripperQuote.Preferences.FONT_NAME);
					rt1.setFontSize(StripperQuote.Preferences.GRID_FONT_SIZE);
					rt1.setFontColor(StripperQuote.Preferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
					index++;
				}
				
				
				for (int b = 0; b < StripperQuote.Preferences.COLUMNS; b++) {
					table.setColumnWidth(b, StripperQuote.Preferences.CELL_WIDTH);
				}

				
				table.setRowHeight(0, StripperQuote.Preferences.CELL_HEIGHT);
				

				table.moveTo(StripperQuote.Preferences.STARTING_X, StripperQuote.Preferences.STARTING_Y + yOffSet);
				yOffSet += 45;
			}
			
			
			
			createSlideNumber(slide, puzzle_slide_no, StripperQuote.Preferences.FONT_NAME, StripperQuote.Preferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}
		
		stripperQuoteSolutions(ppt);
		
		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle is created: " + ppt_file_name);
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		System.out.println("Done.");

		ppt.close();
		scan.close();
	}
	
	@SuppressWarnings("resource")
	private void stripperQuoteSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		int puzzle_slide_no = 1;
		String char_string;
		
		for(int n = 0; n<NUMBER_OF_PUZZLES; n++) {
			String quote = scan.nextLine();
			StripperQuote puzzle = new StripperQuote(quote);
			
			int yOffSet = 30;
			
			HSLFSlide slide = ppt.createSlide();
			String title_name = "Stripper Quote Solution";
			createTitle(slide, title_name, StripperQuote.Preferences.FONT_NAME, StripperQuote.Preferences.TITLE_FONT_SIZE, StripperQuote.Preferences.TITLE_COLOR);
			createLogo(ppt, slide);

			HSLFTable table;
			
			int index = 0;
			for(int i = 0; i<StripperQuote.Preferences.ROWS; i++) {
				
				table = slide.createTable(1, StripperQuote.Preferences.COLUMNS);
				for (int j = 0; j < StripperQuote.Preferences.COLUMNS; j++) {
					if(index<puzzle.getLogicalChars().size()) {
						char_string = puzzle.getLogicalChars().get(index);
					} else {
						char_string = " ";
					}
					
					HSLFTableCell cell1 = table.getCell(0, j);

					if(!char_string.equals(" ")) {
						setBorders(cell1, StripperQuote.Preferences.GRID_COLOR);
						cell1.setText(char_string);
					}
						
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(StripperQuote.Preferences.FONT_NAME);
					rt1.setFontSize(StripperQuote.Preferences.GRID_FONT_SIZE);
					rt1.setFontColor(StripperQuote.Preferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
					index++;
				}
				
				for (int b = 0; b < StripperQuote.Preferences.COLUMNS; b++) {
					table.setColumnWidth(b, StripperQuote.Preferences.CELL_WIDTH);
				}

				
				table.setRowHeight(0, StripperQuote.Preferences.CELL_HEIGHT);
				

				table.moveTo(StripperQuote.Preferences.STARTING_X, StripperQuote.Preferences.STARTING_Y + yOffSet);
				yOffSet += 45;
			}
			
			
			
			createSlideNumber(slide, puzzle_slide_no, StripperQuote.Preferences.FONT_NAME, StripperQuote.Preferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}
	}
	
	
	public void createScrambleQuote() throws SQLException, IOException {
		
		String char_string;
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();
		
		for(int n = 0; n<NUMBER_OF_PUZZLES; n++) {
			
			String quote = scan.nextLine();
			ScrambleQuote puzzle = new ScrambleQuote(quote);
			int yOffSet = 0;
			
			HSLFSlide slide = ppt.createSlide();
			createTitle(slide, ScrambleQuote.Preferences.TITLE, ScrambleQuote.Preferences.FONT_NAME, ScrambleQuote.Preferences.TITLE_FONT_SIZE, ScrambleQuote.Preferences.TITLE_COLOR);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(ScrambleQuote.Preferences.ROWS, ScrambleQuote.Preferences.COLUMNS);
			String[][] grid = puzzle.getBankGrid();
			for (int i = 0; i < ScrambleQuote.Preferences.ROWS; i++) {
				for (int j = 0; j < ScrambleQuote.Preferences.COLUMNS; j++) {
					char_string = String.valueOf(grid[i][j]);
					HSLFTableCell cell1 = table.getCell(i, j);

					cell1.setText(char_string);

					//setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(ScrambleQuote.Preferences.FONT_NAME);
					rt1.setFontSize(ScrambleQuote.Preferences.GRID_FONT_SIZE);
					rt1.setFontColor(ScrambleQuote.Preferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}
			
			for (int b = 0; b < ScrambleQuote.Preferences.COLUMNS; b++) {
				table.setColumnWidth(b, ScrambleQuote.Preferences.CELL_WIDTH);
			}

			for (int b = 0; b < ScrambleQuote.Preferences.ROWS; b++) {
				table.setRowHeight(b, ScrambleQuote.Preferences.CELL_HEIGHT);
			}

			table.moveTo(ScrambleQuote.Preferences.STARTING_X, ScrambleQuote.Preferences.STARTING_Y + yOffSet);
			
			yOffSet += (45*ScrambleQuote.Preferences.ROWS);
			
			int index = 0;
			for(int i = 0; i<ScrambleQuote.Preferences.ROWS; i++) {
				
				table = slide.createTable(1, ScrambleQuote.Preferences.COLUMNS);
				for (int j = 0; j < ScrambleQuote.Preferences.COLUMNS; j++) {
					if(index<puzzle.getLogicalChars().size()) {
						char_string = puzzle.getLogicalChars().get(index);
					} else {
						char_string = " ";
					}
					
					HSLFTableCell cell1 = table.getCell(0, j);

					if(!char_string.equals(" ")) {
						setBorders(cell1, ScrambleQuote.Preferences.GRID_COLOR);
						if(hasPunctuation(char_string))
							cell1.setText(char_string);
						else
							cell1.setText(" ");
					}
						
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(ScrambleQuote.Preferences.FONT_NAME);
					rt1.setFontSize(ScrambleQuote.Preferences.GRID_FONT_SIZE);
					rt1.setFontColor(ScrambleQuote.Preferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
					index++;
				}
				
				for (int b = 0; b < ScrambleQuote.Preferences.COLUMNS; b++) {
					table.setColumnWidth(b, ScrambleQuote.Preferences.CELL_WIDTH);
				}

				
				table.setRowHeight(0, ScrambleQuote.Preferences.CELL_HEIGHT);
				

				table.moveTo(ScrambleQuote.Preferences.STARTING_X, ScrambleQuote.Preferences.STARTING_Y + yOffSet);
				yOffSet += 45;
			}
			
			
			
			createSlideNumber(slide, puzzle_slide_no, ScrambleQuote.Preferences.FONT_NAME, ScrambleQuote.Preferences.SLIDE_NUMBER_COLOR);
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
	
	@SuppressWarnings("resource")
	private void scrambleQuoteSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		int puzzle_slide_no = 1;
		String char_string;
		
		for(int n = 0; n<NUMBER_OF_PUZZLES; n++) {
			String quote = scan.nextLine();
			ScrambleQuote puzzle = new ScrambleQuote(quote);
			
			int yOffSet = 30;
			
			HSLFSlide slide = ppt.createSlide();
			String title_name = "Scramble Quote Solution";
			createTitle(slide, title_name, ScrambleQuote.Preferences.FONT_NAME, ScrambleQuote.Preferences.TITLE_FONT_SIZE, ScrambleQuote.Preferences.TITLE_COLOR);
			createLogo(ppt, slide);

			HSLFTable table;
			
			int index = 0;
			for(int i = 0; i<ScrambleQuote.Preferences.ROWS; i++) {
				
				table = slide.createTable(1, ScrambleQuote.Preferences.COLUMNS);
				for (int j = 0; j < ScrambleQuote.Preferences.COLUMNS; j++) {
					if(index<puzzle.getLogicalChars().size()) {
						char_string = puzzle.getLogicalChars().get(index);
					} else {
						char_string = " ";
					}
					
					HSLFTableCell cell1 = table.getCell(0, j);

					if(!char_string.equals(" ")) {
						setBorders(cell1, ScrambleQuote.Preferences.GRID_COLOR);
						cell1.setText(char_string);
					}
						
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(ScrambleQuote.Preferences.FONT_NAME);
					rt1.setFontSize(ScrambleQuote.Preferences.GRID_FONT_SIZE);
					rt1.setFontColor(ScrambleQuote.Preferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
					index++;
				}
				
				for (int b = 0; b < ScrambleQuote.Preferences.COLUMNS; b++) {
					table.setColumnWidth(b, ScrambleQuote.Preferences.CELL_WIDTH);
				}

				
				table.setRowHeight(0, ScrambleQuote.Preferences.CELL_HEIGHT);
				

				table.moveTo(ScrambleQuote.Preferences.STARTING_X, ScrambleQuote.Preferences.STARTING_Y + yOffSet);
				yOffSet += 45;
			}
			
			
			
			createSlideNumber(slide, puzzle_slide_no, ScrambleQuote.Preferences.FONT_NAME, ScrambleQuote.Preferences.SLIDE_NUMBER_COLOR);
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

	public static void createTitle(HSLFSlide slide, String title, String fontName, double fontSize, Color c) {
		HSLFTextBox titleBox = slide.createTextBox();
		HSLFTextParagraph p1 = titleBox.getTextParagraphs().get(0);
		p1.setTextAlign(TextAlign.CENTER);
		HSLFTextRun run = p1.getTextRuns().get(0);
		run.setFontColor(c);
		run.setText(title.toUpperCase());
		run.setFontFamily(fontName);
		run.setFontSize(fontSize);
		titleBox.setAnchor(new Rectangle(240, 10, 400, 200));
	}
	
	public static void createSlideNumber(HSLFSlide slide, int slideNumber, String fontName, Color c) {
		HSLFTextBox slideNumberBox = slide.createTextBox();
		HSLFTextParagraph p = slideNumberBox.getTextParagraphs().get(0);
		p.setTextAlign(TextAlign.CENTER);
		HSLFTextRun r = p.getTextRuns().get(0);
		r.setText("" + slideNumber + "");
		r.setFontFamily(fontName);
		r.setFontSize(30.);
		r.setFontColor(c);
		slideNumberBox.setAnchor(new Rectangle(220, 10, 50, 30));

		createOutline(slide, 220, 5, 50, 0, c); // top line
		createOutline(slide, 270, 5, 0, 50, c); // right line
		createOutline(slide, 220, 55, 50, 0, c); // bottom line
		createOutline(slide, 220, 5, 0, 50, c); // left line
	}

	public static void createOutline(HSLFSlide slide, int x, int y, int width, int height, Color c) {
		HSLFLine line = new HSLFLine();
		line.setAnchor(new Rectangle(x, y, width, height));
		line.setLineColor(c);
		slide.addShape(line);
	}
	
	public static void createLogo(HSLFSlideShow ppt, HSLFSlide slide) throws IOException {
		byte[] picture = IOUtils.toByteArray(new FileInputStream(new File("logo.png")));
		HSLFPictureData pd = ppt.addPicture(picture, HSLFPictureData.PictureType.PNG);
		HSLFPictureShape pic_shape = slide.createPicture(pd);
		pic_shape.setAnchor(new Rectangle(0, 0, 174, 65));
	}
	
	public static void setBorders(HSLFTableCell cell, Color gridColor) {

		cell.setBorderColor(BorderEdge.bottom, gridColor);
		cell.setBorderColor(BorderEdge.top, gridColor);
		cell.setBorderColor(BorderEdge.right, gridColor);
		cell.setBorderColor(BorderEdge.left, gridColor);

	}
}
