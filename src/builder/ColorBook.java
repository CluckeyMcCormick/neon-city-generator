/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import org.json.*;
import com.jme3.math.ColorRGBA;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import prime.RandomSingleton;

/**
 *
 * @author frick
 */
public class ColorBook {

    public final static int BUILDING_BASE_INDEX = 0;
    public final static int BUILDING_LIGHT_INDEX = 1;
    public final static int BUILDING_WARNING_LIGHT_INDEX = 2;
    public final static int STREET_LIGHT_INDEX = 3;
    
    private final String[] tagnames = new String[]{
     "buildingBase", "buildingLight", "warningLight", "streetLight",
    };
    
    private ColorRGBA[][] colorMatrix;
    
    public ColorBook(){
        this.defaultAll();
    }
    
    public ColorBook(String filePath){
        try{
            StringBuilder jsonBuild = new StringBuilder();
            Scanner scannable = new Scanner(new File(filePath));

            while(scannable.hasNextLine())
                jsonBuild.append( scannable.nextLine() );
            
            JSONObject obj = new JSONObject( jsonBuild.toString() );
            JSONArray arr;
            
            this.colorMatrix = new ColorRGBA[tagnames.length][];
            
            for(int i = 0; i < tagnames.length; i++){
                arr = obj.getJSONArray( tagnames[i] );
                this.processArr( arr, i );
            }
            
        }
        catch(Exception e){
            System.out.println("ENCOUNTERED EXCEPTION");
            System.out.println(e.getClass());
            System.out.println(e.getMessage() + "\n");
            e.printStackTrace();
            this.defaultAll();
        }
        
    }
    
    /* Helper Methods for the constructor */
    
    private void processArr(JSONArray arr, int index) throws JSONException{
        JSONArray current;
        ColorRGBA[] colorArr;
        
        float red;
        float green;
        float blue;
        float alpha;
        
        colorArr = new ColorRGBA[ arr.length() ];
        
        for(int i = 0; i < arr.length(); i++){
            current = arr.getJSONArray(i);

            red = current.getInt(0) / 255f;
            green = current.getInt(1) / 255f;
            blue = current.getInt(2) / 255f;
            alpha = (float) current.getDouble(3);
            
            colorArr[i] = new ColorRGBA(red, green, blue, alpha);
        }
        
        this.colorMatrix[index] = colorArr;
    }
    
    private void defaultAll(){
        this.colorMatrix = new ColorRGBA[tagnames.length][];
        
        this.defaultBuildingLight();
        this.defaultBuildingBase();
        this.defaultWarningLight();
        this.defaultStreetLight();
    }
    
    private void defaultBuildingLight(){
        this.colorMatrix[BUILDING_LIGHT_INDEX] = new ColorRGBA[1];
        this.colorMatrix[BUILDING_LIGHT_INDEX][0] = new ColorRGBA( 173f / 255f, 1f, 1f, 1f );
    }
    
    private void defaultBuildingBase(){
        this.colorMatrix[BUILDING_BASE_INDEX] = new ColorRGBA[1];
        this.colorMatrix[BUILDING_BASE_INDEX][0] = new ColorRGBA( 0f, 42 / 255f, 79 / 255f, 1f );
    }
    
    private void defaultWarningLight(){
        this.colorMatrix[BUILDING_WARNING_LIGHT_INDEX] = new ColorRGBA[1];
        this.colorMatrix[BUILDING_WARNING_LIGHT_INDEX][0] = new ColorRGBA( 0f, 1f, 1f, 1f );
    }
    
    private void defaultStreetLight(){
        this.colorMatrix[STREET_LIGHT_INDEX] = new ColorRGBA[1];
        this.colorMatrix[STREET_LIGHT_INDEX][0] = new ColorRGBA( 193f / 255f, 0f, 0f, 1f ); 
    }
    
    /* Random Getters, Absolute Getters, and All Getters */
    public ColorRGBA getBuildingLight(){
        RandomSingleton rs;
        int colorCount;
        
        colorCount = this.colorMatrix[BUILDING_LIGHT_INDEX].length;
        
        if(colorCount < 2)
            return this.getBuildingLightAbs();
        
        rs = RandomSingleton.getInstance();
        
        return this.colorMatrix[BUILDING_LIGHT_INDEX][ rs.nextInt(colorCount) ];
    }
    
    public ColorRGBA getBuildingLightAbs(){
        return this.colorMatrix[BUILDING_LIGHT_INDEX][0];
    }
    
    public ColorRGBA[] getBuildingLightAll(){
        return this.colorMatrix[BUILDING_LIGHT_INDEX];
    }
    
    
    public ColorRGBA getBuildingBase(){
        return this.getBuildingBaseAbs();
    }
    
    public ColorRGBA getBuildingBaseAbs(){
        return this.colorMatrix[BUILDING_BASE_INDEX][0];
    }    
    
    public ColorRGBA getWarningLight(){
        RandomSingleton rs;
        int colorCount;
        
        colorCount = this.colorMatrix[BUILDING_WARNING_LIGHT_INDEX].length;
        
        if(colorCount < 2)
            return this.getWarningLightAbs();
        
        rs = RandomSingleton.getInstance();
        
        return this.colorMatrix[BUILDING_WARNING_LIGHT_INDEX][ rs.nextInt(colorCount) ];
    }
    
    public ColorRGBA getWarningLightAbs(){
        return this.colorMatrix[BUILDING_WARNING_LIGHT_INDEX][0];
    }
    
    public ColorRGBA[] getWarningLightAll(){
        return this.colorMatrix[BUILDING_WARNING_LIGHT_INDEX];
    }
    
    
    public ColorRGBA getStreetLight(){
        RandomSingleton rs;
        int colorCount;
        
        colorCount = this.colorMatrix[STREET_LIGHT_INDEX].length;
        
        if(colorCount < 2)
            return this.getStreetLightAbs();
        
        rs = RandomSingleton.getInstance();
        
        return this.colorMatrix[STREET_LIGHT_INDEX][ rs.nextInt(colorCount) ];
    }
    
    public ColorRGBA getStreetLightAbs(){
        return this.colorMatrix[STREET_LIGHT_INDEX][0];
    }
    
    public ColorRGBA[] getStreetLightAll(){
        return this.colorMatrix[STREET_LIGHT_INDEX];
    }
}
