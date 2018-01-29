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

        public int clears;
        public int height;
        public int aggregateHeight;
        public int holes;
        public int bumpiness;
        public Point origin;
        public int rot;
        public boolean swap;
        
        public Action(int clears, int height, int holes, int aggregateHeight){}
        
        public Action(){}
        
        @Override
        public String toString(){
            return String.format("Origin - %s - Rotation - %s - Holes %s - Height %s - Aggregate Height %s - Clears %s - Bumpiness %s", origin, rot, holes, height, aggregateHeight, clears, bumpiness);
        }

}
