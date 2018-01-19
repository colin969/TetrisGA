/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.ga;

import java.util.Arrays;

/**
 *
 * @author Colin Berry
 */
public class Individual implements Comparable{
    float[] weights;
    int fitness;
    
    @Override
    public int compareTo(Object ind){
        if(!(ind instanceof Individual)){
            throw new ClassCastException("An individual object expected");
        } else {
            return this.fitness - ((Individual) ind).fitness;
        }
    }
    
    @Override
    public String toString(){
        return String.format("Weights - %s\nFitness - %s", Arrays.toString(weights), fitness);
    }
}
