/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package production;

import org.json.*;
import com.jme3.math.ColorRGBA;
import java.io.File;
import java.util.Scanner;
import prime.RandomSingleton;

/**
 *
 * @author frick
 */
public class ColorBook {

    public final static int LIT_INDEX = 0;
    public final static int BASE_INDEX = 1;
    public final static int MID_INDEX = 2;
    public final static int TOP_INDEX = 3;
    
    private final String[] tagnames = new String[]{
     "LitColor", "BaseColor", "MidColor", "TopColor",
    };
    
    private ColorRGBA[] colors;
    
    public ColorBook(){
        this.defaultAll();
    }
    
    public ColorBook(String filePath){
        try{
            StringBuilder jsonBuild = new StringBuilder();
            Scanner scannable = new Scanner( new File(filePath) );

            while(scannable.hasNextLine())
                jsonBuild.append( scannable.nextLine() );
            
            JSONObject obj = new JSONObject( jsonBuild.toString() );
            JSONArray arr;
            
            this.colors = new ColorRGBA[tagnames.length];
            
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
        ColorRGBA color;
        
        float red;
        float green;
        float blue;
        float alpha;
        
        color = null;
        
        for(int i = 0; i < arr.length(); i++){
            current = arr.getJSONArray(i);

            red = current.getInt(0) / 255f;
            green = current.getInt(1) / 255f;
            blue = current.getInt(2) / 255f;
            alpha = (float) current.getDouble(3);
            
            color = new ColorRGBA(red, green, blue, alpha);
        }
        
        this.colors[index] = color;
    }
    
    private void defaultAll(){
        this.colors = new ColorRGBA[tagnames.length];
        
        this.defaultLitColor();
        this.defaultBaseColor();
        this.defaultMidColor();
        this.defaultTopColor();

    }
    
    private void defaultLitColor(){
        this.colors[LIT_INDEX] = new ColorRGBA( 173f / 255f, 1f, 1f, 1f );
    }
    
    private void defaultBaseColor(){
        this.colors[BASE_INDEX] = new ColorRGBA( 193f / 255f, 0f, 0f, 1f ); 
    }
    
    private void defaultMidColor(){
        this.colors[MID_INDEX] = new ColorRGBA( 0f, 1f, 1f, 1f );
    }
    
    private void defaultTopColor(){
        this.colors[TOP_INDEX] = new ColorRGBA( 0f, 42 / 255f, 79 / 255f, 1f );
    }
    
    /* All Getters */
    
    public ColorRGBA getLit(){
        return this.colors[LIT_INDEX];
    }
    
    public ColorRGBA getBase(){
        return this.colors[BASE_INDEX];
    }    

    public ColorRGBA getMid(){
        return this.colors[MID_INDEX];
    } 
    
    public ColorRGBA getTop(){
        return this.colors[TOP_INDEX];
    } 
    
}
