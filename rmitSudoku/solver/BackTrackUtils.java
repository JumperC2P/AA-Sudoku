package solver;

import java.util.HashMap;
import java.util.Map;

import grid.SudokuGrid;

/**
 * @author Chih-Hsuan Lee <s3714761>
 *
 */
public class BackTrackUtils {
	
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
