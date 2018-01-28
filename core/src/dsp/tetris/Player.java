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
            this(2, false);
        }
        
        public Player(int type, boolean debug){
            this.genome = null;
            this.type = type;
            this.debug = debug;
        }
        
        public Player(int type, float[] genome, boolean debug){
            this.genome = genome;
            this.type = type;
            this.debug = debug;
        }
    
	private int type;
        
        private boolean debug;

	private float[] genome;
        
        public Action nextTestAction;
        
        // Choose move based on weights
        public Action nextMove(ArrayList<Action> actions){
            
            if(type == 2)
                return nextTestAction;
            
            Action bestAction = null;
            float bestScore = 0;
            
            // Set initial move
            bestAction = actions.get(0);
            if(bestAction.clears > 0)
                bestScore += genome[bestAction.clears-1];
            bestScore += genome[4] * (float)bestAction.holes;
            bestScore += genome[5] * (float)bestAction.height;
            bestScore += genome[6] * (float)bestAction.aggregateHeight;
            bestScore += genome[7] * (float)bestAction.bumpiness;
            
            // Find best move(s)
            for(Action action : actions){
                float totalScore = 0;
                if(action.clears > 0)
                    totalScore += genome[action.clears-1];
                totalScore += genome[4] * (float)action.holes;
                totalScore += genome[5] * (float)action.height;
                totalScore += genome[6] * (float)action.aggregateHeight;
                totalScore += genome[7] * (float)action.bumpiness;
                if(totalScore > bestScore){
                    bestAction = action;
                    bestScore = totalScore;
                }
            }
            if(debug)
                System.out.println(bestAction);
            return bestAction;
        }
        
        public int getType(){
            return this.type;
        }
        
        public void debug(){
            debug = !debug;
        }

}
