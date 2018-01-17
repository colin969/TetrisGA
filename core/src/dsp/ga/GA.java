/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.ga;

import java.util.Random;

/**
 *
 * @author Colin Berry
 */
public class GA {
    public static void main(String[] args) {
        
    }
    
    Population pop;
    Individual workingInd;
    
    public void init(){
        pop = new Population(10, 3, 0.95F, 0.08F);
        
        
    }
    
    public float[] startGame(){
        
        while(!pop.isEvaluating){
            pop.doStep();
        }
        workingInd = pop.nextToEval;
        return workingInd.weights;
    }
    
    public void returnResults(int[] results){
        workingInd.fitness = results[0];
        pop.processEval(workingInd);
    }
    
    public float[] getRandom(){
        return pop.pop.get((new Random()).nextInt(10)).weights;
    }
    
    public int getGen(){
        return pop.generation;
    }
    
    public int getGenNum(){
        return pop.evalStep + 1;
    }
    
    public int getGenNumMax(){
        return pop.popSize;
    }
    
    
}
