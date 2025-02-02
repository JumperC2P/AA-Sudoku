/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grid.SudokuGrid;
import grid.KillerSudokuGrid.Cage;


/**
 * Your advanced solver for Killer Sudoku.
 */
public class KillerAdvancedSolver extends KillerSudokuSolver
{

	List<List<Integer>> possibleValueSets = new ArrayList<>();
	List<Matrix> matrixs = null;
	List<DLXNode> solutions = new ArrayList<>();
	List<Integer> deletedColumns = new ArrayList<>();
	Integer size = null;
	List<HeaderNode> headerNodes = new ArrayList<>();
	List<Cage> cages = null;
	List<Integer> numbers = null;
	int sudokuSize = 0;
	
	
    public KillerAdvancedSolver() {
    } // end of KillerAdvancedSolver()


    /**
     * The public solve method for other programs to call
     * @param the sudoku grid
     */
    @Override
    public boolean solve(SudokuGrid sudokuGrid) {
    	
    	int numConstraints = 5;
    	cages = sudokuGrid.cages;
    	sudokuSize = sudokuGrid.size;
    	numbers = new ArrayList<>(Arrays.asList(sudokuGrid.symbols));
    	
    	// if the matrix is empty, it need to be generated by the size of sudoku grid and symbols and the number of constraints.
    	if (matrixs == null)
    		matrixs = MatrixUtils.generateMatrix(sudokuGrid.size, sudokuGrid.sqrt, sudokuGrid.symbols, numConstraints, sudokuGrid.cages);
    	
    	// 5 constraints in killer sudoku:
    	// one-value, row-value, column-value, box-value, cage-value
    	size = sudokuGrid.size*sudokuGrid.size*numConstraints;
    	
    	// use the matrix to generate a dancing link board
    	HeaderNode masterNode = this.makeDLXBoard(matrixs);
    	
    	// call the private solve method to find the solution of the standard sudoku
    	if (solve(masterNode)) {
    		// After finding the solutions, 
    		// add the value to each cell of the standard sudoku grind.
    		for (DLXNode cell : solutions) {
    			sudokuGrid.grid[cell.row][cell.col] = cell.value;
    		}
    		return true;
    	}

        // placeholder
        return false;
    } // end of solve()
    
    
    /**
     * The exact method to solve killer sudoku problem
     * @param masterNode the master node to start the process
     * @return the problem is solved or not.
     */
    private Boolean solve(HeaderNode masterNode) {

    	// If it is back to the master node, terminated successfully.
		if(masterNode.rightNode == masterNode) {
			return true;
		}
		
		// Find the minimize sum of column node in the current matrix
		HeaderNode targetNode = this.findColNodeWithMinSize(masterNode);
		
		// If the min value is 0, then the size of potential solution will be 0,
		// which means the process is terminated unsuccessfully.
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
			
			// Find all the possibile combinations of the partial solution
			Map<String, List<String>> combinations = MatrixUtils.combinations;
			Set<String> partners = new HashSet<>();
			for (String key : combinations.keySet()) {
				List<String> cage = combinations.get(key);
				
				for (String info : cage) {
					String[] groups = info.split(" ");
					for (String group : groups) {
						String[] coordinates = group.split(",");
						if (pointerNode.row == Integer.valueOf(coordinates[0]) && pointerNode.col == Integer.valueOf(coordinates[1])
								&& pointerNode.value == Integer.valueOf(coordinates[2])) {
							partners.addAll(Arrays.asList(groups));
							partners.remove(pointerNode.row+","+pointerNode.col+","+pointerNode.value);
							break;
						}
					}
				}
				if (partners.size()!=0)
					break;
			}
			
			// Get the partner of partial solutions one by one,
			// and cover all the other nodes which related to the same header node, except for the partner node itself
			List<DLXNode> coverCageNodes = new ArrayList<>();
			if (!(pointerNode.row == 1 && pointerNode.col == 0 && pointerNode.value == 4)) {
				for(String partner : partners) {
					String[] groups = partner.split(" ");
					for (String group : groups) {
						String[] info = group.split(",");
						int row = Integer.valueOf(info[0]);
						int col = Integer.valueOf(info[1]);
						int val = Integer.valueOf(info[2]);
						
						if (pointerNode.row == row && pointerNode.col == col && pointerNode.value == val)
							continue;
						
						// find header node from master node
						HeaderNode targerHeaderNode = findHeaderNode(masterNode, (HeaderNode) masterNode.rightNode, (4 * ((int) Math.pow(sudokuSize,2)) + row * sudokuSize + col));
						if (targerHeaderNode.index == null) {
							continue;
						}
						DLXNode partnerNode = findPartnerNode(targerHeaderNode, targerHeaderNode.downNode, row, col, val);
						
						if (targerHeaderNode != partnerNode && partnerNode != null) {
							coverCageNodes = partnerNode.headerNode.coverNodesWithSkipped(coverCageNodes, partnerNode);
						}
					}
				}
			}
			
			// cover column nodes based on the right node of DLXNodes of target column node
			DLXNode rightSiblingNode = pointerNode.rightNode;
			while(true) {
				if (rightSiblingNode == pointerNode) {
					if (rightSiblingNode.headerNode.size == 0)
						rightSiblingNode.headerNode.coverNodes();
					break;
				}
				rightSiblingNode.headerNode.coverNodes();
				masterNode.size--;
				rightSiblingNode = rightSiblingNode.rightNode;
			}
			
			// Use the modified header nodes to find partial solutions recursively.
			if (solve(masterNode)) {
				return true;
			
			// if it returns false, it means that the partial solution is wrong,
			// it need to be uncovered.
			}else {
				solutions.remove(pointerNode);
				DLXNode leftSiblingNode = pointerNode.leftNode;
				while(true) {
					if (leftSiblingNode == pointerNode) {
						break;
					}
					leftSiblingNode.headerNode.uncoverNodes();
					masterNode.size++;
					leftSiblingNode = leftSiblingNode.leftNode;
				}
				// uncover cage nodes
				for (int i = coverCageNodes.size()-1; i >= 0; i--) {
					coverCageNodes.get(i).headerNode.uncoverNodesOnNode(coverCageNodes.get(i));
				}
				targetNode.uncoverNodes();
				pointerNode = pointerNode.downNode;
			}
		}
		return false;
	}
    
    
    /**
     * Find partner nodes which belongs to the same cage.
     * 
     * @param targetNode the header node to those cell nodes (same column)
     * @param downNode indicated node
     * @param row current row
     * @param col current column
     * @param val current value
     * @return the partner node
     */
    private DLXNode findPartnerNode(DLXNode targetNode, DLXNode downNode, int row, int col, int val) {
    	if (downNode == targetNode || (downNode.row == row && downNode.col == col && downNode.value == val)) {
    		return downNode;
    	}else {
    		return findPartnerNode(targetNode, downNode.downNode, row, col, val);
    	}
	}

    
    /**
     * Find header node from master node
     * if the header node cannot be found, which means that the header node has been covered.
     * 
     * @param masterNode master node
     * @param headerNode the target node
     * @param i the index of header node to be found
     * @return the header node found
     */
	private HeaderNode findHeaderNode(HeaderNode masterNode, HeaderNode headerNode, int i) {
		if (headerNode == masterNode || (headerNode.index != null && headerNode.index == i)) {
			return headerNode;
		}else {
			return findHeaderNode(masterNode, (HeaderNode)headerNode.rightNode, i);
		}
	}


	/**
     * Find the minimize sum of column node in the current matrix
     * 
     * @param masterNode master Node
     * @return the header node with mini-sum
     */
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
    
	
	/**
     * To generate the dancing link board based on the matrix
     * @param matrixes the matrix
     * @return the master node
     */
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

} // end of class KillerAdvancedSolver
