package dsp.tetris;

import com.badlogic.gdx.graphics.Color;
import java.awt.Point;

/**
 *
 * @author Colin Berry
 */
public class DataSets {
    
    public static final Tetromino[] TETROMINOES = {
            new Tetromino("I", Color.CYAN, new Point[][]{
                {new Point(0,2), new Point(1,2), new Point(2,2), new Point(3,2)},
                {new Point(2,0), new Point(2,1), new Point(2,2), new Point(2,3)},
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(3,1)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(1,3)} 
            }),
            new Tetromino("J", Color.BLUE, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(0,2)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,2)},
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(2,0)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(0,0)}
            }),
            new Tetromino("L", Color.ORANGE, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(2,2)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,0)},
                {new Point(0,0), new Point(0,1), new Point(1,1), new Point(2,1)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(0,2)}
            }),
            new Tetromino("O", Color.YELLOW, new Point[][]{
                {new Point(1,1), new Point(2,1), new Point(1,2), new Point(2,2)},
                {new Point(1,1), new Point(2,1), new Point(1,2), new Point(2,2)},
                {new Point(1,1), new Point(2,1), new Point(1,2), new Point(2,2)},
                {new Point(1,1), new Point(2,1), new Point(1,2), new Point(2,2)}
            }),
            new Tetromino("S", Color.GREEN, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(1,2), new Point(2,2)},
                {new Point(1,2), new Point(1,1), new Point(2,1), new Point(2,0)},
                {new Point(0,0), new Point(1,0), new Point(1,1), new Point(2,1)},
                {new Point(0,2), new Point(0,1), new Point(1,1), new Point(1,0)}
            }),
            new Tetromino("T", Color.PURPLE, new Point[][]{
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(1,2)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,1)},
                {new Point(0,1), new Point(1,1), new Point(2,1), new Point(1,0)},
                {new Point(1,0), new Point(1,1), new Point(1,2), new Point(0,1)}
            }),
            new Tetromino("Z", Color.RED, new Point[][]{
                {new Point(0,2), new Point(1,2), new Point(1,1), new Point(2,1)},
                {new Point(1,0), new Point(1,1), new Point(2,1), new Point(2,2)},
                {new Point(0,1), new Point(1,1), new Point(1,0), new Point(2,0)},
                {new Point(0,0), new Point(0,1), new Point(1,1), new Point(1,2)}
            })
    };
    
    public static final Point[][] ROT_MATRIX_REST = new Point[][]{
            /* 0 > 1 */ { new Point( 0, 0), new Point(-1, 0), new Point(-1, 1), new Point( 0,-2), new Point(-1,-2)},
            /* 1 > 1 */ { new Point( 0, 0), new Point( 1, 0), new Point( 1,-1), new Point( 0, 2), new Point( 1, 2)},
            /* 1 > 2 */ { new Point( 0, 0), new Point( 1, 0), new Point( 1,-1), new Point( 0, 2), new Point( 1, 2)},
            /* 2 > 1 */ { new Point( 0, 0), new Point(-1, 0), new Point(-1, 1), new Point( 0,-2), new Point(-1,-2)},
            /* 2 > 3 */ { new Point( 0, 0), new Point( 1, 0), new Point( 1, 1), new Point( 0,-2), new Point( 1,-2)},
            /* 3 > 2 */ { new Point( 0, 0), new Point(-1, 0), new Point(-1,-1), new Point( 0, 2), new Point(-1, 2)},
            /* 3 > 0 */ { new Point( 0, 0), new Point(-1, 0), new Point(-1,-1), new Point( 0, 2), new Point(-1, 2)},
            /* 0 > 3 */ { new Point( 0, 0), new Point( 1, 0), new Point( 1, 1), new Point( 0,-2), new Point( 1,-2)}
    };
    
    public static final Point[][] ROT_MATRIX_I = new Point[][]{
            /* 0 > 1 */ { new Point( 0, 0), new Point(-2, 0), new Point( 1, 0), new Point(-2,-1), new Point( 1, 2)},
            /* 1 > 1 */ { new Point( 0, 0), new Point( 2, 0), new Point(-1, 0), new Point( 2, 1), new Point(-1,-2)},
            /* 1 > 2 */ { new Point( 0, 0), new Point(-1, 0), new Point( 2, 0), new Point(-1, 2), new Point( 2,-1)},
            /* 2 > 1 */ { new Point( 0, 0), new Point( 1, 0), new Point(-2, 0), new Point( 1,-2), new Point(-2, 1)},
            /* 2 > 3 */ { new Point( 0, 0), new Point( 2, 0), new Point(-1, 0), new Point( 2, 1), new Point(-1,-2)},
            /* 3 > 2 */ { new Point( 0, 0), new Point(-2, 0), new Point( 1, 0), new Point(-2,-1), new Point( 1, 2)},
            /* 3 > 0 */ { new Point( 0, 0), new Point( 1, 0), new Point(-2, 0), new Point( 1,-2), new Point(-2, 1)},
            /* 0 > 3 */ { new Point( 0, 0), new Point(-1, 0), new Point( 2, 0), new Point(-1, 2), new Point( 2,-1)}
    };
    
    // 3 or 4 MUST be filled, 1 or 2 = Mini T Spin, 1 and 2 = T Spin
    public static final Point[][] T_SPIN_MATRIX = new Point[][]{
        { new Point(0,2), new Point(2,2), new Point(0,0), new Point(2,0) },
        { new Point(2,2), new Point(2,0), new Point(0,2), new Point(0,0) },
        { new Point(2,0), new Point(0,0), new Point(0,2), new Point(2,2) },
        { new Point(0,0), new Point(0,2), new Point(2,0), new Point(2,2) }
    };
}
