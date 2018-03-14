/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg.blocktype;

import building.BuildingFactory;
import building.type.FloorCellBuilding;
import cityorg.BlockDetail;
import cityorg.CityBlock;
import com.jme3.scene.Node;
import prime.RandomSingleton;

/**
 *
 * @author frick
 */
public class BlockFull extends CityBlock{
    
    public BlockFull(BlockDetail blockDet, int[] unitHeight, 
            int unitLength, int unitWidth, int lengthCut, int widthCut
    ){
        super(blockDet, unitHeight, unitLength, unitWidth, lengthCut, widthCut);
    }
    
    public void generateBuildings(BuildingFactory bf) {
        this.subdivGen(bf, 0, 0, this.getUnitLength(), this.getUnitWidth());
        super.generateBuildings(bf);
    }
    
    private void subdivGen(BuildingFactory bf, int ulX, int ulY, int lrX, int lrY) {
        RandomSingleton rand = RandomSingleton.getInstance();      
        FloorCellBuilding b;
        BlockDetail bd;
        Node nd;
        
        int width = lrY - ulY;
        int length = lrX - ulX;
        
        int height;
        
        bd = this.getBlockDet();
        
        System.out.println(length + " " + width + " - " + bd.getIdealFace());
        
        if( width <= bd.getIdealFace() && length <= bd.getIdealFace() ){
            nd = this.getNode();
            
            height = bd.getIdealHeight() + (int)( bd.getHeightDeviant() * rand.nextGaussian() );
            
            b = bf.randomFCB( width, length, height, 1);

            b.setComboTranslation( 
                ulX, 0, ulY,
                b.virtualLength() / 2, b.virtualHeight() / 2, b.virtualWidth() / 2
            );
            
            nd.attachChild( b.getNode() );
        } 
        else{
            int randoY = (width / 2) + (int) (rand.nextGaussian() * bd.getFaceDeviant() );
            int randoX = (length / 2) + (int) (rand.nextGaussian() * bd.getFaceDeviant() );
            
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
            //we only need to divide into top and bottom square
            if( length <= bd.getIdealFace() ){
                this.subdivGen(bf, ulX, ulY, lrX, ulY + randoY ); //North AF
                this.subdivGen(bf, ulX, ulY + randoY, lrX, lrY );//South DI
            }
            //If the width was fine,
            //we only need to divide into left and right
            else if(width <= bd.getIdealFace()){                
                this.subdivGen(bf, ulX, ulY, ulX + randoX, lrY ); //West AH
                this.subdivGen(bf, ulX + randoX, ulY, lrX, lrY ); //East BI
            }
            else{
                //Subdivide into 4 squares
                this.subdivGen(bf, ulX, ulY, ulX + randoX, ulY + randoY); //Northwest AE
                this.subdivGen(bf, ulX + randoX, ulY, lrX, ulY + randoY); //Northeast BF
                this.subdivGen(bf, ulX, ulY + randoY, ulX + randoX, lrY); //Southwest DH
                this.subdivGen(bf, ulX + randoX, ulY + randoY, lrX, lrY); //Southeast EI
            }
   
        }        
    }
}
