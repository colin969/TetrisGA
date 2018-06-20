package dsp.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Colin Berry
 */
public class Population {
    ArrayList<Individual> pop;
    ArrayList<Individual> evalPop;
    int evalStep;
    public Individual nextToEval;
    
    int popSize;
    private float elitism = 0.05F;
    private float mutationDelta = 50F;
    private int numOfFloats;
    private float crossover;
    private float mutation;
    public int generation;
    boolean isEvaluating;
    
    public Population(int popSize, int numOfFloats, float crossover, float mutation){
        pop = new ArrayList();
        
        this.popSize = popSize;
        this.numOfFloats = numOfFloats;
        this.crossover = crossover;
        this.mutation = mutation;
        this.generation = 1;
        
        // Generate population
        Random rand = new Random();
        for(int i = 0; i < popSize; i++){
            Individual ind = new Individual();
            float[] weights = new float[numOfFloats];
            for(int w = 0; w < numOfFloats; w++){
                weights[w] = mutationDelta * rand.nextFloat() * (rand.nextBoolean() ? 1 : -1);
            }
            ind.weights = weights;
            pop.add(ind);
        }
        
        isEvaluating = true;
        evalStep = 0;
        evalPop = pop;
        evaluate();
    }
    
    // Called after every game, will set the next individual to be evaluated every time
    public void doStep(){
        if(isEvaluating){
            evaluate();
            return;
        }
        
        ArrayList<Individual> offspring = tournamentSelection(pop);
        offspring = crossover(offspring);
        evalPop = mutate(offspring);
        
        isEvaluating = true;
        evalStep = 0;
        evaluate();
    }
    
    // Select parents from the previous population, use elitism as bias
    public ArrayList<Individual> tournamentSelection(ArrayList<Individual> tempPop){
        ArrayList<Individual> newPop = new ArrayList();
        Random rand = new Random();
        
        Collections.sort(pop);       
        for(int i = 0; i < Math.floor((float) popSize * elitism); i++){
            newPop.add(pop.get(popSize-(i+1)));
        }
        
        for(int i = 0; i < Math.ceil((float) popSize * (1F - elitism)); i++){
            Individual parent1 = tempPop.get(rand.nextInt(popSize));
            Individual parent2 = tempPop.get(rand.nextInt(popSize));
            
            if(parent1.fitness > parent2.fitness){
                newPop.add(parent1);
            } else {
                newPop.add(parent2);
            }
        }
        return newPop;
    }
    
    public ArrayList<Individual> crossover(ArrayList<Individual> tempPop){
        ArrayList<Individual> newPop = new ArrayList();
        Random rand = new Random();
        
        for(int i = 0; i < popSize; i++){
            Individual child = new Individual();
            if(rand.nextFloat() < crossover){
                Individual parent1 = tempPop.get(rand.nextInt(popSize));
                Individual parent2 = tempPop.get(rand.nextInt(popSize));
                int crossoverPoint = rand.nextInt(numOfFloats);

                float[] weights;

                weights = parent1.weights.clone();
                for(int j = crossoverPoint; j < numOfFloats; j++){
                    weights[j] = parent2.weights[j];
                }

                child.weights = weights;
            } else {
                child.weights = tempPop.get(rand.nextInt(popSize)).weights.clone();
            }
            newPop.add(child);
        }
        
        return newPop;
    }
    
    public ArrayList<Individual> mutate(ArrayList<Individual> tempPop){
        Random rand = new Random();
        for(Individual i : tempPop){
            for(int w = 0; w < numOfFloats; w++){
                if(rand.nextFloat() < mutation){
                    i.weights[w] = mutationDelta * (rand.nextBoolean() ? 1 : -1) * rand.nextFloat();
                }
            }
        }
        return tempPop;
    }
    
    public void evaluate(){
        if(evalStep < popSize){
            nextToEval = evalPop.get(evalStep);
        } else {
            isEvaluating = false;
            pop = evalPop;
            generation++;
        }
    }
    
    public void processEval(Individual ind){
        evalPop.set(evalStep, ind);
        evalStep++; 
        evaluate();
    }

    public Individual getBest() {
        Individual bestInd = pop.get(0);
        
        for(Individual ind : pop){
            if(ind.fitness > bestInd.fitness){
                bestInd = ind;
            }
        }
        
        return bestInd;
    }
    
    
    
}
