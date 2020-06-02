package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grid.KillerSudokuGrid.Cage;
import grid.KillerSudokuGrid.Cell;

/**
 * @author Chih-Hsuan Lee <s3714761>
 *
 */
public class MatrixUtils {
	
	public static Map<String, List<String>> combinations = new HashMap<>();
    
    public static List<Matrix> generateMatrix(Integer size, Integer sqrt, Integer[] symbols, int numberOfConstraints, List<Cage> cages) {

    	// calculate which matrix should have cage constraint
    	Set<String> possibleCombinations = new HashSet<>();
		if (cages != null) {
			List<Integer> numbers = new ArrayList<Integer>(Arrays.asList(symbols));
			int count = 1;
			for (Cage cage : cages) {
				
				List<List<Integer>> possibleValues = sum_up_recursive(new ArrayList<List<Integer>>(), numbers, cage.sum,new ArrayList<Integer>(), cage.positions.size());
				
				List<String> combineList = new ArrayList<>();
				for (List<Integer> partial : possibleValues) {
					int[] a = new int[partial.size()];
		        	for (int i = 0; i < partial.size(); i++) {
		        		a[i]=partial.get(i);
		        	}
		        	Set<List<Integer>> possibilities = permute(new HashSet<>(), a, 0);
		        	for (List<Integer> temp_possiblity : possibilities) {
						StringBuffer sb = new StringBuffer();
						int z = 0;
						for (Cell position : cage.positions) {
							int cageX = position.col;
							int cageY = position.row;
							int number = temp_possiblity.get(z);
							possibleCombinations.add(cageY+","+cageX+","+number);
							sb.append(cageY+","+cageX+","+number).append(" ");
							z++;
						}
						combineList.add(sb.toString());
					}
				}
				
				combinations.put("cage"+count, combineList);
				count++;
			}
		}
		
    	List<Matrix> matrixs = null;
    	matrixs = new ArrayList<>();
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				for (int value : symbols) {
					Integer[] constraints = new Integer[size*size*numberOfConstraints];
					
					// One value constraint: 
					// constraint number : row x size + col
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
					
					// Cage constraint:
					// constraint number : 4 x size^2 + row x size + col
					if (cages != null) {
						boolean isFound = false;
						for (String cageConstraintTarget : possibleCombinations) {
							if ((row+","+col+","+value).equals(cageConstraintTarget)) {
								isFound = true;
								break;
							}
						}
						if (isFound) {
							constraints[ 4 * ((int) Math.pow(size,2)) + row * size + col] = 1;
						}
					}
					
					matrixs.add(new Matrix(row, col, value, constraints));
					
				}
			}
		}
	
		
		// To print the matrix
		//1,1,1 | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c | c c c c c c c c c c c c c c c c 
//		System.out.println("r,c,v | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c | c c c c c c c c c c c c c c c c");
//		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
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
//				System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//		}
		return matrixs;
    }
    
    public static List<List<Integer>> sum_up_recursive(List<List<Integer>> list, List<Integer> symbols, int target, List<Integer> partial, Integer limit) {
       int s = 0;
       
       for (int x: partial) 
    	   s += x;
       
       if (s == target && partial.size() == limit) {
    	   list.add(partial);
       }
       
       if (s >= target)
            return list;
       
       for(int i=0;i<symbols.size();i++) {
             ArrayList<Integer> remaining = new ArrayList<Integer>();
             int n = symbols.get(i);
             for (int j=i; j<symbols.size();j++) remaining.add(symbols.get(j));
             ArrayList<Integer> partial_rec = new ArrayList<Integer>(partial);
             partial_rec.add(n);
             sum_up_recursive(list, remaining,target,partial_rec, limit);
       }
       return list;
    }
    
    
    private static Set<List<Integer>> permute(Set<List<Integer>> possible, int[] a, int k) {
        if (k == a.length){
        	List<Integer> temp = new ArrayList<>();
            for (int i = 0; i < a.length; i++) {
            	temp.add(a[i]);
            }
            possible.add(temp);
            return possible;
        }else {
            for (int i = k; i < a.length; i++) {
                int temp = a[k];
                a[k] = a[i];
                a[i] = temp;
 
                possible = permute(possible, a, k + 1);
 
                temp = a[k];
                a[k] = a[i];
                a[i] = temp;
            }
        }
        return possible;
    } 

}
