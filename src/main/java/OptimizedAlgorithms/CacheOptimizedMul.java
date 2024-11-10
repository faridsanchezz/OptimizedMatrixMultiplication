package OptimizedAlgorithms;

public class CacheOptimizedMul {

	public double[][] execute(double[][] a, double[][] b) {
		int n = a.length;
		double[][] c = new double[n][n];
		double[][] bTransposed = new double[n][n];


		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				bTransposed[j][i] = b[i][j];
			}
		}


		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				double sum = 0;
				for (int k = 0; k < n; k++) {
					sum += a[i][k] * bTransposed[j][k];
				}
				c[i][j] = sum;
			}
		}
		return c;
	}
}
