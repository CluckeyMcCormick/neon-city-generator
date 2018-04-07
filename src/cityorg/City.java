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

    private final int x_block_len;
    private final int z_block_len;

    //pb - per block length / width
    private final int x_perblock_len;    
    private final int z_perblock_len;

    private int[][] grid_heights;
    
    //The roads that run parallel to the x axis; indexed by z
    private RoadSize[] x_para_sizes;
    //The roads that run parallel to the z axis; indexed by x
    private RoadSize[] z_para_sizes;
    
    public City(int x_block_len, int z_block_len, int x_perblock_len, int z_perblock_len) {
        this.x_block_len = x_block_len;
        this.z_block_len = z_block_len;
        this.x_perblock_len = x_perblock_len;
        this.z_perblock_len = z_perblock_len;
        
        //Intersection Heights are adressed x, y
        grid_heights = new int[x_block_len + 1][z_block_len + 1];
        
        x_para_sizes = new RoadSize[z_block_len + 1];
        z_para_sizes = new RoadSize[x_block_len + 1];
        
        this.generateHeights();
        this.generateRoads();
        
        super.setNode( new Node() );
    }
    
    private void generateHeights(){
        RandomSingleton rand = RandomSingleton.getInstance();
        //We have a max grading the hills are allowed to be 
        double max_grade = Math.floor(.2275);

        for(int x = 0; x < grid_heights.length; x++)
            for(int z = 0; z < grid_heights[x].length; z++)
                grid_heights[x][z] = getHeight(x, z, max_grade);
    }
    
    private int getHeight(int x, int z, double grade){
        return (x * 7) + (z * 3);
    }
    
    private void generateRoads(){
        RandomSingleton rand = RandomSingleton.getInstance();
        RoadSize[] vals = RoadSize.values();
                
        for(int z = 0; z < x_para_sizes.length; z++)
            x_para_sizes[z] = vals[ rand.nextInt(vals.length) ];
        
        for(int x = 0; x < z_para_sizes.length; x++)
            z_para_sizes[x] = vals[ rand.nextInt(vals.length) ]; 
        
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
        int[] block_height;
        
        int adjust_x;
        int adjust_z;
        
        int x_len;
        int z_len;
        
        for(int x = 0; x < x_block_len; x++)
            for(int z = 0; z < z_block_len; z++){
                block_height = new int[4];
                
                block_height[0] = grid_heights[x + 1][z + 1];
                block_height[1] = grid_heights[x + 1][z];        
                block_height[2] = grid_heights[x][z];
                block_height[3] = grid_heights[x][z + 1];
                
                block_height[0] = grid_heights[x][z];
                block_height[1] = grid_heights[x][z + 1];        
                block_height[2] = grid_heights[x + 1][z + 1];
                block_height[3] = grid_heights[x + 1][z];
                
                //Formulas for "roadSizes" arrays
                adjust_x = z_para_sizes[x].unitWidth / 2; 
                adjust_z = x_para_sizes[z].unitWidth / 2;
                x_len = x_perblock_len - (adjust_x + (z_para_sizes[x + 1].unitWidth / 2) );
                z_len = z_perblock_len - (adjust_z + (x_para_sizes[z + 1].unitWidth / 2));

                int[] no_cuts = new int[]{0, 0, 0, 0};
                block = new BlockAlley(detail, block_height, no_cuts, x_len, z_len, true);
                //block = new BlockBlank(detail, block_height, no_cuts, bLength, bWidth);
                
                block.generateBuildings(factory);
                block.setComboTranslation(
                    (x * x_perblock_len) + adjust_x, 0, (z * z_perblock_len) + adjust_z, 
                    0, 0, 0
                );
                this.addFeature( block.getNode() );
            }
    }
    
    private void buildIntersections(BuildingFactory factory){
        Geometry section;
        
        float new_x;
        float new_z;
        
        int x_len;
        int z_len;
                
        for(int x = 0; x < grid_heights.length; x++)
            for(int z = 0; z < grid_heights[x].length; z++){
                x_len = z_para_sizes[x].unitWidth;
                z_len = x_para_sizes[z].unitWidth;
                
                section = factory.intersection(grid_heights[x][z], z_len, x_len);
                
                new_x = (x * x_perblock_len - (x_len / 2)) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
                new_z = (z * z_perblock_len - (z_len / 2)) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
       
                section.move(new_x, 0, new_z);
                this.addFeature(section);
            }
    }
    
    private void buildRoads(BuildingFactory factory){
        Geometry section;
        
        float newX;
        float newZ;
        
        int z_len;
        int x_len;
        int[] heights;
        
        //Generate the Z-parallel streets
        for(int x = 0; x < z_para_sizes.length; x++){
            x_len = z_para_sizes[x].unitWidth;
            
            for(int z = 0; z < x_para_sizes.length - 1; z++){
                z_len = z_perblock_len - (x_para_sizes[z].unitWidth / 2) - (x_para_sizes[z + 1].unitWidth / 2);
                heights = new int[]{ 
                    this.grid_heights[x][z], this.grid_heights[x][z + 1],
                    this.grid_heights[x][z + 1], this.grid_heights[x][z]
                };
                
                section = factory.road(heights, x_len, z_len);
                
                newX = (x * x_perblock_len - x_len / 2) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
                newZ = (z * z_perblock_len + x_para_sizes[z].unitWidth / 2) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
                
                section.move(newX, 0, newZ);
                this.addFeature(section);
            }
        }

        //Generate the X-parallel streets
        for(int z = 0; z < x_para_sizes.length; z++){
            z_len = x_para_sizes[z].unitWidth;
            
            for(int x = 0; x < z_para_sizes.length - 1; x++){
                x_len = x_perblock_len - (z_para_sizes[x].unitWidth / 2) - (z_para_sizes[x + 1].unitWidth / 2);
                heights = new int[]{ 
                    this.grid_heights[x][z], this.grid_heights[x][z],
                    this.grid_heights[x + 1][z], this.grid_heights[x + 1][z]
                };
                
                section = factory.road(heights, x_len, z_len);
                
                newX = (x * x_perblock_len + z_para_sizes[x].unitWidth / 2) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
                newZ = (z * z_perblock_len - z_len / 2) * GOLDEN_PIXEL_COUNT * VIRTUAL_LENGTH_PER_PIXEL;
                
                section.move(newX, 0, newZ);

                this.addFeature(section);
            }
        }
       
    }
}
