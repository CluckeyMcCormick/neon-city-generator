package prime;

import production.ColorBook;
import production.DebugShapeFactory;

import building.BuildingFactory;
import building.BuildingDetail;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

import cityorg.BlockDetail;
import cityorg.City;
import cityorg.CityBlock;
import cityorg.blocktype.BlockAlley;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.debug.Arrow;


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
        
        BuildingDetail bd_low_band = new BuildingDetail(
            widthA, dimA,
            dimA * 3, dimA / 2, dimA * 2,
            dimA * 9, dimA * 8,
            new int[] {blankWidthA, blankWidthA, blankHeightA, blankHeightA}
        );

        BuildingDetail bd_high_band = new BuildingDetail(
            widthA, dimA,
            dimA * 6, dimA / 2, dimA * 5,
            dimA * 12, dimA * 8,
            new int[] {blankWidthA, blankWidthA, blankHeightA, blankHeightA}
        );
        
        ColorBook colob = new ColorBook( "assets/base_colors.json" );
        colob = new ColorBook( "assets/banff_colors.json" );
        
        BuildingFactory bf_low_band = new BuildingFactory( colob, bd_low_band, this.assetManager);        
        BlockDetail blockRespDet = new BlockDetail(-1, 12, 9, 6, 3, 1);

        City the_city = new City(16, 12, 32, 32);
        the_city.build(blockRespDet, bf_low_band);
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
