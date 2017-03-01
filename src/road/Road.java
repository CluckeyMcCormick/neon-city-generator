/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package road;

import com.jme3.scene.Geometry;

/**
 *
 * @author root
 */
public class Road {
    
    private RoadSize size;
    private Geometry road;
    private int roadAngle;

    public Road(RoadSize size, int roadAngle) {
        this.size = size;
        this.roadAngle = roadAngle;
    }

    public RoadSize getSize() {
        return size;
    }

    public void setSize(RoadSize size) {
        this.size = size;
    }

    public Geometry getRoad() {
        return road;
    }

    public void setRoad(Geometry road) {
        this.road = road;
    }

    public int getRoadAngle() {
        return roadAngle;
    }

    public void setRoadAngle(int roadAngle) {
        this.roadAngle = roadAngle;
    }
    
}
