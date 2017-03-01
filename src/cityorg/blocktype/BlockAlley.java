/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg.blocktype;

import building.BuildingFactory;
import building.type.FloorCellBuilding;
import cityorg.BlockDetail;
import cityorg.Cardinal;
import cityorg.CityBlock;
import cityorg.CityStructure;
import com.jme3.scene.Node;
import java.util.Arrays;
import prime.RandomSingleton;

/**
 *
 * @author frick
 */
public class BlockAlley extends CityBlock{
    
    private boolean scramble;
    
    public BlockAlley(BlockDetail blockDet, int unitLength, int unitWidth, int[] unitHeight, boolean scramble){
        super(blockDet, unitLength, unitWidth, unitHeight);
        
        this.scramble = scramble;
    }
    
    @Override
    public void generateBuildings(BuildingFactory bf) {
        
        int lengthTotal = this.getUnitLength();
        int widthTotal = this.getUnitWidth();

        this.subdivGen(bf, Cardinal.SOUTH_EAST, 0, 0, lengthTotal / 2, widthTotal / 2, this.lightCreate(Cardinal.SOUTH_EAST) );
        this.subdivGen(bf, Cardinal.SOUTH_WEST, lengthTotal / 2, 0, lengthTotal, widthTotal / 2, this.lightCreate(Cardinal.SOUTH_WEST));
        this.subdivGen(bf, Cardinal.NORTH_WEST, lengthTotal / 2, widthTotal / 2, lengthTotal, widthTotal, this.lightCreate(Cardinal.NORTH_WEST));
        this.subdivGen(bf, Cardinal.NORTH_EAST, 0, widthTotal / 2, lengthTotal / 2, widthTotal, this.lightCreate(Cardinal.NORTH_EAST));
       
        super.generateBuildings(bf);
    }
    
    private void subdivGen(BuildingFactory bf, Cardinal orient, int ulX, int ulY, int lrX, int lrY, boolean[] lightArr) {
        RandomSingleton rand = RandomSingleton.getInstance();      
        FloorCellBuilding b;
        BlockDetail bd;
        Node nd;
        
        int width = lrY - ulY;
        int length = lrX - ulX;
        
        int[] heightAdj;
        int height;
        
        int moveX;
        int moveY;
        
        bd = this.getBlockDet();
        
        if( width <= bd.getIdealFace() + 1 && length <= bd.getIdealFace() + 1 ){
            nd = this.getNode();
            
            height = bd.getIdealHeight() + (int)( bd.getHeightDeviant() * rand.nextGaussian() );    
            
            heightAdj = this.calcHeightAdjust(ulX, ulY, length - 1, width - 1);
            
            if(heightAdj[1] < 2)
                heightAdj[1] = 2;
            
            b = bf.randomFCB( width - 1, length - 1, height, heightAdj[1] );
            Node fade = bf.buildFade(b, this, orient, lightArr, ulX, ulY);
            //if(heightAdj[1] != 0)
            b.addFeature( bf.buildStand( width - 1, length - 1, heightAdj[1] ) );

            switch(orient){
                default:
                case SOUTH_EAST:
                    moveX = ulX;
                    moveY = ulY;
                    break;
                case SOUTH_WEST:
                    moveX = ulX + 1;
                    moveY = ulY;
                    break;
                case NORTH_WEST:
                    moveX = ulX + 1;
                    moveY = ulY + 1;
                    break;
                case NORTH_EAST:
                    moveX = ulX;
                    moveY = ulY + 1;
                    break;
            }
            
            fade.setLocalTranslation(
                moveX * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL,
                0,
                moveY * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL
            );
            
            b.setComboTranslation( 
                moveX, 
                heightAdj[0], 
                moveY,
                0, 
                0, 
                0
            );
          
            //b.addFeature(fade);
            nd.attachChild( fade );
            nd.attachChild( b.getNode() );
        } 
        else{
            int randoX = 0;
            int randoY = 0;
            
            if(width > bd.getIdealFace())
                randoY = rand.nextInt( width - (this.MIN_FRONT + 1) * 2 ) + (this.MIN_FRONT + 1);

            if(length > bd.getIdealFace())
                randoX = rand.nextInt( length - (this.MIN_FRONT + 1) * 2 ) + (this.MIN_FRONT + 1);
            
            if(ulX + randoX >= lrX)
                randoX = (width / 2);
            if(ulY + randoY >= lrY)
                randoY = (length / 2);
            
            //A ulX, ulY
            //B ulX + randoX, ulY
            
            //D ulX, ulY + randoY
            //E ulX + randoX, ulY + randoY
            //F lrX, ulY + randoY
            
            //H ulX + randoX, lrY
            //I lrX, lrY

            //If the length was fine,
            //we only need to divide into top and bottom square (NORTH & SOUTH)
            if( length <= bd.getIdealFace() + 1){
                this.subdivGen(bf, orient, ulX, ulY, lrX, ulY + randoY, this.lightDetermine(Cardinal.SOUTH, lightArr) ); //North AF (side 0 || 2, side 1)
                this.subdivGen(bf, orient, ulX, ulY + randoY, lrX, lrY, this.lightDetermine(Cardinal.NORTH, lightArr)  );//South DI (side 0 || 2, side 3)
            }
            //If the width was fine,
            //we only need to divide into left and right (WEST & EAST)
            else if(width <= bd.getIdealFace() + 1){
                this.subdivGen(bf, orient, ulX, ulY, ulX + randoX, lrY, this.lightDetermine(Cardinal.EAST, lightArr) ); //West AH (side 0, side 1 || 3)
                this.subdivGen(bf, orient, ulX + randoX, ulY, lrX, lrY, this.lightDetermine(Cardinal.WEST, lightArr) ); //East BI (side 2, side 1 || 3)
            }
            else{
                //Subdivide into 4 squares
                this.subdivGen(bf, orient, ulX, ulY, ulX + randoX, ulY + randoY, 
                        this.lightDetermine(Cardinal.SOUTH_EAST, lightArr) ); //Northwest AE (side 1, side 0) 
                
                this.subdivGen(bf, orient, ulX + randoX, ulY, lrX, ulY + randoY, 
                        this.lightDetermine(Cardinal.SOUTH_WEST, lightArr) ); //Northeast BF (side 1, side 2)
                
                this.subdivGen(bf, orient, ulX, ulY + randoY, ulX + randoX, lrY, 
                        this.lightDetermine(Cardinal.NORTH_EAST, lightArr) ); //Southwest DH (side 3, side 0)
                
                this.subdivGen(bf, orient, ulX + randoX, ulY + randoY, lrX, lrY, 
                        this.lightDetermine(Cardinal.NORTH_WEST, lightArr) ); //Southeast EI (side 3, side 2)
            }
        }        
    }
    
    private boolean[] lightCreate(Cardinal orient){
        boolean[] ret;
        
        ret = new boolean[]{false,false,false,false};
        
        switch(orient){
            default:
                break;
            case SOUTH_EAST:
                ret[BuildingFactory.SOUTH_INDEX] = true;
                ret[BuildingFactory.EAST_INDEX] = true;
                break;
            case NORTH_EAST:
                ret[BuildingFactory.NORTH_INDEX] = true;
                ret[BuildingFactory.EAST_INDEX] = true;
                break;
            case NORTH_WEST:
                ret[BuildingFactory.NORTH_INDEX] = true;
                ret[BuildingFactory.WEST_INDEX] = true;
                break;
            case SOUTH_WEST:
                ret[BuildingFactory.SOUTH_INDEX] = true;
                ret[BuildingFactory.WEST_INDEX] = true;
                break;
            case SOUTH:
                ret[BuildingFactory.NORTH_INDEX] = true;
                break;
            case EAST:
                ret[BuildingFactory.EAST_INDEX] = true;
                break;
            case NORTH:
                ret[BuildingFactory.NORTH_INDEX] = true;
                break;
            case WEST:
                ret[BuildingFactory.WEST_INDEX] = true;
                break;
        }
        
        return ret;
    }
    
    private boolean[] lightDetermine(Cardinal newDirect, boolean[] lightArr){
        boolean[] ret = new boolean[]{false,false,false,false};
        
        switch(newDirect){
            default:
                break;
            case SOUTH_EAST:
                ret[BuildingFactory.SOUTH_INDEX] = ret[BuildingFactory.SOUTH_INDEX] || lightArr[BuildingFactory.SOUTH_INDEX];
                ret[BuildingFactory.EAST_INDEX] = ret[BuildingFactory.EAST_INDEX] || lightArr[BuildingFactory.EAST_INDEX];
                break;
            case NORTH_EAST:
                ret[BuildingFactory.NORTH_INDEX] = ret[BuildingFactory.NORTH_INDEX] || lightArr[BuildingFactory.NORTH_INDEX];
                ret[BuildingFactory.EAST_INDEX] = ret[BuildingFactory.EAST_INDEX] || lightArr[BuildingFactory.EAST_INDEX];
                break;
            case NORTH_WEST:
                ret[BuildingFactory.NORTH_INDEX] = ret[BuildingFactory.NORTH_INDEX] || lightArr[BuildingFactory.NORTH_INDEX];
                ret[BuildingFactory.WEST_INDEX] = ret[BuildingFactory.WEST_INDEX] || lightArr[BuildingFactory.WEST_INDEX];
                break;
            case SOUTH_WEST:
                ret[BuildingFactory.SOUTH_INDEX] = ret[BuildingFactory.SOUTH_INDEX] || lightArr[BuildingFactory.SOUTH_INDEX];
                ret[BuildingFactory.WEST_INDEX] = ret[BuildingFactory.WEST_INDEX] || lightArr[BuildingFactory.WEST_INDEX];
                break;
            case SOUTH:
                ret[BuildingFactory.SOUTH_INDEX] = ret[BuildingFactory.SOUTH_INDEX] || lightArr[BuildingFactory.SOUTH_INDEX];
                ret[BuildingFactory.EAST_INDEX] = ret[BuildingFactory.EAST_INDEX] || lightArr[BuildingFactory.EAST_INDEX];
                ret[BuildingFactory.WEST_INDEX] = ret[BuildingFactory.WEST_INDEX] || lightArr[BuildingFactory.WEST_INDEX];     
                break;
            case EAST:           
                ret[BuildingFactory.EAST_INDEX] = ret[BuildingFactory.EAST_INDEX] || lightArr[BuildingFactory.EAST_INDEX];
                ret[BuildingFactory.SOUTH_INDEX] = ret[BuildingFactory.SOUTH_INDEX] || lightArr[BuildingFactory.SOUTH_INDEX];
                ret[BuildingFactory.NORTH_INDEX] = ret[BuildingFactory.NORTH_INDEX] || lightArr[BuildingFactory.NORTH_INDEX];
                break;
            case NORTH:
                ret[BuildingFactory.NORTH_INDEX] = ret[BuildingFactory.NORTH_INDEX] || lightArr[BuildingFactory.NORTH_INDEX];
                ret[BuildingFactory.EAST_INDEX] = ret[BuildingFactory.EAST_INDEX] || lightArr[BuildingFactory.EAST_INDEX];
                ret[BuildingFactory.WEST_INDEX] = ret[BuildingFactory.WEST_INDEX] || lightArr[BuildingFactory.WEST_INDEX];
                
                break;
            case WEST:
                ret[BuildingFactory.WEST_INDEX] = ret[BuildingFactory.WEST_INDEX] || lightArr[BuildingFactory.WEST_INDEX];
                ret[BuildingFactory.SOUTH_INDEX] = ret[BuildingFactory.SOUTH_INDEX] || lightArr[BuildingFactory.SOUTH_INDEX];
                ret[BuildingFactory.NORTH_INDEX] = ret[BuildingFactory.NORTH_INDEX] || lightArr[BuildingFactory.NORTH_INDEX];
                break;
        }
        
        return ret;
    } 
}
