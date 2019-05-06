import java.util.Scanner;

public class Task3 {
	public static void main(String[] args){
		Scanner reader = new Scanner(System.in);
		System.out.println("1) Choose the size of your matricies");
		System.out.println("2) Run with values and output the times from 0-512");
		int input = reader.nextInt();
		
		if (input == 1){
			System.out.println("Size for your two matricies? (Must be a power of 2)");
			int in = reader.nextInt();
			//set two matrixes to random values
			int size = in;
			int[][] matrix1 = setRandVals(size);
			int[][] matrix2 = setRandVals(size);
		
			//print the first matrix
			System.out.println("Matrix 1");
			print(matrix1);
		
			System.out.println();
		
			//print the second matrix
			System.out.println("Matrix 2");
			print(matrix2);
			
			System.out.println();
		
			//run the multiplied using the classical method
			int[][] multiplied = classical(matrix1,matrix2);
			//print the multiplied matrix
			System.out.println("Classical method:");
			print(multiplied);
		
			System.out.println();
		
			//run strassen
			int[][] multiplied2 = strassen(matrix1,matrix2);
			System.out.println("Strassen method: ");
			print(multiplied2);
		}
		
		else if (input == 2){
			System.out.println("Size \t Classical \t Strassen");
			for (int i = 1; i <= 9; i++){
				int size = (int)(Math.pow(2, i));
				int[][] mat1 = setRandVals(size);
				int[][] mat2 = setRandVals(size);
				
				long start = System.nanoTime();
				classical(mat1,mat2);
				long classicalTime = System.nanoTime() - start;
				
				start = System.nanoTime();
				strassen(mat1,mat2);
				long strassenTime = System.nanoTime() - start;
				
				if (i >= 8){
					System.out.println(size + "\t" + classicalTime + "\t" + strassenTime);
				}
				else{
					System.out.println(size + "\t" + classicalTime + "\t\t" + strassenTime);
				}
			}
		}
	}
	
	/**
	 * @param size
	 * @return a matrix with random values given the @value size
	 */
	static int[][] setRandVals(int size){
		int[][] matrix = new int[size][size];
		for (int i = 0; i < matrix.length; i++){
			for (int j = 0; j <matrix.length; j++){
				matrix[i][j] = (int)(Math.random()*10);
			}
		}
		return matrix;
	}
	
	/**
	 * @param matrix
	 * prints the matrix
	 */
	static void print(int[][] matrix){
		for (int i = 0; i < matrix.length; i++){
			for (int j = 0; j < matrix.length; j++){
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * @param mat1
	 * @param mat2
	 * @return a matrix with the values of matrix 1 and matrix 2 multiplied using the classical method
	 */
	static int[][] classical(int[][] mat1, int[][] mat2){
		//create empty matrix with matrix 1 and 2 size
		int[][] multiplied = new int[mat1.length][mat1.length];
		
		//for each value in multiplied, add the current value with the value from the same position in matrix 1 and matrix 2
		for (int i = 0; i < mat1.length; i++){
			for (int j = 0; j < mat1.length; j++){
				multiplied[i][j] = 0;
				for (int k = 0; k < mat1.length; k++){
					multiplied[i][j] += mat1[i][k]*mat2[k][j];
				}
			}
		}
		
		return multiplied;
	}
	
	static int[][] strassen(int[][] mat1, int[][] mat2){
		
		int size = mat1.length;
		
		int[][] res = new int[size][size];
		
		//If the matrix is 1 by 1
		if (size == 1){
			res[0][0] = mat1[0][0] * mat2[0][0];
		}
		else{
			//first matrix in 4 parts
			int[][] A11 = new int[size/2][size/2];
            int[][] A12 = new int[size/2][size/2];
            int[][] A21 = new int[size/2][size/2];
            int[][] A22 = new int[size/2][size/2];
			
			//second matrix in 4 parts
            int[][] B11 = new int[size/2][size/2];
            int[][] B12 = new int[size/2][size/2];
            int[][] B21 = new int[size/2][size/2];
            int[][] B22 = new int[size/2][size/2];
			
			
			//divide first matrix into 4 parts
            split(mat1, A11, 0 , 0);
            split(mat1, A12, 0 , size/2);
            split(mat1, A21, size/2, 0);
            split(mat1, A22, size/2, size/2);
			
			//divide second matrix into 4 parts
            split(mat2, B11, 0 , 0);
            split(mat2, B12, 0 , size/2);
            split(mat2, B21, size/2, 0);
            split(mat2, B22, size/2, size/2);
			
			int [][] M1 = strassen(add(A11, A22), add(B11, B22));
            int [][] M2 = strassen(add(A21, A22), B11);
            int [][] M3 = strassen(A11, sub(B12, B22));
            int [][] M4 = strassen(A22, sub(B21, B11));
            int [][] M5 = strassen(add(A11, A12), B22);
            int [][] M6 = strassen(sub(A21, A11), add(B11, B12));
            int [][] M7 = strassen(sub(A12, A22), add(B21, B22));
			
            int [][] C11 = add(sub(add(M1, M4), M5), M7);
            int [][] C12 = add(M3, M5);
            int [][] C21 = add(M2, M4);
            int [][] C22 = add(sub(add(M1, M3), M2), M6);
			
			copySubArray(C11,res,0,0);
			copySubArray(C12,res,0,size/2);
			copySubArray(C21,res,size/2,0);
			copySubArray(C22,res,size/2,size/2);
		}
		
		return res;
	}
	
	/**
	 * @param C
	 * @param P
	 * @param iB
	 * @param jB
	 */
	public static void copySubArray(int[][] C, int[][] P, int iB, int jB) 
    {
        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
                P[i2][j2] = C[i1][j1];
    }  
	
	/**
	 * @param a
	 * @param b
	 * @return Matrix A + Matrix B
	 */
	public static int[][] add(int[][] a, int[][] b) {
        int n = a.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = a[i][j] + b[i][j];
            }
        }
        return res;
    }
	/**
	 * @param a
	 * @param b
	 * @return Matrix A - Matrix B
	 */
	public static int[][] sub(int[][] a, int[][] b) {
        int n = a.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = a[i][j] - b[i][j];
            }
        }
        return res;
    }
	
	 /**
	 * @param P
	 * @param C
	 * @param iB
	 * @param jB
	 */
	static void split(int[][] P, int[][] C, int iB, int jB){
	        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
	            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
	                C[i1][j1] = P[i2][j2];
	    }
	
}
