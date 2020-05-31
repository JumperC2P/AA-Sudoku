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
    		generateMatrix(sudokuGrid.size, sudokuGrid.sqrt, sudokuGrid.symbols);
    	
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
		
		if (idx == null) {
			System.out.println("IDX is null");
		}
		
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

    
    private void generateMatrix(Integer size, Integer sqrt, int[] symbols) {
    	matrixs = new ArrayList<>();
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				for (int value : symbols) {
					Integer[] constraints = new Integer[size*size*4];
					
					// One value constraint: 
					// constraint number : row x size + value - 1
					constraints[row * size + col] = 1;
					
					// Row constraint:
					// constraint number : size^2 + row x size + value - 1
					constraints[((int) Math.pow(size,2)) + row * size + value - 1] = 1;
					
					// Column constraint:
					// constraint number : 2 x size^2 + col x size + value - 1
					constraints[2 * ((int) Math.pow(size,2)) + col * size + value - 1] = 1;
					
					// Box constraint:
					// constraint number : 3 x size^2 + (row / sqrt) x size x sqrt + (col / sqrt) x size + value - 1
					constraints[3 * ((int) Math.pow(size,2)) + (row / sqrt) * size * sqrt + (col / sqrt) * size + value - 1] = 1;
					
					matrixs.add(new Matrix(row, col, value, constraints));
					
				}
			}
		}
		
		
		
		// To print the matrix
		//1,1,1 | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c | c c c c c c c c c c c c c c c c 
//		System.out.println("r,c,v | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c");
//		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
//		
//		for (Matrix m : matrixs) {
//			System.out.print(m.row+","+m.col+","+m.value+" | ");
//			for (int i = 0; i < m.constraints.length; i++) {
//				if (i != 0 && i % (size*size) == 0)
//					System.out.print("| ");
//				Integer val = m.constraints[i];
//				System.out.print((val == null ? " ":m.constraints[i])+" ");
//			}
//			System.out.println();
//			if (m.value % size == 0)
//				System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
//		}
		
		
    
    
    }

	public class Matrix{
    	
    	int row;
    	int col;
    	int value;
    	Integer[] constraints;
    	
    	public Matrix(int row, int col, int value, Integer[] constraints) {
    		this.row = row;
    		this.col = col;
    		this.value = value;
    		this.constraints = constraints;
    	}
    	
    	public String toString() {
    		
    		StringBuffer sb = new StringBuffer();
    		
    		sb.append(row).append(", ");
    		sb.append(col).append(", ");
    		sb.append(value).append(" --> ");
    		
    		sb.append(", [");
    		
    		for (Integer i : constraints) {
    			sb.append(i+", ");
    		}
    		sb.append("]");
    		
    		return sb.toString();
    	}
    	
    }

} // end of class AlgorXSolver
