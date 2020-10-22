import java.io.*;
import java.util.*;

public class NQueens {
	
	// ArrayList to store all positions of queens
	private static ArrayList<int[]> queenPositions = new ArrayList<int[]>();
	
	// Integer for first queen's column to handle case where first queen is not in first column
 	private static int firstQueenCol = 0;
 	
 	// Boolean to know when a solution has been found
	private static boolean done = false;
	
	public static void main (String[] args) throws IOException{
		
		// check for < 2 command arguments
		if (args.length < 2) {
			System.out.println("Usage: java -jar NQueens.jar <input file> <output file>");
			System.exit(1);
		}
		
		// open files
		Scanner in = new Scanner(new File(args[0]));
		PrintWriter out = new PrintWriter(new FileWriter(args[1]));
		
		// ArrayList to hold lines of input file, String[] to separate board size from queen location
		ArrayList<String[]> inputs = new ArrayList<>();
		
		// copy inputs into ArrayList
		while(in.hasNextLine()) {
			String line = in.nextLine();
			inputs.add(line.split(" "));
		}
		
		// get an array with each boardSize as an element 
		int[] boardSizes = new int[inputs.size()];
		for (int i = 0; i < boardSizes.length; i++) {
			// checks for if there are exactly 3 values on line
			if (inputs.get(i).length != 3) {
				boardSizes[i] = -1;
				continue;
			}
			
 			// checks if line only contains ints
			try {
				boardSizes[i] = Integer.parseInt(inputs.get(i)[0]);
			} catch (NumberFormatException e) {
				boardSizes[i] = -1;
			}
		}
		
		// get an array with each queen column as an element
		int[] queenColumn = new int[inputs.size()];
		for (int i = 0; i < queenColumn.length; i++) {
			// checks for if there are exactly 3 values on line
			if (inputs.get(i).length != 3) {
				queenColumn[i] = -1;
				continue;
			}
						
			// checks if line only contains ints
			try {
				queenColumn[i] = Integer.parseInt(inputs.get(i)[1]);
			} catch (NumberFormatException e) {
				queenColumn[i] = -1;
			}
		}
		
		// get an array with each queen row as an element
		int[] queenRow = new int[inputs.size()];
		for (int i = 0; i < queenRow.length; i++) {
			// checks for if there are exactly 3 values on line
			if (inputs.get(i).length != 3) {
				queenRow[i] = -1;
				continue;
			}
						
			// checks if line only contains ints
			try {
				queenRow[i] = Integer.parseInt(inputs.get(i)[2]);
			} catch (NumberFormatException e) {
				queenRow[i] = -1;
			}
		}
		
		// ArrayList to hold solution sets
		ArrayList<String> solution = new ArrayList<String>();
		
		// Iteratively run NQueens method for each line of input file and add to solution ArrayList
		for (int i = 0; i < inputs.size(); i++) {
			
			// handles cases with invalid queen positions or dimension
			if ((queenRow[i] - 1) < 0 || (queenRow[i] - 1) >= boardSizes[i] || 
				(queenColumn[i] - 1) < 0 || (queenColumn[i] - 1) >= boardSizes[i] || 
				boardSizes[i] < 4) {
				solution.add("No solution");
				continue;
			}
			
			// get a nxn board with n equal to each element of boardSizes array
			boolean[][] board = new boolean[boardSizes[i]][boardSizes[i]];
			
			// initializes 2D board to true, sets done to false, and clears queenPositions
			reset(board);
			
			// sets firstQueenCol to the first queen Column - 1 since placeQueen is 0 indexed 
			firstQueenCol = queenColumn[i] - 1;
			
			// places the first queen on the board
			placeQueen(board, (queenRow[i] - 1), (queenColumn[i] - 1), boardSizes[i]);
			
			// calls the NQueens method
			NQueens(0, board, boardSizes[i]);
			
			// adds the string returned by toAString() which is the solution
			solution.add(toAString());
		}
		
		// print solutions to out file
		for (int i = 0; i < solution.size(); i++) {
			out.println(solution.get(i));
			
		}
		
		// close files
		in.close();
		out.close();	
	}
	
	// recursively places and removes queens to find a solution to nxn chessboard
	public static void NQueens(int queensPlaced, boolean[][] board, int n) {
		// base case: stop when queensPlaced is equal to the dimension
		printBoard(n);
		if (queensPlaced == n) {
			done = true;
			return;
		}
		
		// Iteratively check spots in column spaces not being attacked
		for (int i = n - 1; i >= 0; i--) {
			
			// if a space is not attacked, place Queen and move on to next column
			if (board[i][queensPlaced] == true) {
				placeQueen(board, i, queensPlaced, n);
				NQueens(queensPlaced + 1, board, n);
			}
			
			// returns when done is true so that NQueens is not called again when solution is found
			if (done) {
				return;
			}
			
			// checks if a Queen is in a certain column to handle case where column where the first queen was placed
			if (isQueen(board, queensPlaced)) {
				NQueens(queensPlaced + 1, board, n);
			} 
		}
		
		// removes Queen in previous column as long as it is not the first Queen placed
		if (queensPlaced - 1 != firstQueenCol) {
			removeQueen(board, queensPlaced - 1, n);
			return;
		} else {
			return;
		}
		
	}
	
	// places a Queen on an nxn chessboard, marks spaces queen is attacking to false
	public static void placeQueen(boolean[][] board, int row, int col, int n) {
		
		// set variables to row and col so that they can be manipulated, reset to row and col after each manipulation
		int k = row;
		int l = col;
		
		// sets all spots on board in a certain row to false
		for (int i = 0; i < n; i++) {
			board[i][l] = false;
		}
		
		// sets all spots on board in a certain column to false
		for (int j = 0; j < n; j++) {
			board[k][j] = false;
		}
		
		// sets all spots on board diagonal up + right to false
		k = row;
		l = col;
		board[k][l] = false;
		while (k != 0 && l + 1 < n) {
			board[k - 1][l + 1] = false;
			k--;
			l++;
		}
		
		// sets all spots on board diagonal down + right to false
		k = row;
		l = col;
		board[k][l] = false;
		while (k + 1 < n && l + 1 < n) {
			board[k + 1][l + 1] = false;
			k++;
			l++;
		}
		
		// sets all spots on board diagonal down + left to false
		k = row;
		l = col;
		board[k][l] = false;
		while (k + 1 < n && l != 0) {
			board[k + 1][l - 1] = false;
			k++;
			l--;
		}
		
		// sets all spots on board diagonal up + right to false
		k = row;
		l = col;
		board[k][l] = false;
		while (k != 0 && l != 0) {
			board[k - 1][l - 1] = false;
			k--;
			l--;
		}
		
		// add new queen to queenPositions ArrayList
		queenPositions.add(new int[] {row, col});
	}

	// removes a Queen in a certain column by re-adding all queens to queenPositions except the one in the given column
	public static void removeQueen(boolean[][] board, int col, int n) {
		
		// create a temporary ArrayList clone 
		ArrayList<int[]> clone = new ArrayList<int[]>();
		clone.clear();
		
		// copy contents of queenPositions into clone
		for (int i = 0; i < queenPositions.size(); i++) {
			clone.add(queenPositions.get(i));
		}
		queenPositions.clear();
		
		// reset the board to all true
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = true;
			}
		}
		
		// iteratively place queens from temporary Arraylist clone onto board, skip when on the column of the queen you want to remove
		for (int i = 0; i < clone.size(); i++) {
			if (clone.get(i)[1] == col) {
				continue;
			}
			// calls placeQueen with each queen except the one to be removed
			placeQueen(board, clone.get(i)[0], clone.get(i)[1], n);
		}
	}

	// given a column, returns true if a queen is in that column, helps handle case of first queen in any column
	public static boolean isQueen(boolean[][] board, int col) {
		for (int i = 0; i < queenPositions.size(); i++) {
			if (queenPositions.get(i)[1] == col) {
				return true;
			}
		}
		return false;
	}
	
	// returns a string in proper format so that it can be easily added to solution ArrayList
	public static String toAString() {
		String s = "";
		
		// After NQueens method, queenPositions only has location of first queen when there is no solution
		if (queenPositions.size() == 1) {
			return "No solution";
		}
		
		// builds up string s with col + 1 and row + 1 to return locations that are (1 to n) indexed in order of increasing column
		for (int i = 0; i < queenPositions.size(); i++) {
			for (int j = 0; j < queenPositions.size(); j++) {
				if (queenPositions.get(j)[1] == i) {
					s = s + (queenPositions.get(j)[1] + 1) + " " + (queenPositions.get(j)[0] + 1) + " ";
					break;
				}
			}
		}
		return s;
		
	}

	// resets all global variables so that they don't take values from previous runs of NQueens
	public static void reset(boolean [][] board) {
		done = false;
		queenPositions.clear();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = true;
			}
		}
	}

	public static void printBoard(int n) {
		String [][] queens = new String[n][n];
		for (int i = 0; i < queenPositions.size(); i++) {
			int x = queenPositions.get(i)[0];
			int y = queenPositions.get(i)[1];
			queens[x][y] = "X";
		}
		for (int i = 0; i < queens.length; i++) {
			for (int j = 0; j < queens.length; j++) {
				if (queens[i][j] != "X") {
					queens[i][j] = "_";
				}
			}
		}

		for (int i = 0; i < queens.length; i++) {
			for (int j = 0; j < queens.length; j++) {
				System.out.print(queens[i][j] + " ");
			}
			System.out.println();
			
		}
 
	}
	
}
