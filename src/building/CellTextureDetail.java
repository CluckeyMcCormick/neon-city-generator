/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import com.jme3.texture.Image;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.image.BufferedImage;
import production.TextureBuilder;
import cityorg.CityStructure;

/**
 *
 * @author root
 */
public class CellTextureDetail {
 
    //A hand calculated value for texture length-per-cell (16x16)
    public final static float TEXTURE_DIM16 = .015625f;
    
    //A hand calculated value for virtual length-per-cell (16x16)
    public final static float REAL_DIM16  = .125f;
    
    public final static int BLANK_LEFT_INDEX = 0;
    public final static int BLANK_RIGHT_INDEX = 1; 
    public final static int BLANK_BOTTOM_INDEX = 2; 
    public final static int BLANK_TOP_INDEX = 3; 
    
    //The texture images
    private Image[][] litdim;
    
    //The width and height of these cells
    private int cellPixWidth;
    private int cellPixHeight;
    
    //The pixel position and width of the base color to mid color gradient band
    private int baseMidPos;
    private int baseMidHeight;
    
    //The pixel position and width of the mid color to top color gradient band
    private int midTopPos;
    private int midTopHeight;
    
    //The pixel position of the base color cutoff
    private int baseColorCutoff;
    
    //Specifies the blank areas in the window (the space between the windows)
    private int blanks[];
    
    //Calculated texture dimensions, per cell
    private float texWidthPerCell;
    private float texHeightPerCell;
    
    //Calculated virtual dimensions, per cell
    private float virtWidthPerCell;
    private float virtHeightPerCell;
    
    /*
        @input cellPixWidth - the pixel width of the window "cells"
        @input cellPixHeight - the pixel height of the window "cells"
        @input baseMidPos - the pixel position of the base color to mid color 
            gradient band
        @input baseMidHeight - the pixel size of the mid color to top color 
            gradient band
        @input baseColorCutoff - the pixel position of the base color cutoff
        @input midTopPos - the position of the mid color to top color 
            gradient band
        @input midTopHeight - the size of the mid color to top color 
            gradient band
    */
    public CellTextureDetail(
        int cellPixWidth, int cellPixHeight,
        int baseMidPos, int baseMidHeight, int baseColorCutoff,
        int midTopPos, int midTopHeight, int blanks[]
    ){       
        this.cellPixWidth = cellPixWidth;
        this.cellPixHeight = cellPixHeight;
        
        this.baseMidPos = baseMidPos;
        this.baseMidHeight = baseMidHeight;
        
        this.midTopPos = midTopPos;
        this.midTopHeight = midTopHeight;
        
        this.baseColorCutoff = baseColorCutoff;
        
        this.texWidthPerCell = this.cellPixWidth * CityStructure.TEXTURE_LENGTH_PER_PIXEL;
        this.texHeightPerCell = this.cellPixHeight * CityStructure.TEXTURE_LENGTH_PER_PIXEL;
        
        this.virtWidthPerCell = this.cellPixWidth * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        this.virtHeightPerCell = this.cellPixHeight * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;

        this.blanks = blanks;   
        
        if(cellPixWidth - blanks[BLANK_LEFT_INDEX] - blanks[BLANK_RIGHT_INDEX] <= 0){
            throw new IllegalArgumentException("Blank borders too thick on X axis!");
        }
        if(cellPixHeight - blanks[BLANK_BOTTOM_INDEX] - blanks[BLANK_TOP_INDEX] <= 0){
            throw new IllegalArgumentException("Blank borders too thick on Y axis!");
        }  
        
        this.textureRender();
    }

    private void textureRender(){
        AWTLoader awt;
        BufferedImage[][] bis;
        
        awt = new AWTLoader();
        bis = TextureBuilder.drawOffices(this);
        
        this.litdim = new Image[bis.length][2];
        
        for(int i = 0; i < this.litdim.length; i++){
            this.litdim[i][TextureBuilder.LIT_INDEX] = awt.load(bis[i][TextureBuilder.LIT_INDEX], false);
            this.litdim[i][TextureBuilder.DIM_INDEX] = awt.load(bis[i][TextureBuilder.DIM_INDEX], false);
        }
        
    }
    
    public boolean inBlank(int x, int y){
        return (x < blanks[BLANK_LEFT_INDEX] || y < blanks[BLANK_BOTTOM_INDEX]) || 
        ( x > cellPixWidth - 1 -blanks[BLANK_RIGHT_INDEX] || y > cellPixHeight - 1 -blanks[BLANK_TOP_INDEX]);
    }
    
    public int unitsToCells(int unitLength) {
        return (unitLength * Building.GOLDEN_PIXEL_COUNT) / this.cellPixWidth;
    }
    
    public int cellsToUnits(int cells) {
        return (cells * this.cellPixWidth) / Building.GOLDEN_PIXEL_COUNT ;
    }
    
    public int unitsToFloors(int unitHeight){
        return (unitHeight * Building.GOLDEN_PIXEL_COUNT) / this.cellPixHeight;
    }
    
    public int floorsToUnits(int floors){
        return (floors * this.cellPixHeight) / Building.GOLDEN_PIXEL_COUNT;
    }

    public Image[][] getLitdim() {
        return litdim;
    }

    public int getCellPixWidth() {
        return cellPixWidth;
    }

    public int getCellPixHeight() {
        return cellPixHeight;
    }
    
    public int getBaseMidPos() {
        return baseMidPos;
    }

    public int getBaseMidHeight() {
        return baseMidHeight;
    }

    public int getMidTopPos() {
        return midTopPos;
    }

    public int getMidTopHeight() {
        return midTopHeight;
    }

    public int getBaseColorCutoff() {
        return baseColorCutoff;
    }
    
    public int[] getBlanks() {
        return blanks;
    }

    public float getTexWidthPerCell() {
        return texWidthPerCell;
    }

    public float getTexHeightPerCell() {
        return texHeightPerCell;
    }

    public float getVirtWidthPerCell() {
        return virtWidthPerCell;
    }

    public float getVirtHeightPerCell() {
        return virtHeightPerCell;
    }    
}