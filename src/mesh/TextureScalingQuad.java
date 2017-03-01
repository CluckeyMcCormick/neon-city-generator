/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mesh;

import com.jme3.math.Vector2f;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Quad;
import com.jme3.util.BufferUtils;

/**
 *
 * @author root
 */
public class TextureScalingQuad extends Quad {
    
    public TextureScalingQuad(float x, float y, Vector2f[] scaledCoords){
        super(x, y);  
        this.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(scaledCoords));
        this.updateBound();
    }
    
}
