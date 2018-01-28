/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.tetris;

import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Colin Berry
 */
public class BoardTest {
    
    public static Game game;
    public static Board board;
    public static Player testPlayer;
    
    public BoardTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        game = new Game();
        game.init();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testPlayer = new Player();
        board = new Board(testPlayer, 1, 10, 10);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void pieceCollision(){
        Tetromino piece = Game.tetrominoes[0]; // I Piece
        
        // Test out of bounds X
        Action action = new Action();
        action.rot = 0;
        action.origin = new Point(0,0);
        board.setActivePiece(piece);
        testPlayer.nextTestAction = action;
        board.doStep();
        
        assert(true);
    }
    
    public boolean checkBoardPoints(Point[] points){
        return true;
    }
    
}
