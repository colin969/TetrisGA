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
    public static final int NUM_WEIGHTS = 8;
    
    private Population pop;
    private Individual workingInd;
    
    public void init(){
        pop = new Population(10, NUM_WEIGHTS, 0.95F, 0.08F);
        workingInd = null;
        
    }
    
    public float[] startGame(){
        
        while(!pop.isEvaluating){
            pop.doStep();
        }
        workingInd = pop.nextToEval;
        return workingInd.weights;
    }
    
    public void returnResults(int result){
        workingInd.fitness = result;
        pop.processEval(workingInd);
    }
    
    // TODO
    public void printToCSV(){
        
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
