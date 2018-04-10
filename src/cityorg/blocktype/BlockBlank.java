/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg.blocktype;

import cityorg.BlockDetail;
import cityorg.CityBlock;
import production.MaterialBook;

/**
 * A city block that generates no buildings. Primarily used to generate
 * 
 * @author frick
 */
public class BlockBlank extends CityBlock {
    
    public BlockBlank(MaterialBook mat_book, BlockDetail bd, 
        int unit_x, int unit_z, int[] unitHeight
    ) {
        super(mat_book, bd, unit_x, unit_z, unitHeight );
    }
    
}
