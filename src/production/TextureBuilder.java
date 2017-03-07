/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package production;

import building.BuildingDetail;
import java.awt.Color;
import java.awt.image.BufferedImage;
import static production.CellState.DIM;
import static production.CellState.LIT;
import building.WindowStyle;
import com.jme3.math.ColorRGBA;
import prime.RandomSingleton;

/**
 *
 * @author root
 */
public class TextureBuilder {
    
    public static final int TEXTURE_X = 1024;
    public static final int TEXTURE_Y = 1024;
    
    public static final int PERCENT_DIM = 40;
    public static final int PERCENT_OFF = 100 - PERCENT_DIM - 20;
    
    public static final int LIT_INDEX = 0;
    public static final int DIM_INDEX = 1;
    
    private TextureBuilder(){}
    
    public static BufferedImage[][] drawOffices(BuildingDetail bd){
        BufferedImage[][] bis;
        WindowStyle[] styles;
        
        styles = WindowStyle.values();
        bis = new BufferedImage[styles.length][2];
        
        for(int i = 0; i < styles.length; i++){
            bis[i][LIT_INDEX] = new BufferedImage(TEXTURE_X, TEXTURE_Y, BufferedImage.TYPE_INT_ARGB);
            bis[i][DIM_INDEX] = new BufferedImage(TEXTURE_X, TEXTURE_Y, BufferedImage.TYPE_INT_ARGB);

            switch(styles[i]){
                case RIBBON:
                    ribbonDraw(bd, bis[i]);
                    break;
                case RANDOM:
                    randomDraw(bd, bis[i]);
                    break;
                case VERTICAL:
                    verticalDraw(bd, bis[i]);
                    break;
                case STANDARD:
                default:
                    normalDraw(bd, bis[i]);
                    break;
            }
        }
        
        return bis;
    }
    
    private static void normalDraw(BuildingDetail bd, BufferedImage[] bis){
        int rando;
        CellState state;
        RandomSingleton rand = RandomSingleton.getInstance();
        
        rando = 0;
        state = CellState.NONE;
        
        int totalFloors = TEXTURE_Y / bd.getCellPixHeight();
        int totalCells = TEXTURE_X / bd.getCellPixWidth();
        
        for(int floor = 0; floor < totalFloors; floor++)
            for(int cell = 0; cell < totalCells; cell++){
                if (state == CellState.NONE){
                    rando = rand.nextInt(100);
                    if(rando <= PERCENT_DIM - 1 )
                        state = CellState.DIM;
                    else if ( rando <= PERCENT_DIM + PERCENT_OFF - 1)
                        state = CellState.OFF;
                    else 
                        state = CellState.LIT;

                    rando = rand.nextInt( state.max - state.min ) + state.min;
                }
                
                drawCell(floor, cell, state, bis[LIT_INDEX], bis[DIM_INDEX], bd);
                
                rando--;
                if(rando <= 0)
                    state = CellState.NONE;
            }
    }
    
    private static void verticalDraw(BuildingDetail bd, BufferedImage[] bis){
        int rando;
        CellState state;
        RandomSingleton rand = RandomSingleton.getInstance(); 
        
        rando = 0;
        state = CellState.NONE;
        
        int totalFloors = TEXTURE_Y / bd.getCellPixHeight();
        int totalCells = TEXTURE_X / bd.getCellPixWidth();
        
        for(int cell = 0; cell < totalCells; cell++)
            for(int floor = 0; floor < totalFloors; floor++){
                if (state == CellState.NONE){
                    rando = rand.nextInt(100);
                    if(rando <= PERCENT_DIM - 1 )
                        state = CellState.DIM;
                    else if ( rando <= PERCENT_DIM + PERCENT_OFF - 1)
                        state = CellState.OFF;
                    else 
                        state = CellState.LIT;

                    rando = rand.nextInt( state.max - state.min ) + state.min;
                }
                
                drawCell(floor, cell, state, bis[LIT_INDEX], bis[DIM_INDEX], bd);
                
                rando--;
                if(rando <= 0)
                    state = CellState.NONE;
            }
    }
    
    private static void randomDraw(BuildingDetail bd, BufferedImage[] bis){
        int rando;
        CellState state;
        RandomSingleton rand = RandomSingleton.getInstance();
        
        int totalFloors = TEXTURE_Y / bd.getCellPixHeight();
        int totalCells = TEXTURE_X / bd.getCellPixWidth();
        
        for(int floor = 0; floor < totalFloors; floor++)
            for(int cell = 0; cell < totalCells; cell++){
                rando = rand.nextInt(100);
                if(rando <= PERCENT_DIM - 1 )
                    state = CellState.DIM;
                else if ( rando <= PERCENT_DIM + PERCENT_OFF - 1)
                    state = CellState.OFF;
                else 
                    state = CellState.LIT;
                
                drawCell(floor, cell, state, bis[LIT_INDEX], bis[DIM_INDEX], bd);
            }
    }
    
    private static void ribbonDraw(BuildingDetail bd, BufferedImage[] bis){
        int rando;
        CellState state;
        RandomSingleton rand = RandomSingleton.getInstance();
        
        rando = 0;
        state = CellState.NONE;
        
        int totalFloors = TEXTURE_Y / bd.getCellPixHeight();
        int totalCells = TEXTURE_X / bd.getCellPixWidth();
        
        boolean[][] trashPyramid = new boolean[totalFloors][totalCells]; 
        
        int shift = 0;
        int floor = 0;
        
        for(int i = 0; i < (totalFloors + totalCells); i++){
            if(i < totalFloors)
                shift = 0;
            else
                shift = i - totalFloors;
            for(int j = 0; floor - j >= 0 && shift + j < totalCells; j++){
            
                if (state == CellState.NONE){
                     rando = rand.nextInt(100);
                     if(rando <= PERCENT_DIM - 1 )
                         state = CellState.DIM;
                     else if ( rando <= PERCENT_DIM + PERCENT_OFF - 1)
                         state = CellState.OFF;
                     else 
                         state = CellState.LIT;

                     rando = rand.nextInt( state.max - state.min ) + state.min;
                 }

                 drawCell(floor - j, shift + j, state, bis[LIT_INDEX], bis[DIM_INDEX], bd);

                 trashPyramid[floor - j][shift + j] = true;
                 
                 rando--;
                 if(rando <= 0)
                     state = CellState.NONE;
             }
            if(floor < totalFloors - 1)
                floor++;
        }
        
        for(int i = 0; i < trashPyramid.length; i++)
            for(int j = 0; j < trashPyramid[i].length; j++)
                if(!trashPyramid[i][j])
                    System.out.println(i + ", " + j);
        
    }
    
    private static void drawCell( 
        int floor, int cell, CellState state, BufferedImage biLit, 
        BufferedImage biDim, BuildingDetail bd
    ){
        RandomSingleton rs = RandomSingleton.getInstance();
        BufferedImage bi;
        BufferedImage biAlt;
        int blank;
        int altBlank;
        int gaussed;
        int baseValue;
        int deviation;
   
        baseValue = 218;
        deviation = 32;
        
        switch(state){
            case LIT:
                bi = biLit;
                biAlt = biDim;
                blank = Color.TRANSLUCENT;
                altBlank = Color.WHITE.getRGB();
                break;
            case DIM:
                bi = biDim;
                biAlt = biLit;
                blank = Color.WHITE.getRGB();
                altBlank = Color.TRANSLUCENT;
                break;
            default:
                bi = biLit;
                biAlt = biDim;
                blank = Color.TRANSLUCENT;
                altBlank = Color.WHITE.getRGB();
                break;
        }
        
        for(int y = 0; y < bd.getCellPixHeight(); y++){
            baseValue = baseValue % 256;
            for(int x = 0; x < bd.getCellPixWidth(); x++){
                //Paint the alt 
                biAlt.setRGB( cell * bd.getCellPixWidth() + x, floor * bd.getCellPixHeight() + y, altBlank );
                
                //If this is the "blank space" between windows
                if( bd.inBlank(x, y) || state.isBlank )
                    bi.setRGB( cell * bd.getCellPixWidth() + x, floor * bd.getCellPixHeight() + y, blank );
                else {
                    gaussed = baseValue + Math.round( (float) rs.nextGaussian() * deviation);
                    gaussed = Math.abs(gaussed) % 256;
                    
                    if(gaussed < baseValue - deviation)
                        gaussed = baseValue;
                            
                    bi.setRGB( 
                            cell * bd.getCellPixWidth() + x, 
                            floor * bd.getCellPixHeight() + y, 
                            new Color(gaussed,gaussed,gaussed).getRGB() );        
                }
            }
            baseValue -= 1;
        }
    }   
}
