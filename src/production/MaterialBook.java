/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package production;

import building.WindowStyle;
import com.jme3.material.Material;

/**
 *
 * @author frick
 */
public class MaterialBook {

    private Material[] fullMats;
    private Material baseMat;
    private Material levelMat;

    public MaterialBook(Material[] fullMats, Material baseMat, Material levelMat) {
        this.fullMats = fullMats;
        this.baseMat = baseMat;
        this.levelMat = levelMat;
    }

    public Material getFullMat(WindowStyle ws) {
        return fullMats[ WindowStyle.indexOf(ws) ];
    }

    public Material getBaseMat() {
        return baseMat;
    }
    
    public Material getLevelMat() {
        return levelMat;
    }
    
}
