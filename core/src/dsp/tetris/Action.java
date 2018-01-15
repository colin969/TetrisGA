/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.tetris;

import java.awt.Point;

/**
 *
 * @author Colin Berry
 */
public class Action {

        public boolean valid;
        public int clears;
        public int height;
        public int holes;
        public int blockades;
        public Point origin;
        public int rot;
        
        public Action(int clears, int height, int holes, int blockades){}
        
        public Action(){}
    
	private String type;

	private int score;

}
