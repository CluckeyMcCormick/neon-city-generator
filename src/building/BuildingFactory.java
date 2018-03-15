/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import production.ColorBook;
import production.MaterialBook;
import building.type.FloorCellBuilding;
import mesh.TextureScalingQuad;

import com.jme3.material.Material;
import com.jme3.math.FastMath;

import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import production.MaterialBuilder;
import production.TextureBuilder;

import cityorg.CityStructure;
import com.jme3.asset.AssetManager;
import prime.RandomSingleton;

import mesh.CivilizedBase;
import mesh.ClosedBox;

/**
 *
 * @author root
 */
public class BuildingFactory {
   
    public static final float MIN_VIRTUAL_HEIGHT = 
        (Building.MIN_UNIT_HEIGHT * Building.GOLDEN_PIXEL_COUNT) * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
    
    public static final int SOUTH_INDEX = 0;
    public static final int EAST_INDEX = 1;
    public static final int NORTH_INDEX = 2;
    public static final int WEST_INDEX = 3;
    
    private MaterialBook matBook;
    private ColorBook coBook;
    private BuildingDetail bd;
    
    public BuildingFactory(ColorBook colo, BuildingDetail bd, AssetManager am){    

        this.coBook = colo;
        this.matBook = MaterialBuilder.buildMatBook(coBook, bd, am);
        
        this.bd = bd;
    }
    
    public Geometry blockFloor( int unitWidth, int unitLength, int[] unitHeight, int widDepth, int lenDepth){
        Geometry floor;
        Material baseMat;
        
        float virtWidth;
        float virtLength;
        
        baseMat = this.matBook.getBaseMat();
        
        virtWidth = (unitWidth * CityStructure.GOLDEN_PIXEL_COUNT ) * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        virtLength = (unitLength * CityStructure.GOLDEN_PIXEL_COUNT ) * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        
        floor = new Geometry("block_floor", 
            new CivilizedBase(
                unitWidth, widDepth, unitLength, lenDepth, unitHeight
            ) 
        );
        
        floor.setMaterial(baseMat);
        floor.setLocalRotation( 
            floor.getLocalRotation().fromAngles(0, FastMath.HALF_PI, 0) 
        );
        floor.setLocalTranslation(0, 0, virtWidth);     
        
        return floor;
    }
    
    //In the actual program, we'll be creating buildings to fit gaps
    //So we'll know the width - the number of units - but not the height.
    public FloorCellBuilding randomFCB(int unitWidth, int unitHeight){
        return this.randomFCB(unitWidth, unitWidth, unitHeight, 0);
    }
    
    public FloorCellBuilding randomFCB(int unitLength, int unitWidth, int unitHeight, int heightAdjust){
        FloorCellBuilding fcb;
        
        if( unitHeight < Building.MIN_UNIT_HEIGHT )
           unitHeight = Building.MIN_UNIT_HEIGHT;  
        
        fcb = new FloorCellBuilding(unitLength, unitWidth, unitHeight, this.bd);
        
        this.buildFCB(fcb, heightAdjust);
    
        return fcb;
    }
    
    private void buildFCB(FloorCellBuilding bb, int heightAdjust){

        Material fullMat = this.matBook.getFullMat(WindowStyle.STANDARD);
        
        final float HEIGHT_ADJUST = heightAdjust * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        
        Node node;
        
        ClosedBox fullBox;
        Geometry fullGeom;
        Vector2f[] texCoords;

        node = new Node();
              
        texCoords = this.getRandomTextureCoords(bb);
        
        //Now the dim box
        fullBox = new ClosedBox(
            bb.virtualWidth(),
            bb.virtualLength(), 
            bb.virtualHeight(), 
            texCoords
        );
        
        fullGeom = new Geometry("building_full", fullBox);
        fullGeom.setMaterial(fullMat);
        //fullGeom.setLocalTranslation(0, HEIGHT_ADJUST, 0);
        node.attachChild(fullGeom);
        
        bb.setNode(node);    
    }
    
    public Vector2f[] getRandomTextureCoords(FloorCellBuilding bb){
        Vector2f[] texCoords;
        RandomSingleton rand = RandomSingleton.getInstance();
        
        int totalFloors;
        int totalCells;
        
        float maxX;
        float maxY;
        
        float shiftX;
        float shiftY;
        
        texCoords = new Vector2f[16];       
      
        totalFloors = TextureBuilder.TEXTURE_Y / bd.getCellPixHeight();
        if(TextureBuilder.TEXTURE_Y % bd.getCellPixHeight() != 0)
            totalFloors = totalFloors - 1;
        
        totalCells = TextureBuilder.TEXTURE_X / bd.getCellPixWidth();
        if(TextureBuilder.TEXTURE_X % bd.getCellPixWidth() != 0)
            totalCells = totalCells - 1;
        
        //Back, right, front, left
        for(int i = 0; i < 4; i++){
            shiftX = rand.nextInt(totalCells) * bd.getTexWidthPerCell();
            shiftY = rand.nextInt(totalFloors) * bd.getTexHeightPerCell();
            
            if(i % 2 == 0)
                maxX = shiftX + bb.textureLength();
            else
                maxX = shiftX + bb.textureWidth();
            
            maxY = shiftY + bb.textureHeight();
            
            if(maxX < 0)
                maxX = 0;
            if(maxY < 0)
                maxY = 0;
            
            //Lower left
            texCoords[i * 4] = new Vector2f(shiftX, shiftY);
            //Lower Right
            texCoords[(i * 4) + 1] = new Vector2f(maxX, shiftY);
            //Upper Right
            texCoords[(i * 4) + 2] = new Vector2f(maxX, maxY);
            //Upper Left
            texCoords[(i * 4) + 3] = new Vector2f(shiftX, maxY);
        }
        
        return texCoords;
    }

    public Vector2f[] textureShift(Vector2f[] oldCoord, int heightAdjust){
        Vector2f[] newCoord;
        
        float shiftY = heightAdjust * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.TEXTURE_LENGTH_PER_PIXEL;
        
        newCoord = new Vector2f[16];       
        
        //Back, right, front, left
        for(int i = 0; i < 4; i++){        
            //Lower left
            newCoord[i * 4] = oldCoord[i * 4].clone().addLocal(0, -shiftY);
            //Lower Right
            newCoord[(i * 4) + 1] = oldCoord[(i * 4) + 1].clone().addLocal(0, -shiftY);
            //Upper Right
            newCoord[(i * 4) + 2] = oldCoord[(i * 4) + 2].clone();
            //Upper Left
            newCoord[(i * 4) + 3] = oldCoord[(i * 4) + 3].clone();
        }
        
        return newCoord;
    }
}
