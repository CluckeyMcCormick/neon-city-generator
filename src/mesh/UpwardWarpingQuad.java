/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mesh;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 *
 * @author frick
 */
public class UpwardWarpingQuad extends Mesh {

    public UpwardWarpingQuad(float x, float y){
        Vector3f[] vertextrious;
        Vector2f[] texturious;
        int[] indextrious;
        
        vertextrious = new Vector3f[]{
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, y),
            new Vector3f(x, 0, y),
            new Vector3f(x, 0, 0)
        };
        
        texturious = new Vector2f[]{
            new Vector2f(0, 0),
            new Vector2f(0, 1),
            new Vector2f(1, 1),
            new Vector2f(1, 0)
        };
        
        indextrious = new int[]{
            2, 3, 0,    1, 2, 0
        };
        
        this.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertextrious));
        this.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texturious));
        this.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indextrious));
        this.updateBound();
    }
    
    public UpwardWarpingQuad(float x, float y, float[] height){
        Vector3f[] vertextrious;
        Vector2f[] texturious;
        int[] indextrious;
        
        vertextrious = new Vector3f[]{
            new Vector3f(0, height[0], 0),
            new Vector3f(0, height[1], y),
            new Vector3f(x, height[2], y),
            new Vector3f(x, height[3], 0)
        };
        
        texturious = new Vector2f[]{
            new Vector2f(0, 0),
            new Vector2f(0, 1),
            new Vector2f(1, 1),
            new Vector2f(1, 0)
        };
        
        indextrious = new int[]{
            2, 3, 0,    1, 2, 0
        };
        
        this.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertextrious));
        this.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texturious));
        this.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indextrious));
        this.updateBound();
    }
}
