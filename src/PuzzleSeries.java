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
	
	private static String[][] grid;
	private final int numberOfPuzzles;
	private static String PPT_FILE_NAME = "Puzzles.ppt";
	private static String PUZZLE_TITLE;
	private static String FONT_NAME = "NATS";
	private static double GRID_FONT_SIZE = 18.0;
	private static double TITLE_FONT_SIZE = 24.0;
	private File quotes = new File("Test.txt");
	
	
	public PuzzleSeries(String puzzleType, int count) throws SQLException, IOException {
		numberOfPuzzles = count;
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
			grid = puzzle.getGrid();
			
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
		
		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle is created: " + ppt_file_name);
		System.out.println("Loading...");
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		System.out.println("Done.");

		ppt.close();
		scan.close();
		
	}
	
	public void dropQuotePPT() throws SQLException, IOException {
		Scanner scan = new Scanner(quotes);
		File ppt_file_name = new File(PPT_FILE_NAME);

		int puzzle_slide_no = 1;
		HSLFSlideShow ppt = new HSLFSlideShow();
		
		for(int n = 0; n<numberOfPuzzles; n++) {
			
			String quote = scan.nextLine();
			DropQuote puzzle = new DropQuote(quote);
			grid = puzzle.getScrambleGrid();
			
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
						cell1.setFillColor(Color.BLACK);

						setBorders(cell1);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(FONT_NAME);
						rt1.setFontSize(GRID_FONT_SIZE);
						cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
						cell1.setHorizontalCentered(true);
					} else {
						HSLFTableCell cell1 = table2.getCell(j, i);

						cell1.setText(" ");

						setBorders(cell1);
						HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
						rt1.setFontFamily(FONT_NAME);
						rt1.setFontSize(GRID_FONT_SIZE);
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
		
		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle is created: " + ppt_file_name);
		System.out.println("Loading...");
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
		System.out.println("Loading...");
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
			ArrayList<String> wordSlots = puzzle.getBlankWords();
			int yOffSet = 0;
			int xOffSet = 0;
			
			HSLFSlide slide = ppt.createSlide();
			String title_name = PUZZLE_TITLE;
			createTitle(slide, title_name);
			createLogo(ppt, slide);

			HSLFTable table = slide.createTable(puzzle.getRows(), puzzle.getColumns());
			grid = puzzle.getBankGrid();
			for (int i = 0; i < puzzle.getRows(); i++) {
				for (int j = 0; j < puzzle.getColumns(); j++) {
					String char_string = String.valueOf(grid[i][j]);
					HSLFTableCell cell1 = table.getCell(i, j);

					cell1.setText(char_string);

					//setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(FONT_NAME);
					rt1.setFontSize(GRID_FONT_SIZE);
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

				yOffSet += 45;
			}
			
			yOffSet += 45;
			int currentWordLength = wordSlots.get(0).length();
			int nextWordLength;
			for(int i = 0; i<wordSlots.size(); i++) {
				table = slide.createTable(1, wordSlots.get(i).length());
				String word = wordSlots.get(i);
				for(int a = 0; a<word.length(); a++) {
					String char_string = String.valueOf(word.charAt(a));
					HSLFTableCell cell1 = table.getCell(0, a);
					
					cell1.setText(char_string);

					setBorders(cell1);
					HSLFTextRun rt1 = cell1.getTextParagraphs().get(0).getTextRuns().get(0);
					rt1.setFontFamily(FONT_NAME);
					rt1.setFontSize(GRID_FONT_SIZE);
					cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
					cell1.setHorizontalCentered(true);
				}
				for (int b = 0; b < wordSlots.get(i).length(); b++) {
					table.setColumnWidth(b, puzzle.getCellWidth());
				}


				table.setRowHeight(0, puzzle.getCellHeight());
				

				table.moveTo(puzzle.getSTARTING_X() + xOffSet, puzzle.getSTARTING_Y() + yOffSet);
				
				if((i+1)<wordSlots.size()) {
					nextWordLength = wordSlots.get(i+1).length(); 
					if(currentWordLength + nextWordLength > 20) {
						yOffSet += 45;
						xOffSet = 0;
						currentWordLength = nextWordLength;
					} else {
						xOffSet = (currentWordLength * 32);
						currentWordLength += nextWordLength;
					}
					
				} else {
					yOffSet += 45;
				}
				
			}
			

			createSlideNumber(slide, puzzle_slide_no);
			puzzle_slide_no++;
		}
		
		FileOutputStream out = new FileOutputStream(ppt_file_name);
		ppt.write(out);
		out.close();

		System.out.println("Puzzle is created: " + ppt_file_name);
		System.out.println("Loading...");
		Desktop.getDesktop().browse(ppt_file_name.toURI());
		System.out.println("Done.");

		ppt.close();
		scan.close();
		
	}

	public static void createTitle(HSLFSlide slide, String puzzleName) {
		HSLFTextBox title = slide.createTextBox();
		HSLFTextParagraph p1 = title.getTextParagraphs().get(0);
		p1.setTextAlign(TextAlign.CENTER);
		HSLFTextRun run = p1.getTextRuns().get(0);
		run.setFontColor(Color.black);
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
		slide_number.setAnchor(new Rectangle(220, 10, 50, 30));

		createLine(slide, 220, 5, 50, 0); // top line
		createLine(slide, 270, 5, 0, 50); // right line
		createLine(slide, 220, 55, 50, 0); // bottom line
		createLine(slide, 220, 5, 0, 50); // left line
	}

	public static void createLine(HSLFSlide slide, int x, int y, int width, int height) {
		HSLFLine line = new HSLFLine();
		line.setAnchor(new Rectangle(x, y, width, height));
		line.setLineColor(Color.black);
		slide.addShape(line);
	}
	
	public static void createLogo(HSLFSlideShow ppt, HSLFSlide slide) throws IOException {
		byte[] picture = IOUtils.toByteArray(new FileInputStream(new File("logo.png")));
		HSLFPictureData pd = ppt.addPicture(picture, HSLFPictureData.PictureType.PNG);
		HSLFPictureShape pic_shape = slide.createPicture(pd);
		pic_shape.setAnchor(new Rectangle(0, 0, 174, 65));
	}

//	public static void getLabels(HSLFSlide slide, int num_row, int num_column) {
//		String[] top_label = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
//				"S", "T", "U", "V", "W", "X", "Y", "Z" };
//
//		String[] side_label = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
//				"17", "18", "19", "20", "21", "22", "23", "24", "25", "26" };
//
//		HSLFTable top_row = slide.createTable(1, num_column);
//		HSLFTable side_row = slide.createTable(num_row, 1);
//
//		for (int i = 0; i < num_row; i++) {
//			// side column labels
//			HSLFTableCell side_cell = side_row.getCell(i, 0);
//			side_cell.setText(side_label[i]);
//			setBorders(side_cell);
//			HSLFTextRun rts1 = side_cell.getTextParagraphs().get(0).getTextRuns().get(0);
//			rts1.setFontFamily(FONT_NAME);
//			rts1.setFontSize(table_fontSize - 5); // labels' font size are 5 less than table font size
//			rts1.setBold(true);
//			side_cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//			side_cell.setHorizontalCentered(true);
//
//			for (int j = 0; j < num_column; j++) {
//
//				HSLFTableCell top_cell = top_row.getCell(0, j);
//				top_cell.setText(top_label[j]);
//				setBorders(top_cell);
//				HSLFTextRun rt2s1 = top_cell.getTextParagraphs().get(0).getTextRuns().get(0);
//				rt2s1.setFontFamily(FONT_NAME);
//				rt2s1.setFontSize(table_fontSize - 5);
//				rt2s1.setBold(true);
//				top_cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//				top_cell.setHorizontalCentered(true);
//			}
//		}
//
//		for (int i = 0; i < num_column; i++) {
//			top_row.setColumnWidth(i, puzzle.getCellWidth());
//			side_row.setColumnWidth(0, puzzle.getCellWidth());
//		}
//
//		for (int i = 0; i < num_row; i++) {
//			side_row.setRowHeight(i, puzzle.getCellHeight());
//			top_row.setRowHeight(0, puzzle.getCellHeight());
//		}
//
//		top_row.moveTo(puzzle.getSTARTING_X(), puzzle.getSTARTING_Y() - 30); // y - 20 to match table
//		side_row.moveTo(puzzle.getSTARTING_X() - 30, puzzle.getSTARTING_Y()); // x - 30 to match table
//	}
	
	public static void setBorders(HSLFTableCell cell) {
		cell.setBorderColor(BorderEdge.bottom, Color.black);
		cell.setBorderColor(BorderEdge.top, Color.black);
		cell.setBorderColor(BorderEdge.right, Color.black);
		cell.setBorderColor(BorderEdge.left, Color.black);
	}

	
	
}
