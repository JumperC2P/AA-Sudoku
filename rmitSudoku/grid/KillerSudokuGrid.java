/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

	public Integer size = 0;
	public int[] symbols = null;
	public Integer[][] grid = null;
	public Integer sqrt = 0;
	public Integer cageNumber = null;
	public List<Cage> cages = new ArrayList<>();
	
    public KillerSudokuGrid() {
        super();

        // TODO: any necessary initialisation at the constructor
    } // end of KillerSudokuGrid()


    /* ********************************************************* */


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
	    			symbols = new int[size];
	    			break;
	    		case 2:
	    			// prepare for symbols to use
	    			String[] symbols = line.split(" ");
	    			for (int i = 0; i<this.symbols.length; i++)
	    				this.symbols[i] = Integer.valueOf(symbols[i]);
	    			break;
	    		case 3:
	    			cageNumber = Integer.valueOf(line);
	    			break;
    			default:
    				// 10 2,2 3,1 3,2 3,3
    				String[] items = line.split(" "); // (10), (2,2), (3,1), (3,2), (3,3)
    				
    				List<Map<String, Integer>> positions = new ArrayList<>();
    				for (int i = 1; i<items.length; i++) {
    					String[] coordinates = items[i].split(","); // [2, 2], [3, 1], [3, 2], [3, 3] <= y, x
    					
    					Map<String, Integer> position = new HashMap<String, Integer>();
    					position.put("x", Integer.valueOf(coordinates[1])); // (x, 2)
    					position.put("y", Integer.valueOf(coordinates[0])); // (y, 2)
    					
    					positions.add(position); // [{x:2, y:2},{x:3, y:1},{x:3, y:2},{x:3, y:3}]
    				}
    				
    				cages.add(new Cage(Integer.valueOf(items[0]), positions));
    				break;
    		}
    		lineNumber++;
    		
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
        return CommonUtils.printGridAndCages(grid, size, sqrt, cages);
    } // end of toString()


    @Override
    public boolean validate() {
        // TODO

        // placeholder
        return false;
    } // end of validate()
    
    
    class Cage {
    	public int sum;
    	public List<Map<String, Integer>> positions;
    	
    	public Cage() {}
    	
    	public Cage(int sum, List<Map<String, Integer>> positions) {
    		this.sum = sum;
    		this.positions = positions;
    	}
    	
    	public String toString() {
    		StringBuffer sb = new StringBuffer();
    		sb.append("[");
    		for (Map<String, Integer> position : positions) 
    			sb.append("(").append(position.get("y")).append(", ").append(position.get("x")).append("),");
    		
    		sb = new StringBuffer(sb.substring(0, sb.length()-1)).append("] = ");
    		sb.append(sum);
    		
    		return sb.toString();
    	}
    	
    }

} // end of class KillerSudokuGrid
