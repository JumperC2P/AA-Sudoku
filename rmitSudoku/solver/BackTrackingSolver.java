/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.Map;

import grid.SudokuGrid;


/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver
{

    public BackTrackingSolver() {
    } // end of BackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid sudokuGrid) {
    	
    	Map<String, Integer> currentPosition = BackTrackUtils.findBlankPosition(sudokuGrid);
    	
    	if (currentPosition == null) {
    		return true;
    	}
    	
    	int currentX = currentPosition.get("x");
    	int currentY = currentPosition.get("y");
    	
    	for (int currentNumber : sudokuGrid.symbols) {
    		
    		if(this.validateNumber(sudokuGrid, currentNumber, currentX, currentY)) {
    			sudokuGrid.grid[currentY][currentX] = currentNumber;
    			
    			if (this.solve(sudokuGrid)) {
    				return true;
    			}
    			sudokuGrid.grid[currentY][currentX] = null;
    		}
    	}
    	
        return false;
    } // end of solve()
    

	private boolean validateNumber(SudokuGrid sudokuGrid, int currentNumber, int currentX, int currentY) {
		return BackTrackUtils.basicValidate(sudokuGrid, currentNumber, currentX, currentY);
	}


} // end of class BackTrackingSolver()
