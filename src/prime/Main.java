package prime;

import production.ColorBook;
import production.TextureBuilder;

import building.BuildingFactory;
import building.BuildingDetail;
import road.Road;
import road.RoadSize;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

import cityorg.BlockDetail;
import cityorg.CityBlock;
import cityorg.CityStructure;
import cityorg.blocktype.BlockAlley;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;

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
        
        BuildingFactory bfA = new BuildingFactory( colob, bdA, this.assetManager);
        
        BlockDetail blockDet = new BlockDetail(-1, 12, 9, 1, 3, 1, 3, 3);
        CityBlock lumpyCB = new BlockAlley(blockDet, 64, 64, new int[]{0, 16, 0, 16}, true);
        CityBlock respCB = new BlockAlley(blockDet, 96, 96, new int[]{0, 0, 0, 0}, true);
        
        //RoadFactory rf = new RoadFactory( MaterialBuilder.roadMats(roadColors, assetManager) );
        
        Road rod = new Road(RoadSize.LARGE_STREET, 45);
        //rf.buildRoad(rod, 256);
        
        lumpyCB.generateBuildings(bfA);
        respCB.generateBuildings(bfA);
        
        respCB.setLocalTranslation(-25, 0, 0);
        
        //rootNode.attachChild( rod.getRoad() );
        rootNode.attachChild( lumpyCB.getNode() );
        rootNode.attachChild( respCB.getNode() );
        
        Geometry gemtric = new Geometry("nottin", 
            new UpwardWarpingQuad(
                56 * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL, 
                20, 
                new float[]{
                    0,
                    0,
                    16 * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL,
                    16 * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL
                }
            )
        );
        
        Geometry gtric = new Geometry("wingding",
            new Quad(2.0f, 2.0f)
        );
        
        Material geomat = new Material(this.assetManager, "MatDefs/TripleHeightBuilding.j3md");
        Image[][] generatedTex = bdA.getLitdim();
        Texture2D litTex = new Texture2D( generatedTex[0][TextureBuilder.LIT_INDEX] );
        Texture2D dimTex = new Texture2D( generatedTex[0][TextureBuilder.DIM_INDEX] );
        
        geomat.setTexture("DimMap", dimTex);
        geomat.setTexture("LitMap", litTex);
        
        geomat.setColor("LitColor", new ColorRGBA(0.7294118f, 0.90588236f, 0.8862745f, 1.0f) );
        geomat.setColor("BaseColor", new ColorRGBA(0.9764706f, 0.92941177f, 0.6392157f, 1.0f) );
        geomat.setColor("MidColor", new ColorRGBA(0.48235294f, 0.54901963f, 0.6117647f, 1.0f) );
        geomat.setColor("TopColor", new ColorRGBA(0.08235294f, 0.13333334f, 0.27058825f, 1.0f) );        

        geomat.setFloat("MidTopBand", 0.14f);  
        geomat.setFloat("MidTopWidth", 0.04f);  

        geomat.setFloat("BaseMidBand", 0.04f);  
        geomat.setFloat("BaseMidWidth", 0.03f);
        
        geomat.setFloat("Cutoff", 0.025f);
        
        gemtric.setMaterial( geomat );
        gtric.setMaterial( geomat );
        
        gemtric.setLocalTranslation(
            4 * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL,
            0,
            -20
        );
        gtric.setLocalTranslation(
            0, 4.0f, 0
        );
        
        rootNode.attachChild(gemtric);
        rootNode.attachChild(gtric);
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
