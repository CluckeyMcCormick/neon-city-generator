/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import building.BasicBuildingTemplate;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import mesh.CivilizedWarpingQuad;
import prime.MathHelp;
import production.MaterialBook;

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
    
    public static final int MIN_FRONT = BasicBuildingTemplate.MIN_UNIT_HEIGHT;

    protected MaterialBook mat_book;
    protected BlockDetail block_detail;
    
    private int unit_x; 
    private int unit_z;
    
    private int[] unit_heights;
    
    private final Vector3f north_slope;
    private final Vector3f east_slope;
    private final Vector3f south_slope;
    private final Vector3f west_slope;
    
    private final Vector2f divisor_slope;
    
    private final Vector3f upper_origin;
    private final Vector3f lower_origin;
    
    public CityBlock(
        MaterialBook mat_book, BlockDetail block_detail,  
        int unit_x, int unit_z, int[] unit_heights
    ) {
        this.mat_book = mat_book;
        this.block_detail = block_detail;
        this.unit_heights = unit_heights;
        
        this.unit_x = unit_x;
        this.unit_z = unit_z;
        
        int slope_z = unit_z; 
        int slope_x = unit_x;
        
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
                unit_x, 
                unit_heights[POINT_C], 
                unit_z
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
                0, 
                unit_heights[POINT_A], 
                0
        );
        //                              run           rise
        divisor_slope = new Vector2f( slope_x, -slope_z ).mult(1 / (float) slope_x ); //unitize
        
        super.setNode( new Node() );
    }
    
    public void generateBuildings(){
        Geometry geom;
        Node nod = super.getNode();
        geom = new Geometry("block_floor", 
            new CivilizedWarpingQuad(
                unit_x, unit_z, unit_heights
            ) 
        );
        geom.setMaterial( mat_book.getBaseMat() );
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
    
    public int getUnitX() {
        return unit_x;
    }

    public void setUnitX(int unitLength) {
        this.unit_x = unitLength;
    }

    public int getUnitZ() {
        return unit_z;
    }

    public void setUnitZ(int unitWidth) {
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
        // r = o + sh
        return upper_origin.y + this.north_slope.y * x;
    }  
    //Get the height along the western border line
    public float getWestHeight(int z) {
        // r = o + tv
        return upper_origin.y + this.west_slope.y * z;
    }   
    public float getUpperPlaneHeight(int x, int z){
        // r  = o + sh + tv
        return upper_origin.y + (x * this.north_slope.y) + (z * this.west_slope.y);
    }
    
    //Gets the height along the southern border line
    public float getSouthHeight(int x) {
        return lower_origin.y + south_slope.y * (unit_x - x );
    }
    //Gets the height along the eastern border line
    public float getEastHeight(int z) {
        // r = o + tv
        return lower_origin.y + east_slope.y * (unit_z - z);
    } 
    public float getLowerPlaneHeight(int x, int z){
        // r  = o + sh + tv
        return lower_origin.y 
            + (this.south_slope.y * (unit_x - x))
            + (this.east_slope.y * (unit_z - z));
    }
    
    public float heightGet(int x, int z){
        
        //We're in the upper plane if our current y value is greater than the divider slope
        if( z > lower_origin.z + ( x * divisor_slope.y) )    
            return this.getLowerPlaneHeight(x, z); 
        else  
            return this.getUpperPlaneHeight(x, z);     
    }
}
