package solver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chih-Hsuan Lee <s3714761>
 *
 */
public class MatrixUtils {
    
    public static List<Matrix> generateMatrix(Integer size, Integer sqrt, int[] symbols) {
    	List<Matrix> matrixs = null;
    	matrixs = new ArrayList<>();
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				for (int value : symbols) {
					Integer[] constraints = new Integer[size*size*4];
					
					// One value constraint: 
					// constraint number : row x size + value - 1
					constraints[row * size + col] = 1;
					
					// Row constraint:
					// constraint number : size^2 + row x size + value - 1
					constraints[((int) Math.pow(size,2)) + row * size + value - 1] = 1;
					
					// Column constraint:
					// constraint number : 2 x size^2 + col x size + value - 1
					constraints[2 * ((int) Math.pow(size,2)) + col * size + value - 1] = 1;
					
					// Box constraint:
					// constraint number : 3 x size^2 + (row / sqrt) x size x sqrt + (col / sqrt) x size + value - 1
					constraints[3 * ((int) Math.pow(size,2)) + (row / sqrt) * size * sqrt + (col / sqrt) * size + value - 1] = 1;
					
					matrixs.add(new Matrix(row, col, value, constraints));
					
				}
			}
		}
		
		// To print the matrix
		//1,1,1 | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c | c c c c c c c c c c c c c c c c 
//		System.out.println("r,c,v | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c");
//		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
//		
//		for (Matrix m : matrixs) {
//			System.out.print(m.row+","+m.col+","+m.value+" | ");
//			for (int i = 0; i < m.constraints.length; i++) {
//				if (i != 0 && i % (size*size) == 0)
//					System.out.print("| ");
//				Integer val = m.constraints[i];
//				System.out.print((val == null ? " ":m.constraints[i])+" ");
//			}
//			System.out.println();
//			if (m.value % size == 0)
//				System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
//		}
		return matrixs;
    }

}
