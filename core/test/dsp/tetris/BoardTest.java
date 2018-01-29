/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.tetris;

import com.badlogic.gdx.graphics.Color;
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
    
    // Check that pieces will collide with board edges and other pieces
    @Test
    public void pieceCollision(){
        Tetromino piece = Game.tetrominoes[0]; // I Piece
        Point[] filledTest1 = { new Point(4,0), new Point(4,1), new Point(4,2), new Point(4,3)};
        Point[] filledTest2 = { new Point(3,4), new Point(4,4), new Point(4,4), new Point(5,4)};
        board.doStep();
        
        board.setActivePiece(piece);
        board.rotate(true);
        for (int i = 0; i < 19; i++) {
            board.softDrop();
        }
        // If piece hit floor and stopped
        if(checkBoardPoints(filledTest1)) {
            board.setActivePiece(piece);
            for (int i = 0; i < 16; i++) {
                board.softDrop();
            }
            
            // If piece hit other piece and stopped
            if(checkBoardPoints(filledTest2)){
                assert(true);
            }
            
        }
        
        board.doStep();
    }
    
    // Rotate T piece left into 5th possible test
    @Test
    public void rotation(){
        Tetromino piece = Game.tetrominoes[5];
        Point[] fillRotation = { new Point(7,0), new Point(7,1), new Point(7,2), new Point(6,1)};
        populateBoard(TestBoards.ROTATE);
        board.doStep();
        board.setActivePiece(piece);
        board.setOrigin(new Point(5,2));
        
        board.rotate(false);
        board.softDrop();
        if(checkBoardPoints(fillRotation))
            assert(true);
    }
    
    public void populateBoard(Point[] points){
        Color[][] realBoard = board.getBoard();
        
        for(Point p : points){
            realBoard[p.x][p.y] = Color.GRAY;
        }
    }
    
    public boolean checkBoardPoints(Point[] points){
        Color[][] realBoard = board.getBoard();
        Color background = board.getBackground();
        for(Point p : points){
            if(realBoard[p.x][p.y] == background)
                return false;
        }
        return true;
    }
    
}
