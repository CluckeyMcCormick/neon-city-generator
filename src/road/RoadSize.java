/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package road;

import cityorg.CityStructure;

/**
 *
 * @author root
 */
public enum RoadSize {
    // () Suggests the grouping of streets on sides
    // [] Indicates that the enclosed numbers are the lane sizes of the streets
    // so (16) + (16) is two sidewalks, a left and right
    // and (16 + [16]) + (16) is a sidewalk and road lane on left, and sidewalk on right.
    ALLEY (16), // (16)
    SMALL_STREET (32), // ( 4 + [12] ) + ( [12] + 4 )
    MEDIUM_STREET (64), // ( 8 + [12 + 12] ) + ( [12 + 12] + 8 )
    LARGE_STREET (96); // (12 + [12 + 12 + 12] ) + ( [12 + 12 + 12] + 12)
    
    public static final int LENGTH_CONSTANT = CityStructure.GOLDEN_PIXEL_COUNT;
    
    public final int pixelWidth;
    
    RoadSize(int size){
        this.pixelWidth = size;
    }
}
