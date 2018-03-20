/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mesh;

import building.Building;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 *
 * @author frick
 */
public class CivilizedWarpingQuad extends Mesh{

    public CivilizedWarpingQuad(int unitLength, int unitWidth){
        Vector3f[] vertextrious;
        Vector2f[] texturious;
        int[] indextrious;
        
        float fx = unitLength * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        float fy = unitWidth * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        
        vertextrious = new Vector3f[]{
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, fy),
            new Vector3f(fx, 0, fy),
            new Vector3f(fx, 0, 0)
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
    
    public CivilizedWarpingQuad(int unitLength, int unitWidth, int[] height){
        Vector3f[] vertextrious;
        Vector2f[] texturious;
        int[] indextrious;

        float fx = unitLength * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        float fy = unitWidth * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        
        float[] float_height = new float[4];
        for(int i = 0; i < float_height.length; i++)
            float_height[i] = height[i] * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        
        vertextrious = new Vector3f[]{
            new Vector3f(0, float_height[3], 0),
            new Vector3f(0, float_height[0], fy),
            new Vector3f(fx, float_height[1], fy),
            new Vector3f(fx, float_height[2], 0)
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
