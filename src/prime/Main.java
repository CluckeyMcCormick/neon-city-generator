package prime;

import production.ColorBook;

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
        
        ColorBook colob = new ColorBook( "assets/colors.json" );
        
        BuildingFactory bf_low_band = new BuildingFactory( colob, bd_low_band, this.assetManager);
        BuildingFactory bf_high_band = new BuildingFactory( colob, bd_high_band, this.assetManager);
        
        BlockDetail blockRespDet = new BlockDetail(-1, 12, 9, 6, 3, 1);
        BlockDetail blockLumpyDet = new BlockDetail(-1, 14, 9, 4, 3, 1);
        CityBlock lumpyCB = new BlockAlley(blockLumpyDet, new int[]{0, 7, 0, 7}, new int[]{0, 7, 0, 7}, 32, 32, true);
        CityBlock respCB = new BlockAlley(blockRespDet, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, 128, 128, true);

        /*
        lumpyCB.generateBuildings(bf_low_band);
        respCB.generateBuildings(bf_low_band);
        respCB.setLocalTranslation(-25, 0, 0);

        rootNode.attachChild( lumpyCB.getNode() );
        rootNode.attachChild( respCB.getNode() );
        */
        City the_city = new City(8, 8, 32, 32);
        the_city.generateBlocks(blockRespDet, bf_low_band);
        rootNode.attachChild( the_city.getNode() );
        rootNode.scale(.25f);
        
        attachCoordinateAxes(new Vector3f(0,0,0));
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
  
    private void attachCoordinateAxes(Vector3f pos) {
        Arrow arrow = new Arrow(Vector3f.UNIT_X);
        putShape(arrow, ColorRGBA.Red).setLocalTranslation(pos);

        arrow = new Arrow(Vector3f.UNIT_Y);
        putShape(arrow, ColorRGBA.Green).setLocalTranslation(pos);

        arrow = new Arrow(Vector3f.UNIT_Z);
        putShape(arrow, ColorRGBA.Blue).setLocalTranslation(pos);
    }

    private Geometry putShape(Mesh shape, ColorRGBA color) {
        Geometry g = new Geometry("coordinate axis", shape);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setLineWidth(4);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        rootNode.attachChild(g);
        return g;
    }
}
