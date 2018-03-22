/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import cityorg.CityStructure;

/**
 *
 * @author root
 */
public enum RoadSize {
    ALLEY (2), // (16)
    SMALL_STREET (4), // ( 4 + [12] ) + ( [12] + 4 )
    MEDIUM_STREET (6), // ( 8 + [12 + 12] ) + ( [12 + 12] + 8 )
    LARGE_STREET (8); // (12 + [12 + 12 + 12] ) + ( [12 + 12 + 12] + 12)
    
    public static final int LENGTH_CONSTANT = CityStructure.GOLDEN_PIXEL_COUNT;
    
    public final int unitWidth;
    
    RoadSize(int size){
        this.unitWidth = size;
    }
}
