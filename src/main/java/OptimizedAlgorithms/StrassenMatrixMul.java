package OptimizedAlgorithms;

public class StrassenMatrixMul {

	public double[][] execute(double[][] a, double[][] b) {
		int n = a.length;
		if (n == 1) {
			return new double[][]{{a[0][0] * b[0][0]}};
		}

		int newSize = n / 2;
		double[][] a11 = new double[newSize][newSize];
		double[][] a12 = new double[newSize][newSize];
		double[][] a21 = new double[newSize][newSize];
		double[][] a22 = new double[newSize][newSize];
		double[][] b11 = new double[newSize][newSize];
		double[][] b12 = new double[newSize][newSize];
		double[][] b21 = new double[newSize][newSize];
		double[][] b22 = new double[newSize][newSize];


		divideMatrix(a, a11, 0, 0);
		divideMatrix(a, a12, 0, newSize);
		divideMatrix(a, a21, newSize, 0);
		divideMatrix(a, a22, newSize, newSize);
		divideMatrix(b, b11, 0, 0);
		divideMatrix(b, b12, 0, newSize);
		divideMatrix(b, b21, newSize, 0);
		divideMatrix(b, b22, newSize, newSize);


		double[][] m1 = execute(add(a11, a22), add(b11, b22));
		double[][] m2 = execute(add(a21, a22), b11);
		double[][] m3 = execute(a11, subtract(b12, b22));
		double[][] m4 = execute(a22, subtract(b21, b11));
		double[][] m5 = execute(add(a11, a12), b22);
		double[][] m6 = execute(subtract(a21, a11), add(b11, b12));
		double[][] m7 = execute(subtract(a12, a22), add(b21, b22));


		double[][] c11 = add(subtract(add(m1, m4), m5), m7);
		double[][] c12 = add(m3, m5);
		double[][] c21 = add(m2, m4);
		double[][] c22 = add(subtract(add(m1, m3), m2), m6);


		double[][] c = new double[n][n];
		combineMatrix(c11, c, 0, 0);
		combineMatrix(c12, c, 0, newSize);
		combineMatrix(c21, c, newSize, 0);
		combineMatrix(c22, c, newSize, newSize);

		return c;
	}

	private void divideMatrix(double[][] parent, double[][] child, int iB, int jB) {
		for (int i = 0; i < child.length; i++) {
			System.arraycopy(parent[i + iB], 0 + jB, child[i], 0, child.length);
		}
	}

	private void combineMatrix(double[][] child, double[][] parent, int iB, int jB) {
		for (int i = 0; i < child.length; i++) {
			System.arraycopy(child[i], 0, parent[i + iB], 0 + jB, child.length);
		}
	}

	private double[][] add(double[][] a, double[][] b) {
		int n = a.length;
		double[][] result = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = a[i][j] + b[i][j];
			}
		}
		return result;
	}


	private double[][] subtract(double[][] a, double[][] b) {
		int n = a.length;
		double[][] result = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = a[i][j] - b[i][j];
			}
		}
		return result;
	}
}
