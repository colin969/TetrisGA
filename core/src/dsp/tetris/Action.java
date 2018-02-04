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

        // 4 seperate weights for Clears
        public int clears;
        public int weightedClears;
        public int height;
        public int aggregateHeight;
        public int bumpiness;
        public int highest;
        public int altitudeDiff;
        public int tSpin;
        public int rowTrans;
        public int rowTransWeighted;
        public int colTrans;
        public int colTransWeighted;
        public int coveredHoles;
        
        public Point origin;
        public int rot;
        public boolean swap;
        
        @Override
        public String toString(){
            return String.format("Origin - %s - Rotation - %s - Height %s - Aggregate Height %s - Clears %s\n"
                    + "Weighted Clears %s - Bumpiness %s - AltitudeDiff %s - Highest %s - T Spin %s - Row Trans %s - Row Trans Weighted %s\n"
                    + "Col Trans %s - Col Trans Weighted %s - Covered Holes %s", 
                    origin, rot, height, aggregateHeight, clears, 
                    weightedClears, bumpiness, altitudeDiff, highest, tSpin, rowTrans, rowTransWeighted,
                    colTrans, colTransWeighted, coveredHoles);
        }

}
