/*
 * Percolation.java
 * Assignment 1 
 * CS Princeton Algorithms Part 1 course on Coursera
 * 
 * See here for more details 
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * 
 * A system percolates if there is one full site in the bottom row
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private WeightedQuickUnionUF cells;
  private boolean[][] cells2d;
  
  private int gridSize;
  
  private int topVirtualSite;
  private int bottomVirtualSite;
  
  /*
   * Percolation Constructor Function
   * A QuickUnionUF or WeightedQuickUnionUF object with N * (N + 1) The external row
   * for storing the virtual site
   * A two dimensional array with N rows and N columns
   * Open top row and the lower row
   */
  public Percolation(int N) {
    // adding an extra row and column 
    
    if (N <= 0) {
      throw new IllegalArgumentException("N <= 0");
    }
    
    gridSize = N;
    
    topVirtualSite = (N * N) + 1;
    bottomVirtualSite = N * N;
    
    cells2d = new boolean[N + 1][N + 1];
    
    // 0 in a cell means that site is closed
    // 1 in a cell means that site is open 
    for (int i = 0; i < cells2d.length; i++) {
      for (int j = 0; j < cells2d[i].length; j++) {
        cells2d[i][j] = false;
      }
    }
    
    // Union find cells 
    cells = new WeightedQuickUnionUF((N * N) + 2);
    
  }
  
  /*
   * open method opens the cell on (x, y) coordinate
   * open cells are denoted by 1
   * 
   * check the neighbor cells if they are open
   * connect it with them 
   * neighbor cells are (left, right, up, down)
   * 
   */
  public void open(int x, int y) {
    validateIndices(x, y);
    cells2d[x][y] = true;
    
    int cellId = xyTo1d(x, y);
    
    // if the row = 1 
    // connect to the topVirtualSite
    if (x - 1 == 0) {
      cells.union(topVirtualSite, cellId);
    }
    if (x + 1 > gridSize) {
      // backwash problem emerges if we connect all of them to the lower
      // if it's connected to the topVirtualSite i.e. isFull
      // only in that cases connect it to the bottomVirtualSite
      if (isFull(x, y)) {
        cells.union(bottomVirtualSite, cellId);
      }
    }
    
    // checking the neighbor sites if they are open
    
    // checking the upper neighbor
    if (x - 1 > 0 && x - 1 < gridSize) {
      if (cells2d[x - 1][y]) {
        cells.union(cellId, xyTo1d(x - 1, y));
      }
    }
    // the lower neighbor
    if (x + 1 > 1 && x + 1 <= gridSize) {
      if (cells2d[x + 1][y]) {
        cells.union(cellId, xyTo1d(x + 1, y));
      }
    }
    // the left neighbor
    if (y - 1 > 0 && y - 1 < gridSize) {
      if (cells2d[x][y - 1]) {
        cells.union(cellId, xyTo1d(x, y - 1));
      }
    }
    // the right neighbor
    if (y + 1 > 1 && y + 1 <= gridSize) {
      if (cells2d[x][y + 1]) {
        cells.union(cellId, xyTo1d(x, y + 1));
      }
    }  
  }
  
  /*
   * if the site is open it returns true
   * else 
   * returns false
   */
  public boolean isOpen(int x, int y) {
    validateIndices(x, y);
    return cells2d[x][y];
  }
  
  /*
   * A cell is full if it can be connected to the top row 
   * via itself or the neighboring open chains
   * 
   * return true if it's connected to the top row
   */
  public boolean isFull(int x, int y) {
    validateIndices(x, y);
    int index = xyTo1d(x, y);
    return (cells.connected(topVirtualSite, index));
    
  }
  
  /*
   * The system percolates if the topVirtualSite created connects in some
   * way to the bottom virtual site
   */
  public boolean percolates() {
    return cells.connected(topVirtualSite, bottomVirtualSite);
  }
  

  /*
   * A method to convert 2d coordinates into it's 1d counterparts
   */
  private int xyTo1d(int x, int y) {
    // subtracting 1 and 1 from both to convert them into 0 based index
    int rowsIndex = x - 1;
    int columnIndex = y - 1;
    
    return (rowsIndex * gridSize) + columnIndex;
  }

  /*
   * Validates the indices if they are in the given range
   * Range for x,y >= 1 && x, y <= N 
   */
  private void validateIndices(int x, int y) {
    if (x <= 0 || x > gridSize) {
      throw new IndexOutOfBoundsException("row x out of bounds");
    }
    else if (y <= 0 || y > gridSize) {
      throw new IndexOutOfBoundsException("column y out of bounds");
    }
  }
  
  // main method for testing will not be checked by the
  // online judge
  public static void main(String[] args) {
  }
  
}
