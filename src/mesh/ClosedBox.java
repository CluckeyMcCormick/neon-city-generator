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
public class ClosedBox extends Mesh{ 
    
    public ClosedBox(float x_len, float z_len, float height){
        Vector2f[] texture = new Vector2f[20];
        texture[0] = new Vector2f(0, 0);
        texture[1] = new Vector2f(1, 0);
        texture[2] = new Vector2f(1, 1);
        texture[3] = new Vector2f(0, 1);
        
        texture[4] = new Vector2f(0, 0);
        texture[5] = new Vector2f(1, 0);
        texture[6] = new Vector2f(1, 1);
        texture[7] = new Vector2f(0, 1);
        
        texture[8] = new Vector2f(0, 0);
        texture[9] = new Vector2f(1, 0);
        texture[10] = new Vector2f(1, 1);
        texture[11] = new Vector2f(0, 1);
        
        texture[12] = new Vector2f(0, 0);
        texture[13] = new Vector2f(1, 0);
        texture[14] = new Vector2f(1, 1);
        texture[15] = new Vector2f(0, 1);
        
        texture[16] = new Vector2f(1, 1);
        texture[17] = new Vector2f(1, 1);
        texture[18] = new Vector2f(1, 1);
        texture[19] = new Vector2f(1, 1);
                                
        this.buildBox( x_len, z_len, height, texture);
    }
    
    public ClosedBox( float x_len, float z_len, float height, Vector2f[] texture){
        this.buildBox(x_len, z_len,  height, texture);
    }
    
    private void buildBox(float x_len, float z_len, float height, Vector2f[] texture){
        Vector3f[] vertex;
        int[] indexes;

        vertex = new Vector3f[20];
        
        vertex[0] = new Vector3f(0,0,0);
        vertex[1] = new Vector3f(x_len, 0, 0);
        vertex[2] = new Vector3f(x_len, height, 0);
        vertex[3] = new Vector3f(0, height, 0);
        
        vertex[4] = new Vector3f(x_len, 0,0);
        vertex[5] = new Vector3f(x_len, 0, z_len);
        vertex[6] = new Vector3f(x_len, height, z_len);
        vertex[7] = new Vector3f(x_len, height, 0);
        
        vertex[8] = new Vector3f(0, 0, z_len);
        vertex[9] = new Vector3f(x_len, 0, z_len);
        vertex[10] = new Vector3f(x_len, height, z_len);
        vertex[11] = new Vector3f(0, height, z_len);
        
        vertex[12] = new Vector3f(0, 0, 0);
        vertex[13] = new Vector3f(0, 0, z_len);
        vertex[14] = new Vector3f(0, height, z_len);
        vertex[15] = new Vector3f(0, height, 0);
        
        vertex[16] = new Vector3f(0, height, 0);
        vertex[17] = new Vector3f(0, height, z_len);
        vertex[18] = new Vector3f(x_len, height, z_len);
        vertex[19] = new Vector3f(x_len, height, 0);
        
        indexes = new int[]{
            //FACING -Z, PARALELL X
            0,3,2,      0,2,1,
            //FACING X, PARALELL Z
            4,7,6,      4,6,5,
            //FACING Z, PARALELL X
            10,11,8,    9,10,8,
            //FACING -X, PARALELL Z
            14,15,12,   13,14,12,
            //TOP
            18, 19, 16,    17, 18, 16
        }; 
        
        this.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertex));
        this.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texture));
        this.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indexes));
        this.updateBound();
    }
    
}
