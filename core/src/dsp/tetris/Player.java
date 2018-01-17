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

        public Player(boolean cpu, float[] genome){
            this.genome = new float[1];
            this.genome = genome;
            this.cpu = cpu;
        }
    
	private boolean cpu;

	private float[] genome;
        
        // SIMPLE AI - Build without holes, best clears, then lowest height
        public Action nextMove(ArrayList<Action> actions){
            
            Action bestAction = null;
            float bestScore = 0;
            
            // Set initial move
            bestAction = actions.get(0);
            bestScore += genome[0] * (float)bestAction.holes;
            bestScore += genome[1] * (float)bestAction.clears;
            bestScore += genome[2] * (float)bestAction.height;
            
            // Find best move(s)
            for(Action action : actions){
                float totalScore = 0;
                totalScore += genome[0] * (float)action.holes;
                totalScore += genome[1] * (float)action.clears;
                totalScore += genome[2] * (float)action.height;
                if(totalScore > bestScore){
                    bestAction = action;
                    bestScore = totalScore;
                }
            }
            
            return bestAction;
        }
        
        public boolean getCpu(){
            return this.cpu;
        }

}
