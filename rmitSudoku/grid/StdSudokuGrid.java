/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


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

        // TODO: any necessary initialisation at the constructor
    } // end of StdSudokuGrid()


    /* ********************************************************* */


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
    			symbols = new int[size];
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


    @Override
    public void outputGrid(String filename)
        throws FileNotFoundException, IOException
    {
    	CommonUtils.outputGrid(filename, grid, size);
    } // end of outputBoard()


	@Override
    public String toString() {
        return CommonUtils.printGridAndCages(grid, size, sqrt, null);
    } // end of toString()

	
    @Override
    public boolean validate() {
    	
    	for (int currentY = 0; currentY < size; currentY++) {
    		for (int currentX = 0; currentX < size; currentX++) {
    			int currentNumber = grid[currentY][currentX];
    			for (int x = 0; x < size; x++) {
    				if (currentX != x && grid[currentY][x] == currentNumber)
    					return false;
    			}
    			
    			// compare each number in one column
    			for (int y = 0; y < size; y++) {
    				if (currentY != y && grid[y][currentX] == currentNumber)
    					return false;
    			}
    			
    			// compare each number in one sub-grid
    			int modX = currentX / sqrt;
    			int modY = currentY / sqrt;
    			
    			for (int y = (modY*sqrt); y<((modY+1)*sqrt); y++) {
    				for (int x = (modX*sqrt); x<((modX+1)*sqrt); x++) {
    					if (currentX != x && currentY != y && grid[y][x] == currentNumber)
    						return false;
    				}
    			}
    		}
    	}
		
		return true;
    } // end of validate()

} // end of class StdSudokuGrid
