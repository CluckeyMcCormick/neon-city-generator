/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mesh;

import building.Building;
import building.BuildingFactory;
import cityorg.CityStructure;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/**
 *
 * @author frick
 */
public class CivilizedFadeMesh extends Mesh{
    
    public CivilizedFadeMesh(int unitLength, int lenDepth, int unitWidth, int widDepth, int[] unitHeight ){   
        
        int single = Building.MIN_UNIT_HEIGHT;
        
        if(widDepth != single && lenDepth != single){
            this.configurationA(unitLength, unitWidth, unitHeight, widDepth, lenDepth);
        }
        else if(widDepth != single){
            this.configurationA(unitLength, unitWidth, unitHeight, widDepth, lenDepth);
        }
        else if(lenDepth != single){
            this.configurationA(unitLength, unitWidth, unitHeight, widDepth, lenDepth);
        }
        else{
            this.configurationD(unitLength, unitWidth, unitHeight, BuildingFactory.MIN_VIRTUAL_HEIGHT);
        }
    }
    
    private void configurationA(int unitLength, int unitWidth, int[] unitHeight, int widDepth, int lenDepth){
        /**
        * This is our configuration.
        * 
        * BB  _9==13========17==23
        *     ||  ||        ||  ||
        * _3==_8==12========16==22==27
        * ||  ||                ||  ||
        * _2==_7                21==26
        * ||  ||                ||  ||
        * ||  ||                ||  ||
        * ||  ||                ||  ||
        * _1==_6                20==25
        * ||  ||                ||  ||
        * _0==_5==11========15==19==24
        *     ||  ||        ||  ||
        *     _4==10========14==18 
        * The letter parts indicate the locations of our four height specifiers:
        * AA - 0  BB - 1  CC - 2  DD - 3  
        */

        final int POINT_A = 0;
        final int POINT_B = 1;
        final int POINT_C = 2;
        final int POINT_D = 3;
        
        float length = unitLength * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        float width = unitWidth * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;

        float lenAlt = widDepth * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        float widAlt = lenDepth * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;

        float single = BuildingFactory.MIN_VIRTUAL_HEIGHT;
        
        float midRightVert = (width - widAlt) / (width - single * 2);
        float midLeftVert = widAlt / (width - single * 2);
        
        float midRightHoriz = (length - lenAlt) / (length - single * 2);
        float midLeftHoriz = lenAlt / (length - single * 2);
        
        float[] virtHeight = new float[unitHeight.length];

        for(int i = 0; i < virtHeight.length; i++)
            virtHeight[i] = unitHeight[i] * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        
        Vector3f[] vertex = new Vector3f[28];
        vertex[0] = new Vector3f(0, virtHeight[POINT_A], single);
        vertex[1] = new Vector3f(0, virtHeight[POINT_A], widAlt);
        vertex[2] = new Vector3f(0, virtHeight[POINT_B], width - widAlt);
        vertex[3] = new Vector3f(0, virtHeight[POINT_B], width - single);
        
        vertex[4] = new Vector3f(single, virtHeight[POINT_A], 0);
        vertex[5] = new Vector3f(single, virtHeight[POINT_A], single);
        vertex[6] = new Vector3f(single, virtHeight[POINT_A], widAlt);
        vertex[7] = new Vector3f(single, virtHeight[POINT_B], width - widAlt);
        vertex[8] = new Vector3f(single, virtHeight[POINT_B], width - single);
        vertex[9] = new Vector3f(single, virtHeight[POINT_B], width);
        
        vertex[10] = new Vector3f(lenAlt, virtHeight[POINT_A], 0);
        vertex[11] = new Vector3f(lenAlt, virtHeight[POINT_A], single);
        vertex[12] = new Vector3f(lenAlt, virtHeight[POINT_B], width - single);
        vertex[13] = new Vector3f(lenAlt, virtHeight[POINT_B], width);
        
        vertex[14] = new Vector3f(length - lenAlt, virtHeight[POINT_D], 0);
        vertex[15] = new Vector3f(length - lenAlt, virtHeight[POINT_D], single);
        vertex[16] = new Vector3f(length - lenAlt, virtHeight[POINT_C], width - single);
        vertex[17] = new Vector3f(length - lenAlt, virtHeight[POINT_C], width);

        vertex[18] = new Vector3f(length - single, virtHeight[POINT_D], 0);
        vertex[19] = new Vector3f(length - single, virtHeight[POINT_D], single);
        vertex[20] = new Vector3f(length - single, virtHeight[POINT_D], widAlt);
        vertex[21] = new Vector3f(length - single, virtHeight[POINT_C], width - widAlt);
        vertex[22] = new Vector3f(length - single, virtHeight[POINT_C], width - single);
        vertex[23] = new Vector3f(length - single, virtHeight[POINT_C], width);
        
        vertex[24] = new Vector3f(length, virtHeight[POINT_D], single);
        vertex[25] = new Vector3f(length, virtHeight[POINT_D], widAlt);
        vertex[26] = new Vector3f(length, virtHeight[POINT_C], width - widAlt);
        vertex[27] = new Vector3f(length, virtHeight[POINT_C], width - single);

        Vector2f[] texture = new Vector2f[28];
        
        texture[0] = new Vector2f(1,0); //right, bottom
        texture[1] = new Vector2f(midRightVert,0); //mid-right, bottom
        texture[2] = new Vector2f(midLeftVert,0); //mid-left, bottom
        texture[3] = new Vector2f(0,0); //left, bottom
        texture[8] = new Vector2f(0,1); //left, top
        texture[7] = new Vector2f(midLeftVert,1); //mid-left, top
        texture[6] = new Vector2f(midRightVert,1); //mid-right, top
        texture[5] = new Vector2f(1,1); //right, top

        texture[9] = new Vector2f(0,0); //left, bottom
        texture[13] = new Vector2f(midLeftHoriz,0); //mid-left, bottom
        texture[17] = new Vector2f(midRightHoriz,0); //mid-right, bottom
        texture[23] = new Vector2f(1,0); //right, bottom
        texture[22] = new Vector2f(1,1); //right, top
        texture[12] = new Vector2f(midRightVert,1); //mid-right, top
        texture[16] = new Vector2f(midLeftVert,1); //mid-left, top
        //texture[8] = new Vector2f(0,1); //left, top
        
        texture[27] = new Vector2f(1,0); //right, bottom
        texture[26] = new Vector2f(midRightHoriz,0); //mid-right, bottom
        texture[25] = new Vector2f(midLeftHoriz,0); //mid-left, bottom
        texture[24] = new Vector2f(0,0); //left, bottom
        texture[19] = new Vector2f(0,1); //left, top
        texture[20] = new Vector2f(midLeftVert,1); //mid-left, top
        texture[21] = new Vector2f(midRightVert,1); //mid-right, top
        //texture[22] = new Vector2f(1,1); //right, top
        
        texture[4] = new Vector2f(1,0); //right, bottom
        texture[10] = new Vector2f(midRightHoriz,0); //mid-right, bottom
        texture[14] = new Vector2f(midLeftHoriz,0); //mid-left, bottom
        texture[18] = new Vector2f(0,0); //left, bottom
        //texture[19] = new Vector2f(0,1); //left, top
        texture[15] = new Vector2f(midLeftVert,1); //mid-left, top
        texture[11] = new Vector2f(midRightVert,1); //mid-right, top
        //texture[5] = new Vector2f(1,1); //right, top
        
        int[] indexes = new int[]{
            //A           B
            0,1,6,      5,0,6,
            //C           D
            1,2,7,      6,1,7,
            //E           F
            2,3,8,      7,2,8,
            //G           H
            8,9,13,     12,8,13,
            //I           J
            12,13,17,   16,12,17,
            //K           L
            16,17,23,   22,16,23,
            //M           N
            21,22,27,   26,21,27,
            //O           P
            20,21,26,   25,20,26,
            //Q           R
            19,20,25,   24,19,25,
            //S           T
            14,15,19,   18,14,19,
            //U           V
            10,11,15,   14,10,15,
            //W           X
            4,5,11,     10,4,11
        };

        this.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertex));
        this.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texture));
        this.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
        this.updateBound();
    }
    private void configurationB(int unitLength, int unitWidth, int[] unitHeight, float single){
        /**
        * This is our configuration.
        * 
        *   BB  _0=====_1  CC
        *       ||     || 
        *   10==_3=====_2==_4
        *   ||  ||     ||  ||
        *   ||  ||     ||  ||
        *   ||  ||     ||  ||
        *   11==_7=====_6==_5
        *       ||     ||
        *   AA  _9=====_8  DD
        *
        * The letter parts indicate the locations of our four height specifiers:
        * AA - 0  BB - 1  CC - 2  DD - 3  
        */

        float length = unitLength * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        float width = unitWidth * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;

        float[] xzCoord = new float[]{
            single, 0, //0
            length - single, 0, // 1
            length - single, single, // 2
            single, single, // 3
            length, single, // 4
            length, width - single, // 5
            length - single, width - single, //6
            single, width - single, //7
            length - single, width, //8
            single, width, //9
            0, single, //10
            0, width - single //11
        };

        Vector2f[] texture = new Vector2f[12];
        texture[0] = new Vector2f(0, 0); //left, bottom
        texture[1] = new Vector2f(1, 0); //right, bottom
        texture[2] = new Vector2f(1, 1); //right, top
        texture[3] = new Vector2f(0, 1); //left, bottom
        texture[4] = new Vector2f(1, 0); //right, bottom
        texture[5] = new Vector2f(0, 0); //left, bottom
        texture[6] = new Vector2f(0, 1); //left, top
        texture[7] = new Vector2f(1, 1); //right, top
        texture[8] = new Vector2f(0, 0); //left, bottom
        texture[9] = new Vector2f(1, 0); //right, bottom
        texture[10] = new Vector2f(0, 0); //left, bottom
        texture[11] = new Vector2f(1, 0); //right, bottom

        int[] indexes = new int[]{
            //A           B
            0,3,2,      0,2,1,
            //C           D
            2,6,5,      2,5,4,
            //E           F
            7,9,8,      7,8,6,
            //G           H
            10,11,7,    10,7,3
        };

        this.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer( this.heightByPoint(unitHeight, xzCoord) ));
        this.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texture));
        this.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
        this.updateBound();
    }
    
    private void configurationC(int unitLength, int unitWidth, int[] unitHeight, float single){
        /**
        * This is our configuration.
        * 
        *   BB  _0=====_1  CC
        *       ||     || 
        *   10==_3=====_2==_4
        *   ||  ||     ||  ||
        *   ||  ||     ||  ||
        *   ||  ||     ||  ||
        *   11==_7=====_6==_5
        *       ||     ||
        *   AA  _9=====_8  DD
        *
        * The letter parts indicate the locations of our four height specifiers:
        * AA - 0  BB - 1  CC - 2  DD - 3  
        */

        float length = unitLength * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        float width = unitWidth * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;

        float[] xzCoord = new float[]{
            single, 0, //0
            length - single, 0, // 1
            length - single, single, // 2
            single, single, // 3
            length, single, // 4
            length, width - single, // 5
            length - single, width - single, //6
            single, width - single, //7
            length - single, width, //8
            single, width, //9
            0, single, //10
            0, width - single //11
        };

        Vector2f[] texture = new Vector2f[12];
        texture[0] = new Vector2f(0, 0); //left, bottom
        texture[1] = new Vector2f(1, 0); //right, bottom
        texture[2] = new Vector2f(1, 1); //right, top
        texture[3] = new Vector2f(0, 1); //left, bottom
        texture[4] = new Vector2f(1, 0); //right, bottom
        texture[5] = new Vector2f(0, 0); //left, bottom
        texture[6] = new Vector2f(0, 1); //left, top
        texture[7] = new Vector2f(1, 1); //right, top
        texture[8] = new Vector2f(0, 0); //left, bottom
        texture[9] = new Vector2f(1, 0); //right, bottom
        texture[10] = new Vector2f(0, 0); //left, bottom
        texture[11] = new Vector2f(1, 0); //right, bottom

        int[] indexes = new int[]{
            //A           B
            0,3,2,      0,2,1,
            //C           D
            2,6,5,      2,5,4,
            //E           F
            7,9,8,      7,8,6,
            //G           H
            10,11,7,    10,7,3
        };

        this.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer( this.heightByPoint(unitHeight, xzCoord) ));
        this.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texture));
        this.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
        this.updateBound();
    }
    
    private void configurationD(int unitLength, int unitWidth, int[] unitHeight, float single){
        /**
        * This is our configuration.
        * 
        *   BB  _0=====_1  CC
        *       ||     || 
        *   10==_3=====_2==_4
        *   ||  ||     ||  ||
        *   ||  ||     ||  ||
        *   ||  ||     ||  ||
        *   11==_7=====_6==_5
        *       ||     ||
        *   AA  _9=====_8  DD
        *
        * The letter parts indicate the locations of our four height specifiers:
        * AA - 0  BB - 1  CC - 2  DD - 3  
        */

        float length = unitLength * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        float width = unitWidth * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;

        float[] xzCoord = new float[]{
            single, 0, //0
            length - single, 0, // 1
            length - single, single, // 2
            single, single, // 3
            length, single, // 4
            length, width - single, // 5
            length - single, width - single, //6
            single, width - single, //7
            length - single, width, //8
            single, width, //9
            0, single, //10
            0, width - single //11
        };

        Vector2f[] texture = new Vector2f[12];
        texture[0] = new Vector2f(0, 0); //left, bottom
        texture[1] = new Vector2f(1, 0); //right, bottom
        texture[2] = new Vector2f(1, 1); //right, top
        texture[3] = new Vector2f(0, 1); //left, bottom
        texture[4] = new Vector2f(1, 0); //right, bottom
        texture[5] = new Vector2f(0, 0); //left, bottom
        texture[6] = new Vector2f(0, 1); //left, top
        texture[7] = new Vector2f(1, 1); //right, top
        texture[8] = new Vector2f(0, 0); //left, bottom
        texture[9] = new Vector2f(1, 0); //right, bottom
        texture[10] = new Vector2f(0, 0); //left, bottom
        texture[11] = new Vector2f(1, 0); //right, bottom

        int[] indexes = new int[]{
            //A           B
            0,3,2,      0,2,1,
            //C           D
            2,6,5,      2,5,4,
            //E           F
            7,9,8,      7,8,6,
            //G           H
            10,11,7,    10,7,3
        };

        this.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer( this.heightByPoint(unitHeight, xzCoord) ));
        this.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texture));
        this.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
        this.updateBound();
    }
    
    private Vector3f[] heightByPoint(int[] unitHeight, float[] xzCoord ){
        final int POINT_A = 0;
        final int POINT_B = 1;
        final int POINT_C = 2;
        final int POINT_D = 3;
        
        Vector3f[] ret = new Vector3f[xzCoord.length / 2];
        
        float y;
        
        for(int i = 0; i < xzCoord.length; i += 2){
            switch(i / 2){
                default:
                case 10: case 3: case 0:
                    y = unitHeight[POINT_A] * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
                    break;
                case 11: case 7: case 9:
                    y = unitHeight[POINT_B] * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
                    break;
                
                case 4: case 2: case 1:
                    y = unitHeight[POINT_D] * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
                    break;
                case 8: case 6: case 5:
                    y = unitHeight[POINT_C] * Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
                    break;
            }
            
            System.out.println((i / 2) + " " + xzCoord[i] + " " + y + " " + xzCoord[i + 1]);
            
            ret[i / 2] = new Vector3f(xzCoord[i], y , xzCoord[i + 1]);
        }

        return ret;
    }
    
}
