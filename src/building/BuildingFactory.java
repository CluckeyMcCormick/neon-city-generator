/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import builder.ColorBook;
import builder.MaterialBook;
import building.type.FloorCellBuilding;
import mesh.TextureScalingQuad;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import engine.sprites.SpriteImage;
import builder.MaterialBuilder;
import builder.TextureBuilder;
import cityorg.Cardinal;
import static cityorg.Cardinal.NORTH;
import cityorg.CityBlock;
import cityorg.CityStructure;
import com.jme3.asset.AssetManager;
import prime.RandomSingleton;
import mesh.CivilizedFadeMesh;
import mesh.CivilizedBase;
import mesh.OpenBox;
import mesh.UpwardWarpingQuad;
import mesh.VertShiftQuad;

/**
 *
 * @author root
 */
public class BuildingFactory {
   
    public static final float MIN_VIRTUAL_HEIGHT = 
        (Building.MIN_UNIT_HEIGHT * Building.GOLDEN_PIXEL_COUNT) * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
    
    public static final int SOUTH_INDEX = 0;
    public static final int EAST_INDEX = 1;
    public static final int NORTH_INDEX = 2;
    public static final int WEST_INDEX = 3;
    
    private MaterialBook matBook;
    private ColorBook coBook;
    
    private SpriteImage warnLight;
    private ColorRGBA warnColor;
    
    private BuildingDetail bd;
    
    public BuildingFactory(ColorBook colo, BuildingDetail bd, SpriteImage warnLight, AssetManager am){    

        this.coBook = colo;
        this.matBook = MaterialBuilder.buildMatBook(coBook, bd, am);
        
        this.warnLight = warnLight;
        
        this.bd = bd;
    }
    
    public Geometry[] blockFloor( int unitWidth, int unitLength, int[] unitHeight, int widDepth, int lenDepth){
        Material baseMat;
        Material fadeMat;
        
        float virtWidth;
        float virtLength;
        
        Geometry[] geoms;
        
        baseMat = this.matBook.getBaseMat();
        fadeMat = this.matBook.getFadeMat(0);
        
        virtWidth = (unitWidth * CityStructure.GOLDEN_PIXEL_COUNT ) * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        virtLength = (unitLength * CityStructure.GOLDEN_PIXEL_COUNT ) * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        
        geoms = new Geometry[2];
        
        geoms[0] = new Geometry("rood", new CivilizedBase(unitWidth, widDepth, unitLength, lenDepth, unitHeight) );
        geoms[1] = new Geometry("block_fade", new CivilizedFadeMesh(unitLength, lenDepth, unitWidth, widDepth, unitHeight) );
        
        geoms[0].setMaterial(baseMat);
        geoms[1].setMaterial(fadeMat);
        
        geoms[0].setLocalRotation( geoms[0].getLocalRotation().fromAngles(0, FastMath.HALF_PI, 0) );
        geoms[0].updateGeometricState();

        geoms[1].setQueueBucket(RenderQueue.Bucket.Transparent);

        geoms[0].setLocalTranslation(0, 0, virtWidth);
        geoms[1].setLocalTranslation(0, 0.001f, 0);      
        
        return geoms;
    }
    
    //In the actual program, we'll be creating buildings to fit gaps
    //So we'll know the width - the number of units - but not the height.
    public FloorCellBuilding randomFCB(int unitWidth, int unitHeight){
        return this.randomFCB(unitWidth, unitWidth, unitHeight, 0);
    }
    
    public FloorCellBuilding randomFCB(int unitLength, int unitWidth, int unitHeight, int heightAdjust){
        FloorCellBuilding fcb;
        
        if( unitHeight < Building.MIN_UNIT_HEIGHT )
           unitHeight = Building.MIN_UNIT_HEIGHT;  
        
        fcb = new FloorCellBuilding(unitLength, unitWidth, unitHeight, this.bd);
        
        this.buildFCB(fcb, heightAdjust);
    
        return fcb;
    }
    
    private void buildFCB(FloorCellBuilding bb, int heightAdjust){

        Material litMat = this.matBook.getLitMat(WindowStyle.STANDARD);
        Material dimMat = this.matBook.getDimMat(WindowStyle.STANDARD);
        Material topMat = this.matBook.getBaseMat();
        
        final int BOX_SIDES = 4;
        final float LIT_ADD = 0.002f;
        final float HEIGHT_ADJUST = heightAdjust * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        
        Node node;
        
        TextureScalingQuad[] litQuads;
        OpenBox dimBox;
        UpwardWarpingQuad boxtop;
               
        Geometry[] litGeoms;
        Geometry dimGeom;
        Geometry topGeom;
        
        Vector2f[] texCoords;
        Vector2f[] quadCoords;
        
        Quaternion[] rotations;
        Vector3f[] basicTrans;
        Vector3f[] litTrans;
        
        node = new Node();
        
        litQuads = new TextureScalingQuad[BOX_SIDES];   
        
        litGeoms = new Geometry[BOX_SIDES];
              
        texCoords = this.getRandomTextureCoords(bb);
        quadCoords = new Vector2f[BOX_SIDES]; 
        
        rotations = new Quaternion[4];
        rotations[0] = new Quaternion().fromAngleAxis(FastMath.PI, new Vector3f(0,1,0) );
        rotations[1] = new Quaternion().fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0) );
        rotations[2] = new Quaternion().fromAngleAxis(0, new Vector3f(0,1,0) );
        rotations[3] = new Quaternion().fromAngleAxis(-FastMath.PI/2, new Vector3f(0,1,0) );

        basicTrans = new Vector3f[4];
        basicTrans[0] = new Vector3f(bb.virtualLength(), -bb.virtualHeight(), -bb.virtualWidth());
        basicTrans[1] = new Vector3f(bb.virtualLength(), -bb.virtualHeight(), bb.virtualWidth());
        basicTrans[2] = new Vector3f(-bb.virtualLength(), -bb.virtualHeight(), bb.virtualWidth());
        basicTrans[3] = new Vector3f(-bb.virtualLength(), -bb.virtualHeight(), -bb.virtualWidth());
        
        //North
        basicTrans[0] = new Vector3f(bb.virtualLength(), 0, 0);
        //East
        basicTrans[1] = new Vector3f(bb.virtualLength(), 0,bb.virtualWidth());
        //North
        basicTrans[2] = new Vector3f(0, 0, bb.virtualWidth());
        //West
        basicTrans[3] = new Vector3f(0, 0, 0);
        
        litTrans = new Vector3f[4];
        litTrans[0] = basicTrans[0].add( 0, HEIGHT_ADJUST, -LIT_ADD );
        litTrans[1] = basicTrans[1].add( LIT_ADD, HEIGHT_ADJUST, 0 );
        litTrans[2] = basicTrans[2].add( 0, HEIGHT_ADJUST, LIT_ADD );
        litTrans[3] = basicTrans[3].add( -LIT_ADD, HEIGHT_ADJUST, 0 );
        
        float width;
        //First, create the lits and the fades
        //Back, Right, Front, Left
        for(int i = 0; i < BOX_SIDES; i++){
            quadCoords[0] = texCoords[i * 4];
            quadCoords[1] = texCoords[i * 4 + 1];
            quadCoords[2] = texCoords[i * 4 + 2];
            quadCoords[3] = texCoords[i * 4 + 3];
    
            if(i % 2 == 0)
                width = bb.virtualLength();
            else
                width = bb.virtualWidth();            
            
            litQuads[i] = new TextureScalingQuad( width, bb.virtualHeight(), quadCoords);         
        }
        //Now the dim box
        dimBox = new OpenBox(
            bb.virtualWidth(),
            bb.virtualLength(), 
            bb.virtualHeight(), 
             texCoords);
        //And the box topper
        boxtop = new UpwardWarpingQuad(bb.virtualLength(), bb.virtualWidth());
        
        //Next, create the geoms for the lits and the fades
        //Back, Right, Front, Left
        for(int i = 0; i < BOX_SIDES; i++){
            litGeoms[i] = new Geometry("building_lit" + i, litQuads[i]);
            
            litGeoms[i].setMaterial(litMat);        
            
            litGeoms[i].setQueueBucket(RenderQueue.Bucket.Transparent);
            
            litGeoms[i].setLocalRotation(rotations[i]);
            
            litGeoms[i].setLocalTranslation(litTrans[i]);
            
            node.attachChild(litGeoms[i]);
        }
        
        dimGeom = new Geometry("building_dim", dimBox);
        dimGeom.setMaterial(dimMat);
        dimGeom.setLocalTranslation(0, HEIGHT_ADJUST, 0);
        node.attachChild(dimGeom);
        
        topGeom = new Geometry("building_top", boxtop);
        topGeom.setMaterial(topMat);
        topGeom.setLocalTranslation(0, bb.virtualHeight() + HEIGHT_ADJUST, 0);
        node.attachChild(topGeom);
        
        bb.setNode(node);
        
    }
    /**
    if( warn ){
            Sprite[] warnSprites;
            Vector3f[] warnTrans;
            
            warnSprites = new Sprite[BOX_SIDES];  
            
            warnTrans = new Vector3f[4];
            for(int i = 0; i < warnTrans.length; i++)
            warnTrans[i] = basicTrans[i].add(0, bb.virtualHeight(), 0);
            
            for(int i = 0; i < BOX_SIDES; i++){
                warnSprites[i] = new Sprite(warnLight);
                warnSprites[i].setPosition(warnTrans[i]);
                warnSprites[i].setColor(warnColor);
                warnSprites[i].setSize(WARN_SIZE);
            }
            
            bb.setSprites(warnSprites);
        }
    **/
    
    public Node buildFade(FloorCellBuilding fcb, CityBlock block, Cardinal orient, boolean[] lightArr, int ulX, int ulY){
        short[] lightRating;
        Node returnable = new Node();
        int colorIndex;
        
        lightRating = this.getLightRating(lightArr);
        
        switch(orient){
            default:
            case NORTH_WEST:
                colorIndex = 0; //RED
                break;
            case NORTH_EAST:
                colorIndex = 1; //CYAN
                break;
            case SOUTH_WEST:
                colorIndex = 2; //GREEN
                break;
            case SOUTH_EAST:
                colorIndex = 3; //BLUE
                break;
        }
        
        if(lightRating[0] >= 2)
            returnable.attachChild( buildFullFadeGeom(fcb, block, Cardinal.SOUTH, colorIndex, ulX, ulY) );
        else if (lightRating[0] > 0)
            returnable.attachChild( buildTransFadeGeom(fcb, block, Cardinal.SOUTH, orient, colorIndex) );
        
        if(lightRating[1] >= 2)
            returnable.attachChild( buildFullFadeGeom(fcb, block, Cardinal.EAST, colorIndex, ulX, ulY) );
        else if (lightRating[1] > 0)
            returnable.attachChild( buildTransFadeGeom(fcb, block,Cardinal.EAST, orient, colorIndex) );
        
        if(lightRating[2] >= 2)
            returnable.attachChild( buildFullFadeGeom(fcb, block, Cardinal.NORTH, colorIndex, ulX, ulY) );
        else if (lightRating[2] > 0)
            returnable.attachChild( buildTransFadeGeom(fcb, block, Cardinal.NORTH, orient, colorIndex) );
        
        if(lightRating[3] >= 2)
            returnable.attachChild( buildFullFadeGeom(fcb, block, Cardinal.WEST, colorIndex, ulX, ulY) );
        else if (lightRating[3] > 0)
            returnable.attachChild( buildTransFadeGeom(fcb, block, Cardinal.WEST, orient, colorIndex) );
        
        return returnable;
    }
    
    private short[] getLightRating(boolean[] lit){
        short[] lightRating = new short[4];
        
        if(lit[0]){
            lightRating[0] += 2; //SOUTH: FULL
            lightRating[1]++; //WEST: PARTIAL
            lightRating[3]++; //EAST: PARTIAL
        }
        
        if(lit[1]){
            lightRating[1] += 2; //WEST: FULL
            lightRating[0]++; //SOUTH: PARTIAL
            lightRating[2]++; //NORTH: PARTIAL
        }
        
        if(lit[2]){
            lightRating[2] += 2; //NORTH: FULL
            lightRating[1]++; //WEST: PARTIAL
            lightRating[3]++; //EAST: PARTIAL
        }
        
        if(lit[3]){
            lightRating[3] += 2; //EAST: FULL
            lightRating[0]++; //SOUTH: PARTIAL
            lightRating[2]++; //NORTH: PARTIAL
        }
        
        return lightRating;
    }
    
    private Geometry buildFullFadeGeom( FloorCellBuilding fcb, CityBlock block, Cardinal side, int colorIndex, int ulX, int ulY){
        final float FADE_ADD = 0.001f;
        Geometry fadeGeom;
        Quaternion rotation;
        Vector3f trans;
        float sideLen;
                    
        float rightShift = Building.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL;
        float leftShift = rightShift;
        
        switch(side){
            default:
            case SOUTH:
                leftShift *= block.getSouthHeight( ulX + fcb.unitLength() );
                rightShift *= block.getSouthHeight( ulX );
                rotation = new Quaternion().fromAngleAxis(FastMath.PI, new Vector3f(0,1,0) );
                
                trans = new Vector3f(
                    fcb.virtualLength(), 
                    Math.max(leftShift, rightShift) - Math.min(leftShift, rightShift), 
                    - FADE_ADD
                );
                
                sideLen = fcb.virtualLength();
                break;
            case EAST:
                leftShift = 0;
                rightShift = leftShift;
                rotation = new Quaternion().fromAngleAxis(-FastMath.PI/2, new Vector3f(0,1,0) );
                trans = new Vector3f(-FADE_ADD, 0, 0);   
                sideLen = fcb.virtualWidth();
                break;
            case NORTH:
                leftShift = 0;
                rightShift = leftShift;
                rotation = new Quaternion().fromAngleAxis(0, new Vector3f(0,1,0) );
                trans = new Vector3f(0, 0, fcb.virtualWidth() + FADE_ADD);
                sideLen = fcb.virtualLength();
                break;
            case WEST:
                leftShift = 0;
                rightShift = leftShift;
                rotation = new Quaternion().fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0) );
                trans = new Vector3f( fcb.virtualLength() + FADE_ADD, 0, fcb.virtualWidth() );
                sideLen = fcb.virtualWidth();
                break;
        }
        
        fadeGeom = new Geometry("building_fade", new VertShiftQuad( sideLen + FADE_ADD * 2, MIN_VIRTUAL_HEIGHT, leftShift, rightShift) );
        fadeGeom.setMaterial( this.matBook.getFadeMat(colorIndex) );         
        fadeGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        fadeGeom.setLocalRotation(rotation);
        fadeGeom.setLocalTranslation(trans);
        
        return fadeGeom;
    }
    
    private Geometry buildTransFadeGeom( FloorCellBuilding fcb, CityBlock block, Cardinal side, Cardinal orient, int colorIndex){
        final float FADE_ADD = 0.001f;
        Geometry fadeGeom;
        Quaternion rotation;
        Vector3f trans;
        Material mat;
        float convert = Building.GOLDEN_PIXEL_COUNT * Building.VIRTUAL_LENGTH_PER_PIXEL;
        float sideLen = Building.MIN_UNIT_HEIGHT * convert;
        
        mat = this.matBook.getTransMat(colorIndex);
        
        if(side == Cardinal.SOUTH){
            rotation = new Quaternion().fromAngleAxis(FastMath.PI, new Vector3f(0,1,0) );
            
            if(orient == Cardinal.NORTH_EAST || orient == Cardinal.SOUTH_EAST)  
                //FACE: SOUTH   EDGE: EAST
                trans = new Vector3f(
                        sideLen, 
                        block.getEastHeight(0) * convert, 
                        -FADE_ADD
                );
            else {
                //FACE: SOUTH   EDGE: WEST
                trans = new Vector3f(
                        fcb.virtualLength(  ), 
                        block.getWestHeight(fcb.unitWidth()) * convert, 
                        -FADE_ADD
                );
                
                mat = this.matBook.getFlipTransMat(colorIndex);
            }
        }
        else if(side == Cardinal.EAST){
            rotation = new Quaternion().fromAngleAxis(-FastMath.PI/2, new Vector3f(0,1,0) );
            
            if(orient == Cardinal.SOUTH_WEST || orient == Cardinal.SOUTH_EAST){
                //FACE: EAST    EDGE: SOUTH               
                trans = new Vector3f( 
                        -FADE_ADD, 
                        block.getSouthHeight( 0 ) * convert, 
                        0 
                );
                
                mat = this.matBook.getFlipTransMat(colorIndex);
            } else
                //FACE: EAST    EDGE: NORTH
                trans = new Vector3f( 
                        -FADE_ADD, 
                        block.getNorthHeight(0) * convert, 
                        fcb.virtualWidth() - sideLen
                ); 
        } 
        else if (side == Cardinal.NORTH){
            rotation = new Quaternion().fromAngleAxis(0, new Vector3f(0,1,0) );
            
            if(orient == Cardinal.NORTH_EAST || orient == Cardinal.SOUTH_EAST){           
                //FACE: NORTH   EDGE: EAST
                trans = new Vector3f( 
                        0, 
                        block.getEastHeight( 0 ) * convert, 
                        fcb.virtualWidth() + FADE_ADD 
                );
                
                mat = this.matBook.getFlipTransMat(colorIndex);
            } else
                //FACE: NORTH   EDGE: WEST
                trans = new Vector3f( 
                        fcb.virtualLength() - sideLen, 
                        block.getWestHeight( fcb.unitWidth() ) * convert, 
                        fcb.virtualWidth() + FADE_ADD 
                );
        } 
        else if(side == Cardinal.WEST){
            rotation = new Quaternion().fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0) );
            
            if(orient == Cardinal.SOUTH_WEST || orient == Cardinal.SOUTH_EAST)
                //FACE: WEST    EDGE: SOUTH
                trans = new Vector3f( 
                        fcb.virtualLength() + FADE_ADD, 
                        block.getSouthHeight( fcb.unitLength() ) * convert, 
                        sideLen
                );
            else{
                //FACE: WEST    EDGE: NORTH
                trans = new Vector3f(
                        fcb.virtualLength() + FADE_ADD, 
                        block.getNorthHeight( fcb.unitLength() ) * convert, 
                        fcb.virtualWidth() 
                );
                mat = this.matBook.getFlipTransMat(colorIndex);
            }
        }
        else {
            rotation = new Quaternion();
            trans = new Vector3f();     
        }
        
        fadeGeom = new Geometry("building_fade", new Quad( sideLen + FADE_ADD * 2, MIN_VIRTUAL_HEIGHT) );
        fadeGeom.setMaterial( mat );         
        fadeGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        fadeGeom.setLocalRotation(rotation);
        fadeGeom.setLocalTranslation(trans);
        
        return fadeGeom;
    }
    
    public Geometry buildStand(int unitWidth, int unitLength, int unitHeight){     
        
        Geometry g = new Geometry( "stando", 
            new OpenBox(
                unitWidth * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL, 
                unitLength * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL, 
                unitHeight * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.VIRTUAL_LENGTH_PER_PIXEL
            ) 
        );
        g.setMaterial( this.matBook.getBaseMat() );
        
        
        
        return g;
    }
    
    public Vector2f[] getRandomTextureCoords(FloorCellBuilding bb){
        Vector2f[] texCoords;
        RandomSingleton rand = RandomSingleton.getInstance();
        
        int totalFloors;
        int totalCells;
        
        float maxX;
        float maxY;
        
        float shiftX;
        float shiftY;
        
        texCoords = new Vector2f[16];       
      
        totalFloors = TextureBuilder.TEXTURE_Y / bd.getCellPixHeight();
        if(TextureBuilder.TEXTURE_Y % bd.getCellPixHeight() != 0)
            totalFloors = totalFloors - 1;
        
        totalCells = TextureBuilder.TEXTURE_X / bd.getCellPixWidth();
        if(TextureBuilder.TEXTURE_X % bd.getCellPixWidth() != 0)
            totalCells = totalCells - 1;
        
        //Back, right, front, left
        for(int i = 0; i < 4; i++){
            shiftX = rand.nextInt(totalCells) * bd.getTexWidthPerCell();
            shiftY = rand.nextInt(totalFloors) * bd.getTexHeightPerCell();
            
            if(i % 2 == 0)
                maxX = shiftX + bb.textureLength();
            else
                maxX = shiftX + bb.textureWidth();
            
            maxY = shiftY + bb.textureHeight();
            
            if(maxX < 0)
                maxX = 0;
            if(maxY < 0)
                maxY = 0;
            
            //Lower left
            texCoords[i * 4] = new Vector2f(shiftX, shiftY);
            //Lower Right
            texCoords[(i * 4) + 1] = new Vector2f(maxX, shiftY);
            //Upper Right
            texCoords[(i * 4) + 2] = new Vector2f(maxX, maxY);
            //Upper Left
            texCoords[(i * 4) + 3] = new Vector2f(shiftX, maxY);
        }
        
        return texCoords;
    }

    public Vector2f[] textureShift(Vector2f[] oldCoord, int heightAdjust){
        Vector2f[] newCoord;
        
        float shiftY = heightAdjust * CityStructure.GOLDEN_PIXEL_COUNT * CityStructure.TEXTURE_LENGTH_PER_PIXEL;
        
        newCoord = new Vector2f[16];       
        
        //Back, right, front, left
        for(int i = 0; i < 4; i++){        
            //Lower left
            newCoord[i * 4] = oldCoord[i * 4].clone().addLocal(0, -shiftY);
            //Lower Right
            newCoord[(i * 4) + 1] = oldCoord[(i * 4) + 1].clone().addLocal(0, -shiftY);
            //Upper Right
            newCoord[(i * 4) + 2] = oldCoord[(i * 4) + 2].clone();
            //Upper Left
            newCoord[(i * 4) + 3] = oldCoord[(i * 4) + 3].clone();
        }
        
        return newCoord;
    }
}
