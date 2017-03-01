/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import building.Building;
import building.BuildingFactory;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.Arrays;
import prime.MathHelp;

/**
 *
 * @author root
 */
public abstract class CityBlock extends CityStructure {

    public final int MIN_FRONT = Building.MIN_UNIT_HEIGHT;
    
    private BlockDetail blockDet;
    
    private int unitLength; 
    private int unitWidth;

    private int[] unitHeight;
    
    private float northSlope;
    private float eastSlope;
    private float southSlope;
    private float westSlope;
    
    private final int POINT_A = 0;
    private final int POINT_B = 1;
    private final int POINT_C = 2;
    private final int POINT_D = 3;
    
    public CityBlock(BlockDetail bd, int unitLength, int unitWidth, int[] unitHeight) {
        this.blockDet = bd;
        this.unitLength = unitLength;
        this.unitWidth = unitWidth;
        this.unitHeight = unitHeight;
        
        eastSlope = MathHelp.calculateSlope(unitHeight[POINT_B] - unitHeight[POINT_A], unitWidth - (bd.getLenDepth() * 2) );
        southSlope = MathHelp.calculateSlope(unitHeight[POINT_D] - unitHeight[POINT_A], unitLength - (bd.getWidDepth() * 2) );
        westSlope = MathHelp.calculateSlope(unitHeight[POINT_D] - unitHeight[POINT_C], unitWidth - (bd.getLenDepth() * 2) );
        northSlope = MathHelp.calculateSlope(unitHeight[POINT_B] - unitHeight[POINT_C], unitLength - (bd.getWidDepth() * 2) );
        
        System.out.println( southSlope + " " + northSlope);
        
        super.setNode( new Node() );
    }
    
    public void generateBuildings(BuildingFactory bf){
        Geometry[] geoms;
        Node nod = super.getNode();
        geoms = bf.blockFloor(
            unitWidth, unitLength, unitHeight, 
            this.blockDet.getWidDepth(), 
            this.blockDet.getLenDepth()
        );
        
        for(int i = 0; i < geoms.length; i++)
            nod.attachChild(geoms[i]);
    }
    
    public int[] calcHeightAdjust(int ulX, int ulY, int length, int width){
        int[] coords = {
            ulX, ulY,
            ulX + length, ulY,
            ulX, ulY + width,
            ulX + length, ulY + width
        };

        int[] oldHeight = new int[4];
        boolean upperPlane;
        //Rise
        int rise;
        //Run
        int run;
        
        float slo;

        int lenCut = this.blockDet.getWidDepth();
        int widCut = this.blockDet.getLenDepth();

        int min = Integer.MAX_VALUE;
        int curr;
        
        
        rise = this.unitWidth - widCut * 2;
        run = -this.unitLength - lenCut * 2;
        slo = (float) rise / (float) run;
        
        for(int i = 0; i < coords.length; i += 2){
            
            if(coords[i] <= lenCut)
                curr = (int) this.getEastHeight(coords[i + 1]);
            else if(coords[i] >= this.unitLength - lenCut)
                curr = (int) this.getWestHeight(coords[i + 1]);
            else if(coords[i + 1] <= widCut)
                curr = (int) this.getSouthHeight(coords[i]);
            else if(coords[i + 1] >= this.unitWidth - widCut)
                curr = (int) this.getNorthHeight(coords[i]);
            else{
                upperPlane = coords[i + 1] >= ( slo * (coords[i] + run) );
                if(upperPlane)    
                    curr = (int)( this.getNorthHeight(coords[i]) + this.getWestHeight(coords[i + 1]) ) - unitHeight[2];
                else
                    curr = (int)( this.getSouthHeight(coords[i]) + this.getEastHeight(coords[i + 1]) ) - unitHeight[0];  
            }            
            
            oldHeight[i / 2] = curr;
            
            min = Math.min(min, curr);
        }
        
        if(min < 0)
            min += Math.abs(min);
        
        return new int[]{
            min, 
            Math.max( Math.max(oldHeight[0], oldHeight[1]), Math.max(oldHeight[2], oldHeight[3]) ) - min
        };
    }
    
    public int getUnitLength() {
        return unitLength;
    }

    public void setUnitLength(int unitLength) {
        this.unitLength = unitLength;
    }

    public int getUnitWidth() {
        return unitWidth;
    }

    public void setUnitWidth(int unitWidth) {
        this.unitWidth = unitWidth;
    }

    public int[] getUnitHeight() {
        return unitHeight;
    }

    public void setUnitHeight(int[] unitHeight) {
        this.unitHeight = unitHeight;
    }
    
    public BlockDetail getBlockDet() {
        return blockDet;
    }

    public float getSouthHeight(int x) {
        int depth = this.blockDet.getWidDepth();
        
        if( x <= depth )
            return this.unitHeight[0];
        else if(x >= this.unitLength - depth)
            return this.unitHeight[3];
        else
            return southSlope * ( x + depth ) + unitHeight[0] - 1;
    }

    public float getEastHeight(int x) {
        int depth = this.blockDet.getLenDepth();
        
        if( x <= depth )
            return this.unitHeight[0];
        else if(x >= this.unitWidth - depth)
            return this.unitHeight[1];
        else
            return eastSlope * ( x + depth ) + unitHeight[0];
    } 

    public float getNorthHeight(int x) {
        int depth = this.blockDet.getWidDepth();
        
        if( x <= depth )
            return this.unitHeight[1];
        else if(x >= this.unitLength - depth)
            return this.unitHeight[2];
        else
            return -northSlope * ( x - (this.unitLength - depth * 2) + depth ) + unitHeight[2];
    }
    
    public float getWestHeight(int x) {
        int depth = this.blockDet.getLenDepth();
        
        if( x <= depth )
            return this.unitHeight[3];
        else if(x >= this.unitWidth - depth)
            return this.unitHeight[2];
        else
            return -westSlope * ( x - (this.unitWidth - depth * 2) + depth ) + unitHeight[2];
    }
}
