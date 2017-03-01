/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import prime.RandomSingleton;

/**
 *
 * @author root
 */
public enum WindowStyle {
    STANDARD,
    VERTICAL,
    RANDOM,
    RIBBON;
    
    public static WindowStyle randomStyle(){
        RandomSingleton rs = RandomSingleton.getInstance();
        WindowStyle[] values = WindowStyle.values();
        return values[rs.nextInt(values.length)];   
    }
    
    public static int indexOf(WindowStyle ws){
        switch(ws){
            default:
            case STANDARD:
                return 0;
            case VERTICAL:
                return 1;
            case RANDOM:
                return 2;
            case RIBBON:
                return 3;
        }
    }
}
