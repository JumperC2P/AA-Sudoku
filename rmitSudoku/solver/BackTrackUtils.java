package solver;

import java.util.HashMap;
import java.util.Map;

import grid.SudokuGrid;

/**
 * The BackTrackUtils contains two methods which are used in solving either standard or killer sudoku problems with backtracking mechansim.
 * 
 * @author Chih-Hsuan Lee <s3714761>
 *
 */
public class BackTrackUtils {
	
	/**
	 * Find the blank cell and return with a map object
	 * @param sudokuGrid the sudoku grid
	 * @return two sets of data: 
	 * 			x : column
	 * 			y : row
	 */
	public static Map<String, Integer> findBlankPosition(SudokuGrid sudokuGrid){
		
		Map<String, Integer> map = new HashMap<>();
		for (int y = 0; y < sudokuGrid.size; y++) {
			for (int x = 0; x < sudokuGrid.size; x++) {
				if (sudokuGrid.grid[y][x] == null) {
					map.put("y", y);
					map.put("x", x);
					return map;
				}
			}
		}
		return null;
	}
	
	/**
	 * To do the basic validation of sudoku grid.
	 * In the method, it only checks basic 3 constraints, 
	 * which are box-value constraints, row-value constraints and column-value constraints
	 * 
	 * @param sudokuGrid the sudoku grid
	 * @param currentNumber the current number to check
	 * @param currentX the current column of the cell
	 * @param currentY the current row of the cell
	 * @return true: valid, false: invalid
	 */
	public static boolean basicValidate(SudokuGrid sudokuGrid, int currentNumber, int currentX, int currentY) {
		

		// compare each number in one row
		for (int x = 0; x < sudokuGrid.size; x++) {
			if (currentX != x && sudokuGrid.grid[currentY][x] != null && currentNumber == sudokuGrid.grid[currentY][x])
				return false;
		}
		
		// compare each number in one column
		for (int y = 0; y < sudokuGrid.size; y++) {
			if (currentY != y && sudokuGrid.grid[y][currentX] != null && currentNumber == sudokuGrid.grid[y][currentX])
				return false;
		}
		
		// compare each number in one sub-grid
		int modX = currentX / sudokuGrid.sqrt;
		int modY = currentY / sudokuGrid.sqrt;
		
		for (int y = (modY*sudokuGrid.sqrt); y<((modY+1)*sudokuGrid.sqrt); y++) {
			for (int x = (modX*sudokuGrid.sqrt); x<((modX+1)*sudokuGrid.sqrt); x++) {
				if (currentX != x && currentY != y && sudokuGrid.grid[y][x] != null && sudokuGrid.grid[y][x] == currentNumber)
					return false;
			}
		}
		
		return true;
		
	}
	
	

}
