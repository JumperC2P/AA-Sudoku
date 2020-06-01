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
