/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package production;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;

/**
 *
 * @author frick
 */
public class DebugShapeFactory {
    
    Material xMat;
    Material yMat;
    Material zMat;

    public DebugShapeFactory(AssetManager manager) {
        xMat = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
        xMat.getAdditionalRenderState().setWireframe(true);
        xMat.getAdditionalRenderState().setLineWidth(4);
        xMat.setColor("Color", ColorRGBA.Red);
        
        yMat = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
        yMat.getAdditionalRenderState().setWireframe(true);
        yMat.getAdditionalRenderState().setLineWidth(4);
        yMat.setColor("Color", ColorRGBA.Green);
        
        zMat = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
        zMat.getAdditionalRenderState().setWireframe(true);
        zMat.getAdditionalRenderState().setLineWidth(4);
        zMat.setColor("Color", ColorRGBA.Blue);
    }
    
    public void attachCoordinateAxes(Node node, Vector3f pos) {
        Arrow arrow = new Arrow(Vector3f.UNIT_X);
        putShape(node, arrow, xMat).setLocalTranslation(pos);

        arrow = new Arrow(Vector3f.UNIT_Y);
        putShape(node, arrow, yMat).setLocalTranslation(pos);

        arrow = new Arrow(Vector3f.UNIT_Z);
        putShape(node, arrow, zMat).setLocalTranslation(pos);
    }

    public Geometry putShape(Node node, Mesh shape, Material mat) {
        Geometry g = new Geometry("coordinate axis", shape);
        g.setMaterial(mat);
        node.attachChild(g);
        return g;
    }
    
}
