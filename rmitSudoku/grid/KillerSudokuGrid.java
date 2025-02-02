/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import solver.BackTrackUtils;


/**
 * Class implementing the grid for Killer Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task E and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid
{

    public KillerSudokuGrid() {
        super();

    } // end of KillerSudokuGrid()


    /* ********************************************************* */

    /**
     * Read the file and prepare the sudoku grid
     */
    @Override
    public void initGrid(String filename)
        throws FileNotFoundException, IOException
    {
    	// read the file
    	BufferedReader inReader = new BufferedReader(new FileReader(filename));
    	
    	String line = null;
    	int lineNumber = 1;
    	while ((line = inReader.readLine()) != null) {
    		
    		switch(lineNumber) {
	    		case 1:
	    			// set the grid size
	    			size = Integer.valueOf(line);
	    			sqrt = (int) Math.sqrt(size);
	    			grid = new Integer[size][size];
	    			symbols = new Integer[size];
	    			break;
	    		case 2:
	    			// prepare for symbols to use
	    			String[] symbols = line.split(" ");
	    			for (int i = 0; i<this.symbols.length; i++)
	    				this.symbols[i] = Integer.valueOf(symbols[i]);
	    			break;
	    		case 3:
	    			// get the number of cages
	    			cageNumber = Integer.valueOf(line);
	    			break;
    			default:
    				// extract cages information and save in a list
    				// 10 2,2 3,1 3,2 3,3
    				String[] items = line.split(" "); // (10), (2,2), (3,1), (3,2), (3,3)
    				
    				List<Cell> positions = new ArrayList<>();
    				for (int i = 1; i<items.length; i++) {
    					String[] coordinates = items[i].split(","); // [2, 2], [3, 1], [3, 2], [3, 3] <= y, x
    					positions.add(new Cell(Integer.valueOf(coordinates[0]), Integer.valueOf(coordinates[1]))); // [{x:2, y:2},{x:3, y:1},{x:3, y:2},{x:3, y:3}]
    				}
    				
    				cages.add(new Cage(Integer.valueOf(items[0]), positions));
    				break;
    		}
    		lineNumber++;
    		
    	}
    	
    	inReader.close();
    } // end of initBoard()


    /**
     * Print the grid and cages to a file
     */
    @Override
    public void outputGrid(String filename)
        throws FileNotFoundException, IOException
    {
    	// call the method "outputGrid" in CommonUtils to print the gird out.
    	CommonUtils.outputGrid(filename, grid, size);
    } // end of outputBoard()
    

    /**
     * print the grid and cages in console
     * 
     * @return a String Object of all the information in the KillerSudokuGrid
     */
    @Override
    public String toString() {
        return CommonUtils.printGridAndCages(grid, size, sqrt, cages);
    } // end of toString()


    /**
     * check the killer sudoku grid is valid or not.
     * @return true: valid, false: invalid
     */
    @Override
    public boolean validate() {
    	
    	for (int currentY = 0; currentY < size; currentY++) {
    		for (int currentX = 0; currentX < size; currentX++) {
    			int currentNumber = grid[currentY][currentX];
    			// validate for 3 constraints
    			boolean basicValidation = BackTrackUtils.basicValidate(this, currentNumber, currentX, currentY);
    			
    			if (!basicValidation)
    				return basicValidation;
    			
    			// compare the values in same cage
    			List<Cage> cages = this.cages;
    			
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
    				
    				if (isFull && this.grid[cageY][cageX] == null) {
    					isFull = false;
    					continue;
    				}
    				
    				if (this.grid[cageY][cageX] != null)
    					targetCageSum += this.grid[cageY][cageX];
    			}
    			
    			// If the cage is full of numbers, the sum of those numbers in the cage should be equals to the indicated value
    			if (isFull && targetCageSum != targetCage.sum) 
    				return false;
    			
    			// If the cage is not full of numbers, the sum of those numbers in the cage should not be equals to or greater than the indicated value
    			if (!isFull && targetCageSum >= targetCage.sum)
    				return false;
    		}
		}
		
		return true;
    } // end of validate()
    
    /**
     * The Object of Cage with storing the sum and cells
     * @author Chih-Hsuan Lee <s3714761>
     *
     */
    public class Cage {
    	public int sum;
    	public List<Cell> positions;
    	
    	public Cage() {}
    	
    	public Cage(int sum, List<Cell> positions) {
    		this.sum = sum;
    		this.positions = positions;
    	}
    	
    	public String toString() {
    		StringBuffer sb = new StringBuffer();
    		sb.append("[");
    		for (Cell position : positions) 
    			sb.append(position);
    		
    		sb.append("] = ");
    		sb.append(sum);
    		
    		return sb.toString();
    	}
    	
    }
    
    /**
     * The cell object of row and column
     * @author Chih-Hsuan Lee <s3714761>
     *
     */
    public class Cell {
    	
    	public int row;
    	public int col;
    	
    	public Cell(int row, int col) {
    		this.row = row;
    		this.col = col;
    	}
    	
    	public String toString() {
    		return "(" + row + ", " + col + ")";
    	}
    	
    }

} // end of class KillerSudokuGrid
