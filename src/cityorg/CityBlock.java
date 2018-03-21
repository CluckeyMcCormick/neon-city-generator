/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import building.Building;
import building.BuildingFactory;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
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
     A_*---A_L---------B_L---B_*     _* indicates the dedicated corner points:
      |     |           |     |         our height inputs
      |     |           |     |      
      |     |           |     |      _L indicates deviation along the length cut   
     A_W---A_F---------B_F---B_W        (width depth)
      |     |         / |     |      
      |     |       R/  |     |      _W indicates deviation along the width cut
    W |     |      O/   |     | E       (length depth)
    E |     |     S/    |     | A    
    S |     |    I/     |     | S    _F indicates deviation along both cuts
    T |     |   V/      |     | T    
      |     |  I/       |     |      DIVISOR divides the upper and lower planes,                   
      |     | D/        |     |         upper being A_F-B_F-C_F,
      |     | /         |     |         lower being A_F-C_F-D_F.
     D_W---D_F---------C_F---C_W     
      |     |           |     |      All points are the same height as their 
      |     |           |     |      letter fellows - so all A's are the same, 
      |     |           |     |      as are the B's, and so on.
     D_*---D_L---------C_L---C_*
                SOUTH    
                                                            
        We use following formula for slopes: 
    
            r = o + sh + tv
    
            r is the resultant point coordinate ()
            o is the origin point coordinate 
            h is the horizontal vector  
            v is the vertical vector
            s and t are scalars that indicate our distance along those vectors
    
        So we basically have two formulas:
            r = A_F + s(A_F <-> B_F) + t(A_F <-> D_F) (upper plane)
            r = C_F + s(C_F <-> D_F) + t(C_F <-> B_F) (lower plane)
    
    Now, our vectors - both horizontal and vertical - are actually the entirety 
    of the slopes they represent. Thus, our distance scalars - s & t - must be
    represented as percentages of our distance from the origin points. So, at the
    origin point would be 0, at the other point would be 1.
    */
    public static final int POINT_A = 2; //0;
    public static final int POINT_B = 1;
    public static final int POINT_C = 0; //2;
    public static final int POINT_D = 3;
    
    public static final int MIN_FRONT = Building.MIN_UNIT_HEIGHT;
    
    private BlockDetail blockDet;
    
    private int unitLength; 
    private int unitWidth;
    
    private int[] unitHeights;
    private int[] cardinalCuts;

    int westCut;
    int eastCut;
    int southCut;
    int northCut; 
    
    private final Vector3f northSlope;
    private final Vector3f eastSlope;
    private final Vector3f southSlope;
    private final Vector3f westSlope;
    
    private final Vector2f divisorSlope;
    
    private final Vector3f upperOrigin;
    private final Vector3f lowerOrigin;
    
    
    public CityBlock(
            BlockDetail bd, int[] unitHeights, int[] cardinalCuts, 
            int unitLength, int unitWidth
    ) {
        this.blockDet = bd;
        this.unitHeights = unitHeights;
        
        this.unitLength = unitLength;
        this.unitWidth = unitWidth;
        
        this.cardinalCuts = cardinalCuts;
        
        this.westCut = cardinalCuts[Cardinal.WEST.value / 2];
        this.eastCut = cardinalCuts[Cardinal.EAST.value / 2];
        
        this.southCut = cardinalCuts[Cardinal.SOUTH.value / 2];
        this.northCut = cardinalCuts[Cardinal.NORTH.value / 2];
        
        int slopeWidth = unitWidth - ( westCut + eastCut );
        int slopeLength = unitLength - ( southCut + northCut );
        
        //Create our slopes
        eastSlope = new Vector3f(
                0, 
                unitHeights[POINT_B] - unitHeights[POINT_C],  //rise
                -slopeWidth           //run
        ).mult(1 / (float) slopeWidth ); //divide by width to unitize
        southSlope = new Vector3f(
                -slopeLength,            //run
                unitHeights[POINT_D] - unitHeights[POINT_C],  //rise
                0
        ).mult(1 / (float) slopeLength ); //divide by length to unitize
        lowerOrigin = new Vector3f(
                unitLength - eastCut, 
                unitHeights[POINT_C], 
                unitWidth - northCut
        );
        
        westSlope = new Vector3f(
                0,
                unitHeights[POINT_D] - unitHeights[POINT_A],  //rise
                slopeWidth                                    //run
        ).mult(1 / (float) slopeWidth ); //divide by width to unitize
        northSlope = new Vector3f(
                slopeLength,             //run
                unitHeights[POINT_B] - unitHeights[POINT_A],  //rise
                0 
        ).mult(1 / (float) slopeLength ); //divide by length to unitize
        upperOrigin = new Vector3f(
                westCut, 
                unitHeights[POINT_A], 
                southCut
        );
        //                              run           rise
        divisorSlope = new Vector2f( slopeLength, -slopeWidth ).mult(1 / (float) slopeLength ); //unitize
        
        super.setNode( new Node() );
    }
    
    public void generateBuildings(BuildingFactory bf){
        Geometry geom;
        Node nod = super.getNode();
        geom = bf.blockFloor(
            unitHeights, cardinalCuts, unitWidth, unitLength
        );
        
        nod.attachChild(geom);
    }
    
    public float calcHeightAdjust(int ulX, int ulZ, int length, int width){
        int[] coords = {
            ulX, ulZ,
            ulX + length, ulZ,
            ulX, ulZ + width,
            ulX + length, ulZ + width
        };

        float[] oldHeight = new float[4];
        float curr;
        
        for(int i = 0; i < coords.length; i += 2){
            curr = heightGet(coords[i], coords[i + 1]);
            oldHeight[i / 2] = curr;
        }
        
        return Math.min( 
            Math.min(oldHeight[0], oldHeight[1]), 
            Math.min(oldHeight[2], oldHeight[3]) 
        );
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
        return unitHeights;
    }

    public void setUnitHeight(int[] unitHeight) {
        this.unitHeights = unitHeight;
    }
    
    public BlockDetail getBlockDet() {
        return blockDet;
    }
    
    //Get the height along the northern border line
    public float getNorthHeight(int x) {
        if( x <= westCut )
            return this.unitHeights[POINT_A];
        else if(x >= this.unitLength - eastCut)
            return this.unitHeights[POINT_B];
        else
            // r = o + sh
            return upperOrigin.y + this.northSlope.y * (x - westCut);
    }  
    //Get the height along the western border line
    public float getWestHeight(int z) {
        if( z <= northCut)
            return this.unitHeights[POINT_A];
        else if(z >= this.unitWidth - southCut)
            return this.unitHeights[POINT_D];
        else
            // r = o + tv
            return upperOrigin.y + this.westSlope.y * (z - northCut);
    }   
    public float getUpperPlaneHeight(int x, int z){
        // r  = o + sh + tv
        return upperOrigin.y + ((x - westCut) * this.northSlope.y) + ((z - northCut) * this.westSlope.y);
    }
    
    //Gets the height along the southern border line
    public float getSouthHeight(int x) { 
        if( x <= westCut )
            return this.unitHeights[POINT_D];
        else if(x >= this.unitLength - eastCut)
            return this.unitHeights[POINT_C];
        else
            // r  = o + sh
            return lowerOrigin.y + southSlope.y * ((unitLength - (eastCut + westCut)) - (x - westCut));
    }
    //Gets the height along the eastern border line
    public float getEastHeight(int z) {
        if( z <= northCut )
            return this.unitHeights[POINT_B];
        else if(z >= this.unitWidth - southCut)
            return this.unitHeights[POINT_C];
        else 
            // r = o + tv
            return lowerOrigin.y + eastSlope.y * ((unitWidth - (southCut + northCut)) - (z - northCut));
    } 
    public float getLowerPlaneHeight(int x, int z){
        // r  = o + sh + tv
        return lowerOrigin.y 
            + (this.southSlope.y * ((unitLength - (westCut + eastCut)) - (x - westCut)))
            + (this.eastSlope.y * ((unitWidth - (southCut + northCut)) - (z - northCut)));
    }
    
    //To Do: Given X and Y, return Height
    public float heightGet(int x, int z){       
        float height;
        
        //if our x value is in the western flat space
        if(x <= westCut)
            return this.getWestHeight(z);
        //else, if our x value is in the eastern flat space
        else if(x >= this.unitLength - eastCut)
            return this.getEastHeight(z);
        //else, if our y value is in the northern flat space
        else if(z <= northCut)
            return this.getNorthHeight(x);
        //else, if our y value is in the southern flat space
        else if(z >= this.unitWidth - southCut)
            return this.getSouthHeight(x);
        else
            //We're in the upper plane if our current y value is greater than the divider slope
            if( z > lowerOrigin.z + ( (x - westCut) * divisorSlope.y))    
                return this.getLowerPlaneHeight(x, z); 
            else  
                return this.getUpperPlaneHeight(x, z);     
    }
}
