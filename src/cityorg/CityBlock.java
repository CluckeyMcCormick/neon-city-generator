/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import building.Building;
import building.BuildingFactory;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import prime.MathHelp;

/**
 *
 * @author root
 */
public abstract class CityBlock extends CityStructure {

    /*
     Here is a diagram of a city block:
    
                NORTH
     B_*---B_L---------C_L---C_*     _* indicates the dedicated corner points:
      |     |           |     |         our height inputs
      |     |           |     |      
      |     |           |     |      _L indicates deviation along the length cut   
     B_W---B_F---------C_F---C_W        (width depth)
      |     |         / |     |      
      |     |       R/  |     |      _W indicates deviation along the width cut
    W |     |      O/   |     | E       (length depth)
    E |     |     S/    |     | A    
    S |     |    I/     |     | S    _F indicates deviation along both cuts
    T |     |   V/      |     | T    
      |     |  I/       |     |      DIVISOR divides the upper and lower planes,                   
      |     | D/        |     |         upper being A_F-B_F-C_F,
      |     | /         |     |         lower being A_F-C_F-D_F.
     A_W---A_F---------D_F---D_W     
      |     |           |     |      All points are the same height as their 
      |     |           |     |      letter fellows - so all A's are the same, 
      |     |           |     |      as are the B's, and so on.
     A_*---A_L---------D_L---D_*
                SOUTH    
    
        This means our slopes occur along:
        (A_W <-> B_W) & (A_F <-> B_F) - the West Slope      Point A_* should be 
        (D_W <-> C_W) & (D_F <-> C_F) - the East Slope      our 0, 0
        (A_L <-> D_L) & (A_F <-> D_F) - the South Slope     
        (B_L <-> C_L) & (B_F <-> C_F) - the North Slope     Point D_* should be
        (A_F <-> C_F) - the DIVISOR                         our MAX_X, MAX_Y 
    */
    
    public final int MIN_FRONT = Building.MIN_UNIT_HEIGHT;
    
    private BlockDetail blockDet;
    
    private int unitLength; 
    private int unitWidth;

    //the cut that occurs into the length
    int lenCut = this.lenCut;
    //the cut that occurs into the width
    int widCut = this.widCut;
    
    private int[] unitHeight;
    
    private final float northSlope;
    private final float eastSlope;
    private final float southSlope;
    private final float westSlope;
    
    private final int POINT_A = 0;
    private final int POINT_B = 1;
    private final int POINT_C = 2;
    private final int POINT_D = 3;
    
    public CityBlock(
            BlockDetail bd, int[] unitHeight, 
            int unitLength, int unitWidth, int lengthCut, int widthCut 
    ) {
        this.blockDet = bd;
        this.unitHeight = unitHeight;
        
        this.unitLength = unitLength;
        this.unitWidth = unitWidth;
        
        this.lenCut = lengthCut;
        this.widCut = widthCut;
        
        /*
        if(this.widCut < Building.MIN_UNIT_HEIGHT)
            this.widCut = Building.MIN_UNIT_HEIGHT;
            
        if(this.lenCut < Building.MIN_UNIT_HEIGHT)
            this.lenCut = Building.MIN_UNIT_HEIGHT;
        */
        eastSlope = MathHelp.calculateSlope(
                unitHeight[POINT_B] - unitHeight[POINT_A], 
                unitWidth - (this.widCut * 2) );
        southSlope = MathHelp.calculateSlope(
                unitHeight[POINT_D] - unitHeight[POINT_A], 
                unitLength - (this.lenCut * 2) );
        
        westSlope = MathHelp.calculateSlope(
                unitHeight[POINT_D] - unitHeight[POINT_C], 
                unitWidth - (this.widCut * 2) );
        northSlope = MathHelp.calculateSlope(
                unitHeight[POINT_B] - unitHeight[POINT_C],
                unitLength - (this.lenCut * 2) );
        
        System.out.println( southSlope + " " + northSlope);
        
        super.setNode( new Node() );
    }
    
    public void generateBuildings(BuildingFactory bf){
        Geometry[] geoms;
        Node nod = super.getNode();
        geoms = bf.blockFloor( 
            unitWidth, unitLength, unitHeight, this.lenCut, this.widCut
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

        //the cut that occurs into the length
        int lenCut = this.lenCut;
        //the cut that occurs into the width
        int widCut = this.widCut;

        int min = Integer.MAX_VALUE;
        int curr;
        
        
        rise = this.unitWidth - widCut * 2;
        run = -this.unitLength - lenCut * 2;
        slo = (float) rise / (float) run;
        
        for(int i = 0; i < coords.length; i += 2){
            
            //if our x value is in the eastern flat space
            if(coords[i] <= lenCut)
                curr = (int) this.getEastHeight(coords[i + 1]);
            //else, if our x value is in the western flat space
            else if(coords[i] >= this.unitLength - lenCut)
                curr = (int) this.getWestHeight(coords[i + 1]);
            //else, if our y value is in the southern flat space
            else if(coords[i + 1] <= widCut)
                curr = (int) this.getSouthHeight(coords[i]);
            //else, if our y value is in the northern flat space
            else if(coords[i + 1] >= this.unitWidth - widCut)
                curr = (int) this.getNorthHeight(coords[i]);
            else{
                //We're in the upper plane if our current y value is greater than the divider slope
                upperPlane = coords[i + 1] >= ( slo * (coords[i] + run) );
                if(upperPlane)    
                    curr = (int)( this.getNorthHeight(coords[i]) + this.getWestHeight(coords[i + 1]) ) - unitHeight[POINT_C];
                else
                    curr = (int)( this.getSouthHeight(coords[i]) + this.getEastHeight(coords[i + 1]) ) - unitHeight[POINT_A];  
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

    //Gets the height along the southern border line
    public float getSouthHeight(int x) {
        int depth = this.lenCut;
        
        if( x <= depth )
            return this.unitHeight[0];
        else if(x >= this.unitLength - depth)
            return this.unitHeight[3];
        else
            return southSlope * ( x + depth ) + unitHeight[0] - 1;
    }

    //Gets the height along the eastern border line
    public float getEastHeight(int x) {
        int depth = this.widCut;
        
        if( x <= depth )
            return this.unitHeight[0];
        else if(x >= this.unitWidth - depth)
            return this.unitHeight[1];
        else
            return eastSlope * ( x + depth ) + unitHeight[0];
    } 

    //Get the height along the northern border line
    public float getNorthHeight(int x) {
        int depth = this.lenCut;
        
        if( x <= depth )
            return this.unitHeight[1];
        else if(x >= this.unitLength - depth)
            return this.unitHeight[2];
        else
            return -northSlope * ( x - (this.unitLength - depth * 2) + depth ) + unitHeight[2];
    }
    
    //Get the height along the western border line
    public float getWestHeight(int x) {
        int depth = this.widCut;
        
        if( x <= depth )
            return this.unitHeight[3];
        else if(x >= this.unitWidth - depth)
            return this.unitHeight[2];
        else
            return -westSlope * ( x - (this.unitWidth - depth * 2) + depth ) + unitHeight[2];
    }
    
    //To Do: Given X and Y, return Height
}
