package solver;

/**
 * @author Chih-Hsuan Lee <s3714761>
 *
 */
public class Matrix {
	
	int row;
	int col;
	int value;
	Integer[] constraints;
	
	public Matrix(int row, int col, int value, Integer[] constraints) {
		this.row = row;
		this.col = col;
		this.value = value;
		this.constraints = constraints;
	}
	
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(row).append(", ");
		sb.append(col).append(", ");
		sb.append(value).append(" --> ");
		
		sb.append(", [");
		
		for (Integer i : constraints) {
			sb.append(i+", ");
		}
		sb.append("]");
		
		return sb.toString();
	}
	

}
