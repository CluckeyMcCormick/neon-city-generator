/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import building.BasicBuildingTemplate;
import building.WindowStyle;
import java.util.List;
import prime.RandomSingleton;

/**
 *
 * @author frick
 */
public class BlockDetail {
    //The window styles that are available
    private List<WindowStyle> windows;
    
    //The window styles that are available
    private List<BasicBuildingTemplate> buildings;
    
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
        List<WindowStyle> windows, List<BasicBuildingTemplate> buildings, 
        int idealHeight, int idealFace, int heightDeviant, 
        int faceDeviant, int depthDeviant
    ) {
        this.windows = windows;
        this.buildings = buildings;
        
        this.idealHeight = idealHeight;
        this.heightDeviant = heightDeviant;
        this.depthDeviant = depthDeviant;
        this.idealFace = idealFace;
        this.faceDeviant = faceDeviant;
    }

    public BasicBuildingTemplate getRandomTemplate(){
        RandomSingleton rs = RandomSingleton.getInstance();
        return buildings.get( rs.nextInt( buildings.size() ) );
    }

    public WindowStyle getRandomWindow(){
        RandomSingleton rs = RandomSingleton.getInstance();
        return windows.get( rs.nextInt( windows.size() ) );
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
