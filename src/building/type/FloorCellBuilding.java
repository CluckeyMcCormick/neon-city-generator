/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package building.type;

import building.Building;
import building.BuildingDetail;

/**
 *
 * @author root
 */
public class FloorCellBuilding extends Building {
    
    // Specifies the number of floors 
    int floors;
    // Specifies the number of cells per floor, length-wise
    int cellLength;
    // Specifies the number of cells per floor, width-wise
    int cellWidth; 
    
    public FloorCellBuilding(int unitWidth, int unitLength, int unitHeight, BuildingDetail bd){
        this.cellWidth = bd.unitsToCells(unitWidth);
        this.cellLength = bd.unitsToCells(unitLength);
        
        if( unitHeight < Building.MIN_UNIT_HEIGHT )
            this.floors = bd.unitsToFloors( Building.MIN_UNIT_HEIGHT );
        else
            this.floors = bd.unitsToFloors(unitHeight);
        
        super.setDeets(bd, unitWidth, unitLength);
    }
    
    public float textureWidth(){
        return this.cellWidth * super.getDeets().getTexWidthPerCell();
    }
    
    public float textureLength(){
        return this.cellLength * super.getDeets().getTexWidthPerCell();
    }
    
    public float textureHeight(){
        return this.floors * super.getDeets().getTexHeightPerCell();
    }
    
    public float virtualWidth(){
        return this.cellWidth * super.getDeets().getVirtWidthPerCell();
    }
    
    public float virtualLength(){
        return this.cellLength * super.getDeets().getVirtWidthPerCell();
    }
    
    public float virtualHeight(){
        return this.floors * super.getDeets().getVirtHeightPerCell();
    }
    
    public int unitHeight(){
        return super.getDeets().floorsToUnits(this.floors);
    }
  
    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }
    
    public int getCellLength() {
        return cellLength;
    }

    public void setCellLength(int cellLength) {
        this.cellLength = cellLength;
    }
  
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }   
}
