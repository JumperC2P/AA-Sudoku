/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.List;

import grid.SudokuGrid;


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver
{
	List<Matrix> matrixs = null;
	List<DLXNode> solutions = new ArrayList<>();
	List<Integer> deletedColumns = new ArrayList<>();
	Integer size = null;
	List<HeaderNode> headerNodes = new ArrayList<>();

    public DancingLinksSolver() {
    } // end of DancingLinksSolver()


    @Override
    public boolean solve(SudokuGrid sudokuGrid) {
    	if (matrixs == null)
    		matrixs = MatrixUtils.generateMatrix(sudokuGrid.size, sudokuGrid.sqrt, sudokuGrid.symbols, 4, null);
//    	
    	removeRowsByGrid(sudokuGrid);
    	
    	size = sudokuGrid.size*sudokuGrid.size*4;
    	
    	HeaderNode masterNode = this.makeDLXBoard(matrixs);
    	
    	if (solve(masterNode)) {
    		for (DLXNode cell : solutions) {
    			sudokuGrid.grid[cell.row][cell.col] = cell.value;
    		}
    		return true;
    	}

        // placeholder
        return false;
    } // end of solve()
    
    private Boolean solve(HeaderNode masterNode) {

		if(masterNode.rightNode == masterNode) {
			return true;
		}
		
		HeaderNode targetNode = this.findColNodeWithMinSize(masterNode);
		
		
		if (targetNode.size == 0) {
			return false;
		}
		
		// cover column node
		targetNode.coverNodes();
		
		// cover row nodes based on the column node
		DLXNode pointerNode = targetNode.downNode;
		while(true) {
			if (pointerNode == targetNode) {
				break;
			}
			// add particial solution
			solutions.add(pointerNode);
			
			// cover column nodes based on the right node of DLXNodes of target column node
			DLXNode rightSiblingNode = pointerNode.rightNode;
			while(true) {
				if (rightSiblingNode == pointerNode) {
					if (rightSiblingNode.headerNode.size == 0)
						rightSiblingNode.headerNode.coverNodes();
					break;
				}
				rightSiblingNode.headerNode.coverNodes();
				rightSiblingNode = rightSiblingNode.rightNode;
			}
			
			if (solve(masterNode)) {
				return true;
			}else {
				solutions.remove(pointerNode);
				DLXNode leftSiblingNode = pointerNode.leftNode;
				while(true) {
					if (leftSiblingNode == pointerNode) {
						break;
					}
					leftSiblingNode.headerNode.uncoverNodes();
					leftSiblingNode = leftSiblingNode.leftNode;
				}
				targetNode.uncoverNodes();
				pointerNode = pointerNode.downNode;
			}
			
		}
		
		
		return false;
	}
    
    private HeaderNode findColNodeWithMinSize(HeaderNode masterNode) {
		
		HeaderNode currentNode = null;
		HeaderNode targetNode = null;
		Integer min = null;
		while(true) {
			
			if (currentNode == null) {
				currentNode = targetNode = (HeaderNode) masterNode.rightNode;
				min = currentNode.size;
			}else {
				currentNode = (HeaderNode) currentNode.rightNode;
				if (currentNode == masterNode) {
					break;
				}
				if (min > currentNode.size) {
					targetNode = currentNode;
					min = currentNode.size;
				}
			}
		}
		
		return targetNode;
	}
    
    private HeaderNode makeDLXBoard(List<Matrix> matrixs) {
		
		HeaderNode masterNode = new HeaderNode(null);
		
		for (int idx = 0; idx < size; idx++) {
			HeaderNode colNode = new HeaderNode(idx);
			headerNodes.add(colNode);
			masterNode.setLeft(colNode);
		}
		
		masterNode.size = headerNodes.size();
		
		for (Matrix m : matrixs) {
			DLXNode prevNode = null;
			for (int idx = 0; idx < m.constraints.length; idx++) {
				if (deletedColumns.contains(idx)) {
					continue;
				}
				if (m.constraints[idx] != null) {
					HeaderNode colNode = headerNodes.get(idx);
					DLXNode newNode = new DLXNode(m.row, m.col, m.value, colNode);
					if (prevNode == null) {
						prevNode = newNode;
					}
					colNode.upNode.setDown(newNode);
					prevNode = prevNode.setRight(newNode);
					colNode.size++;
				}
			}
		}
		
		
		for (Integer delIdx : deletedColumns) {
			headerNodes.get(delIdx).coverNodes();
		}
		
		masterNode.size = headerNodes.size() - deletedColumns.size();
		
		return masterNode;
		
	}
    
    private void removeRowsByGrid(SudokuGrid sudokuGrid) {
    	
    	for (int i = 0; i < sudokuGrid.size; i++) {
    		for (int j = 0; j < sudokuGrid.size; j++) {
    			if (sudokuGrid.grid[i][j] != null) {
    				// delete row by idx
    				for (Matrix m : matrixs) {
    					if (m.row == i && m.col == j && m.value == sudokuGrid.grid[i][j]) {
    						for (int k = 0; k < m.constraints.length; k++) {
    	    					if (m.constraints[k] != null && m.constraints[k] == 1) {
    	    						deletedColumns.add(k);
    	    					}
    	    				}
    						break;
    					}
    				}
    			}
    		}
    	}
	}

} // end of class DancingLinksSolver
