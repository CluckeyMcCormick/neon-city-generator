/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cityorg;

import building.BasicBuildingTemplate;
import building.BuildingInstance;
import building.QuarterTurnTemplate;
import building.SixthTurnTemplate;
import building.ThirdTurnTemplate;
import building.WindowStyle;
import com.jme3.material.Material;
import production.*;
import org.json.*;
import com.jme3.math.ColorRGBA;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.Function;
import prime.RandomSingleton;

/**
 *
 * @author frick
 */
public class CityRandomTemplate {

    private List<BlockDetail> detail_list;
    
    public CityRandomTemplate(){
        this.defaultAll();
    }
    
    public CityRandomTemplate(String filePath){
        RandomSingleton rs = RandomSingleton.getInstance();
        
        try{
            StringBuilder jsonBuild = new StringBuilder();
            Scanner scannable = new Scanner( new File(filePath) );

            while(scannable.hasNextLine())
                jsonBuild.append( scannable.nextLine() );
            
            JSONArray arr = new JSONArray( jsonBuild.toString() ); 
            JSONObject obj;
            
            detail_list = new LinkedList<BlockDetail>();
            
            for(int i = 0; i < arr.length(); i++){
                obj = arr.getJSONObject(i);
                this.processBlockTemplate( obj );
            }
            
            Collections.shuffle( detail_list, rs.getRandom() );
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
    
    private void processBlockTemplate(JSONObject template) throws Exception{
        List<BasicBuildingTemplate> building_types;
        List<WindowStyle> styles;
        
        BlockDetail bd;
        
        int ideal_height;
        int ideal_face;
        
        int height_deviant;
        int face_deviant;
        int depth_deviant;
        
        int weight;
        
        //Step 1: Get the WindowStyles
        styles = processWindowStyles(template);
        
        //Step 2: Get the BuildingTypes
        building_types = processBuildingStyles(template);
        
        //Step 3: Get the BlockDetail variables
        ideal_height = template.getInt("ideal_height");
        ideal_face = template.getInt("ideal_face");
        height_deviant = template.getInt("height_deviant");
        face_deviant = template.getInt("face_deviant");
        depth_deviant = template.getInt("depth_deviant");
        
        //Step 4: Create the BlockDetail
        bd = new BlockDetail(styles, building_types, ideal_height, ideal_face, 
            height_deviant, face_deviant, depth_deviant
        );
        
        //Step 5: Get the weight
        weight = template.getInt("weight");
        
        //Step 6: Add the BlockDetail to our list, (weight) times.
        for(int i = 0; i < weight; i++)
            detail_list.add(bd);
    }
    
    private List<WindowStyle> processWindowStyles(JSONObject template) throws Exception {
        RandomSingleton rs = RandomSingleton.getInstance();
        
        JSONArray style_array = template.getJSONArray("window_styles");
        JSONArray weight_array = template.getJSONArray("window_weights");
        List<WindowStyle> styles = new LinkedList<WindowStyle>();
        WindowStyle current;
        int weight = 0;
        
        for(int i = 0; i < style_array.length(); i++){
            if( style_array.getString(i).equalsIgnoreCase("STANDARD") )
                current = WindowStyle.STANDARD;
            else if( style_array.getString(i).equalsIgnoreCase("VERTICAL") )
                current = WindowStyle.VERTICAL;
            else if( style_array.getString(i).equalsIgnoreCase("RANDOM") )
                current = WindowStyle.RANDOM;
            else if( style_array.getString(i).equalsIgnoreCase("RIBBON") )
                current = WindowStyle.RIBBON;
            else
                throw new Exception("Style " + style_array.getString(i) + " does not exist!");
            
            weight = weight_array.getInt(i);
            
            for(int w = 0; w < weight; w++)
                styles.add(current);
        }
        
        Collections.shuffle( styles, rs.getRandom() );

        return styles;
    }

    private List<BasicBuildingTemplate>  processBuildingStyles(JSONObject template) throws Exception {
        RandomSingleton rs = RandomSingleton.getInstance();
        
        JSONArray style_array = template.getJSONArray("building_types");
        JSONArray weight_array = template.getJSONArray("building_weights");
        List<BasicBuildingTemplate> styles = new LinkedList<BasicBuildingTemplate>();
        BasicBuildingTemplate current;
        int weight;
        
        for(int i = 0; i < style_array.length(); i++){
            if( style_array.getString(i).equalsIgnoreCase("BASIC") )
                current = new BasicBuildingTemplate();
            else if( style_array.getString(i).equalsIgnoreCase("SIXTH") )
                current = new SixthTurnTemplate();
            else if( style_array.getString(i).equalsIgnoreCase("QUARTER") )
                current = new QuarterTurnTemplate();
            else if( style_array.getString(i).equalsIgnoreCase("THIRD") )
                current = new ThirdTurnTemplate();
            else
                throw new Exception("Style " + style_array.getString(i) + " does not exist!");
            
            weight = weight_array.getInt(i);
            
            for(int w = 0; w < weight; w++)
                styles.add(current);
        }
        
        Collections.shuffle( styles, rs.getRandom() );

        return styles;
    }
    
    private void defaultAll(){
        detail_list = new LinkedList<BlockDetail>();
        List<WindowStyle> windows;
        List<BasicBuildingTemplate> buildings;
        
        windows = new LinkedList<WindowStyle>();
        windows.add(WindowStyle.STANDARD);
        windows.add(WindowStyle.RIBBON);
        windows.add(WindowStyle.VERTICAL);
        windows.add(WindowStyle.RANDOM);
        
        buildings = new LinkedList<BasicBuildingTemplate>();
        buildings.add( new BasicBuildingTemplate() );
        
        detail_list.add( new BlockDetail(windows, buildings, 12, 9, 6, 3, 1) );
    }
    
    /* Istance Methods */
    public BlockDetail getRandomDetail(){
        RandomSingleton rs = RandomSingleton.getInstance();
        return detail_list.get( rs.nextInt( detail_list.size() ) );
    }
}
