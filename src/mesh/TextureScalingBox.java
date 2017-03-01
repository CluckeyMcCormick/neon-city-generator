/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mesh;

import com.jme3.math.Vector2f;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.scene.shape.Box;

/**
 *
 * @author root
 */
public class TextureScalingBox extends Box {
   
    public TextureScalingBox(float x, float y, float z, Vector2f[] scaledCoords){
        super(x, y, z);
        
        Vector2f[] adjTexCoord = new Vector2f[24];
        //Copy over the new data
        System.arraycopy(scaledCoords, 0, adjTexCoord, 0, scaledCoords.length);
        //Eliminate the top and bottom
        for(int i = scaledCoords.length; i < adjTexCoord.length; i++)
            adjTexCoord[i] = Vector2f.ZERO;
        
        this.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(adjTexCoord));
        this.updateBound();
    }
}
