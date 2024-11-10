package OptimizedAlgorithms;

public class BlockMatrixMul {

	private static final int BLOCK_SIZE = 2;

	public static double[][] blockMatrixMultiplication(double[][] A, double[][] B, double[][] C, int N) {
		for (int i = 0; i < N; i += BLOCK_SIZE) {
			for (int j = 0; j < N; j += BLOCK_SIZE) {
				for (int k = 0; k < N; k += BLOCK_SIZE) {
					multiplyBlock(A, B, C, i, j, k, N);
				}
			}
		}
		return C;
	}

	private static void multiplyBlock(double[][] A, double[][] B, double[][] C, int rowBlock, int colBlock, int kBlock, int N) {
		for (int i = rowBlock; i < Math.min(rowBlock + BLOCK_SIZE, N); i++) {
			for (int j = colBlock; j < Math.min(colBlock + BLOCK_SIZE, N); j++) {
				double sum = 0;
				for (int k = kBlock; k < Math.min(kBlock + BLOCK_SIZE, N); k++) {
					sum += A[i][k] * B[k][j];
				}
				C[i][j] += sum;
			}
		}
	}
}
