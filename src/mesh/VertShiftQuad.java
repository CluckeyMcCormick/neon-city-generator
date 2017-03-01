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
public class VertShiftQuad extends Mesh{

    public VertShiftQuad(float x, float y, float leftShift, float rightShift){
        Vector3f[] vertextrious;
        Vector2f[] texturious;
        int[] indextrious;
        
        vertextrious = new Vector3f[]{
            new Vector3f(0, leftShift, 0),
            new Vector3f(0, y + leftShift, 0),
            new Vector3f(x, y + rightShift, 0),
            new Vector3f(x, rightShift, 0)
        };
        
        texturious = new Vector2f[]{
            new Vector2f(0, 0),
            new Vector2f(0, 1),
            new Vector2f(1, 1),
            new Vector2f(1, 0)
        };
        
        indextrious = new int[]{
            0, 3, 2,    0, 2, 1
        };
        
        this.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertextrious));
        this.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texturious));
        this.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indextrious));
        this.updateBound();
    }
}
