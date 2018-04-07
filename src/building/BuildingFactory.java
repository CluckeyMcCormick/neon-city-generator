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
import mesh.CivilizedWarpingQuad;
import mesh.ClosedBox;
import production.DebugShapeFactory;

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
    
    private MaterialBook mat_book;
    private ColorBook color_book;
    private CellTextureDetail build_detail;
    private DebugShapeFactory debug_factory;
    
    public BuildingFactory(ColorBook colo, CellTextureDetail bd, AssetManager am){    

        this.color_book = colo;
        this.mat_book = MaterialBuilder.buildMatBook(color_book, bd, am);
        
        this.build_detail = bd;
        
        debug_factory = new DebugShapeFactory(am);
    }
    
    public Geometry blockFloor(int[] unit_heights, int[] cardinal_cuts, int unit_x, int unit_z){
        Geometry floor;
        Material baseMat;
        
        baseMat = this.mat_book.getBaseMat();
        
        if(cardinal_cuts[0] == 0 && cardinal_cuts[1] == 0 &&
            cardinal_cuts[2] == 0 && cardinal_cuts[3] == 0
        )
            floor = new Geometry("block_floor", 
                new CivilizedWarpingQuad(
                    unit_x, unit_z, unit_heights
                ) 
            );
        else
            floor = new Geometry("block_floor", 
                new CivilizedBase(
                    unit_heights, cardinal_cuts, unit_x, unit_z
                ) 
            );
        
        floor.setMaterial(baseMat);

        return floor;
    }

    public Geometry intersection(int unit_height, int unit_z, int unit_x ){
        Geometry sect;
        Material baseMat;
        
        baseMat = this.mat_book.getBaseMat();
        
        sect = new Geometry("intersection", 
            new CivilizedWarpingQuad(
                unit_x, unit_z, unit_height
            ) 
        );
        
        sect.setMaterial(baseMat);   
        
        return sect;
    }

    public Geometry road(int[] unit_heights, int unit_x, int unit_z){
        Geometry road;
        Material baseMat;
        
        baseMat = this.mat_book.getBaseMat();
        
        road = new Geometry("road", 
            new CivilizedWarpingQuad(
                unit_x, unit_z, unit_heights
            ) 
        );
        
        road.setMaterial(baseMat);

        return road;
    }
    
    //In the actual program, we'll be creating buildings to fit gaps
    //So we'll know the width - the number of units - but not the height.
    public FloorCellBuilding randomFCB(int unit_length, int unit_height){
        return this.randomFCB(unit_length, unit_length, unit_height, 0);
    }
    
    public FloorCellBuilding randomFCB(int unit_x, int unit_z, int unit_height, float height_adjust){
        FloorCellBuilding fcb;
        
        if( unit_height < Building.MIN_UNIT_HEIGHT )
           unit_height = Building.MIN_UNIT_HEIGHT;  
        
        fcb = new FloorCellBuilding(unit_x, unit_z, unit_height, this.build_detail);
        
        this.buildFCB(fcb, height_adjust);
    
        return fcb;
    }
    
    private void buildFCB(FloorCellBuilding bb, float heightAdjust){

        Material fullMat = this.mat_book.getFullMat(WindowStyle.STANDARD);
        
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
        fullGeom.setLocalTranslation(0, HEIGHT_ADJUST, 0);
        node.attachChild(fullGeom);
        
        bb.setNode(node);    
    }
    
    public Vector2f[] getRandomTextureCoords(FloorCellBuilding bb){
        Vector2f[] tex_coords;
        RandomSingleton rand = RandomSingleton.getInstance();
        
        int total_floors;
        int total_cells;
        
        float max_x;
        float max_y;
        
        float shift_x;
        float shift_y;
        
        tex_coords = new Vector2f[16];       
      
        total_floors = TextureBuilder.TEXTURE_Y / build_detail.getCellPixHeight();
        if(TextureBuilder.TEXTURE_Y % build_detail.getCellPixHeight() != 0)
            total_floors = total_floors - 1;
        
        total_cells = TextureBuilder.TEXTURE_X / build_detail.getCellPixWidth();
        if(TextureBuilder.TEXTURE_X % build_detail.getCellPixWidth() != 0)
            total_cells = total_cells - 1;
        
        //Back, right, front, left
        for(int i = 0; i < 4; i++){
            shift_x = rand.nextInt(total_cells) * build_detail.getTexWidthPerCell();
            shift_y = rand.nextInt(total_floors) * build_detail.getTexHeightPerCell();
            
            if(i % 2 == 0)
                max_x = shift_x + bb.textureLength();
            else
                max_x = shift_x + bb.textureWidth();
            
            max_y = shift_y + bb.textureHeight();
            
            if(max_x < 0)
                max_x = 0;
            if(max_y < 0)
                max_y = 0;
            
            //Lower left
            tex_coords[i * 4] = new Vector2f(shift_x, shift_y);
            //Lower Right
            tex_coords[(i * 4) + 1] = new Vector2f(max_x, shift_y);
            //Upper Right
            tex_coords[(i * 4) + 2] = new Vector2f(max_x, max_y);
            //Upper Left
            tex_coords[(i * 4) + 3] = new Vector2f(shift_x, max_y);
        }
        
        return tex_coords;
    }

    public Vector2f[] textureShift(Vector2f[] old_coord, int height_adjust){
        Vector2f[] new_coord;
        
        float shift_y = height_adjust * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.TEXTURE_LENGTH_PER_PIXEL;
        
        new_coord = new Vector2f[16];       
        
        //Back, right, front, left
        for(int i = 0; i < 4; i++){        
            //Lower left
            new_coord[i * 4] = old_coord[i * 4].clone().addLocal(0, -shift_y);
            //Lower Right
            new_coord[(i * 4) + 1] = old_coord[(i * 4) + 1].clone().addLocal(0, -shift_y);
            //Upper Right
            new_coord[(i * 4) + 2] = old_coord[(i * 4) + 2].clone();
            //Upper Left
            new_coord[(i * 4) + 3] = old_coord[(i * 4) + 3].clone();
        }
        
        return new_coord;
    }
}
