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

        public Player(boolean cpu, float genome){
            this.genome = new float[1];
            this.genome[0] = genome;
            this.cpu = cpu;
        }
    
	private boolean cpu;

	private float[] genome;
        
        // SIMPLE AI - Build without holes, best clears, then lowest height
        public Action nextMove(Action[][] actions){
            
            ArrayList<Action> bestActions = new ArrayList();
            
            for(int x = 0; x < 10; x++){
                for(int y = 0; y < 4; y++){
                    if(actions[x][y].valid){
                        bestActions.add(actions[x][y]);
                        break;
                    }
                }
                if(!bestActions.isEmpty())
                    break;
            }
            
            // If no valid moves available, return invalid move
            if(bestActions.isEmpty()){
                return actions[0][0];
            }
            
            for(Action[] actionSet: actions){
                for(int rot = 0; rot < 4; rot++){
                    Action action = actionSet[rot];
                    if(action.valid){
                        action.rot = rot;
                        Action tempBest = bestActions.get(0);
                        if(action.holes < tempBest.holes){
                            bestActions.clear();
                            bestActions.add(action);
                        }
                        else if(action.holes == tempBest.holes){
                            if(action.clears > tempBest.clears){
                                bestActions.clear();
                                bestActions.add(action);
                            }
                            else if(action.clears == tempBest.clears){
                                if(action.height < tempBest.height){
                                    bestActions.clear();
                                    bestActions.add(action);
                                } else if(action.height == tempBest.height)
                                    bestActions.add(action);
                            }
                        }
                    }
                }
            }
            
            Action best = bestActions.get(new Random().nextInt(bestActions.size()));
//            Action best = bestActions.get(0);
            return best;
        }
        
        public boolean getCpu(){
            return this.cpu;
        }

}
