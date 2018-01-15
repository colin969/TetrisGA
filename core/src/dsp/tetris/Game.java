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

/**
 *
 * @author Colin Berry
 */
public class Game {

	public static Tetromino[] tetrominoes;
        
        private int step;
        
        private int seed = 4;

	public Board[] boards;

	public void init() {
            tetrominoes = new Tetromino[7];
            // I Block
            tetrominoes[0] = new Tetromino(Color.CYAN, new Point[][]{
                {new Point(0,2), new Point(1,2), new Point(2,2), new Point(3,2)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(1,3)},
                {new Point(0,2), new Point(1,2), new Point(2,2), new Point(3,2)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(1,3)} 
            });
            
            // J Block
            tetrominoes[1] = new Tetromino(Color.BLUE, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(2,0)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(0,0)},
                {new Point(0,0), new Point(1,0), new Point(2,0), new Point(0,1)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,2)}
            });
            
            // L Block
            tetrominoes[2] = new Tetromino(Color.ORANGE, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(0,0)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(0,2)},
                {new Point(0,0), new Point(1,0), new Point(2,0), new Point(2,1)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,0)}
            });
            
            // O Block
            tetrominoes[3] = new Tetromino(Color.YELLOW, new Point[][]{
                {new Point(1,0), new Point(1,1), new Point(2,1), new Point(2,0)},
                {new Point(1,0), new Point(1,1), new Point(2,1), new Point(2,0)},
                {new Point(1,0), new Point(1,1), new Point(2,1), new Point(2,0)},
                {new Point(1,0), new Point(1,1), new Point(2,1), new Point(2,0)}
            });
            
            // S Block
            tetrominoes[4] = new Tetromino(Color.GREEN, new Point[][]{
                {new Point(0,0), new Point(1,0), new Point(1,1), new Point(2,1)},
                {new Point(0,2), new Point(0,1), new Point(1,1), new Point(1,0)},
                {new Point(0,0), new Point(1,0), new Point(1,1), new Point(2,1)},
                {new Point(0,2), new Point(0,1), new Point(1,1), new Point(1,0)}
            });
            
            // T Block
            tetrominoes[5] = new Tetromino(Color.PURPLE, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(1,0)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(0,1)},
                {new Point(0,0), new Point(1,0), new Point(2,0), new Point(1,1)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,1)}
            });
            
            // Z Block
            tetrominoes[6] = new Tetromino(Color.RED, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(1,0), new Point(2,0)},
                {new Point(1,0), new Point(1,1), new Point(2,1), new Point(2,2)},
                {new Point(0,1), new Point(1,1), new Point(1,0), new Point(2,0)},
                {new Point(1,0), new Point(1,1), new Point(2,1), new Point(2,2)}
            });
            
            boards = new Board[2];
            boards[0] = new Board(new Player(true, 1), seed, 10, 10);
            boards[1] = new Board(new Player(true, 1), seed, 310, 10);
            
            step = 0;
            
	}

	public void resetGame() {
            boards = new Board[2];
            seed += 1;
            boards[0] = new Board(new Player(true, 1), seed, 10, 10);
            boards[1] = new Board(new Player(true, 1), seed, 310, 10);
	}
        
        public void drawGame(ShapeRenderer renderer, BitmapFont font, SpriteBatch batch){
            // Draw boards
            renderer.begin();
            boards[0].draw(renderer);
            boards[1].draw(renderer);
            renderer.end();
            
            // Draw step and score values
            batch.begin();
            font.draw(batch, "Step", 240, 480);
            font.draw(batch, String.valueOf(step), 260, 450);
            font.draw(batch, "Score", 240, 420);
            font.draw(batch, String.valueOf(boards[0].getScore()), 260, 390);
            batch.end();
            
        }
        
        public void doStep(){
//            System.out.println("BOARD 1");
            boards[0].doStep();
//            System.out.println("BOARD 2");
            boards[1].doStep();
            
            // Move any garbage created to other board
            if(boards[0].garbageLevel > 0){
                boards[1].queueGarbage(boards[0].garbageLevel);
                boards[0].garbageLevel = 0;
            }
            if(boards[1].garbageLevel > 0){
                boards[0].queueGarbage(boards[1].garbageLevel);
                boards[1].garbageLevel = 0;
            }
            
            // Someone has lost, reset game.
            if(!boards[0].isAlive() || !boards[1].isAlive()){
                resetGame();
            }
            step++;
        }

	public Board getBoard(int id) {
		return null;
	}

}
