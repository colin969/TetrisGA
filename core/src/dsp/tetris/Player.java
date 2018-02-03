/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.tetris;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Colin Berry
 */
public class Player {

        public Player(){
            this(false, false, 0);
        }
        
        public Player(boolean cpu, boolean debug, int id){
            this(cpu, null, debug, id);
        }
        
        public Player(boolean cpu, float[] genome, boolean debug, int id){
            this.id = id;
            this.genome = genome;
            this.cpu = cpu;
            this.debug = debug;
        }
        
        private int id;
    
	private boolean cpu;
        
        private boolean debug;

	private float[] genome;
        
        public Action nextTestAction;
        
        // Choose move based on weights
        public Action nextMove(ArrayList<Action> actions){
            
            Action bestAction = null;
            float bestScore = 0;
            
            // Set initial move
            bestAction = actions.get(0);
            bestScore = addUp(bestAction);
            
            // Find best move(s)
            for(Action action : actions){
                float totalScore = addUp(action);
                if(totalScore > bestScore){
                    bestAction = action;
                    bestScore = totalScore;
                }
            }
            if(debug)
                System.out.println(bestAction);
            return bestAction;
        }
        
        private float addUp(Action action){
            float totalScore = 0;
            if(action.clears > 0)
                totalScore += genome[action.clears-1];
            totalScore += genome[4] * (float)action.holes;
            totalScore += genome[5] * (float)action.height;
            totalScore += genome[6] * (float)action.aggregateHeight;
            totalScore += genome[7] * (float)action.bumpiness;
            totalScore += genome[8] * (float)action.highest;
            totalScore += genome[9] * (float)action.altitudeDiff;
            totalScore += genome[10] * (float)action.weightedClears;
            totalScore += genome[11] * (float)action.tSpin;
            totalScore += genome[12] * (float)action.rowTrans;
            totalScore += genome[13] * (float)action.rowTransWeighted;
            totalScore += genome[14] * (float)action.colTrans;
            totalScore += genome[15] * (float)action.colTransWeighted;
            return totalScore;
        }
        
        public boolean getCpu(){
            return this.cpu;
        }
        
        public int getId(){
            return this.id;
        }
        
        public void debug(){
            debug = !debug;
        }

}
