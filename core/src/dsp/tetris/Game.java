/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author Colin Berry
 */
public class Game {
        
        private int step;
        
        private int seed = 4;
        
        public int[] results;
        
        public boolean gameEnded;

	public Board[] boards;
        
        public boolean garbage;
        
        public boolean singlePlayer;
        
        public int stepsPerPermagarbage = 500;

	public void init() {
            results = new int[2];
            gameEnded = false;
            garbage = true;
            singlePlayer = false;
            
	}

	public void resetGame(Player playerOne, Player playerTwo) {
            boards = new Board[2];
            gameEnded = false;
            step = 1;
            
            boards[0] = new Board(playerOne, seed, 10, 10);
            boards[1] = new Board(playerTwo, seed, 310, 10);
	}
        
        public void drawGame(ShapeRenderer renderer, BitmapFont font, SpriteBatch batch, boolean renderBoard){
            // Draw boards
            if(renderBoard){
                renderer.begin();
                boards[0].draw(renderer);
                boards[1].draw(renderer);
                renderer.end();
            }
            
            // Draw step and score values
            batch.begin();
            font.draw(batch, String.format("Piece\n%s", boards[0].getStep()), 220, 480);
            font.draw(batch, String.format("Score\n%s", boards[0].getScore()) , 220, 440);
            font.draw(batch, String.format("Lines\n%s", boards[0].getLinesCleared()), 270, 480);
            batch.end();
            
        }
        
        public void doStep(){
            // Add permanent garbage every specified number of steps
            if(boards[0].getStep() % stepsPerPermagarbage == 0){
                boards[0].addSolidLine();
                boards[1].addSolidLine();
            }
            
            // Process the action for this step
            boards[0].doStep();
            while(boards[0].getStep() > boards[1].getStep())
                boards[1].doStep();
            
            // Move any garbage created to other board
            if(garbage){
                if(boards[0].getGarbageLevel() > 0){
                    boards[1].queueGarbage(boards[0].getGarbageLevel());
                    boards[0].setGarbageLevel(0);
                }
                if(boards[1].getGarbageLevel() > 0){
                    boards[0].queueGarbage(boards[1].getGarbageLevel());
                    boards[1].setGarbageLevel(0);
                }
            }
            
            // Someone has lost, set the results for GA to process later
            if(!boards[0].isAlive() || !boards[1].isAlive()){
                gameEnded = true;
                results[0] = boards[0].getResults();
                results[1] = boards[1].getResults();
                
            }
            step++;
        }
        
        public void loadCPU(float[] cpu, int board){
            
        }
        
        public void saveGameData(){
            
        }

	public Board getBoard(int id) {
		return boards[id];
	}
        
        public Board getPlayerBoard(int player){
            for(Board b : boards){
                if(b.getPlayer().getId() == player)
                    return b;
            }
            
            return null;
        }
        
        // Change the seed given to the piece randomizer
        public void updateSeed(){
            seed = (new Random()).nextInt();
        }

}
