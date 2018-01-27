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

	public static Tetromino[] tetrominoes;
        
        private int step;
        
        private int seed = 4;
        
        public int[] results;
        
        public boolean gameEnded;

	public Board[] boards;
        
        public boolean garbage;
        
        public boolean singlePlayer;
        
        public int stepsPerPermagarbage = 150;

	public void init() {
            results = new int[2];
            gameEnded = false;
            garbage = true;
            singlePlayer = false;
            tetrominoes = new Tetromino[7];
            // I Block
            tetrominoes[0] = new Tetromino(Color.CYAN, new Point[][]{
                {new Point(0,2), new Point(1,2), new Point(2,2), new Point(3,2)},
                {new Point(2,0), new Point(2,1), new Point(1,2), new Point(2,3)},
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(3,1)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(1,3)} 
            });
            
            // J Block
            tetrominoes[1] = new Tetromino(Color.BLUE, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(0,2)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,2)},
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(2,0)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(0,0)}
            });
            
            // L Block
            tetrominoes[2] = new Tetromino(Color.ORANGE, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(2,2)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,0)},
                {new Point(0,0), new Point(0,1), new Point(1,1), new Point(2,1)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(0,2)}
            });
            
            // O Block
            tetrominoes[3] = new Tetromino(Color.YELLOW, new Point[][]{
                {new Point(1,1), new Point(2,1), new Point(1,2), new Point(2,2)},
                {new Point(1,1), new Point(2,1), new Point(1,2), new Point(2,2)},
                {new Point(1,1), new Point(2,1), new Point(1,2), new Point(2,2)},
                {new Point(1,1), new Point(2,1), new Point(1,2), new Point(2,2)}
            });
            
            // S Block
            tetrominoes[4] = new Tetromino(Color.GREEN, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(1,2), new Point(2,2)},
                {new Point(1,2), new Point(1,1), new Point(2,1), new Point(2,0)},
                {new Point(0,0), new Point(1,0), new Point(1,1), new Point(2,1)},
                {new Point(0,2), new Point(0,1), new Point(1,1), new Point(1,0)}
            });
            
            // T Block
            tetrominoes[5] = new Tetromino(Color.PURPLE, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(1,2)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,1)},
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(1,0)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(0,1)}
            });
            
            // Z Block
            tetrominoes[6] = new Tetromino(Color.RED, new Point[][]{
                {new Point(0,2), new Point(1,2), new Point(1,1), new Point(2,1)},
                {new Point(1,0), new Point(1,1), new Point(2,1), new Point(2,2)},
                {new Point(0,1), new Point(1,1), new Point(1,0), new Point(2,0)},
                {new Point(0,0), new Point(0,1), new Point(1,1), new Point(1,2)}
            });
            
	}

	public void resetGame(float[] playerOne, float[] playerTwo) {
            boards = new Board[2];
            gameEnded = false;
            step = 1;
            
            boards[0] = new Board(new Player(true, playerOne, false), seed, 10, 10);
            boards[1] = new Board(new Player(true, playerTwo, false), seed, 310, 10);
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
            font.draw(batch, String.format("Piece\n%s", step), 220, 480);
            font.draw(batch, String.format("Score\n%s", boards[0].getScore()) , 220, 440);
            font.draw(batch, String.format("Lines\n%s", boards[0].getLinesCleared()), 270, 480);
            batch.end();
            
        }
        
        public void doStep(){
            // Add permanent garbage every specified number of steps
            if(step % stepsPerPermagarbage == 0){
                boards[0].addPerma();
                if(!singlePlayer)
                    boards[1].addPerma();
            }
            
            // Process the action for this step
            boards[0].doStep();
            if(!singlePlayer)
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

	public Board getBoard(int id) {
		return boards[id];
	}
        
        // Change the seed given to the piece randomizer
        public void updateSeed(){
            seed = (new Random()).nextInt();
        }

}
