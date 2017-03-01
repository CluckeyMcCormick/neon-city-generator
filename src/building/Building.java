/*
 * To change this template, choose Tools | Templates
 * and open the import static mygame.Building.CELL_HEIGHT;
import static mygame.Building.CELL_WIDTH;template in the editor.
 */
package building;

import cityorg.CityStructure;

/**
 *
 * @author root
 */
public abstract class Building extends CityStructure {
    
    // The details of this building - concerning cell widths and the like
    private BuildingDetail deets;
    
    private int unitWidth;
    
    private int unitLength;
    
    public static final int MIN_UNIT_HEIGHT = 4;
    
    public void setDeets(BuildingDetail deets, int unitWidth, int unitLength) {
        this.deets = deets;
        this.unitLength = unitLength;
        this.unitWidth = unitWidth;
    }
    
    public BuildingDetail getDeets() {
        return deets;
    }
    
    public int unitWidth(){
        return this.unitWidth; 
    }
    
    public int unitLength(){
        return this.unitLength; 
    }
    
    public abstract float virtualWidth();
    
    public abstract float virtualLength();
    
    public abstract float virtualHeight();
}
