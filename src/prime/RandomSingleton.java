/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prime;

import java.util.Random;

/**
 *
 * @author frick
 */
public class RandomSingleton {
    private static RandomSingleton instance;
    private Random rnd;

    private RandomSingleton(long seed) {
        rnd = new Random(seed);
    }
    
    private RandomSingleton() {
        rnd = new Random();
    }

    public static RandomSingleton getInstance() {
        if(instance == null) {
            instance = new RandomSingleton();
        }
        return instance;
    }
    
    public static RandomSingleton getInstance(long seed) {
        if(instance == null) {
            instance = new RandomSingleton(seed);
        }
        
        return instance;
    }

    public int nextInt() {
         return rnd.nextInt();
    }
    
    public int nextInt(int n) {
        if(n == 0)
            return 0;
        return rnd.nextInt(n);
    }
    
    public double nextGaussian(){
        return rnd.nextGaussian();
    }
}