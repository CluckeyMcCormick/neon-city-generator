/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg.blocktype;

import cityorg.BlockDetail;
import cityorg.CityBlock;

/**
 * A city block that generates no buildings. Primarily used to generate
 * 
 * @author frick
 */
public class BlockBlank extends CityBlock {
    
    public BlockBlank(BlockDetail bd, int[] unitHeight, int unitLength, int unitWidth, int lengthCut, int widthCut) {
        super(bd, unitHeight, unitLength, unitWidth, lengthCut, widthCut);
    }
    
}
