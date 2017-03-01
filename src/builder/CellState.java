/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

/**
 *
 * @author root
 */
public enum CellState {
    LIT (1, 8, false),
    DIM (1, 8, false),
    OFF (1, 8, true),
    NONE (0, 0, true);
    
    //Whats the minimum number of cells, in a row, of this state?
    public final int min;
    //Whats the maximum number of cells, in a row, of this state?
    public final int max;
    //Is a cell, in this state, equivalent to a blank?
    public final boolean isBlank;
    
    CellState(int min, int max, boolean isBlank){
        this.min = min;
        this.max = max;
        this.isBlank = isBlank;
    }

}
