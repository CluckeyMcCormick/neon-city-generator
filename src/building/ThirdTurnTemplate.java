/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import cityorg.CityStructure;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
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
public class ThirdTurnTemplate extends BasicBuildingTemplate {
        
    public ThirdTurnTemplate(){}
    
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
        
        if( unit_height < ThirdTurnTemplate.MIN_UNIT_HEIGHT )
           unit_height = ThirdTurnTemplate.MIN_UNIT_HEIGHT;  

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
        fullGeom.setLocalRotation(
            fullGeom.getLocalRotation().fromAngles(0, FastMath.ONE_THIRD / 2, 0)
        );
        
        node.attachChild(fullGeom);
        bi.setNode(node);
        
        return bi;
    }

}
