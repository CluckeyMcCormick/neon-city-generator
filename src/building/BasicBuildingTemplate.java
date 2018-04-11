/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import cityorg.CityStructure;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import mesh.ClosedBox;
import prime.RandomSingleton;
import production.TextureBuilder;

/**
 *
 * @author root
 */
public class BasicBuildingTemplate {
    
    public static final int MIN_UNIT_HEIGHT = 4;
    
    public BasicBuildingTemplate(){}
    
    public BuildingInstance nonStaticBuild(int unit_x, int unit_z, int unit_height, Material mat){
        return build(unit_x, unit_z, unit_height, mat);
    }
    
    public static BuildingInstance build(int unit_x, int unit_z, int unit_height, Material mat){
        BuildingInstance bi;
        
        Node node;
        ClosedBox fullBox;
        Geometry fullGeom;
        Vector2f[] texCoords;
        
        float virtualHeight;
        
        if( unit_height < BasicBuildingTemplate.MIN_UNIT_HEIGHT )
           unit_height = BasicBuildingTemplate.MIN_UNIT_HEIGHT;  

        virtualHeight = unit_height * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        
        bi = new BuildingInstance(
            unit_x, 
            unit_z, 
            virtualHeight
        );

        node = new Node();
              
        texCoords = random16TexCoords(unit_x, unit_z, unit_height);
        
        //Now the dim box
        fullBox = new ClosedBox(
            bi.virtualX(),
            bi.virtualZ(), 
            bi.virtualHeight(), 
            texCoords
        );
        
        fullGeom = new Geometry("building_full", fullBox);
        fullGeom.setMaterial(mat);
        node.attachChild(fullGeom);
        bi.setNode(node);
        
        return bi;
    }
    
    /*
    Returns random coordinates for a single rectangle
    */
    public static Vector2f[] random4TexCoords(int unitLength, int unitHeight){
        Vector2f[] tex_coords;
        RandomSingleton rand = RandomSingleton.getInstance();
        
        int total_floors;
        int total_cells;
        
        float max_x;
        float max_y;
        
        float shift_x;
        float shift_y;
        
        tex_coords = new Vector2f[16];       
      
        total_floors = TextureBuilder.TEXTURE_Y / CityStructure.GOLDEN_PIXEL_COUNT;
        if(TextureBuilder.TEXTURE_Y % CityStructure.GOLDEN_PIXEL_COUNT != 0)
            total_floors = total_floors - 1;
        
        total_cells = TextureBuilder.TEXTURE_X / CityStructure.GOLDEN_PIXEL_COUNT;
        if(TextureBuilder.TEXTURE_X % CityStructure.GOLDEN_PIXEL_COUNT != 0)
            total_cells = total_cells - 1;
        
        shift_x = rand.nextInt(total_cells) * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.TEXTURE_LENGTH_PER_PIXEL;
        shift_y = rand.nextInt(total_floors) * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.TEXTURE_LENGTH_PER_PIXEL;
            
        max_x = shift_x + (unitLength * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.TEXTURE_LENGTH_PER_PIXEL);  
        max_y = shift_y + (unitHeight * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.TEXTURE_LENGTH_PER_PIXEL);

        if(max_x < 0)
            max_x = 0;
        if(max_y < 0)
            max_y = 0;

        //Lower left
        tex_coords[0] = new Vector2f(shift_x, shift_y);
        //Lower Right
        tex_coords[1] = new Vector2f(max_x, shift_y);
        //Upper Right
        tex_coords[2] = new Vector2f(max_x, max_y);
        //Upper Left
        tex_coords[3] = new Vector2f(shift_x, max_y);
       
        
        return tex_coords;
    }
    
    /*
    Returns random coordinates for a box-shape, where each of the four sides is 
    random (and separate)
    */
    public static Vector2f[] random16TexCoords(int unit_x, int unit_z, int unitHeight){
        Vector2f[] tex_coords;
        Vector2f[] new_coords;
        
        tex_coords = new Vector2f[16];       
        
        //, right, front, left
        for(int i = 0; i < 4; i++){

            if(i % 2 == 0)
                new_coords = random4TexCoords(unit_x, unitHeight);
            else
                new_coords = random4TexCoords(unit_z, unitHeight);
            
            //Lower left
            tex_coords[i * 4] = new_coords[0];
            //Lower Right
            tex_coords[(i * 4) + 1] = new_coords[1];
            //Upper Right
            tex_coords[(i * 4) + 2] = new_coords[2];
            //Upper Left
            tex_coords[(i * 4) + 3] = new_coords[3];
        }
        
        return tex_coords;
    }

}
