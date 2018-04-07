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
    public static final int POINT_A = 0; //2;
    public static final int POINT_B = 3;
    public static final int POINT_C = 2; //0;
    public static final int POINT_D = 1;
    
    public static final int MIN_FRONT = Building.MIN_UNIT_HEIGHT;
    
    private BlockDetail block_detail;
    
    private int unit_x; 
    private int unit_z;
    
    private int[] unit_heights;
    private int[] cardinal_cuts;

    private int west_cut;
    private int east_cut;
    private int south_cut;
    private int north_cut; 
    
    private final Vector3f north_slope;
    private final Vector3f east_slope;
    private final Vector3f south_slope;
    private final Vector3f west_slope;
    
    private final Vector2f divisor_slope;
    
    private final Vector3f upper_origin;
    private final Vector3f lower_origin;
    
    
    public CityBlock(
        BlockDetail block_detail, int[] unit_heights, int[] cardinal_cuts, 
        int unit_x, int unit_z
    ) {
        this.block_detail = block_detail;
        this.unit_heights = unit_heights;
        
        this.unit_x = unit_x;
        this.unit_z = unit_z;
        
        this.cardinal_cuts = cardinal_cuts;
        
        this.west_cut = cardinal_cuts[Cardinal.WEST.value / 2];
        this.east_cut = cardinal_cuts[Cardinal.EAST.value / 2];
        
        this.south_cut = cardinal_cuts[Cardinal.SOUTH.value / 2];
        this.north_cut = cardinal_cuts[Cardinal.NORTH.value / 2];
        
        int slope_z = unit_z - ( west_cut + east_cut );
        int slope_x = unit_x - ( south_cut + north_cut );
        
        //Create our slopes
        east_slope = new Vector3f(
                0, 
                unit_heights[POINT_B] - unit_heights[POINT_C],  //rise
                -slope_z           //run
        ).mult(1 / (float) slope_z ); //divide by width to unitize
        south_slope = new Vector3f(
                -slope_x,            //run
                unit_heights[POINT_D] - unit_heights[POINT_C],  //rise
                0
        ).mult(1 / (float) slope_x ); //divide by length to unitize
        lower_origin = new Vector3f(
                unit_x - east_cut, 
                unit_heights[POINT_C], 
                unit_z - north_cut
        );
        
        west_slope = new Vector3f(
                0,
                unit_heights[POINT_D] - unit_heights[POINT_A],  //rise
                slope_z                                    //run
        ).mult(1 / (float) slope_z ); //divide by width to unitize
        north_slope = new Vector3f(
                slope_x,             //run
                unit_heights[POINT_B] - unit_heights[POINT_A],  //rise
                0 
        ).mult(1 / (float) slope_x ); //divide by length to unitize
        upper_origin = new Vector3f(
                west_cut, 
                unit_heights[POINT_A], 
                south_cut
        );
        //                              run           rise
        divisor_slope = new Vector2f( slope_x, -slope_z ).mult(1 / (float) slope_x ); //unitize
        
        super.setNode( new Node() );
    }
    
    public void generateBuildings(BuildingFactory bf){
        Geometry geom;
        Node nod = super.getNode();
        geom = bf.blockFloor(unit_heights, cardinal_cuts, unit_x, unit_z);
        
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
        return unit_x;
    }

    public void setUnitLength(int unitLength) {
        this.unit_x = unitLength;
    }

    public int getUnitWidth() {
        return unit_z;
    }

    public void setUnitWidth(int unitWidth) {
        this.unit_z = unitWidth;
    }

    public int[] getUnitHeight() {
        return unit_heights;
    }

    public void setUnitHeight(int[] unitHeight) {
        this.unit_heights = unitHeight;
    }
    
    public BlockDetail getBlockDet() {
        return block_detail;
    }
    
    //Get the height along the northern border line
    public float getNorthHeight(int x) {
        if( x <= west_cut )
            return this.unit_heights[POINT_A];
        else if(x >= this.unit_x - east_cut)
            return this.unit_heights[POINT_B];
        else
            // r = o + sh
            return upper_origin.y + this.north_slope.y * (x - west_cut);
    }  
    //Get the height along the western border line
    public float getWestHeight(int z) {
        if( z <= north_cut)
            return this.unit_heights[POINT_A];
        else if(z >= this.unit_z - south_cut)
            return this.unit_heights[POINT_D];
        else
            // r = o + tv
            return upper_origin.y + this.west_slope.y * (z - north_cut);
    }   
    public float getUpperPlaneHeight(int x, int z){
        // r  = o + sh + tv
        return upper_origin.y + ((x - west_cut) * this.north_slope.y) + ((z - north_cut) * this.west_slope.y);
    }
    
    //Gets the height along the southern border line
    public float getSouthHeight(int x) { 
        if( x <= west_cut )
            return this.unit_heights[POINT_D];
        else if(x >= this.unit_x - east_cut)
            return this.unit_heights[POINT_C];
        else
            // r  = o + sh
            return lower_origin.y + south_slope.y * ((unit_x - (east_cut + west_cut)) - (x - west_cut));
    }
    //Gets the height along the eastern border line
    public float getEastHeight(int z) {
        if( z <= north_cut )
            return this.unit_heights[POINT_B];
        else if(z >= this.unit_z - south_cut)
            return this.unit_heights[POINT_C];
        else 
            // r = o + tv
            return lower_origin.y + east_slope.y * ((unit_z - (south_cut + north_cut)) - (z - north_cut));
    } 
    public float getLowerPlaneHeight(int x, int z){
        // r  = o + sh + tv
        return lower_origin.y 
            + (this.south_slope.y * ((unit_x - (west_cut + east_cut)) - (x - west_cut)))
            + (this.east_slope.y * ((unit_z - (south_cut + north_cut)) - (z - north_cut)));
    }
    
    //To Do: Given X and Y, return Height
    public float heightGet(int x, int z){       
        float height;
        
        //if our x value is in the western flat space
        if(x <= west_cut)
            return this.getWestHeight(z);
        //else, if our x value is in the eastern flat space
        else if(x >= this.unit_x - east_cut)
            return this.getEastHeight(z);
        //else, if our y value is in the northern flat space
        else if(z <= north_cut)
            return this.getNorthHeight(x);
        //else, if our y value is in the southern flat space
        else if(z >= this.unit_z - south_cut)
            return this.getSouthHeight(x);
        else
            //We're in the upper plane if our current y value is greater than the divider slope
            if( z > lower_origin.z + ( (x - west_cut) * divisor_slope.y))    
                return this.getLowerPlaneHeight(x, z); 
            else  
                return this.getUpperPlaneHeight(x, z);     
    }
}
