package dsp.tetris;

import com.badlogic.gdx.graphics.Color;
import java.awt.Point;

/**
 *
 * @author Colin Berry
 */
public class Tetromino {
    
    public String letter;
    public Color color;
    public Point[][] point;
    
    public Tetromino(String letter, Color color, Point[][] point){
        this.letter = letter;
        this.color = color;
        this.point = point;
    }
    
}
