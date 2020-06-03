/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import solver.BackTrackUtils;


/**
 * Class implementing the grid for standard Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task A and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class StdSudokuGrid extends SudokuGrid
{

    public StdSudokuGrid() {
        super();
    } // end of StdSudokuGrid()


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
    	while ((line = inReader.readLine()) != null) {
    		
    		if (line.length() == 1) {
    			// set the grid size
    			size = Integer.valueOf(line);
    			sqrt = (int) Math.sqrt(size);
    			grid = new Integer[size][size];
    			symbols = new Integer[size];
    		}else if (line.length() == 5) {
    			// extract value and position
    			String[] items = line.split(" "); // (y,x), (value)
    			String[] position = items[0].split(","); // y, x
    			grid[Integer.valueOf(position[0])][Integer.valueOf(position[1])] = Integer.valueOf(items[1]);
    			
    		}else {
    			// prepare for symbols to use
    			String[] symbols = line.split(" ");
    			for (int i = 0; i<this.symbols.length; i++)
    				this.symbols[i] = Integer.valueOf(symbols[i]);
    		}
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
    	CommonUtils.outputGrid(filename, grid, size);
    } // end of outputBoard()

    
    /**
     * print the grid in console
     * 
     * @return a String Object of all the information in the StdSudokuGrid
     */
	@Override
    public String toString() {
        return CommonUtils.printGridAndCages(grid, size, sqrt, null);
    } // end of toString()

	
	/**
     * check the standard sudoku grid is valid or not.
     * @return true: valid, false: invalid
     */
    @Override
    public boolean validate() {
    	
    	for (int currentY = 0; currentY < size; currentY++) {
    		for (int currentX = 0; currentX < size; currentX++) {
    			int currentNumber = grid[currentY][currentX];
    			if (!BackTrackUtils.basicValidate(this, currentNumber, currentX, currentY))
    				return false;
    		}
    	}
		
		return true;
    } // end of validate()

} // end of class StdSudokuGrid
