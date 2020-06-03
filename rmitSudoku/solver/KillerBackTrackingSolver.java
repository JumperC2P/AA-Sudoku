/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.List;
import java.util.Map;

import grid.KillerSudokuGrid.Cage;
import grid.KillerSudokuGrid.Cell;
import grid.SudokuGrid;


/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver
{

    public KillerBackTrackingSolver() {
    } // end of KillerBackTrackingSolver()


    /**
     * The solve method to solve killer sudoku probleam with backtracking mechanism.
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
		
		// validate for 3 constraints
		boolean basicValidation = BackTrackUtils.basicValidate(sudokuGrid, currentNumber, currentX, currentY);
		
		if (!basicValidation)
			return basicValidation;
		
		// compare the values in same cage
		List<Cage> cages = sudokuGrid.cages;
		
		Cage targetCage = null;
		
		for (Cage cage : cages) {
			for (Cell position : cage.positions) {
				int cageX = position.col;
				int cageY = position.row;
				if (cageX == currentX && cageY == currentY) {
					targetCage = cage;
					break;
				}
				if (targetCage != null)
					break;
			}
		}
		
		// It's impossible if the current number is greater than the sum of numbers in the same cage
		if (currentNumber >= targetCage.sum)
			return false;
		
		boolean isFull = true;
		int targetCageSum = 0;
		for (Cell position : targetCage.positions) {
			int cageX = position.col;
			int cageY = position.row;
			
			if (cageX == currentX && cageY == currentY) {
				targetCageSum += currentNumber;
				continue;
			}
			
			if (isFull && sudokuGrid.grid[cageY][cageX] == null) {
				isFull = false;
				continue;
			}
			
			if (sudokuGrid.grid[cageY][cageX] != null)
				targetCageSum += sudokuGrid.grid[cageY][cageX];
		}
		
		// If the cage is full of numbers, the sum of those numbers in the cage should be equals to the indicated value
		if (isFull && targetCageSum != targetCage.sum) 
			return false;
		
		// If the cage is not full of numbers, the sum of those numbers in the cage should not be equals to or greater than the indicated value
		if (!isFull && targetCageSum >= targetCage.sum)
			return false;
		
		
		return true;
	}

} // end of class KillerBackTrackingSolver()
