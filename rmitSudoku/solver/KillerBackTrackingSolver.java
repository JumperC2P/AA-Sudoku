/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.List;
import java.util.Map;

import grid.KillerSudokuGrid.Cage;
import grid.SudokuGrid;


/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver
{

    public KillerBackTrackingSolver() {
    } // end of KillerBackTrackingSolver()


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
		
		// validate for 3 constraints
		boolean basicValidation = BackTrackUtils.basicValidate(sudokuGrid, currentNumber, currentX, currentY);
		
		if (!basicValidation)
			return basicValidation;
		
		// compare the values in same cage
		List<Cage> cages = sudokuGrid.cages;
		
		Cage targetCage = null;
		
		for (Cage cage : cages) {
			for (Map<String, Integer> position : cage.positions) {
				int cageX = position.get("x");
				int cageY = position.get("y");
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
		for (Map<String, Integer> position : targetCage.positions) {
			int cageX = position.get("x");
			int cageY = position.get("y");
			
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
