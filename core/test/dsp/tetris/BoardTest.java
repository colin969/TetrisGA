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
    
    public static Board board;
    public static Player testPlayer;
    
    public BoardTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
    public void collides(){
        // I piece
        Tetromino piece = DataSets.TETROMINOES[0]; // I Piece
        Point[] filledTest1 = { new Point(5,0), new Point(5,1), new Point(5,2), new Point(5,3)};
        Point[] filledTest2 = { new Point(3,4), new Point(4,4), new Point(4,4), new Point(5,4)};
        board.doStep();
        
        board.setActivePiece(piece);
        board.setPieceRot(1);
        board.hardDrop();
        // If piece hit floor and stopped
        if(checkBoardPoints(filledTest1)) {
            board.setActivePiece(piece);
            board.hardDrop();
            
            assertTrue(checkBoardPoints(filledTest2));
        } else {
            assertTrue(false);
        }
    }
    
    // Rotate T piece left into 5th possible spot
    @Test
    public void rotate(){
        
        // Rotate Left
        Tetromino piece = DataSets.TETROMINOES[5];
        Point[] leftRotate = piece.point[3];
        Point[] rightRotate = piece.point[1];
        
        // Rotate left
        board.setActivePiece(piece);
        board.setOrigin(new Point(6,5));
        board.rotate(false);
        assertTrue(comparePoints(board.getActivePiece().point[board.getPieceRot()], leftRotate));
        
        // Rotate right
        board.setPieceRot(0);
        board.rotate(true);
        assertTrue(comparePoints(board.getActivePiece().point[board.getPieceRot()], rightRotate));
        
        // Reset piece
        board.setPieceRot(0);
        
        // Kick data check
        // T piece
        Point[] fillRotation = { new Point(7,0), new Point(7,1), new Point(7,2), new Point(6,1) };
        populateBoard(TestBoards.ROTATE);
        board.setOrigin(new Point(5,2));
        
        board.rotate(false);
        board.hardDrop();
        assertTrue(checkBoardPoints(fillRotation));
    }
    
    @Test
    public void slide(){
        // I Piece
        Tetromino piece = DataSets.TETROMINOES[0];
        board.setActivePiece(piece);
        board.setOrigin(new Point(3,-2));
        
        // Shift left once, starts at 3,-2, must be at 2, -2
        board.slide(false);
        assertTrue(board.getOrigin().equals(new Point(2,-2)));
        
        // Shift into left wall, should stop at edge 0, -2
        for (int i = 0; i < 10; i++) {
            board.slide(false);
        }
        assertTrue(board.getOrigin().equals(new Point(0,-2)));
        
        // Shift right
        board.slide(true);
        assertTrue(board.getOrigin().equals(new Point(1,-2)));
        
        // Shift into right wall, should stop at edge 6, -2
        for (int i = 0; i < 10; i++) {
            board.slide(true);
        }
        assertTrue(board.getOrigin().equals(new Point(6,-2)));
        
    }
    
    // Drop 6 I pieces (fill 0-23), next step should end game due to overlapping on spawn
    @Test
    public void overlapFailure(){
        
        // I piece
        Tetromino piece = DataSets.TETROMINOES[0];
        for (int i = 0; i < 6; i++) {
            board.setActivePiece(piece);
            board.setOrigin(new Point(3,20));
            board.setPieceRot(1);
            board.hardDrop();
        }
        assertTrue(!board.isAlive());
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
    
    public boolean comparePoints(Point[] points1, Point[] points2){
        for (int i = 0; i < points2.length; i++) {
            if(!points1.equals(points2)){
                return false;
            }
        }
        return true;
    }
    
}
