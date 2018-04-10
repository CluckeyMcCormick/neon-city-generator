/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import cityorg.CityStructure;

/**
 *
 * @author frick
 */
public class BuildingInstance extends CityStructure {
    
    // The details of this building - concerning cell widths and the like
    private CellTextureDetail deets;

    private int unit_x;
    private int unit_z;

    private float virtual_height;
    
    public BuildingInstance( int unit_x, int unit_z, float virtual_height ) {
        this.unit_x = unit_x;
        this.unit_z = unit_z;
        this.virtual_height = virtual_height;
    }

    public int unitX(){
        return unit_x; 
    }
    
    public int unitZ(){
        return unit_z; 
    }
    
    public float virtualX(){
        return unit_x * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
    }
    
    public float virtualZ(){
        return unit_z * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
    }
    
    public float virtualHeight(){
        return virtual_height;
    }
}
