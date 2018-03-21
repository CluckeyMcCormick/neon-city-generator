/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package production;

import building.BuildingDetail;
import cityorg.CityStructure;
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
        Material[] fullMats;
        Material baseMat;
        Material levelMat;    
        
        Texture litTex;
        Texture dimTex;
        Texture flatTex;
        
        float temp;
        
        generatedTex = bd.getLitdim();
        fullMats = new Material[generatedTex.length];
        
        //Generate a full material for every window style
        for(int i = 0; i < fullMats.length; i++){
            litTex = new Texture2D( generatedTex[i][TextureBuilder.LIT_INDEX] );
            litTex.setWrap(Texture.WrapMode.Repeat);

            dimTex = new Texture2D( generatedTex[i][TextureBuilder.DIM_INDEX] );
            dimTex.setWrap(Texture.WrapMode.Repeat);
            
            fullMats[i] = new Material(am, "MatDefs/TripleHeightBuilding.j3md");
            fullMats[i].setTexture("DimMap", dimTex);
            fullMats[i].setTexture("LitMap", litTex);
        
            fullMats[i].setColor("LitColor", colorBook.getLit() );
            fullMats[i].setColor("BaseColor", colorBook.getBase() );
            fullMats[i].setColor("MidColor", colorBook.getMid() );
            fullMats[i].setColor("TopColor", colorBook.getTop() );        
            
            temp = bd.getBaseMidPos() * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
            fullMats[i].setFloat("BaseMidBand", temp);
            temp = bd.getBaseMidHeight() * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
            fullMats[i].setFloat("BaseMidWidth", temp);
            
            temp = bd.getMidTopPos() * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
            fullMats[i].setFloat("MidTopBand", temp); 
            temp = bd.getMidTopHeight() * CityStructure.VIRTUAL_LENGTH_PER_PIXEL; 
            fullMats[i].setFloat("MidTopWidth", temp);

            temp = bd.getBaseColorCutoff() * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
            fullMats[i].setFloat("Cutoff", temp);
        }
        /* Stage 2: Other Materials ~~~~~~~~~~~~~~~ */        
        flatTex = am.loadTexture( new TextureKey("Textures/BlankSizer.png", false) );
        flatTex.setWrap(Texture.WrapMode.Repeat);
            
        levelMat = new Material(am, "MatDefs/TripleHeightBuilding.j3md");
        levelMat.setTexture("DimMap", flatTex);

        levelMat.setColor("LitColor", colorBook.getLit() );
        levelMat.setColor("BaseColor", colorBook.getBase() );
        levelMat.setColor("MidColor", colorBook.getMid() );
        levelMat.setColor("TopColor", colorBook.getTop() );        
            
        temp = bd.getBaseMidPos() * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        levelMat.setFloat("BaseMidBand", temp);
        temp = bd.getBaseMidHeight() * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        levelMat.setFloat("BaseMidWidth", temp);

        temp = bd.getMidTopPos() * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        levelMat.setFloat("MidTopBand", temp); 
        temp = bd.getMidTopHeight() * CityStructure.VIRTUAL_LENGTH_PER_PIXEL; 
        levelMat.setFloat("MidTopWidth", temp);

        temp = bd.getBaseColorCutoff() * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        levelMat.setFloat("Cutoff", temp);
        
        baseMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        baseMat.setTexture("ColorMap", flatTex);
        baseMat.setColor("Color", colorBook.getBase());
        
        
        baseMat.setColor("Color", ColorRGBA.Cyan);
        baseMat.getAdditionalRenderState().setWireframe(true);
        baseMat.getAdditionalRenderState().setLineWidth(1);
       
        return new MaterialBook(fullMats, baseMat, levelMat);
    }
    
}
