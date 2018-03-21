/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import building.BuildingFactory;
import cityorg.blocktype.BlockAlley;
import cityorg.blocktype.BlockBlank;
import com.jme3.scene.Node;
import prime.RandomSingleton;

/**
 *
 * @author frick
 */
public class City extends CityStructure{

    private final int blockLength;
    private final int blockWidth;

    //pb - per block length / width
    private final int pbUnitLength;    
    private final int pbUnitWidth;

    private int[][] intersectHeights;
    private RoadSize[][] roadSizes;
    
    public City(int blockLength, int blockWidth, int pbUnitLength, int pbUnitWidth) {
        this.blockLength = blockLength;
        this.blockWidth = blockWidth;
        this.pbUnitLength = pbUnitLength;
        this.pbUnitWidth = pbUnitWidth;
        
        //Intersection Heights are adressed x, y
        intersectHeights = new int[blockLength + 1][blockWidth + 1];
        
        //Road sizes are adressed y, x
        roadSizes = new RoadSize[3 + (2 * (blockWidth - 1))][];
        for(int i = 0; i < roadSizes.length; i++){
            if( i % 2 == 0)
                roadSizes[i] = new RoadSize[blockLength];
            else
                roadSizes[i] = new RoadSize[blockLength + 1];
        }
        
        this.generateHeights();
        this.generateRoads();
        
        super.setNode( new Node() );
    }
    
    private void generateHeights(){
        RandomSingleton rand = RandomSingleton.getInstance();
         
        for(int x = 0; x < intersectHeights.length; x++)
            for(int y = 0; y < intersectHeights[x].length; y++)
                intersectHeights[x][y] = rand.nextInt(16);
    }
    
    private void generateRoads(){
        RandomSingleton rand = RandomSingleton.getInstance();
        RoadSize[] vals = RoadSize.values();
                
        for(int y = 0; y < roadSizes.length; y++)
            for(int x = 0; x < roadSizes[y].length; x++)
                roadSizes[y][x] = RoadSize.MEDIUM_STREET; //vals[ rand.nextInt(vals.length) ]; //RoadSize.LARGE_STREET; 
    }
    
    public void generateBlocks(BlockDetail detail, BuildingFactory factory){
        CityBlock block;
        int[] blockHeight;
        
        int adjustX;
        int adjustY;
        
        int bLength;
        int bWidth;
        
        int ty;
        
        for(int x = 0; x < blockLength; x++)
            for(int y = 0; y < blockWidth; y++){
                blockHeight = new int[4];
                
                blockHeight[0] = intersectHeights[x + 1][y + 1];
                blockHeight[1] = intersectHeights[x + 1][y];        
                blockHeight[2] = intersectHeights[x][y];
                blockHeight[3] = intersectHeights[x][y + 1];
                
                //"True" y - one that we've adjusted for
                ty = y * 2;
                
                adjustX = roadSizes[ty + 1][x].unitWidth / 2; 
                adjustY = roadSizes[ty][x].unitWidth / 2;
                
                bLength = pbUnitLength - (adjustX + (roadSizes[ty + 1][x + 1].unitWidth / 2) );
                bWidth = pbUnitWidth - (adjustY + (roadSizes[ty + 2][x].unitWidth / 2));
                
                int[] cuts = new int[]{0, 8, 0, 8};
                cuts = new int[]{8, 8, 8, 8};
                
                int[] no_cuts = new int[]{0, 0, 0, 0};
                if(x >= blockLength / 2)
                    block = new BlockBlank(detail, blockHeight, cuts, bLength, bWidth);
                else
                    block = new BlockBlank(detail, blockHeight, no_cuts, bLength, bWidth);
                
                block.generateBuildings(factory);
                block.setComboTranslation(
                    (x * pbUnitLength) + adjustX, 0, (y * pbUnitWidth) + adjustY, 
                    0, 0, 0
                );
                this.addFeature( block.getNode() );
            }
    }
}
