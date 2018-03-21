/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mesh;

import building.Building;
import cityorg.Cardinal;
import cityorg.CityBlock;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 *
 * @author frick
 */
public class CivilizedBase extends Mesh {
    
    public CivilizedBase(int[] unitHeights, int[] cardinalCuts, int unitLength, int unitWidth){
    /**
        * This is our configuration.
        * 
        *   _3==_7=====11==15
        *   ||AA|| EE  ||BB||
        *   _2==_6=====10==14
        *   ||  ||     ||  ||
        *   ||HH|| II  ||FF||
        *   ||  ||     ||  ||
        *   _1==_5=====_9==13
        *   ||DD|| GG  ||CC||
        *   _0==_4=====_8==12
        * So our coordinates are grouped by squares
        * All points in AA, CC, EE, & GG are level
        * BB, DD, FF, HH, & II are all formed by connecting these points
        *
        * The level indicies are crafted as such:
        * AA - 0  BB - 1  CC - 2  DD - 3  
        * 
        * The Block Floor is split on II is split on 5 - 10
        */
        Vector3f[] vertex;
        Vector2f[] texture;
        float[] virtualHeight;
        float[] texLen;
        float[] texWidth;
        float[] lens = new float[4];
        float[] wids = new float[4];
        
        int westCut = cardinalCuts[Cardinal.WEST.value / 2];
        int eastCut = cardinalCuts[Cardinal.EAST.value / 2];
        
        int southCut = cardinalCuts[Cardinal.SOUTH.value / 2];
        int northCut = cardinalCuts[Cardinal.NORTH.value / 2];
        
        lens[0] = 0;
        lens[1] = westCut * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        lens[2] = (unitLength - eastCut) * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        lens[3] = unitLength * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        
        wids[0] = 0;
        wids[1] = northCut * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        wids[2] = (unitWidth - southCut) * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        wids[3] = unitWidth * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
     
        virtualHeight = new float[unitHeights.length];
        
        for(int i = 0; i < virtualHeight.length; i++)
            virtualHeight[i] = unitHeights[i] * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
            
        int[] indexes = new int[]{
            //AA-1      AA-2
            0,1,5,      4,0,5,
            //BB-1      BB-2
            1,2,6,      5,1,6,
            //CC-1      CC-2
            2,3,7,      6,2,7,
            //DD-1      DD-2
            6,7,11,     10,6,11,
            //EE-1      EE-2
            10,11,15,   14,10,15,
            //FF-1      FF-2
            9,10,14,    13,9,14,
            //GG-1      GG-2
            8,9,13,     12,8,13,
            //HH-1      HH-2
            4,5,9,      8,4,9,
            //II-1      II-2
            5,6,10,     9,5,10
        };
        
        texLen = new float[]{ 
            0, westCut / (float) unitLength,
            (unitLength - eastCut) / (float) unitLength, 1
        };
        
        texWidth = new float[]{ 
            0, southCut / (float) unitWidth,
            (unitWidth - northCut) / (float) unitWidth, 1
        };
        
        vertex = new Vector3f[16];
        texture = new Vector2f[vertex.length];
        
        float currHeight;
        
        for(int i = 0; i < texture.length; i++){
            texture[i] = new Vector2f( texLen[i / 4], texWidth[i % 4] );
            
            switch(i){
                default:
                case 0: case 1: case 4: case 5:
                    currHeight = virtualHeight[ CityBlock.POINT_D ]; //1
                    break;
                case 2: case 3: case 6: case 7:
                    currHeight = virtualHeight[ CityBlock.POINT_C ]; //2
                    break;
                case 10: case 11: case 14: case 15:
                    currHeight = virtualHeight[ CityBlock.POINT_B ]; //3
                    break;
                case 8: case 9: case 12: case 13:
                    currHeight = virtualHeight[ CityBlock.POINT_A ]; //0
                    break;
            }
            
            vertex[i] = new Vector3f(lens[i / 4], currHeight, wids[i % 4]);    
        }
        this.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertex));
        this.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texture));
        this.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indexes));
        this.updateBound();
    }
}
