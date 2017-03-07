/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author root
 */
public abstract class CityStructure {
    //The number by which all other numbers are compared
    public static final int GOLDEN_PIXEL_COUNT = 16;
    
    //The correspondence between a pixel and a texture length
    public final static float TEXTURE_LENGTH_PER_PIXEL = .000976562f;
    
    //The correspondence between a pixel and virtual length
    public final static float VIRTUAL_LENGTH_PER_PIXEL = .0078125f;
    
    // The node that references this structure
    private Node node;
    
    public void addFeature(Spatial s){
        this.node.attachChild(s);
    }
    
    public void setLocalTranslation(float x, float y, float z) {
        this.node.setLocalTranslation(x, y, z);
    }
    
    public void setUnitTranslation(int x, int y, int z) {
        
        float newX = x * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
        float newY = y * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
        float newZ = z * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
        
        this.setLocalTranslation(newX, newY, newZ);
    }
    
    public void setComboTranslation(int uX, int uY, int uZ, float lX, float lY, float lZ) {
        
        float newX = uX * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
        float newY = uY * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
        float newZ = uZ * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
        
        this.setLocalTranslation(newX + lX, newY + lY, newZ + lZ);
    }
    
    public void setNode(Node node){
        this.node = node;
    }
    
    public Node getNode(){
        return this.node;
    }
}
