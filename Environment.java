import java.security.SecureRandom;
import java.util.*;

/**
 * Maintain the environment for a 2D cellular automaton.
 * 
 * @author David J. Barnes
 * @contributor Brian McKiernan
 * @date 11/5/2016
 * @version  2016.02.29
 */
public class Environment
{
    // Default size for the environment.
    private static final int DEFAULT_ROWS = 50;
    private static final int DEFAULT_COLS = 50;
    
    // The grid of cells.
    private Cell[][] cells;
    // Visualization of the environment.
    private final EnvironmentView view;

    /**
     * Create an environment with the default size.
     */
    public Environment()
    {
        this(DEFAULT_ROWS, DEFAULT_COLS);
    }

    /**
     * Create an environment with the given size.
     * @param numRows The number of rows.
     * @param numCols The number of cols;
     */
    public Environment(int numRows, int numCols)
    {
        setup(numRows, numCols);
        randomize();
        view = new EnvironmentView(this, numRows, numCols);
        view.showCells();
    }
    
    /**
     * Run the automaton for one step.
     */
    public void step()
    {
        int numRows = cells.length;
        int numCols = cells[0].length;
        // Build a record of the next state of each cell.
        int[][] nextStates = new int[numRows][numCols];
        // Ask each cell to determine its next state.
        for(int row = 0; row < numRows; row++) {
            int[] rowOfStates = nextStates[row];
            for(int col = 0; col < numCols; col++) {
                rowOfStates[col] = cells[row][col].getNextState();
            }
        }
        // Update the cells' states.
        for(int row = 0; row < numRows; row++) {
            int[] rowOfStates = nextStates[row];
            for(int col = 0; col < numCols; col++) {
                setCellState(row, col, rowOfStates[col]);
            }
        }
    }
    
    /**
     * Reset the state of the automaton to all DEAD.
     */
    public void reset()
    {
        int numRows = cells.length;
        int numCols = cells[0].length;
        for(int row = 0; row < numRows; row++) {
            for(int col = 0; col < numCols; col++) {
                setCellState(row, col, Cell.DEAD);
            }
        }
    }
    
    /**
     * Generate a random setup.
     */
    public void randomize()
    {
        int numRows = cells.length;
        int numCols = cells[0].length;
        SecureRandom rand = new SecureRandom();
        for(int row = 0; row < numRows; row++) {
            for(int col = 0; col < numCols; col++) {
                setCellState(row, col, rand.nextInt(Cell.NUM_STATES));
            }
        }
    }
    
    /**
     * Set the state of one cell.
     * @param row The cell's row.
     * @param col The cell's col.
     * @param state The cell's state.
     */
    public void setCellState(int row, int col, int state)
    {
        cells[row][col].setState(state);
    }
    
    /**
     * Return the grid of cells.
     * @return The grid of cells.
     */
    public Cell[][] getCells()
    {
        return cells;
    }
    
    /**
     * Setup a new environment of the given size.
     * @param numRows The number of rows (+ 2 dummy rows).
     * @param numCols The number of cols (+ 2 dummy columns);
     * Modified for Method Exercise 7-39 to make sure there will be 
     * neighbors for the rows and columns passed. See param parenthesis.
     */
    private void setup(int numRows, int numCols)
    {
        //The two rows and columns added below and in the for loop
        //are two create two unseen row and columns of dummy cells.
        cells = new Cell[numRows+2][numCols+2];
        for(int row = 0; row < numRows+2; row++) {
            for (int col = 0; col < numCols+2; col++) {
                cells[row][col] = new Cell();
            }
        }
        setupNeighbors();
    }
    
    /**
     * Give to a cell a list of its neighbors.
     * setupNeighbors method has two rows and columns subtracted from 
     * row-length and column-length so that neighbors will only be found
     * for the specified number of rows/columns passed upon initialization.
     * 
     * Also the initialization of the nr and nc variables in the two 
     * innermost for-loops have had their value-assigning code changed
     * so the neighbors for the last row and column are not assigned the 
     * initial rows and columns and therefore the cells do not exist 
     * on a torus.
     */
    private void setupNeighbors()
    {
        //Subtract the two dummy rows & columns from respective lengths.
        int numRows = -2+(cells.length);
        int numCols = -2+(cells[0].length);
        //UnComment the bottom row for debugging - to make sure rows
        //and columns are equal to the number passed upon initialization.
        //System.out.println("numRows- "+numRows+" numColumns- "+numCols);
        //Neighbors ArrayList does not need a set size.
        ArrayList<Cell> neighbors = new ArrayList<Cell>();
        for(int row = 1; row <= numRows; row++) {
            for(int col = 1; col <= numCols; col++) {
                Cell cell = cells[row][col];
                /*
                 * If the program is giving a null pointer exception
                 * and degbugging is needed to locate the 
                 * cell-neighbors region throwing the exception 
                 * uncomment println below.
                 */
                //System.out.println("Row - " +row+ " Col - " +col);
                // This process will also include the cell.
                for(int dr = -1; dr <= 1; dr++) {
                    for(int dc = -1; dc <= 1; dc++) {
                        int nr = (row + dr);
                        int nc = (col + dc);
                        neighbors.add(cells[nr][nc]);
                    }
                }
                // The neighbours should not include the cell at
                // (row,col) so remove it.
                neighbors.remove(cell);
                cell.setNeighbors(neighbors);
                neighbors.clear();
            }
        }
    }

}
