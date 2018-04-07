/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package building.type;

import building.Building;
import building.CellTextureDetail;

/**
 *
 * @author root
 */
public class FloorCellBuilding extends Building {
    
    // Specifies the number of floors 
    int floors;
    // Specifies the number of cells per floor, length-wise
    int cell_x_len;
    // Specifies the number of cells per floor, width-wise
    int cell_z_len; 
    
    public FloorCellBuilding(int unit_z, int unit_x, int unitHeight, CellTextureDetail bd){
        this.cell_z_len = bd.unitsToCells(unit_z);
        this.cell_x_len = bd.unitsToCells(unit_x);
        
        if( unitHeight < Building.MIN_UNIT_HEIGHT )
            this.floors = bd.unitsToFloors( Building.MIN_UNIT_HEIGHT );
        else
            this.floors = bd.unitsToFloors(unitHeight);
        
        super.setDeets(bd, unit_z, unit_x);
    }
    
    public float textureWidth(){
        return this.cell_z_len * super.getDeets().getTexWidthPerCell();
    }
    
    public float textureLength(){
        return this.cell_x_len * super.getDeets().getTexWidthPerCell();
    }
    
    public float textureHeight(){
        return this.floors * super.getDeets().getTexHeightPerCell();
    }
    
    public float virtualWidth(){
        return this.cell_z_len * super.getDeets().getVirtWidthPerCell();
    }
    
    public float virtualLength(){
        return this.cell_x_len * super.getDeets().getVirtWidthPerCell();
    }
    
    public float virtualHeight(){
        return this.floors * super.getDeets().getVirtHeightPerCell();
    }
    
    public int unitHeight(){
        return super.getDeets().floorsToUnits(this.floors);
    }
  
    public int getZLen() {
        return cell_z_len;
    }

    public void setZLen(int cellWidth) {
        this.cell_z_len = cellWidth;
    }
    
    public int getXLen() {
        return cell_x_len;
    }

    public void setXLen(int cellLength) {
        this.cell_x_len = cellLength;
    }
  
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }   
}
