/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import building.Building;

/**
 *
 * @author frick
 */
public class BlockDetail {
    //If a building equals or exceeds this unitHeight, then it has warning lights
    //If negative, then no warning lights!
    private int warnHeight;
    
    // Our ideal height for the buildings in this block
    private int idealHeight;
    
    //The ideal length for the street-facing side of the building
    private int idealFace;
    
    //The standard height deviation for the buildings in this block
    private int heightDeviant;

    //The standard street-facing length deviation
    private int faceDeviant;
    
    //The standard deviation for the depth of buildings in this block
    private int depthDeviant;
    
    //The depth to which the linear part of the base extends, widthwise
    //MUST BE GREATER THAN Building.MIN_UNIT_HEIGHT
    private int widDepth;
    
    //The depth to which the linear part of the base extends, lengthwise
    //MUST BE GREATER THAN Building.MIN_UNIT_HEIGHT
    private int lenDepth;
    
    public BlockDetail(
        int warnHeight, int idealHeight, int idealFace, int heightDeviant, 
        int faceDeviant, int depthDeviant, int widDepth, int lenDepth
    ) {
        this.warnHeight = warnHeight;
        this.idealHeight = idealHeight;
        this.heightDeviant = heightDeviant;
        this.depthDeviant = depthDeviant;
        this.idealFace = idealFace;
        this.faceDeviant = faceDeviant;
        this.widDepth = widDepth;
        this.lenDepth = lenDepth;
        
        if(this.widDepth < Building.MIN_UNIT_HEIGHT)
            this.widDepth = Building.MIN_UNIT_HEIGHT;
        if(this.lenDepth < Building.MIN_UNIT_HEIGHT)
            this.lenDepth = Building.MIN_UNIT_HEIGHT;
    }

    public boolean hasWarn(int height){
        if(this.warnHeight < 0)
            return false;
        return height >= this.warnHeight;
    }
    
    public int getWarnHeight() {
        return warnHeight;
    }

    public int getIdealHeight() {
        return idealHeight;
    }

    public int getIdealFace() {
        return idealFace;
    }

    public int getHeightDeviant() {
        return heightDeviant;
    }

    public int getFaceDeviant() {
        return faceDeviant;
    }

    public int getDepthDeviant() {
        return depthDeviant;
    }

    public int getWidDepth() {
        return widDepth;
    }

    public int getLenDepth() {
        return lenDepth;
    }
    
}
