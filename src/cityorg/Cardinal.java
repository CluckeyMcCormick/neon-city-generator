/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

/**
 *
 * @author frick
 */
public enum Cardinal {
    NONE (-1),
    
    EAST (0),
    NORTH_EAST (1),
    NORTH (2),
    NORTH_WEST (3),
    WEST (4),
    SOUTH_WEST (5),
    SOUTH (6),
    SOUTH_EAST(7);


    public final int value;
    
    Cardinal(int value){
        this.value = value;
    }
}
