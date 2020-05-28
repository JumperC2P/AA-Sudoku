package grid;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grid.KillerSudokuGrid.Cage;

/**
 * @author Chih-Hsuan Lee <s3714761>
 *
 */
public class CommonUtils {
	

	public static Map<String, Integer> findBlankPosition(SudokuGrid sudokuGrid){
		
		Map<String, Integer> map = new HashMap<>();
		for (int y = 0; y < sudokuGrid.size; y++) {
			for (int x = 0; x < sudokuGrid.size; x++) {
				if (sudokuGrid.grid[y][x] == null) {
					map.put("y", y);
					map.put("x", x);
					return map;
				}
			}
		}
		return null;
	}
	
	public static boolean basicValidate(SudokuGrid sudokuGrid, int currentNumber, int currentX, int currentY) {
		

		// compare each number in one row
		for (int x = 0; x < sudokuGrid.size; x++) {
			if (currentX != x && sudokuGrid.grid[currentY][x] != null && currentNumber == sudokuGrid.grid[currentY][x])
				return false;
		}
		
		// compare each number in one column
		for (int y = 0; y < sudokuGrid.size; y++) {
			if (currentY != y && sudokuGrid.grid[y][currentX] != null && currentNumber == sudokuGrid.grid[y][currentX])
				return false;
		}
		
		// compare each number in one sub-grid
		int modX = currentX / sudokuGrid.sqrt;
		int modY = currentY / sudokuGrid.sqrt;
		
		for (int y = (modY*sudokuGrid.sqrt); y<((modY+1)*sudokuGrid.sqrt); y++) {
			for (int x = (modX*sudokuGrid.sqrt); x<((modX+1)*sudokuGrid.sqrt); x++) {
				if (sudokuGrid.grid[y][x] != null && sudokuGrid.grid[y][x] == currentNumber)
					return false;
			}
		}
		
		return true;
		
	}
	
	
	public static void outputGrid(String fileName, Integer[][] grid, int size) throws FileNotFoundException, IOException {
		
		PrintWriter outWriter = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/rmitSudoku/output/"+fileName), true);
	    
		for (int y = 0; y<size; y++) {
			StringBuffer sb = new StringBuffer();
			for (int x = 0; x<size; x++) {
				sb.append(grid[y][x]).append(",");
			}
			outWriter.println(sb.substring(0, sb.length()-1));
		}
		
		outWriter.close();
	}
	
	private static StringBuffer getHrLine(int size, int sqrt) {
    	
    	StringBuffer hr = new StringBuffer();
    	
    	for (int i = 0; i<((size+sqrt-2)*2+1); i++) {
    		hr.append("-");
    	}
    	
    	hr.append("\n");
    	
		return hr;
	}
	
	public static String printGridAndCages(Integer[][] grid, int size, int sqrt, List<Cage> cages) {
		
		StringBuffer hr = getHrLine(size, sqrt);
		
		StringBuffer sb = new StringBuffer();
		
		for (int y = 0; y<size ; y++) {
        	if (y % sqrt == 0 && y != 0) {
        		sb.append(hr);
        	}
        	for (int x = 0; x<size; x++) {
        		if (x % sqrt == 0 && x != 0) {
        			sb.append("| ");
        		}
        		if (grid[y][x] == null)
        			sb.append("  ");
        		else
        			sb.append(grid[y][x]+" ");
        	}
        	sb.append("\n");
        }
		
		if (cages != null) {
			// print cages
			sb.append("Cages: \n");
			for (Cage cage: cages) 
				sb.append(cage).append("\n");
		}
				
		return sb.toString();
		
	}

}
