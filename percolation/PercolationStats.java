/*
 * PercolationStats.java
 * A monte carlo simulation for calculating the percolation threshold
 * very accurately
 * 
 * Assignment 1: Percolation
 * Princeton Algorithms Course Coursera
 */

/*
 * Importing the required libraries
 */
// StdIn and StdOut
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
// StdStats library for performing the statistic operations
// involved in the problem
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;
import java.lang.Math;

public class PercolationStats {
	// Performs T independent Percolation experiments on an N-by-N grid

	private double[] thresholdArray;
	
	public PercolationStats(int N, int T){
		// Arguments not acceptaple if N <= 0 || T <= 0
		if (N <= 0 || T <= 0){
			throw new IllegalArgumentException("N or T should not be less than or equal to 0");
		}
		
		Percolation perc;
		thresholdArray = new double[T];
		// number of opensites
		double openSites = 0;
		
		double threshold = 0;
		
		// Performing T independent operations on perc
		for (int i = 0; i < T; i++){
			perc = new Percolation(N);
			openSites = 0;
			threshold = 0;
			
			while(!perc.percolates()){
				// Generating random x and y coordinates
				int row = StdRandom.uniform(1, N + 1);
				int column = StdRandom.uniform(1, N + 1);
				
				/// Opening the sites if it's not already opened
				if (!perc.isOpen(row, column)){
					perc.open(row, column);
					openSites++;
				}
			}
			
			if (perc.percolates()){
				// calculating the threshold 
				// by dividing the number of openSites by totalSites
				threshold = openSites / (N * N);
				thresholdArray[i] = threshold;
			}
		}
	}
	
	// calculates the mean of all the given thresholds
	public double mean(){
		return StdStats.mean(thresholdArray);
	}
	
	// calculates the standard deviation of the given thresholds
	public double stddev(){
		return StdStats.stddev(thresholdArray);
	}
	
	// Low confidence interval for the percolation threshold
	public double confidenceLo(){
		double mean = mean();
		double stddev = stddev();
		double sqrtT = Math.sqrt(thresholdArray.length);
		double confidenceInterval = mean - ((1.96 * stddev) / sqrtT);
		
		return confidenceInterval;
	}
	
	// High Confidence interval for the percolation threshold
	public double confidenceHigh(){
		double mean = mean();
		double stddev = stddev();
		double sqrtT = Math.sqrt(thresholdArray.length);
		double confidenceInterval = mean + ((1.96 * stddev) / sqrtT); 
		
		return confidenceInterval;
	}
	
	// main method for testing
	public static void main(String[] args){
		
		// Read the grid Size for the percolation square
		int N = StdIn.readInt();
		// The next int should be the number of independent operations
		// to be performed
		int T = StdIn.readInt();
		
		PercolationStats ps = new PercolationStats(N, T);
		
		/// Calculation all the values gotten from the tests performed
		double mean = ps.mean();
		double stddev = ps.stddev();
		double confidenceLo = ps.confidenceLo();
		double confidenceHigh = ps.confidenceHigh();
		
		// Writing the output to StdOut
		
		// Using StdOut.printf to format the output by tabs
		// printing mean first
		StdOut.printf("mean\t\t\t= %s\n", Double.toString(mean));
		
		// now it's the turn for standard deviation
		StdOut.printf("stddev\t\t\t= %s\n", Double.toString(stddev));
		
		// Now printing the high confidence and low confidence interval 
		// seperated by commas
		StdOut.printf("95%% confidence interval = %s, %s \n", 
							Double.toString(confidenceLo), 
							Double.toString(confidenceHigh));
	}
	
}