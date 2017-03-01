/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import building.BuildingDetail;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

/**
 *
 * @author root
 */
public class MaterialBuilder {
    
    public static final int ALLEY_INDEX = 0;
    public static final int SMALL_STREET_INDEX = 1;
    public static final int MEDIUM_STREET_INDEX = 2;
    public static final int LARGE_STREET_INDEX = 3;
    
    public static MaterialBook buildMatBook(ColorBook colorBook, BuildingDetail bd, AssetManager am){
        
        /* Stage 1: Lit Materials ~~~~~~~~~~~~~~~ */
        Image[][] generatedTex;
        Material[][] litMats;
        ColorRGBA[] litColors;
        Texture litTex;
        
        generatedTex = bd.getLitdim();
        litColors = colorBook.getBuildingLightAll();
        litMats = new Material[litColors.length][generatedTex.length];
        
        for(int i = 0; i < litMats.length; i++)
            for(int j = 0; j < litMats[i].length; j++){
                litTex = new Texture2D( generatedTex[j][TextureBuilder.LIT_INDEX] );
                litTex.setWrap(Texture.WrapMode.Repeat);
                litMats[i][j] = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
                litMats[i][j].setTransparent(true);
                litMats[i][j].setTexture("ColorMap", litTex);     
                litMats[i][j].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                litMats[i][j].setColor("Color", litColors[i]);
            }
            
        /* Stage 2: Other Materials ~~~~~~~~~~~~~~~ */
        Material[][] otherMats;
        Texture dimTex;
        Texture baseTex;
        Texture fadeTex;
        Texture transTex;
        Texture flipTransTex;
        
        otherMats = new Material[MaterialBook.FLIP_TRANS_INDEX + 1][]; 
        
        /* Stage 2a: Base Material ~~~~~~~~~~~~~~~ */
        ColorRGBA baseColor = colorBook.getBuildingBase();
        otherMats[MaterialBook.BASE_INDEX] = new Material[1];
        otherMats[MaterialBook.BASE_INDEX][0] = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        baseTex = am.loadTexture( new TextureKey("Textures/BlankSizer.png", false) );
        baseTex.setWrap(Texture.WrapMode.Repeat);
        otherMats[MaterialBook.BASE_INDEX][0].setTransparent(true);
        otherMats[MaterialBook.BASE_INDEX][0].setTexture("ColorMap", baseTex);
        otherMats[MaterialBook.BASE_INDEX][0].setColor("Color", baseColor);
        
        /* Stage 2b: Dim Materials ~~~~~~~~~~~~~~~ */
        otherMats[MaterialBook.DIM_INDEX] = new Material[generatedTex.length];
        for(int i = 0; i < otherMats[MaterialBook.DIM_INDEX].length; i++){
            otherMats[MaterialBook.DIM_INDEX][i] = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
            dimTex = new Texture2D( generatedTex[i][TextureBuilder.DIM_INDEX] );
            dimTex.setWrap(Texture.WrapMode.Repeat);
            otherMats[MaterialBook.DIM_INDEX][i].setTransparent(true);
            otherMats[MaterialBook.DIM_INDEX][i].setTexture("ColorMap", dimTex);
            otherMats[MaterialBook.DIM_INDEX][i].setColor("Color", baseColor);
        }
        
        /* Stage 2c: Fade Material ~~~~~~~~~~~~~~~ */
        ColorRGBA[] fadeColor = colorBook.getStreetLightAll();
        fadeTex = am.loadTexture( new TextureKey("Textures/BaseTrans.png", false) );
        fadeTex.setWrap(Texture.WrapMode.Repeat);
        otherMats[MaterialBook.FADE_INDEX] = new Material[fadeColor.length];
        for(int i = 0; i < otherMats[MaterialBook.FADE_INDEX].length; i++){
            otherMats[MaterialBook.FADE_INDEX][i] = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
            otherMats[MaterialBook.FADE_INDEX][i].setTransparent(true);
            otherMats[MaterialBook.FADE_INDEX][i].setTexture("ColorMap", fadeTex);
            otherMats[MaterialBook.FADE_INDEX][i].setColor("Color", fadeColor[i]);
            otherMats[MaterialBook.FADE_INDEX][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        
        /* Stage 2d: Trans Material ~~~~~~~~~~~~~~~ */
        transTex = am.loadTexture( new TextureKey("Textures/LightTrans1.png", false) );
        flipTransTex = am.loadTexture( new TextureKey("Textures/LightTrans2.png", false) );
        transTex.setWrap(Texture.WrapMode.Repeat);
        flipTransTex.setWrap(Texture.WrapMode.Repeat);
        otherMats[MaterialBook.TRANS_INDEX] = new Material[fadeColor.length];
        otherMats[MaterialBook.FLIP_TRANS_INDEX] = new Material[fadeColor.length];
        for(int i = 0; i < otherMats[MaterialBook.FADE_INDEX].length; i++){
            otherMats[MaterialBook.TRANS_INDEX][i] = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
            otherMats[MaterialBook.TRANS_INDEX][i].setTransparent(true);
            otherMats[MaterialBook.TRANS_INDEX][i].setTexture("ColorMap", transTex);
            otherMats[MaterialBook.TRANS_INDEX][i].setColor("Color", fadeColor[i]);
            otherMats[MaterialBook.TRANS_INDEX][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            
            otherMats[MaterialBook.FLIP_TRANS_INDEX][i] = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
            otherMats[MaterialBook.FLIP_TRANS_INDEX][i].setTransparent(true);
            otherMats[MaterialBook.FLIP_TRANS_INDEX][i].setTexture("ColorMap", flipTransTex);
            otherMats[MaterialBook.FLIP_TRANS_INDEX][i].setColor("Color", fadeColor[i]);
            otherMats[MaterialBook.FLIP_TRANS_INDEX][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        
        return new MaterialBook(litMats, otherMats);
    }

    public static Material[] roadMats( ColorRGBA[] colors, AssetManager am ){
        Material[] mats;
        
        Texture smallTex;
        Texture mediumTex; 
        Texture largeTex;
        
        mats = new Material[4]; 
        
        mats[ALLEY_INDEX] = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        mats[SMALL_STREET_INDEX] = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        mats[MEDIUM_STREET_INDEX] = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        mats[LARGE_STREET_INDEX] = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        
        mats[ALLEY_INDEX].setColor("Color", colors[ALLEY_INDEX]);
        mats[SMALL_STREET_INDEX].setColor("Color", colors[SMALL_STREET_INDEX]);
        mats[MEDIUM_STREET_INDEX].setColor("Color", colors[MEDIUM_STREET_INDEX]); 
        mats[LARGE_STREET_INDEX].setColor("Color", colors[LARGE_STREET_INDEX]);
        
        smallTex = am.loadTexture( new TextureKey("Textures/StreetSmall32.png", false) );
        smallTex.setWrap(Texture.WrapMode.Repeat);
        mats[SMALL_STREET_INDEX].setTexture("ColorMap", smallTex); 
        
        mediumTex = am.loadTexture( new TextureKey("Textures/StreetMedium64.png", false) );
        mediumTex.setWrap(Texture.WrapMode.Repeat);
        mats[MEDIUM_STREET_INDEX].setTexture("ColorMap", mediumTex); 
        
        largeTex = am.loadTexture( new TextureKey("Textures/StreetLarge96.png", false) );
        largeTex.setWrap(Texture.WrapMode.Repeat);
        mats[LARGE_STREET_INDEX].setTexture("ColorMap", largeTex); 
        
        return mats;    
    }
    
}
