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
import com.jme3.scene.Node;
import prime.RandomSingleton;

/**
 *
 * @author frick
 */
public class BlockAlley extends CityBlock{
    
    private boolean scramble;
    
    public BlockAlley(
        BlockDetail blockDet,  int[] unitHeight, int[] cardinalCuts,
        int unitLength, int unitWidth, boolean scramble
    ){
        super(blockDet, unitHeight, cardinalCuts, unitLength, unitWidth);
        
        this.scramble = scramble;
    }
    
    @Override
    public void generateBuildings(BuildingFactory bf) {
        
        int lengthTotal = this.getUnitLength();
        int widthTotal = this.getUnitWidth();

        this.subdivGen(bf, Cardinal.SOUTH_EAST, 0, 0, lengthTotal / 2, widthTotal / 2 );
        this.subdivGen(bf, Cardinal.SOUTH_WEST, lengthTotal / 2, 0, lengthTotal, widthTotal / 2);
        this.subdivGen(bf, Cardinal.NORTH_WEST, lengthTotal / 2, widthTotal / 2, lengthTotal, widthTotal);
        this.subdivGen(bf, Cardinal.NORTH_EAST, 0, widthTotal / 2, lengthTotal / 2, widthTotal);
       
        super.generateBuildings(bf);
    }
    
    private void subdivGen(BuildingFactory bf, Cardinal orient, int ulX, int ulY, int lrX, int lrY) {
        RandomSingleton rand = RandomSingleton.getInstance();      
        FloorCellBuilding b;
        BlockDetail bd;
        Node nd;
        
        int width = lrY - ulY;
        int length = lrX - ulX;
        
        float heightAdj;
        int height;
        
        int moveX;
        int moveY;
        
        bd = this.getBlockDet();
        
        //If our current section is at the ideal length, make a building
        if( width <= bd.getIdealFace() + 1 && length <= bd.getIdealFace() + 1 ){
            nd = this.getNode();
            
            height = bd.getIdealHeight() + (int)( bd.getHeightDeviant() * rand.nextGaussian() );    
            
            heightAdj = this.calcHeightAdjust(ulX, ulY, length - 1, width - 1);
            
            b = bf.randomFCB( width - 1, length - 1, height, heightAdj );

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
            
            b.setComboTranslation( 
                moveX, 0, moveY,
                0, 0, 0
            );
          
            nd.attachChild( b.getNode() );
        }
        //
        else{
            int randoX = 0;
            int randoY = 0;
            
            if(width > bd.getIdealFace())
                randoY = rand.nextInt( width - (MIN_FRONT + 1) * 2 ) + (MIN_FRONT + 1);

            if(length > bd.getIdealFace())
                randoX = rand.nextInt( length - (MIN_FRONT + 1) * 2 ) + (MIN_FRONT + 1);
            
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
                //North AF (side 0 || 2, side 1)
                this.subdivGen(bf, orient, ulX, ulY, lrX, ulY + randoY);
                //South DI (side 0 || 2, side 3)
                this.subdivGen(bf, orient, ulX, ulY + randoY, lrX, lrY);
            }
            //If the width was fine,
            //we only need to divide into left and right (WEST & EAST)
            else if(width <= bd.getIdealFace() + 1){
                //West AH (side 0, side 1 || 3)
                this.subdivGen(bf, orient, ulX, ulY, ulX + randoX, lrY); 
                //East BI (side 2, side 1 || 3)
                this.subdivGen(bf, orient, ulX + randoX, ulY, lrX, lrY); 
            }
            else{
                //Subdivide into 4 squares
                //Northwest AE (side 1, side 0)
                this.subdivGen(bf, orient, ulX, ulY, ulX + randoX, ulY + randoY);  
                //Northeast BF (side 1, side 2)
                this.subdivGen(bf, orient, ulX + randoX, ulY, lrX, ulY + randoY); 
                //Southwest DH (side 3, side 0)
                this.subdivGen(bf, orient, ulX, ulY + randoY, ulX + randoX, lrY); 
                //Southeast EI (side 3, side 2)
                this.subdivGen(bf, orient, ulX + randoX, ulY + randoY, lrX, lrY); 
            }
        }        
    }
}
