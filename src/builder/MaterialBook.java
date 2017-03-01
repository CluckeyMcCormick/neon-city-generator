/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import building.WindowStyle;
import com.jme3.material.Material;
import prime.RandomSingleton;

/**
 *
 * @author frick
 */
public class MaterialBook {

    protected static final int BASE_INDEX = 0;
    protected static final int DIM_INDEX = 1;
    protected static final int FADE_INDEX = 2;
    protected static final int TRANS_INDEX = 3;
    protected static final int FLIP_TRANS_INDEX = 4;
    
    private Material[][] litMat; 
    private Material[][] otherMat;
    
    public MaterialBook(Material[][] litMat, Material[][] otherMat){
        this.litMat = litMat;
        this.otherMat = otherMat;
    }
    
    /* Lit Material getters (with some random) */
    public Material getLitMat(WindowStyle ws){
        return this.getLitMat( RandomSingleton.getInstance().nextInt(litMat.length), ws);
    }
    
    public Material getLitMat(int colorIndex){
        return this.getLitMat(colorIndex, WindowStyle.randomStyle() );
    }
    
    public Material getLitMat(int colorIndex, WindowStyle ws){
        return this.litMat[colorIndex][ WindowStyle.indexOf(ws) ];
    }    
    
    /* Base Material getter */
    public Material getBaseMat(){
        return this.otherMat[BASE_INDEX][0];
    }
    
    /* Dim Material getter */
    public Material getDimMat(WindowStyle ws){
        return this.otherMat[DIM_INDEX][WindowStyle.indexOf(ws)];
    }
    
    /* Fade Material getter*/
    public Material getFadeMat(int colorIndex){
        return this.otherMat[FADE_INDEX][colorIndex];
    }
    
    /* Trans-fade Material getter*/
    public Material getTransMat(int colorIndex){
        return this.otherMat[TRANS_INDEX][colorIndex];
    }
    
    /* Trans-fade Material getter*/
    public Material getFlipTransMat(int colorIndex){
        return this.otherMat[FLIP_TRANS_INDEX][colorIndex];
    }
}
