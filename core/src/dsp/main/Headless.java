/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.main;

import dsp.ga.GA;
import dsp.tetris.Game;
import dsp.tetris.Player;

/**
 *
 * @author Colin Berry
 */
public class Headless {
    public static void main(String[] args) {
        Game game = new Game();
        game.init();
        GA ga = new GA();
        ga.init();
        Player testCase = new Player(true, ga.getRandom(), false, 1);
        int lastGen = 1;
        int gensToRun = 30;
        
        game.resetGame(new Player(true, ga.startGame(), false, 0), testCase);
        
        while(ga.getGen() < gensToRun){
            // Game ended, process results, start next game
            if(game.gameEnded){
                // Return left board (evaluating) result
                ga.returnResults(game.results[0]);
                // Check for change in generation
                if(ga.getGen() != lastGen){
                    testCase = new Player(true, ga.getBest(), false, 1);
                    lastGen = ga.getGen();
                    System.out.println(String.format("Generation %s", ga.getGen()));
                    ga.printGen();
                    ga.printToCSV();
                    game.updateSeed();
                }
                game.resetGame(new Player(true, ga.startGame(), false, 0), testCase);
            }
            game.doStep();
        }
        ga.closeCSV();
    }
}
