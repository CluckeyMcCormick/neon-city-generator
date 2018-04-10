/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import building.BasicBuildingTemplate;

/**
 *
 * @author frick
 */
public class BlockDetail {
    //If a building equals or exceeds this unitHeight, then it has warning lights
    //If negative, then no warning lights!
    private final int warnHeight;
    
    // Our ideal height for the buildings in this block
    private final int idealHeight;
    
    //The ideal length for the street-facing side of the building
    private final int idealFace;
    
    //The standard height deviation for the buildings in this block
    private final int heightDeviant;

    //The standard street-facing length deviation
    private final int faceDeviant;
    
    //The standard deviation for the depth of buildings in this block
    private final int depthDeviant;
    
    public BlockDetail(
        int warnHeight, int idealHeight, int idealFace, int heightDeviant, 
        int faceDeviant, int depthDeviant
    ) {
        this.warnHeight = warnHeight;
        this.idealHeight = idealHeight;
        this.heightDeviant = heightDeviant;
        this.depthDeviant = depthDeviant;
        this.idealFace = idealFace;
        this.faceDeviant = faceDeviant;
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
    
}
