package prime;

import production.ColorBook;
import production.DebugShapeFactory;

import building.CellTextureDetail;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

import cityorg.BlockDetail;
import cityorg.City;

import com.jme3.math.Vector3f;
import production.MaterialBook;
import production.MaterialBuilder;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        final int dimA = 16;
        final int widthA = 16;
        final int blankWidthA = 2;
        final int blankHeightA = 2;
        
        CellTextureDetail bd_low_band = new CellTextureDetail(
            widthA, dimA,
            dimA * 3, dimA / 2, dimA * 2,
            dimA * 9, dimA * 8,
            new int[] {blankWidthA, blankWidthA, blankHeightA, blankHeightA}
        );

        CellTextureDetail bd_high_band = new CellTextureDetail(
            widthA, dimA,
            dimA * 6, dimA / 2, dimA * 5,
            dimA * 12, dimA * 8,
            new int[] {blankWidthA, blankWidthA, blankHeightA, blankHeightA}
        );
        
        ColorBook colob = new ColorBook( "assets/base_colors.json" );
        //colob = new ColorBook( "assets/banff_colors.json" );
        
        MaterialBook mb  = MaterialBuilder.buildMatBook(colob, bd_low_band, this.assetManager);        
        BlockDetail blockRespDet = new BlockDetail(-1, 12, 9, 6, 3, 1);

        City the_city = new City(mb, 6, 6, 32, 32);
        the_city.build(blockRespDet);
        rootNode.attachChild( the_city.getNode() );
        //CityBlock block = new BlockAlley(blockRespDet, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, 16, 48, false);
        //block.generateBuildings(bf_low_band);
        rootNode.scale(1f);
        //rootNode.attachChild( block.getNode() );
        this.setDisplayFps(false);
        this.setDisplayStatView(false);
        
        DebugShapeFactory debugFactory = new DebugShapeFactory(assetManager);
        
        debugFactory.attachCoordinateAxes(rootNode, new Vector3f(0,0,0));
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
  
}
