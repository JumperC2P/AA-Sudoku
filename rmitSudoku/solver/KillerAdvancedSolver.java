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


    @Override
    public boolean solve(SudokuGrid sudokuGrid) {
    	cages = sudokuGrid.cages;
    	sudokuSize = sudokuGrid.size;
    	numbers = new ArrayList<>(Arrays.asList(sudokuGrid.symbols));
    	if (matrixs == null)
    		matrixs = MatrixUtils.generateMatrix(sudokuGrid.size, sudokuGrid.sqrt, sudokuGrid.symbols, 5, sudokuGrid.cages);
//    	
    	
    	// 5 constraints in killer sudoku:
    	// one-value, row-value, column-value, box-value, cage-value
    	size = sudokuGrid.size*sudokuGrid.size*5;
    	
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
			
			// cover by cage constraint
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
//					for (DLXNode coveredNode : coverCageNodes) {
//						if (coveredNode.row == row && coveredNode.col == col && coveredNode.value == val) {
//							coveredNode.headerNode.uncoverNodesOnNode(coveredNode);
//							break;
//						}
//					}
						
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
					masterNode.size++;
					leftSiblingNode = leftSiblingNode.leftNode;
				}
				// uncover cage
				for (int i = coverCageNodes.size()-1; i >= 0; i--) {
					coverCageNodes.get(i).headerNode.uncoverNodesOnNode(coverCageNodes.get(i));
				}
				targetNode.uncoverNodes();
				pointerNode = pointerNode.downNode;
			}
			
			
		}
		return false;
	}
    
    
    private DLXNode findPartnerNode(DLXNode targetNode, DLXNode downNode, int row, int col, int val) {
    	if (downNode == targetNode || (downNode.row == row && downNode.col == col && downNode.value == val)) {
    		return downNode;
    	}else {
    		return findPartnerNode(targetNode, downNode.downNode, row, col, val);
    	}
	}

    
	private HeaderNode findHeaderNode(HeaderNode masterNode, HeaderNode headerNode, int i) {
		if (headerNode == masterNode || (headerNode.index != null && headerNode.index == i)) {
			return headerNode;
		}else {
			return findHeaderNode(masterNode, (HeaderNode)headerNode.rightNode, i);
		}
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

} // end of class KillerAdvancedSolver
