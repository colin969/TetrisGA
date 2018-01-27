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
    private int weights = 8;
    
    private Population pop;
    private Individual workingInd;
    
    public void init(){
        pop = new Population(10, weights, 0.95F, 0.08F);
        
        
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
    
    public float[] getBest(){
        return pop.getBest().weights;
    }
    
    public int getGen(){
        return pop.generation;
    }
    
    public int getIndNum(){
        return pop.evalStep + 1;
    }
    
    public int getIndNumMax(){
        return pop.popSize;
    }

    public void printGen() {
        System.out.println(String.format("Best Individual\n%s", pop.getBest()));
    }
    
    
}
