/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg.blocktype;

import building.Building;
import building.BuildingFactory;
import cityorg.BlockDetail;
import cityorg.Cardinal;
import cityorg.CityBlock;
import com.jme3.scene.Node;
import java.util.ArrayList;
import prime.RandomSingleton;

/**
 *
 * @author frick
 */
public class BlockRing extends CityBlock{
    
    private int widthIncursion;
    private int lengthIncursion;
    
    public BlockRing(BlockDetail blockDet, int[] unitHeight, 
            int unitLength, int unitWidth, int lengthIncur, int widthIncur
    ){
        super(blockDet, unitHeight, unitLength, unitWidth, lengthIncur, widthIncur );
        
        this.widthIncursion = widthIncur;
        if(this.widthIncursion > unitWidth / 2)
            this.widthIncursion = unitWidth / 2;
        
        if(this.widthIncursion < MIN_FRONT)
            this.widthIncursion = 0;
        
        this.lengthIncursion = lengthIncur;
        if(this.lengthIncursion > unitLength / 2)
            this.lengthIncursion = unitLength / 2;
    
        if(this.lengthIncursion < MIN_FRONT)
            this.lengthIncursion = 0;
    }

    @Override
    public void generateBuildings(BuildingFactory bf) {
        
        /**
         *   1 2 3
         *   8 _ 4
         *   7 6 5
         */
        int[] xValues = new int[4];
        int[] yValues = new int[4];
        
        xValues[0] = 0;
        xValues[1] = this.lengthIncursion;
        xValues[2] = this.getUnitLength() - this.lengthIncursion;
        xValues[3] = this.getUnitLength();
        
        yValues[0] = 0;
        yValues[1] = this.widthIncursion;
        yValues[2] = this.getUnitLength() - this.lengthIncursion;
        yValues[3] = this.getUnitLength();
        
        // 1
        this.buildCorner(bf, Cardinal.NORTH_WEST, xValues[0], yValues[0], xValues[1], yValues[1]);
        // 2
        this.buildSide(bf, Cardinal.NORTH, xValues[1], yValues[0], xValues[2], yValues[1]);
        // 3
        this.buildCorner(bf, Cardinal.NORTH_EAST, xValues[2], yValues[0], xValues[3], yValues[1]);
        // 4
        this.buildSide(bf, Cardinal.EAST, xValues[2], yValues[1], xValues[3], yValues[2]);
        // 5
        this.buildCorner(bf, Cardinal.SOUTH_EAST, xValues[2], yValues[2], xValues[3], yValues[3]);
        // 6
        this.buildSide(bf, Cardinal.SOUTH, xValues[1], yValues[2], xValues[2], yValues[3]);
        // 7
        this.buildCorner(bf, Cardinal.SOUTH_WEST, xValues[0], yValues[2], xValues[1], yValues[3]);
        // 8
        this.buildSide(bf, Cardinal.WEST, xValues[0], yValues[1], xValues[1], yValues[2]);
        
        super.generateBuildings(bf);
    }
    
    private void buildCorner(BuildingFactory bf, Cardinal card, int ulX, int ulY, int lrX, int lrY){
        RandomSingleton rs = RandomSingleton.getInstance();
        
        BlockDetail bd;
        Building b;
        Node nd;
        
        int length;
        int width;
        int height;
        
        int moveX = 0;
        int moveY = 0;
        
        bd = this.getBlockDet();
        
        width = this.widthIncursion - (int) Math.abs( rs.nextGaussian() * bd.getDepthDeviant() );
        length = this.lengthIncursion - (int) Math.abs( rs.nextGaussian() * bd.getDepthDeviant() );
        height = bd.getIdealHeight() + (int)( bd.getHeightDeviant() * rs.nextGaussian() );
        
        nd = this.getNode();
        
        b = bf.randomFCB(width, length, height, 1);
        
        switch(card){
            case NORTH_EAST:
                moveX = lrX - length;
                moveY = ulY;
                break;
            case SOUTH_EAST:
                moveX = lrX - length;
                moveY = lrY - width;
                break;
            case SOUTH_WEST:
                moveX = ulX;
                moveY = lrY - width;
                break;
            case NORTH_WEST:
            default:
                moveX = ulX;
                moveY = ulY;
                break;
        }
        
        b.setComboTranslation(
            moveX, 0, moveY, 
            b.virtualLength() / 2, b.virtualHeight() / 2, b.virtualWidth() / 2
        );
        
        nd.attachChild( b.getNode() );
    }
    
    private void buildSide(BuildingFactory bf, Cardinal card, int ulX, int ulY, int lrX, int lrY){
        RandomSingleton rs = RandomSingleton.getInstance();
        BlockDetail bd;
        Building b;
        Node nd;
        
        int[] sizes;
        int offset;
        int height;
        int depth;
        
        int moveX;
        int moveY;
        
        //Stage 1: Divy up the space
        switch(card){
            default:
            case NORTH:
            case SOUTH:
                sizes = this.pickLength(ulX, lrX);
                break;
            case WEST:
            case EAST:
                sizes = this.pickLength(ulY, lrY);
                break;
        }
        
        nd = this.getNode();
        bd = this.getBlockDet();
        offset = 1;
        
        //Stage 2: Building Manufacture
        for(int i = 0; i < sizes.length; i++){
            
            height = bd.getIdealHeight() + (int)( bd.getHeightDeviant() * rs.nextGaussian() );
            
            // Calculate the depth, build the building
            switch(card){
                default:
                case NORTH:
                case SOUTH: 
                    depth = this.widthIncursion - (int) Math.abs( rs.nextGaussian() * bd.getDepthDeviant() );
                    b = bf.randomFCB(depth, sizes[i], height, 1);
                    break;
                case WEST:
                case EAST:
                    depth = this.lengthIncursion - (int) Math.abs( rs.nextGaussian() * bd.getDepthDeviant() );
                    b = bf.randomFCB(sizes[i], depth, height, 1);
                    break;
            }
            
            //Move the building to the appropriate position
            switch(card){
                default:
                case NORTH:
                    moveX = ulX + offset;
                    moveY = ulY;
                    break;
                case EAST:
                    moveX = lrX - depth;
                    moveY = ulY + offset;
                    break;
                case SOUTH:
                    moveX = ulX + offset;
                    moveY = lrY - depth;
                    break;
                case WEST:
                    moveX = ulX;
                    moveY = ulY + offset;
                    break;
            }
            
            b.setComboTranslation(
            moveX, 
            0, 
            moveY, 
            b.virtualLength() / 2, 
            b.virtualHeight() / 2, 
            b.virtualWidth() / 2);
        
            nd.attachChild( b.getNode() );
            
            offset += sizes[i] + 1;
        }
    }
    
    private int[] pickLength(int a, int b){

        RandomSingleton rs = RandomSingleton.getInstance();
        ArrayList<Integer> listo = new ArrayList();
        BlockDetail bd = this.getBlockDet();
        //Determine how much space we have (account for border alleys)
        int frontage = 0; // - 1 is first border alley 
        int face;

        int[] reto;
        
        System.out.println(b - a);
        
        //While we still have space left
        while(frontage != b - a - (listo.size() + 1) ){

            //Generate a random value according to our specified algorithim
            face = bd.getIdealFace() + (int)( bd.getFaceDeviant() * rs.nextGaussian() ) ; 
            //if this random value doesn't leave enough space for another building
            if(face < this.MIN_FRONT)
                face = this.MIN_FRONT; //tack on the remaining space

            //If we still have some space
            if( frontage + face != b - a - (listo.size() + 2 ) ) 
                //But there isn't ebough space
                if( b - a - (listo.size() + 2) - (frontage + face) < this.MIN_FRONT + 1 )
                    //tack on the remaining space
                    face += b - a - (listo.size() + 2) - (frontage + face);
                    
            //add our size to the list
            listo.add(face);
            //subtract our size from the total remaining space
            frontage += face;
        }

        reto = new int[ listo.size() ];
       
        for(int i = 0; i < reto.length; i++)
            reto[i] = listo.get(i);
        
        //return it
        return reto;
    }
}
