/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg.blocktype;

import building.BasicBuildingTemplate;
import building.BuildingInstance;
import building.WindowStyle;
import cityorg.BlockDetail;
import cityorg.Cardinal;
import cityorg.CityBlock;
import cityorg.CityStructure;
import com.jme3.scene.Node;
import prime.RandomSingleton;
import production.MaterialBook;

/**
 *
 * @author frick
 */
public class BlockAlley extends CityBlock{
    
    private boolean scramble;
    
    public BlockAlley(MaterialBook mat_book, BlockDetail bd,  
        int unit_x, int unit_z, int[] unitHeight, boolean scramble
    ){
        super(mat_book, bd, unit_x, unit_z, unitHeight);
        
        this.scramble = scramble;
    }
    
    @Override
    public void generateBuildings() {
        
        int lengthTotal = this.getUnitLength();
        int widthTotal = this.getUnitWidth();

        this.subdivGen(Cardinal.SOUTH_EAST, 0, 0, lengthTotal / 2, widthTotal / 2 );
        this.subdivGen(Cardinal.SOUTH_WEST, lengthTotal / 2, 0, lengthTotal, widthTotal / 2);
        this.subdivGen(Cardinal.NORTH_WEST, lengthTotal / 2, widthTotal / 2, lengthTotal, widthTotal);
        this.subdivGen(Cardinal.NORTH_EAST, 0, widthTotal / 2, lengthTotal / 2, widthTotal);
       
        super.generateBuildings();
    }
    
    private void subdivGen(Cardinal orient, int ulX, int ulZ, int lrX, int lrZ) {
        RandomSingleton rand = RandomSingleton.getInstance();      
        BuildingInstance b;
        BlockDetail bd;
        Node nd;

        int unit_x = lrX - ulX;
        int unit_z = lrZ - ulZ;
        
        float heightAdj;
        int height;
        
        int moveX;
        int moveY;
        
        bd = this.getBlockDet();
        
        //If our current section is at the ideal length, make a building
        if( unit_x <= bd.getIdealFace() + 1 && unit_x <= bd.getIdealFace() + 1 ){
            nd = this.getNode();
            
            height = bd.getIdealHeight() + (int)( bd.getHeightDeviant() * rand.nextGaussian() );    
            
            heightAdj = this.calcHeightAdjust(ulX, ulZ, unit_x - 1, unit_z - 1);
            
            heightAdj *= CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
            
            b = BasicBuildingTemplate.build(
                unit_x - 1, unit_z - 1, height, 
                this.mat_book.getRandomFullMat()
            );

            switch(orient){
                default:
                case SOUTH_EAST:
                    moveX = ulX;
                    moveY = ulZ;
                    break;
                case SOUTH_WEST:
                    moveX = ulX + 1;
                    moveY = ulZ;
                    break;
                case NORTH_WEST:
                    moveX = ulX + 1;
                    moveY = ulZ + 1;
                    break;
                case NORTH_EAST:
                    moveX = ulX;
                    moveY = ulZ + 1;
                    break;
            }
            
            b.setComboTranslation( 
                moveX, 0, moveY,
                0, heightAdj, 0
            );
          
            nd.attachChild( b.getNode() );
        }
        //
        else{
            int randoX = 0;
            int randoY = 0;
            
            if(unit_x > bd.getIdealFace())
                randoY = rand.nextInt( unit_x - (MIN_FRONT + 1) * 2 ) + (MIN_FRONT + 1);

            if(unit_x > bd.getIdealFace())
                randoX = rand.nextInt( unit_x - (MIN_FRONT + 1) * 2 ) + (MIN_FRONT + 1);
            
            if(ulX + randoX >= lrX)
                randoX = (unit_x / 2);
            if(ulZ + randoY >= lrZ)
                randoY = (unit_x / 2);
            
            //A ulX, ulY
            //B ulX + randoX, ulY
            
            //D ulX, ulY + randoY
            //E ulX + randoX, ulY + randoY
            //F lrX, ulY + randoY
            
            //H ulX + randoX, lrY
            //I lrX, lrY

            //If the length was fine,
            //we only need to divide into top and bottom square (NORTH & SOUTH)
            if( unit_x <= bd.getIdealFace() + 1){
                //North AF (side 0 || 2, side 1)
                this.subdivGen(orient, ulX, ulZ, lrX, ulZ + randoY);
                //South DI (side 0 || 2, side 3)
                this.subdivGen(orient, ulX, ulZ + randoY, lrX, lrZ);
            }
            //If the width was fine,
            //we only need to divide into left and right (WEST & EAST)
            else if(unit_x <= bd.getIdealFace() + 1){
                //West AH (side 0, side 1 || 3)
                this.subdivGen(orient, ulX, ulZ, ulX + randoX, lrZ); 
                //East BI (side 2, side 1 || 3)
                this.subdivGen(orient, ulX + randoX, ulZ, lrX, lrZ); 
            }
            else{
                //Subdivide into 4 squares
                //Northwest AE (side 1, side 0)
                this.subdivGen(orient, ulX, ulZ, ulX + randoX, ulZ + randoY);  
                //Northeast BF (side 1, side 2)
                this.subdivGen(orient, ulX + randoX, ulZ, lrX, ulZ + randoY); 
                //Southwest DH (side 3, side 0)
                this.subdivGen(orient, ulX, ulZ + randoY, ulX + randoX, lrZ); 
                //Southeast EI (side 3, side 2)
                this.subdivGen(orient, ulX + randoX, ulZ + randoY, lrX, lrZ); 
            }
        }        
    }
}
