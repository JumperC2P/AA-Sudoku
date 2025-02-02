/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

 package grid;

 import java.io.*;
import java.util.ArrayList;
import java.util.List;

import grid.KillerSudokuGrid.Cage;


/**
 * Abstract class representing the general interface for a Sudoku grid.
 * Both standard and Killer Sudoku extend from this abstract class.
 */
public abstract class SudokuGrid
{
	
	public Integer size = 0;
	public Integer[] symbols = null;
	public Integer[][] grid = null;
	public Integer sqrt = 0;
	public Integer cageNumber = null;
	public List<Cage> cages = new ArrayList<>();

    /**
     * Load the specified file and construct an initial grid from the contents
     * of the file.  See assignment specifications and sampleGames to see
     * more details about the format of the input files.
     *
     * @param filename Filename of the file containing the intial configuration
     *                  of the grid we will solve.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void initGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Write out the current values in the grid to file.  This must be implemented
     * in order for your assignment to be evaluated by our testing.
     *
     * @param filename Name of file to write output to.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void outputGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Converts grid to a String representation.  Useful for displaying to
     * output streams.
     *
     * @return String representation of the grid.
     */
    public abstract String toString();


    /**
     * Checks and validates whether the current grid satisfies the constraints
     * of the game in question (either standard or Killer Sudoku).  Override to
     * implement game specific checking.
     *
     * @return True if grid satisfies all constraints of the game in question.
     */
    public abstract boolean validate();

} // end of abstract class SudokuGrid
