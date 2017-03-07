/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package road;

import building.Building;
import mesh.TextureScalingQuad;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import production.MaterialBuilder;
import building.BuildingFactory;
import cityorg.CityStructure;

/**
 *
 * @author root
 */
public class RoadFactory {
    
    //Height of street lights above the street
    public final static float LIGHT_HEIGHT = BuildingFactory.MIN_VIRTUAL_HEIGHT / 2;
    
    private Material alleyMat;
    private Material smallMat;
    private Material mediumMat;
    private Material largeMat;
    
    public RoadFactory(Material[] matList){
        this.alleyMat = matList[MaterialBuilder.ALLEY_INDEX];
        this.smallMat = matList[MaterialBuilder.SMALL_STREET_INDEX];
        this.mediumMat = matList[MaterialBuilder.MEDIUM_STREET_INDEX];
        this.largeMat = matList[MaterialBuilder.LARGE_STREET_INDEX];
    }
    
    public boolean buildRoad(Road road, int cellLength){
        Material mat;
        
        float virtWidth;
        float virtLength;
        
        Geometry geom;
        TextureScalingQuad tsq;
        Quaternion quat;
        
        switch(road.getSize()){
            case SMALL_STREET:
                mat = smallMat;
                break;
            case MEDIUM_STREET:
                mat = mediumMat;
                break;
            case LARGE_STREET:
                mat = largeMat;
                break;
            default:
                mat = alleyMat;
                break;
        }
        
        virtWidth = road.getSize().pixelWidth * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        virtLength = RoadSize.LENGTH_CONSTANT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL * cellLength;
        
        Vector2f[] texes = new Vector2f[]{
            new Vector2f(0, 0), new Vector2f(1, 0), 
            new Vector2f(1, 1 * cellLength), new Vector2f(0, 1 * cellLength) 
        };
              
        tsq = new TextureScalingQuad(virtWidth, virtLength, texes);
        
        geom = new Geometry("rood", tsq);
        
        geom.setMaterial(mat); 
        
        System.out.println( geom.getLocalRotation() );
        
        float[] angles = new float[]{-FastMath.HALF_PI, road.getRoadAngle() * FastMath.DEG_TO_RAD, 0};
        
        geom.setLocalRotation( geom.getLocalRotation().fromAngles(angles) );
        geom.updateGeometricState();
        
        System.out.println( geom.getLocalRotation() );;
        
        road.setRoad(geom);
        
        //geom.setLocalRotation( new Quaternion().fromAngleAxis(-FastMath.PI/2, Vector3f.UNIT_X));
        
        return true;
    }
    
}
