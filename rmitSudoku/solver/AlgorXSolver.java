/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grid.SudokuGrid;


/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver
{

	List<Matrix> matrixs = null;
	List<Matrix> solutions = new ArrayList<>();
	List<Integer> deletedColumns = new ArrayList<>();
	Integer size = null;
	
    public AlgorXSolver() {
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid sudokuGrid) {
    	
    	if (matrixs == null)
    		matrixs = MatrixUtils.generateMatrix(sudokuGrid.size, sudokuGrid.sqrt, sudokuGrid.symbols, 4, null);
    	
    	removeRowsByGrid(sudokuGrid);
    	
    	size = sudokuGrid.size*sudokuGrid.size*4;
    	
    	if (solve(matrixs, deletedColumns)) {
    		for (Matrix matrix : solutions) {
    			sudokuGrid.grid[matrix.row][matrix.col] = matrix.value;
    		}
    		return true;
    	}

        // placeholder
        return false;
    } // end of solve()
    
	@SuppressWarnings("unchecked")
	private Boolean solve(List<Matrix> matrixs, List<Integer> deletedColumns) {
		
		// If Matrix is empty, the problem is solved; terminate successfully.
		if (matrixs.size() == 0) {
			return true;
		}
		
		Map<String, Object> minMap = findMinSumByColumn(matrixs, deletedColumns);
		List<Matrix> potentialSols = (List<Matrix>) minMap.get("potentialSols");

		if (minMap.get("idx") == null) {
			return false;
		}
		
		// If the min value is 0, then the size of potential solution will be 0,
		// which means the process is terminated unsuccessfully.
		if (potentialSols.size() == 0) {
			return false;
		}
		
		List<Matrix> tempMatrixs = new ArrayList<>();
		tempMatrixs.addAll(matrixs);
		List<Integer> tempDeletedColumns = new ArrayList<>();
		tempDeletedColumns.addAll(deletedColumns);
		
		for (Matrix matrix : potentialSols) {
			// add the particial solutions
			solutions.add(matrix);
			
			// find which columns should be deleted
			for (int i = 0; i < matrix.constraints.length; i++) {
				if (matrix.constraints[i] != null && matrix.constraints[i] == 1) {
					deletedColumns.add(i);
				}
			}
			
			for (Integer i : deletedColumns) {
				// delete row by idx
				matrixs.removeIf(x->x.constraints[i] != null);
			}
			
			if (solve(matrixs, deletedColumns)) {
				return true;
			}else {
				solutions.remove(matrix);
				matrixs.removeAll(matrixs);
				matrixs.addAll(tempMatrixs);
				deletedColumns.removeAll(deletedColumns);
				deletedColumns.addAll(tempDeletedColumns);
			}
		}
		
		return false;
	}
    
    private Map<String, Object> findMinSumByColumn(List<Matrix> matrixs, List<Integer> deletedColumns) {
		
		Integer[] sum = new Integer[size]; 
		Integer min = null;
		Integer idx = null;
	
		for (Matrix m : matrixs) {
			for (int i = 0; i < size; i++) {
				if (m.constraints[i] != null) {
					if (sum[i] == null) 
						sum[i] = 0;
					sum[i] = sum[i] + m.constraints[i];
				}
			}
		}
		
		
		for (int i = 0; i < size; i++) {
			if (deletedColumns.contains(i)) {
				continue;
			}
			if (sum[i] == null) {
				idx = i;
				break;
			}
			if (min == null || min > sum[i]) {
				min = sum[i];
				idx = i;
			}
		}
		
		List<Matrix> potentialSols = new ArrayList<>();
		
		if (min != null) {
			for (Matrix m : matrixs) {
				if (m.constraints[idx] != null) {
					potentialSols.add(m);
				}
			}
		}
		
		Map<String, Object> minMap = new HashMap<>();
		minMap.put("potentialSols", potentialSols);
		minMap.put("idx", idx);
		
		assert(idx == null);
		
		return minMap;
	}
    

    private void removeRowsByGrid(SudokuGrid sudokuGrid) {
    	
    	for (int i = 0; i < sudokuGrid.size; i++) {
    		for (int j = 0; j < sudokuGrid.size; j++) {
    			if (sudokuGrid.grid[i][j] != null) {
    				// delete row by idx
    				for (Matrix m : matrixs) {
    					if (m.row == i && m.col == j && m.value == sudokuGrid.grid[i][j]) {
    						matrixs.remove(m);
    						
    						for (int k = 0; k < m.constraints.length; k++) {
    	    					if (m.constraints[k] != null && m.constraints[k] == 1) {
    	    						deletedColumns.add(k);
    	    					}
    	    				}

    	    				for (Integer n : deletedColumns) {
    	    					// delete row by idx
    	    					matrixs.removeIf(x->x.constraints[n] != null);
    	    				}
    						
    						break;
    					}
    				}
    			}
    		}
    	}
	}

} // end of class AlgorXSolver
