package OptimizedAlgorithms;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)

public class MatrixMultipliocationBenchmark {

	@Benchmark
	public double[][] CacheOptimizedMul(BenchmarkState state) {
		CacheOptimizedMul CacheOptimizedMul = new CacheOptimizedMul();
		return CacheOptimizedMul.execute(state.matrixA, state.matrixB);
	}

	@Benchmark
	public double[][] LoopUnrollingMultiplication(BenchmarkState state) {
		LoopUnrollingMul LoopUnrollingMul = new LoopUnrollingMul();
		return LoopUnrollingMul.execute(state.matrixA, state.matrixB);
	}

	@Benchmark
	public double[][] strassenMultiplication(BenchmarkState state) {
		StrassenMatrixMul strassenMatrixMul = new StrassenMatrixMul();
		return strassenMatrixMul.execute(state.matrixA, state.matrixB);
	}

	@Benchmark
	public double[][] BlockMatrixMultiplication(BenchmarkState state) {
		return BlockMatrixMul.blockMatrixMultiplication(state.matrixA, state.matrixB, state.matrixC, state.size);
	}

	@Benchmark
	public SparseMatrixCSCMul.CSCMatrix sparseCSC(BenchmarkState state) {
		return state.cscA.multiply(state.cscB);
	}

	@Benchmark
	public SparseMatrixCSRMul.CSRMatrix sparseCRS(BenchmarkState state) {
		return state.csrA.multiply(state.csrB);
	}

	@Benchmark
	public double[][] BaseMatrixMultiplication(BenchmarkState state) {
		BaseMatrixMultiplication baseMatrixMultiplication = new BaseMatrixMultiplication();
		return baseMatrixMultiplication.execute(state.matrixA, state.matrixB);
	}

	@State(Scope.Benchmark)
	public static class BenchmarkState {

		private final ArrayList<Double> memoryUsageList = new ArrayList<>();
		@Param({"100", "500", "700", "1000"})
		private int size;

		@Param({"0.1", "0.5", "0.9"})
		private double sparsity;
		private double[][] matrixA;
		private double[][] matrixB;
		private double[][] matrixC;
		private SparseMatrixCSRMul.CSRMatrix csrA;
		private SparseMatrixCSRMul.CSRMatrix csrB;

		private SparseMatrixCSCMul.CSCMatrix cscA;
		private SparseMatrixCSCMul.CSCMatrix cscB;
		private long initialMemory;

		private double[][] generateSparseMatrix(int size, double sparsity) {
			Random random = new Random();
			double[][] matrix = new double[size][size];
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					matrix[i][j] = (random.nextInt() < sparsity) ? random.nextDouble() : 0.0;
				}
			}
			return matrix;
		}

		@Setup(Level.Trial)
		public void setupTrial() {
			matrixA = generateSparseMatrix(size, sparsity);
			matrixB = generateSparseMatrix(size, sparsity);
			matrixC = new double[size][size];
			csrA = SparseMatrixCSRMul.convertToCSR(matrixA);
			csrB = SparseMatrixCSRMul.convertToCSR(matrixB);
			cscA = SparseMatrixCSCMul.convertToCSC(matrixA);
			cscB = SparseMatrixCSCMul.convertToCSC(matrixB);
		}

		@Setup(Level.Iteration)
		public void setupIteration() {
			Runtime runtime = Runtime.getRuntime();
			runtime.gc();
			initialMemory = runtime.totalMemory() - runtime.freeMemory();
		}

		@TearDown(Level.Iteration)
		public void tearDownIteration() {
			Runtime runtime = Runtime.getRuntime();
			long finalMemory = runtime.totalMemory() - runtime.freeMemory();
			long memoryUsed = (finalMemory - initialMemory);
			memoryUsageList.add(memoryUsed / 1024.0);
			System.out.println("\nCurrent memory usage: " + (memoryUsed / 1024.0) + " KB");
		}

		@TearDown(Level.Trial)
		public void tearDownTrial() {
			ArrayList<Double> subList = new ArrayList<>(memoryUsageList.subList(5, 15));
			double averageMemoryUsage = subList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
			System.out.println("Average memory usage of the last 10 iterations: " + averageMemoryUsage + " KB");

		}
	}
}
