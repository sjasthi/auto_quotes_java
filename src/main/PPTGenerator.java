package main;
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
import games.DropQuote;
import games.FloatQuote;
import games.ScrambleQuote;
import games.SplitQuote;
import games.StripperQuote;
import preferences.DropQuotePreferences;
import preferences.FloatQuotePreferences;
import preferences.ScrambleQuotePreferences;
import preferences.SplitQuotePreferences;
import preferences.StripperQuotePreferences;



public class PPTGenerator {

	private File quotes = new File("Test.txt");
	private static String PPT_FILE_NAME;
	
	public PPTGenerator() throws SQLException, IOException {
		System.out.println("Loading...");
		
	}

	public void createSplitQuote() throws SQLException, IOException {

		String timeStamp = String.valueOf(System.currentTimeMillis());
		PPT_FILE_NAME = "Puzzles_" + timeStamp + ".ppt";
		
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();

		for(int n = 0; n<SplitQuotePreferences.PUZZLE_COUNT; n++) {

			String quote = scan.nextLine();
			SplitQuote puzzle = new SplitQuote(quote);
			String[][] grid = puzzle.getPuzzleGrid();

			HSLFSlide slide = ppt.createSlide();
			createTitle(slide, SplitQuotePreferences.TITLE, SplitQuotePreferences.FONT_NAME, SplitQuotePreferences.TITLE_FONT_SIZE, SplitQuotePreferences.TITLE_COLOR);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(SplitQuotePreferences.ROWS, SplitQuotePreferences.COLUMNS); 
			//getLabels(slide, puzzle.getRows(), puzzle.getColumns()); 

			for (int i = 0; i<SplitQuotePreferences.COLUMNS; i++) {
				for (int j = 0; j<SplitQuotePreferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					HSLFTableCell cell1 = table.getCell(j, i);

					cell1.setText(char_string);

					if(!char_string.equals(""))
						setBorders(cell1, SplitQuotePreferences.GRID_COLOR);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(SplitQuotePreferences.FONT_NAME);
					rt1.setFontSize(SplitQuotePreferences.GRID_FONT_SIZE);
					rt1.setFontColor(SplitQuotePreferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int i = 0; i<SplitQuotePreferences.COLUMNS; i++) {
				table.setColumnWidth(i, SplitQuotePreferences.CELL_WIDTH);
			}

			for (int i = 0; i<SplitQuotePreferences.ROWS; i++) {
				table.setRowHeight(i, SplitQuotePreferences.CELL_HEIGHT);
			}

			table.moveTo(SplitQuotePreferences.STARTING_X, SplitQuotePreferences.STARTING_Y);

			createSlideNumber(slide, puzzle_slide_no, SplitQuotePreferences.FONT_NAME, SplitQuotePreferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}

		splitQuoteSolutions(ppt);

		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle created: " + ppt_file_name);
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		
		ppt.close();
		scan.close();

	}

	@SuppressWarnings("resource")
	private void splitQuoteSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);

		int puzzle_slide_no = 1;

		for(int n = 0; n<SplitQuotePreferences.PUZZLE_COUNT; n++) {

			String quote = scan.nextLine();
			SplitQuote puzzle = new SplitQuote(quote);
			String[][] grid = puzzle.getSolutionGrid();

			HSLFSlide slide = ppt.createSlide();
			String title_name = "Split Quote Solution";
			createTitle(slide, title_name, SplitQuotePreferences.FONT_NAME, SplitQuotePreferences.TITLE_FONT_SIZE, SplitQuotePreferences.TITLE_COLOR);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(SplitQuotePreferences.ROWS, SplitQuotePreferences.COLUMNS); 
			//getLabels(slide, puzzle.getRows(), puzzle.getColumns()); 

			for (int i = 0; i<SplitQuotePreferences.COLUMNS; i++) {
				for (int j = 0; j<SplitQuotePreferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					HSLFTableCell cell1 = table.getCell(j, i);

					cell1.setText(char_string);

					if(!char_string.equals(""))
						setBorders(cell1, SplitQuotePreferences.GRID_COLOR);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(SplitQuotePreferences.FONT_NAME);
					rt1.setFontSize(SplitQuotePreferences.GRID_FONT_SIZE);
					rt1.setFontColor(SplitQuotePreferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int i = 0; i<SplitQuotePreferences.COLUMNS; i++) {
				table.setColumnWidth(i, SplitQuotePreferences.CELL_WIDTH);
			}

			for (int i = 0; i<SplitQuotePreferences.ROWS; i++) {
				table.setRowHeight(i, SplitQuotePreferences.CELL_HEIGHT);
			}

			table.moveTo(SplitQuotePreferences.STARTING_X, SplitQuotePreferences.STARTING_Y);

			createSlideNumber(slide, puzzle_slide_no, SplitQuotePreferences.FONT_NAME, SplitQuotePreferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}
	}

	public void createDropQuote() throws SQLException, IOException {

		String timeStamp = String.valueOf(System.currentTimeMillis());
		PPT_FILE_NAME = "Puzzles_" + timeStamp + ".ppt";
		
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();

		//Repeat all this for each puzzle
		for(int n = 0; n<DropQuotePreferences.PUZZLE_COUNT; n++) {

			String quote = scan.nextLine();
			DropQuote puzzle = new DropQuote(quote);
			String[][] grid = puzzle.getScrambleGrid();

			HSLFSlide slide = ppt.createSlide();
			createTitle(slide, DropQuotePreferences.TITLE, DropQuotePreferences.FONT_NAME, DropQuotePreferences.TITLE_FONT_SIZE, DropQuotePreferences.TITLE_COLOR);
			createLogo(ppt, slide);

			//creating the clue at top of slide 
			HSLFTable table = slide.createTable(DropQuotePreferences.ROWS, DropQuotePreferences.COLUMNS); 
			for (int i = 0; i<DropQuotePreferences.COLUMNS; i++) {
				for (int j = 0; j<DropQuotePreferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					HSLFTableCell cell1 = table.getCell(j, i);

					cell1.setText(char_string);

					if(DropQuotePreferences.HAS_BOARDERS)
						setBorders(cell1, DropQuotePreferences.GRID_COLOR);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(DropQuotePreferences.FONT_NAME);
					rt1.setFontColor(DropQuotePreferences.TEXT_COLOR);
					rt1.setFontSize(DropQuotePreferences.GRID_FONT_SIZE);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int i = 0; i<DropQuotePreferences.COLUMNS; i++) {
				table.setColumnWidth(i, DropQuotePreferences.CELL_WIDTH);
			}

			for (int i = 0; i<DropQuotePreferences.ROWS; i++) {
				table.setRowHeight(i, DropQuotePreferences.CELL_HEIGHT);
			}

			table.moveTo(DropQuotePreferences.STARTING_X, DropQuotePreferences.STARTING_Y);

			//creating the puzzle on the slide
			HSLFTable table2 = slide.createTable(DropQuotePreferences.ROWS, DropQuotePreferences.COLUMNS);
			grid = puzzle.getPuzzleGrid();
			for (int i = 0; i<DropQuotePreferences.COLUMNS; i++) {
				for (int j = 0; j<DropQuotePreferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					if(char_string.equals(" ")) {
						HSLFTableCell cell1 = table2.getCell(j, i);

						cell1.setText(" ");
						cell1.setFillColor(DropQuotePreferences.FILL_COLOR);

						if(DropQuotePreferences.HAS_BOARDERS)
							setBorders(cell1, DropQuotePreferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(DropQuotePreferences.FONT_NAME);
						rt1.setFontSize(DropQuotePreferences.GRID_FONT_SIZE);
						rt1.setFontColor(DropQuotePreferences.TEXT_COLOR);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					} else {
						HSLFTableCell cell1 = table2.getCell(j, i);

						cell1.setText(" ");

						if(DropQuotePreferences.HAS_BOARDERS)
							setBorders(cell1, DropQuotePreferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(DropQuotePreferences.FONT_NAME);
						rt1.setFontSize(DropQuotePreferences.GRID_FONT_SIZE);
						rt1.setFontColor(DropQuotePreferences.TEXT_COLOR);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					}
				}
			}

			for (int i = 0; i<DropQuotePreferences.COLUMNS; i++) {
				table2.setColumnWidth(i, DropQuotePreferences.CELL_WIDTH);
			}

			for (int i = 0; i<DropQuotePreferences.ROWS; i++) {
				table2.setRowHeight(i, DropQuotePreferences.CELL_HEIGHT);
			}

			table2.moveTo(DropQuotePreferences.STARTING_X, DropQuotePreferences.STARTING_Y*4);


			createSlideNumber(slide, puzzle_slide_no, DropQuotePreferences.FONT_NAME, DropQuotePreferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}

		dropSolutions(ppt);

		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle created: " + ppt_file_name);
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		
		ppt.close();
		scan.close();

	}

	@SuppressWarnings("resource")
	private void dropSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		int puzzle_slide_no = 1;

		for(int n = 0; n<DropQuotePreferences.PUZZLE_COUNT; n++) {
			String quote = scan.nextLine();
			DropQuote puzzle = new DropQuote(quote);			
			HSLFSlide slide = ppt.createSlide();
			String title_name = "Drop Quote Solution";
			createTitle(slide, title_name, DropQuotePreferences.FONT_NAME, DropQuotePreferences.TITLE_FONT_SIZE, DropQuotePreferences.TITLE_COLOR);
			createLogo(ppt, slide);

			//creating the solution on the slide
			HSLFTable table = slide.createTable(DropQuotePreferences.ROWS, DropQuotePreferences.COLUMNS);
			String[][] grid = puzzle.getPuzzleGrid();
			for (int i = 0; i<DropQuotePreferences.COLUMNS; i++) {
				for (int j = 0; j<DropQuotePreferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);

					//Fill in spaces
					if(char_string.equals(" ")) {
						HSLFTableCell cell1 = table.getCell(j, i);
						cell1.setText(" ");
						cell1.setFillColor(DropQuotePreferences.FILL_COLOR);
						if(DropQuotePreferences.HAS_BOARDERS)
							setBorders(cell1, DropQuotePreferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontColor(DropQuotePreferences.TEXT_COLOR);
						rt1.setFontFamily(DropQuotePreferences.FONT_NAME);
						rt1.setFontSize(DropQuotePreferences.GRID_FONT_SIZE);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					} else {
						HSLFTableCell cell1 = table.getCell(j, i);

						cell1.setText(char_string);

						if(DropQuotePreferences.HAS_BOARDERS)
							setBorders(cell1, DropQuotePreferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(DropQuotePreferences.FONT_NAME);
						rt1.setFontSize(DropQuotePreferences.GRID_FONT_SIZE);
						rt1.setFontColor(DropQuotePreferences.TEXT_COLOR);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					}
				}
			}

			for (int i = 0; i<DropQuotePreferences.COLUMNS; i++) {
				table.setColumnWidth(i, DropQuotePreferences.CELL_WIDTH);
			}

			for (int i = 0; i<DropQuotePreferences.ROWS; i++) {
				table.setRowHeight(i, DropQuotePreferences.CELL_HEIGHT);
			}

			table.moveTo(DropQuotePreferences.STARTING_X, DropQuotePreferences.STARTING_Y);


			createSlideNumber(slide, puzzle_slide_no, DropQuotePreferences.FONT_NAME, DropQuotePreferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}
	}

	public void createFloatQuote() throws SQLException, IOException {

		String timeStamp = String.valueOf(System.currentTimeMillis());
		PPT_FILE_NAME = "Puzzles_" + timeStamp + ".ppt";
		
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();

		//Repeat all this for each puzzle
		for(int n = 0; n<FloatQuotePreferences.PUZZLE_COUNT; n++) {

			String quote = scan.nextLine();
			FloatQuote puzzle = new FloatQuote(quote);
			String[][] grid = puzzle.getPuzzleGrid();

			HSLFSlide slide = ppt.createSlide();
			createTitle(slide, FloatQuotePreferences.TITLE, FloatQuotePreferences.FONT_NAME, FloatQuotePreferences.TITLE_FONT_SIZE, FloatQuotePreferences.TITLE_COLOR);
			createLogo(ppt, slide);

			//creating puzzle at top of the slide
			HSLFTable table2 = slide.createTable(FloatQuotePreferences.ROWS, FloatQuotePreferences.COLUMNS);
			for (int i = 0; i<FloatQuotePreferences.COLUMNS; i++) {
				for (int j = 0; j<FloatQuotePreferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					if(char_string.equals(" ")) {
						HSLFTableCell cell1 = table2.getCell(j, i);

						cell1.setText(" ");
						cell1.setFillColor(FloatQuotePreferences.FILL_COLOR);

						if(FloatQuotePreferences.HAS_BOARDERS)
							setBorders(cell1, FloatQuotePreferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(FloatQuotePreferences.FONT_NAME);
						rt1.setFontSize(FloatQuotePreferences.GRID_FONT_SIZE);
						rt1.setFontColor(FloatQuotePreferences.TEXT_COLOR);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					} else {
						HSLFTableCell cell1 = table2.getCell(j, i);

						cell1.setText(" ");

						if(FloatQuotePreferences.HAS_BOARDERS)
							setBorders(cell1, FloatQuotePreferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(FloatQuotePreferences.FONT_NAME);
						rt1.setFontSize(FloatQuotePreferences.GRID_FONT_SIZE);
						rt1.setFontColor(FloatQuotePreferences.TEXT_COLOR);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					}
				}
			}

			for (int i = 0; i<FloatQuotePreferences.COLUMNS; i++) {
				table2.setColumnWidth(i, FloatQuotePreferences.CELL_WIDTH);
			}

			for (int i = 0; i<FloatQuotePreferences.ROWS; i++) {
				table2.setRowHeight(i, FloatQuotePreferences.CELL_HEIGHT);
			}

			table2.moveTo(FloatQuotePreferences.STARTING_X, FloatQuotePreferences.STARTING_Y);

			//creating the clue at bottom of slide
			grid = puzzle.getScrambleGrid();
			HSLFTable table = slide.createTable(FloatQuotePreferences.ROWS, FloatQuotePreferences.COLUMNS); 
			for (int i = 0; i<FloatQuotePreferences.COLUMNS; i++) {
				for (int j = 0; j<FloatQuotePreferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);
					HSLFTableCell cell1 = table.getCell(j, i);

					cell1.setText(char_string);

					if(FloatQuotePreferences.HAS_BOARDERS)
						setBorders(cell1, FloatQuotePreferences.GRID_COLOR);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(FloatQuotePreferences.FONT_NAME);
					rt1.setFontColor(FloatQuotePreferences.TEXT_COLOR);
					rt1.setFontSize(FloatQuotePreferences.GRID_FONT_SIZE);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int i = 0; i<FloatQuotePreferences.COLUMNS; i++) {
				table.setColumnWidth(i, FloatQuotePreferences.CELL_WIDTH);
			}

			for (int i = 0; i<FloatQuotePreferences.ROWS; i++) {
				table.setRowHeight(i, FloatQuotePreferences.CELL_HEIGHT);
			}

			table.moveTo(FloatQuotePreferences.STARTING_X, FloatQuotePreferences.STARTING_Y*4);

			createSlideNumber(slide, puzzle_slide_no, FloatQuotePreferences.FONT_NAME, FloatQuotePreferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;


		}

		floatSolutions(ppt);

		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle created: " + ppt_file_name);
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		
		ppt.close();
		scan.close();

	}

	@SuppressWarnings("resource")
	private void floatSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		int puzzle_slide_no = 1;

		for(int n = 0; n<FloatQuotePreferences.PUZZLE_COUNT; n++) {
			String quote = scan.nextLine();
			FloatQuote puzzle = new FloatQuote(quote);			
			HSLFSlide slide = ppt.createSlide();
			String title_name = "Float Quote Solution";
			createTitle(slide, title_name, FloatQuotePreferences.FONT_NAME, FloatQuotePreferences.TITLE_FONT_SIZE, FloatQuotePreferences.TITLE_COLOR);
			createLogo(ppt, slide);

			//creating the solution on the slide
			HSLFTable table = slide.createTable(FloatQuotePreferences.ROWS, FloatQuotePreferences.COLUMNS);
			String[][] grid = puzzle.getPuzzleGrid();
			for (int i = 0; i<FloatQuotePreferences.COLUMNS; i++) {
				for (int j = 0; j<FloatQuotePreferences.ROWS; j++) {
					String char_string = String.valueOf(grid[j][i]);

					//Fill in spaces
					if(char_string.equals(" ")) {
						HSLFTableCell cell1 = table.getCell(j, i);
						cell1.setText(" ");
						cell1.setFillColor(FloatQuotePreferences.FILL_COLOR);
						if(FloatQuotePreferences.HAS_BOARDERS)
							setBorders(cell1, FloatQuotePreferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontColor(FloatQuotePreferences.TEXT_COLOR);
						rt1.setFontFamily(FloatQuotePreferences.FONT_NAME);
						rt1.setFontSize(FloatQuotePreferences.GRID_FONT_SIZE);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					} else {
						HSLFTableCell cell1 = table.getCell(j, i);

						cell1.setText(char_string);

						if(FloatQuotePreferences.HAS_BOARDERS)
							setBorders(cell1, FloatQuotePreferences.GRID_COLOR);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(FloatQuotePreferences.FONT_NAME);
						rt1.setFontSize(FloatQuotePreferences.GRID_FONT_SIZE);
						rt1.setFontColor(FloatQuotePreferences.TEXT_COLOR);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					}
				}
			}

			for (int i = 0; i<FloatQuotePreferences.COLUMNS; i++) {
				table.setColumnWidth(i, FloatQuotePreferences.CELL_WIDTH);
			}

			for (int i = 0; i<FloatQuotePreferences.ROWS; i++) {
				table.setRowHeight(i, FloatQuotePreferences.CELL_HEIGHT);
			}

			table.moveTo(FloatQuotePreferences.STARTING_X, FloatQuotePreferences.STARTING_Y);


			createSlideNumber(slide, puzzle_slide_no, FloatQuotePreferences.FONT_NAME, FloatQuotePreferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}
	}


	public void createStripperQuote() throws IOException, SQLException {

		String timeStamp = String.valueOf(System.currentTimeMillis());
		PPT_FILE_NAME = "Puzzles_" + timeStamp + ".ppt";
		
		String char_string;
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		int yOffSet;
		HSLFSlideShow ppt = new HSLFSlideShow();

		for(int n = 0; n<StripperQuotePreferences.PUZZLE_COUNT; n++) {

			String quote = scan.nextLine();
			StripperQuote puzzle = new StripperQuote(quote);
			yOffSet = 0;

			HSLFSlide slide = ppt.createSlide();
			createTitle(slide, StripperQuotePreferences.TITLE, StripperQuotePreferences.FONT_NAME, StripperQuotePreferences.TITLE_FONT_SIZE, StripperQuotePreferences.SLIDE_NUMBER_COLOR);
			createLogo(ppt, slide);

			//Creating the clue at top of slide
			HSLFTable table = slide.createTable(StripperQuotePreferences.ROWS, StripperQuotePreferences.COLUMNS);
			String[][] grid = puzzle.getBankGrid();
			for (int i = 0; i<StripperQuotePreferences.ROWS; i++) {
				for (int j = 0; j<StripperQuotePreferences.COLUMNS; j++) {
					char_string = String.valueOf(grid[i][j]);
					HSLFTableCell cell1 = table.getCell(i, j);

					cell1.setText(char_string);

					//setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(StripperQuotePreferences.FONT_NAME);
					rt1.setFontSize(StripperQuotePreferences.GRID_FONT_SIZE);
					rt1.setFontColor(StripperQuotePreferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int b = 0; b<StripperQuotePreferences.COLUMNS; b++) {
				table.setColumnWidth(b, StripperQuotePreferences.CELL_WIDTH);
			}

			for (int b = 0; b<StripperQuotePreferences.ROWS; b++) {
				table.setRowHeight(b, StripperQuotePreferences.CELL_HEIGHT);
			}

			table.moveTo(StripperQuotePreferences.STARTING_X, StripperQuotePreferences.STARTING_Y + yOffSet);

			yOffSet += (45*StripperQuotePreferences.ROWS);

			//Creating the puzzle at bottom of slide
			int index = 0;
			for(int i = 0; i<StripperQuotePreferences.ROWS; i++) {

				table = slide.createTable(1, StripperQuotePreferences.COLUMNS);
				for (int j = 0; j < StripperQuotePreferences.COLUMNS; j++) {
					if(index<puzzle.getLogicalChars().size()) {
						char_string = puzzle.getLogicalChars().get(index);
					} else {
						char_string = " ";
					}

					HSLFTableCell cell1 = table.getCell(0, j);

					if(!char_string.equals(" ")) {
						setBorders(cell1, StripperQuotePreferences.GRID_COLOR);
						if(hasPunctuation(char_string))
							cell1.setText(char_string);
						else
							cell1.setText(" ");
					}

					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(StripperQuotePreferences.FONT_NAME);
					rt1.setFontSize(StripperQuotePreferences.GRID_FONT_SIZE);
					rt1.setFontColor(StripperQuotePreferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
					index++;
				}


				for (int b = 0; b < StripperQuotePreferences.COLUMNS; b++) {
					table.setColumnWidth(b, StripperQuotePreferences.CELL_WIDTH);
				}


				table.setRowHeight(0, StripperQuotePreferences.CELL_HEIGHT);


				table.moveTo(StripperQuotePreferences.STARTING_X, StripperQuotePreferences.STARTING_Y + yOffSet);
				yOffSet += 45;
			}



			createSlideNumber(slide, puzzle_slide_no, StripperQuotePreferences.FONT_NAME, StripperQuotePreferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}

		stripperQuoteSolutions(ppt);

		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle created: " + ppt_file_name);
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		
		ppt.close();
		scan.close();
	}

	@SuppressWarnings("resource")
	private void stripperQuoteSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		int puzzle_slide_no = 1;
		String char_string;

		for(int n = 0; n<StripperQuotePreferences.PUZZLE_COUNT; n++) {
			String quote = scan.nextLine();
			StripperQuote puzzle = new StripperQuote(quote);

			int yOffSet = 30;

			HSLFSlide slide = ppt.createSlide();
			String title_name = "Stripper Quote Solution";
			createTitle(slide, title_name, StripperQuotePreferences.FONT_NAME, StripperQuotePreferences.TITLE_FONT_SIZE, StripperQuotePreferences.TITLE_COLOR);
			createLogo(ppt, slide);

			HSLFTable table;

			int index = 0;
			for(int i = 0; i<StripperQuotePreferences.ROWS; i++) {

				table = slide.createTable(1, StripperQuotePreferences.COLUMNS);
				for (int j = 0; j < StripperQuotePreferences.COLUMNS; j++) {
					if(index<puzzle.getLogicalChars().size()) {
						char_string = puzzle.getLogicalChars().get(index);
					} else {
						char_string = " ";
					}

					HSLFTableCell cell1 = table.getCell(0, j);

					if(!char_string.equals(" ")) {
						setBorders(cell1, StripperQuotePreferences.GRID_COLOR);
						cell1.setText(char_string);
					}

					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(StripperQuotePreferences.FONT_NAME);
					rt1.setFontSize(StripperQuotePreferences.GRID_FONT_SIZE);
					rt1.setFontColor(StripperQuotePreferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
					index++;
				}

				for (int b = 0; b < StripperQuotePreferences.COLUMNS; b++) {
					table.setColumnWidth(b, StripperQuotePreferences.CELL_WIDTH);
				}


				table.setRowHeight(0, StripperQuotePreferences.CELL_HEIGHT);


				table.moveTo(StripperQuotePreferences.STARTING_X, StripperQuotePreferences.STARTING_Y + yOffSet);
				yOffSet += 45;
			}



			createSlideNumber(slide, puzzle_slide_no, StripperQuotePreferences.FONT_NAME, StripperQuotePreferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}
	}


	public void createScrambleQuote() throws SQLException, IOException {

		String timeStamp = String.valueOf(System.currentTimeMillis());
		PPT_FILE_NAME = "Puzzles_" + timeStamp + ".ppt";
		
		String char_string;
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();

		for(int n = 0; n<ScrambleQuotePreferences.PUZZLE_COUNT; n++) {

			String quote = scan.nextLine();
			ScrambleQuote puzzle = new ScrambleQuote(quote);
			int yOffSet = 0;

			HSLFSlide slide = ppt.createSlide();
			createTitle(slide, ScrambleQuotePreferences.TITLE, ScrambleQuotePreferences.FONT_NAME, ScrambleQuotePreferences.TITLE_FONT_SIZE, ScrambleQuotePreferences.TITLE_COLOR);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(ScrambleQuotePreferences.ROWS, ScrambleQuotePreferences.COLUMNS);
			String[][] grid = puzzle.getBankGrid();
			for (int i = 0; i < ScrambleQuotePreferences.ROWS; i++) {
				for (int j = 0; j < ScrambleQuotePreferences.COLUMNS; j++) {
					char_string = String.valueOf(grid[i][j]);
					HSLFTableCell cell1 = table.getCell(i, j);

					cell1.setText(char_string);

					//setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(ScrambleQuotePreferences.FONT_NAME);
					rt1.setFontSize(ScrambleQuotePreferences.GRID_FONT_SIZE);
					rt1.setFontColor(ScrambleQuotePreferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
			}

			for (int b = 0; b < ScrambleQuotePreferences.COLUMNS; b++) {
				table.setColumnWidth(b, ScrambleQuotePreferences.CELL_WIDTH);
			}

			for (int b = 0; b < ScrambleQuotePreferences.ROWS; b++) {
				table.setRowHeight(b, ScrambleQuotePreferences.CELL_HEIGHT);
			}

			table.moveTo(ScrambleQuotePreferences.STARTING_X, ScrambleQuotePreferences.STARTING_Y + yOffSet);

			yOffSet += (45*ScrambleQuotePreferences.ROWS);

			int index = 0;
			for(int i = 0; i<ScrambleQuotePreferences.ROWS; i++) {

				table = slide.createTable(1, ScrambleQuotePreferences.COLUMNS);
				for (int j = 0; j < ScrambleQuotePreferences.COLUMNS; j++) {
					if(index<puzzle.getLogicalChars().size()) {
						char_string = puzzle.getLogicalChars().get(index);
					} else {
						char_string = " ";
					}

					HSLFTableCell cell1 = table.getCell(0, j);

					if(!char_string.equals(" ")) {
						setBorders(cell1, ScrambleQuotePreferences.GRID_COLOR);
						if(hasPunctuation(char_string))
							cell1.setText(char_string);
						else
							cell1.setText(" ");
					}

					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(ScrambleQuotePreferences.FONT_NAME);
					rt1.setFontSize(ScrambleQuotePreferences.GRID_FONT_SIZE);
					rt1.setFontColor(ScrambleQuotePreferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
					index++;
				}

				for (int b = 0; b < ScrambleQuotePreferences.COLUMNS; b++) {
					table.setColumnWidth(b, ScrambleQuotePreferences.CELL_WIDTH);
				}


				table.setRowHeight(0, ScrambleQuotePreferences.CELL_HEIGHT);


				table.moveTo(ScrambleQuotePreferences.STARTING_X, ScrambleQuotePreferences.STARTING_Y + yOffSet);
				yOffSet += 45;
			}



			createSlideNumber(slide, puzzle_slide_no, ScrambleQuotePreferences.FONT_NAME, ScrambleQuotePreferences.SLIDE_NUMBER_COLOR);
			puzzle_slide_no++;
		}

		scrambleQuoteSolutions(ppt);

		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle created: " + ppt_file_name);
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		
		ppt.close();
		scan.close();

	}

	@SuppressWarnings("resource")
	private void scrambleQuoteSolutions(HSLFSlideShow ppt) throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		int puzzle_slide_no = 1;
		String char_string;

		for(int n = 0; n<ScrambleQuotePreferences.PUZZLE_COUNT; n++) {
			String quote = scan.nextLine();
			ScrambleQuote puzzle = new ScrambleQuote(quote);

			int yOffSet = 30;

			HSLFSlide slide = ppt.createSlide();
			String title_name = "Scramble Quote Solution";
			createTitle(slide, title_name, ScrambleQuotePreferences.FONT_NAME, ScrambleQuotePreferences.TITLE_FONT_SIZE, ScrambleQuotePreferences.TITLE_COLOR);
			createLogo(ppt, slide);

			HSLFTable table;

			int index = 0;
			for(int i = 0; i<ScrambleQuotePreferences.ROWS; i++) {

				table = slide.createTable(1, ScrambleQuotePreferences.COLUMNS);
				for (int j = 0; j < ScrambleQuotePreferences.COLUMNS; j++) {
					if(index<puzzle.getLogicalChars().size()) {
						char_string = puzzle.getLogicalChars().get(index);
					} else {
						char_string = " ";
					}

					HSLFTableCell cell1 = table.getCell(0, j);

					if(!char_string.equals(" ")) {
						setBorders(cell1, ScrambleQuotePreferences.GRID_COLOR);
						cell1.setText(char_string);
					}

					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(ScrambleQuotePreferences.FONT_NAME);
					rt1.setFontSize(ScrambleQuotePreferences.GRID_FONT_SIZE);
					rt1.setFontColor(ScrambleQuotePreferences.TEXT_COLOR);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
					index++;
				}

				for (int b = 0; b < ScrambleQuotePreferences.COLUMNS; b++) {
					table.setColumnWidth(b, ScrambleQuotePreferences.CELL_WIDTH);
				}


				table.setRowHeight(0, ScrambleQuotePreferences.CELL_HEIGHT);


				table.moveTo(ScrambleQuotePreferences.STARTING_X, ScrambleQuotePreferences.STARTING_Y + yOffSet);
				yOffSet += 45;
			}



			createSlideNumber(slide, puzzle_slide_no, ScrambleQuotePreferences.FONT_NAME, ScrambleQuotePreferences.SLIDE_NUMBER_COLOR);
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
