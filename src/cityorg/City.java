/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import building.BuildingFactory;
import cityorg.blocktype.BlockAlley;
import cityorg.blocktype.BlockBlank;
import com.jme3.scene.Geometry;
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
    
    private RoadSize[] horizSizes;
    private RoadSize[] vertSizes;
    
    public City(int blockLength, int blockWidth, int pbUnitLength, int pbUnitWidth) {
        this.blockLength = blockLength;
        this.blockWidth = blockWidth;
        this.pbUnitLength = pbUnitLength;
        this.pbUnitWidth = pbUnitWidth;
        
        //Intersection Heights are adressed x, y
        intersectHeights = new int[blockLength + 1][blockWidth + 1];
        
        horizSizes = new RoadSize[this.blockWidth + 1];
        vertSizes = new RoadSize[this.blockLength + 1];
        
        this.generateHeights();
        this.generateRoads();
        
        super.setNode( new Node() );
    }
    
    private void generateHeights(){
        RandomSingleton rand = RandomSingleton.getInstance();
        //We have a max grading the hills are allowed to be 
        double max_grade = Math.floor(.2275);

        for(int x = 0; x < intersectHeights.length; x++)
            for(int y = 0; y < intersectHeights[x].length; y++)
                intersectHeights[x][y] = getHeight(x, y, max_grade);
    }
    
    private int getHeight(int x, int y, double grade){
        return x * 7;
    }
    
    private void generateRoads(){
        RandomSingleton rand = RandomSingleton.getInstance();
        RoadSize[] vals = RoadSize.values();
                
        for(int y = 0; y < horizSizes.length; y++)
            horizSizes[y] = vals[ rand.nextInt(vals.length) ];
        
        for(int x = 0; x < vertSizes.length; x++)
            vertSizes[x] = vals[ rand.nextInt(vals.length) ]; 
        
        //vals[ rand.nextInt(vals.length) ];
    }
    
    public void build(BlockDetail detail, BuildingFactory factory){
        //Step 1: Generate the blocks, block by block
        this.buildBlocks(detail, factory);
        //Step 2: Generate the intersections, intersection by intersection
        this.buildIntersections(factory);
        //Step 3: Generate the roads
        this.buildRoads(factory);
    }
    
    private void buildBlocks(BlockDetail detail, BuildingFactory factory){
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
                
                //Formulas for "roadSizes" arrays
                adjustX = vertSizes[x].unitWidth / 2; 
                adjustY = horizSizes[y].unitWidth / 2;
                bLength = pbUnitLength - (adjustX + (vertSizes[x + 1].unitWidth / 2) );
                bWidth = pbUnitWidth - (adjustY + (horizSizes[y + 1].unitWidth / 2));

                int[] no_cuts = new int[]{0, 0, 0, 0};
                block = new BlockAlley(detail, blockHeight, no_cuts, bLength, bWidth, true);
                //block = new BlockBlank(detail, blockHeight, no_cuts, bLength, bWidth);
                
                block.generateBuildings(factory);
                block.setComboTranslation(
                    (x * pbUnitLength) + adjustX, 0, (y * pbUnitWidth) + adjustY, 
                    0, 0, 0
                );
                this.addFeature( block.getNode() );
            }
    }
    
    private void buildIntersections(BuildingFactory factory){
        Geometry section;
        
        float newX;
        float newZ;
        
        int width;
        int length;
        
        for(int x = 0; x < intersectHeights.length; x++)
            for(int y = 0; y < intersectHeights[x].length; y++){
                width = horizSizes[y].unitWidth;
                length = vertSizes[x].unitWidth;
                
                section = factory.intersection(intersectHeights[x][y], width, length);
                
                newX = (x * pbUnitLength - (length / 2)) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
                newZ = (y * pbUnitWidth - (width / 2)) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
       
                section.move(newX, 0, newZ);
                this.addFeature(section);
            }
    }
    
    private void buildRoads(BuildingFactory factory){
        Geometry section;
        
        float newX;
        float newZ;
        
        int width;
        int length;
        int[] heights;
        
        for(int x = 0; x < vertSizes.length; x++){
            length = vertSizes[x].unitWidth;
            
            for(int y = 0; y < horizSizes.length - 1; y++){
                width = pbUnitWidth - (horizSizes[y].unitWidth / 2) - (horizSizes[y + 1].unitWidth / 2);
                heights = new int[]{ 
                    this.intersectHeights[x][y + 1], this.intersectHeights[x][y],
                    this.intersectHeights[x][y], this.intersectHeights[x][y + 1]
                };
                
                section = factory.road(heights, width, length);
                
                newX = (x * pbUnitLength - length / 2) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
                newZ = (y * pbUnitWidth + horizSizes[y].unitWidth / 2) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
                
                section.move(newX, 0, newZ);
                this.addFeature(section);
            }
        }

        for(int y = 0; y < horizSizes.length; y++){
            width = horizSizes[y].unitWidth;
            
            for(int x = 0; x < vertSizes.length - 1; x++){
                length = pbUnitLength - (vertSizes[x].unitWidth / 2) - (vertSizes[x + 1].unitWidth / 2);
                heights = new int[]{ 
                    this.intersectHeights[x + 1][y], this.intersectHeights[x + 1][y],
                    this.intersectHeights[x][y], this.intersectHeights[x][y]
                };
                
                section = factory.road(heights, width, length);
                
                newX = (x * pbUnitLength + vertSizes[x].unitWidth / 2) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
                newZ = (y * pbUnitWidth - width / 2) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
                
                section.move(newX, 0, newZ);
                this.addFeature(section);
            }
        }
    }
}
