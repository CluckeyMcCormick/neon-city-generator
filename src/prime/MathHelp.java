/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prime;

/**
 *
 * @author frick
 */
public class MathHelp {   
    public static float[] makePlaneSlope(int oriX, int oriY, int bX, int bY, int cX, int cY){
        return makePlaneSlope(oriX, oriY, bX, bY, oriX, oriY, cX, cY);
    }
    
    public static float[] makePlaneSlope(int aX, int aY, int bX, int bY, int cX, int cY, int dX, int dY){
        return new float[]{
            calculateSlope(aX, aY, bX, bY),
            calculateSlope(cX, cY, dX, dY)
        };
    }
    
    public static float calculateSlope(int rise, int run){
        return (float) rise / (float) run;
    }
    
    public static float calculateSlope (int oriX, int oriY, int bX, int bY){
        return calculateSlope(bY - oriY, bX - oriX);
    }
    
}
