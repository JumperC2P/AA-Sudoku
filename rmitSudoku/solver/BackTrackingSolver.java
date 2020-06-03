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


    /**
     * The solve method to solve standard sudoku probleam with backtracking mechanism.
     * @param the sudoku grid
     */
    @Override
    public boolean solve(SudokuGrid sudokuGrid) {
    	
    	// find a blank cell to work on.
    	Map<String, Integer> currentPosition = BackTrackUtils.findBlankPosition(sudokuGrid);
    	
    	if (currentPosition == null) {
    		return true;
    	}
    	
    	// get the current row and column values
    	int currentX = currentPosition.get("x");
    	int currentY = currentPosition.get("y");
    	
    	// try all the symbols one by one
    	for (int currentNumber : sudokuGrid.symbols) {
    		
    		// if the current number is valid, put it into the cell
    		if(this.validateNumber(sudokuGrid, currentNumber, currentX, currentY)) {
    			sudokuGrid.grid[currentY][currentX] = currentNumber;
    			
    			// recursively to find solutions
    			if (this.solve(sudokuGrid)) {
    				return true;
    			}
    			
    			// Once there is no symbol which is valid, recover the value of the value to null
    			sudokuGrid.grid[currentY][currentX] = null;
    		}
    	}
    	
        return false;
    } // end of solve()
    

    /**
     * the validation method for checking the number is ok or not
     * 
     * @param sudokuGrid the sudoku grid
     * @param currentNumber the current number to check
     * @param currentX the current column of the cell
     * @param currentY the current row of the cell
     * @return true: valid, false: invalid
     */
	private boolean validateNumber(SudokuGrid sudokuGrid, int currentNumber, int currentX, int currentY) {
		return BackTrackUtils.basicValidate(sudokuGrid, currentNumber, currentX, currentY);
	}


} // end of class BackTrackingSolver()
