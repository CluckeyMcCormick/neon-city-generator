package prime;

import builder.ColorBook;
import building.BuildingFactory;
import building.BuildingDetail;
import road.Road;
import road.RoadSize;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;

import cityorg.BlockDetail;
import cityorg.CityBlock;
import cityorg.CityStructure;
import cityorg.blocktype.BlockAlley;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import mesh.UpwardWarpingQuad;
import mesh.OpenBox;


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
        
        final int dimB = 16;
        final int widthB = 16;
        final int blankWidthB = 2;
        final int blankHeightB = 2;
        
        BuildingDetail bdA = new BuildingDetail(
            widthA, dimA, 
            new int[] {blankWidthA, blankWidthA, blankHeightA, blankHeightA}
        );
        
        ColorBook colob = new ColorBook( "assets/colors.json" );
        
        SpriteManager spriteManager = new SpriteManager(
            1024, 1024, SpriteMesh.Strategy.KEEP_BUFFER, rootNode, assetManager 
        );
        getStateManager().attach(spriteManager);    
        
        SpriteImage spi = spriteManager.createSpriteImage("Textures/Glow2.png", true);
        
        BuildingFactory bfA = new BuildingFactory( colob, bdA, spi, this.assetManager);
        
        BlockDetail blockDet = new BlockDetail(-1, 12, 9, 1, 3, 1, 3, 3);
        CityBlock cb = new BlockAlley(blockDet, 256, 64, new int[]{0, 16, 0, 16}, true);
        
        //RoadFactory rf = new RoadFactory( MaterialBuilder.roadMats(roadColors, assetManager) );
        
        Road rod = new Road(RoadSize.LARGE_STREET, 45);
        //rf.buildRoad(rod, 256);
        
        cb.generateBuildings(bfA);
        
        //rootNode.attachChild( rod.getRoad() );
        rootNode.attachChild( cb.getNode() );
        
        Geometry gemtric = new Geometry("nottin", 
            new UpwardWarpingQuad(
                248 * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL, 
                20, 
                new float[]{
                    0,
                    0,
                    16 * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL,
                    16 * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL
                }
            )
        );
        
        gemtric.setMaterial( new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md") );
        gemtric.setLocalTranslation(
            4 * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL,
            0,
            -20
        );
        rootNode.attachChild(gemtric);
        rootNode.scale(0.25f);
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
