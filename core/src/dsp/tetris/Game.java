package dsp.tetris;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Colin Berry
 */
public class Game {
    
        private CSVWriter csv;
        
        private int playerRun;
        
        private int seed = 4;
        
        public int[] results;
        
        public boolean gameEnded;

	public Board[] boards;
        
        public boolean garbage;
        
        public boolean singlePlayer;
        
        public int stepsPerPermaGarbage = 350;
        
        public int nextGarbageStep;

	public void init() {
            results = new int[2];
            gameEnded = false;
            garbage = true;
            singlePlayer = false;
            
	}

	public void resetGame(Player playerOne, Player playerTwo) {
            boards = new Board[2];
            gameEnded = false;
            nextGarbageStep = stepsPerPermaGarbage;
            
            boards[0] = new Board(playerOne, seed, 10, 10);
            boards[1] = new Board(playerTwo, seed, 310, 10);
	}
        
        public void drawGame(ShapeRenderer renderer, BitmapFont font, SpriteBatch batch, boolean renderBoard){
            // Draw boards
            if(renderBoard){
                renderer.begin();
                boards[0].draw(renderer);
                boards[1].draw(renderer);
                renderer.end();
            }
            
            // Draw step and score values
            batch.begin();
            font.draw(batch, String.format("Piece\n%s", boards[0].getStep()), 220, 480);
            font.draw(batch, String.format("Score\n%s", boards[0].getScore()) , 220, 440);
            font.draw(batch, String.format("Lines\n%s", boards[0].getLinesCleared()), 270, 480);
            batch.end();
            
        }
        
        public void doStep(){
            // Add permanent garbage every specified number of steps
            if(boards[0].getStep() == nextGarbageStep){
                nextGarbageStep += stepsPerPermaGarbage;
                boards[0].addSolidLine();
                boards[1].addSolidLine();
            }
            
            // Process the action for this step
            boards[0].doStep();
            while(boards[0].getStep() > boards[1].getStep())
                boards[1].doStep();
            
            // Move any garbage created to other board
            if(garbage){
                if(boards[0].getGarbageLevel() > 0){
                    boards[1].queueGarbage(boards[0].getGarbageLevel());
                    boards[0].setGarbageLevel(0);
                }
                if(boards[1].getGarbageLevel() > 0){
                    boards[0].queueGarbage(boards[1].getGarbageLevel());
                    boards[1].setGarbageLevel(0);
                }
            }
            
            // Someone has lost, set the results for GA to process later
            if(!boards[0].isAlive() || !boards[1].isAlive()){
                gameEnded = true;
                results[0] = boards[0].getResults();
                results[1] = boards[1].getResults();
                
            }
        }
        
        public void openGameData(){
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            
            Writer writer;
            try {
                writer = Files.newBufferedWriter(Paths.get(String.format("./game_%s-%s-%s.csv", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND))));

                csv = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);

                String[] headers = {"Run", "Steps"};
                csv.writeNext(headers);
                playerRun = 1;
            } catch (IOException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void closeGameData(){
            if(csv != null){
                try {
                    csv.close();
                } catch (IOException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        public void saveGameData(){
            // Negative if AI lost, postitive if won, number of steps it ran for
            int score = boards[0].getStep() * (boards[1].isAlive() ? 1 : -1);
            
            String[] toWrite = { String.valueOf(playerRun), String.valueOf(score) };
            csv.writeNext(toWrite);
            playerRun++;
        }

	public Board getBoard(int id) {
		return boards[id];
	}
        
        // Returns the Board object a given player is on
        public Board getPlayerBoard(int player){
            for(Board b : boards){
                if(b.getPlayer().getId() == player)
                    return b;
            }
            
            return null;
        }
        
        // Change the seed given to the piece randomizer
        public void updateSeed(){
            seed = (new Random()).nextInt();
        }

}
