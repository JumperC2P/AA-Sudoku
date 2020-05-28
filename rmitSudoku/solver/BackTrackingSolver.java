/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.HashMap;
import java.util.Map;

import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver
{
    // TODO: Add attributes as needed.

    public BackTrackingSolver() {
        // TODO: any initialisation you want to implement.
    } // end of BackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid sudokuGrid) {
    	
    	StdSudokuGrid stdSudokuGrid = (StdSudokuGrid) sudokuGrid;

    	Map<String, Integer> currentPosition = this.findBlankPosition(stdSudokuGrid);
    	
    	if (currentPosition == null) {
    		return true;
    	}
    	
    	int currentX = currentPosition.get("x");
    	int currentY = currentPosition.get("y");
    	
    	for (int currentNumber : stdSudokuGrid.symbols) {
    		
    		if(this.validateNumber(stdSudokuGrid, currentNumber, currentX, currentY)) {
    			stdSudokuGrid.grid[currentY][currentX] = currentNumber;
    			
    			if (this.solve(stdSudokuGrid)) {
    				return true;
    			}
    			stdSudokuGrid.grid[currentY][currentX] = null;
    		}
    	}
    	
        return false;
    } // end of solve()
    

	private Map<String, Integer> findBlankPosition(StdSudokuGrid stdSudokuGrid){
		
		Map<String, Integer> map = new HashMap<>();
		for (int y = 0; y < stdSudokuGrid.size; y++) {
			for (int x = 0; x < stdSudokuGrid.size; x++) {
				if (stdSudokuGrid.grid[y][x] == null) {
					map.put("y", y);
					map.put("x", x);
					return map;
				}
			}
		}
		return null;
	}
	
	private boolean validateNumber(StdSudokuGrid sudokuGrid, int currentNumber, int currentX, int currentY) {
		
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
				if (sudokuGrid.grid[y][x] != null && sudokuGrid.grid[y][x] == currentNumber)
					return false;
			}
		}
		
		return true;
	}


} // end of class BackTrackingSolver()
