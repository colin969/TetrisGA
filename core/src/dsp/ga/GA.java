/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.ga;

import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Colin Berry
 */
public class GA {
    public static final int NUM_WEIGHTS = 13;
    public static final int POP_SIZE = 100;
    
    private Population pop;
    private Individual workingInd;
    private CSVWriter csv;
    
    public void init(){
        pop = new Population(POP_SIZE, NUM_WEIGHTS, 0.85F, 0.03F);
        workingInd = null;
        
        try{
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            
            Writer writer = Files.newBufferedWriter(Paths.get(String.format("./training_%s-%s.csv", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))));

            csv = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            
            String[] headers = {"Generation", "Relative Fitness", "Best Weights"};
            csv.writeNext(headers);
        } catch (IOException e){
        }
        
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
        Individual best = pop.getBest();
        String[] data = new String[2 + NUM_WEIGHTS];
        data[0] = String.valueOf(pop.generation);
        for (int i = 0; i < NUM_WEIGHTS; i++) {
            data[i+2] = String.valueOf(best.weights[i]);
        }
        data[1] = String.valueOf(best.fitness);
        csv.writeNext(data);
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

    public void closeCSV() {
        try {
            csv.close();
        } catch (IOException ex) {
            
        }
    }
    
    
}
