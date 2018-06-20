package dsp.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dsp.ga.GA;
import dsp.tetris.Board;
import dsp.tetris.Game;
import dsp.tetris.Player;
import javax.swing.JOptionPane;

/**
 *
 * @author Colin Berry
 */
public class TetrisGA extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;
        private BitmapFont font;
        private Game game;
        private ShapeRenderer shapeRenderer;
        private float gameUpdateRate = 1F;
        private float timePassed;
        private long lastPress;
        private boolean stillPressed;
        private long repeatTime;
        private Player testCase;
        private int lastGen;
        private boolean fullRender;
        private float savedUpdateRate;
        private boolean paused;
        private Player heldSolution;
        private final int trainingLimit = 25;
        private GA ga;
        private boolean gen = true;
        private boolean lastGameGen;
	
	@Override
	public void create () {
                // Set up initial GA pop
                ga = new GA();
                ga.init();
            
                // Set up default game values
                game = new Game();
                game.init();
                timePassed = 0;
                lastPress = 0;
                stillPressed = false;
                repeatTime = (long) (0.2 * 1000.0);
                
                testCase = new Player(true, ga.getRandom(), false, 1);
                lastGen = 1;
                if(gen){
                    game.resetGame(new Player(true, ga.startGame(), false, 0), testCase);
                    lastGameGen = true;
                }
                else
                    game.resetGame(new Player(false, false, 0), testCase);
                
                // Set up rendering objects
                shapeRenderer = new ShapeRenderer();
                shapeRenderer.setAutoShapeType(true);
                
                batch = new SpriteBatch();
                
                font = new BitmapFont();
                font.setColor(Color.BLACK);
                
                fullRender = true;
                paused = false;
                
                // Use key and touch methods here for processing inputs
                Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
                timePassed += Gdx.graphics.getDeltaTime();
                
                if(stillPressed && !gen && (lastPress + repeatTime) < System.currentTimeMillis() && !paused){
                    lastPress = System.currentTimeMillis();
                    checkInputs();
                }
                
                if(!paused){
                    // Game ended, process results, start next game
                    if(game.gameEnded){
                        // Continuing training
                        if(gen && lastGameGen){
                            // Return left board (evaluating) result
                            ga.returnResults(game.results[0]);
                            // Check for change in generation
                            if(ga.getGen() != lastGen){
                                // Reset program when done with training, do a fresh population next
                                if(ga.getGen() > trainingLimit){
                                    ga.printGen();
                                    ga.printToCSV();
                                    dispose();
                                    create();
                                    return;
                                }
                                testCase = new Player(true, ga.getBest(), false, 1);
                                lastGen = ga.getGen();
                                ga.printGen();
                                ga.printToCSV();
                                game.updateSeed();
                            }
                            game.resetGame(new Player(true, ga.startGame(), false, 0), testCase);
                        } 
                        // Starting player game, training was cut early, Solution is held to be put back into training later
                        if(gen && !lastGameGen){
                            game.resetGame(heldSolution, testCase);
                            lastGameGen = true;
                        }
                        // Continue player games
                        if (!gen){
                            game.saveGameData();
                            game.resetGame(new Player(false, false, 0), testCase);
                        }
                    }
                    
                    // Go forward 1 step in the game logic
                    while(timePassed > gameUpdateRate){
                        timePassed = 0;
                        game.doStep();
                    }
                }
                
                
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                // Print running Generation and progress through population
                batch.begin();
                if(gen){
                    font.draw(batch, String.format("Generation\n%s", ga.getGen()), 220, 100);
                    font.draw(batch, String.format("Individual\n%s of %s", ga.getIndNum(), ga.getIndNumMax()), 220, 60);
                }
                font.draw(batch, String.format("Step Rate\n%s", 1/gameUpdateRate), 520, 60);
                // Pause text
                if(paused)
                    font.draw(batch, "P A U S E D", 220, 200);
                batch.end();

                // Render boards (includes upcoming pieces and held), fullRender toggle will only render stats, not pieces.
                game.drawGame(shapeRenderer, font, batch, fullRender);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
                font.dispose();
                // Ensure CSV is closed properly when app closes
                ga.closeCSV();
                game.closeGameData();
	}
        
        // Checks for continuous inputs to allow smooth slides and drops, NOT the main input handler.
        
        private void checkInputs(){
            Board board = game.getPlayerBoard(0);
            if(board != null){
                board.checkPieceExists();
                
                if (Gdx.input.isKeyPressed(Keys.DOWN))
                    board.softDrop();
                else if (Gdx.input.isKeyPressed(Keys.LEFT))
                    board.slide(false);
                else if (Gdx.input.isKeyPressed(Keys.RIGHT))
                    board.slide(true);
                else{
                    stillPressed = false;
                }
            }
        }
        
        // Main input handler, hotkeys and player controls
        
        @Override
        public boolean keyDown(int keycode) {
            // Adjust step change if shift is held
            float step = 0.05F;
            if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
                step = 0.5F;
            }
            
            // Disable board rendering, puts to full speed - TODO : Tweaks to show rest of UI
            if(keycode == Keys.R){
                fullRender = !fullRender;
                if(!fullRender){
                    savedUpdateRate = gameUpdateRate;
                    gameUpdateRate = 0;
                } else {
                    gameUpdateRate = savedUpdateRate;
                }
            }
            
            // Stop/start garbage
            if(keycode == Keys.G){
                game.garbage = !game.garbage;
            }
            
            // Stop/start second board
            if(keycode == Keys.S){
                game.singlePlayer = !game.singlePlayer;
            }
            
            // Pause/unpause game
            if(keycode == Keys.SPACE){
                paused = !paused;
            }
            
            // Enable action debug
            if(keycode == Keys.A){
                Player player = game.getBoard(0).getPlayer();
                player.debug();
            }
            
            // Change step speed
            if(keycode == Keys.LEFT_BRACKET){
                gameUpdateRate += step;
            }
            if(keycode == Keys.RIGHT_BRACKET){
                gameUpdateRate -= step;
                if(gameUpdateRate < 0){
                    gameUpdateRate = 0;
                }
            }
            
            // Load in an AI to play against
            if(keycode == Keys.L){
                String[] weights = JOptionPane.showInputDialog("Enter a list of weights").split("\t");
                testCase = new Player(true, weights, 1);
                gen = !gen;
                game.gameEnded = true;
                if(!gen){
                    lastGameGen = false;
                    heldSolution = game.getBoard(0).getPlayer();
                }
                game.openGameData();
                gameUpdateRate = 0F;
            }
            
            // Swap between player and AI training games
            if(keycode == Keys.ENTER){
                gen = !gen;
                game.gameEnded = true;
                if(!gen){
                    lastGameGen = false;
                    heldSolution = game.getBoard(0).getPlayer();
                }
            }
            
            // Player controls
            if(!gen && !paused){
                Board board = game.getPlayerBoard(0);
                if(board != null){
                    if(board.isAlive()){
                        board.checkPieceExists();
                        stillPressed = true;
                        lastPress = System.currentTimeMillis();

                        if(keycode == Keys.UP)
                            board.hardDrop();
                        else if (keycode == Keys.DOWN)
                            board.softDrop();
                        else if (keycode == Keys.LEFT)
                            board.slide(false);
                        else if (keycode == Keys.RIGHT)
                            board.slide(true);
                        else if (keycode == Keys.X)
                            board.rotate(true);
                        else if (keycode == Keys.Z)
                            board.rotate(false);
                        else if (keycode == Keys.C)
                            board.hold();
                    }
                }
            }
            
            return true;
        }
        
        @Override
        public boolean keyUp(int keycode) {
            return true;
        }

        @Override
        public boolean keyTyped(char character) {
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return true;
        }

        @Override
        public boolean scrolled(int amount) {
            return true;
        }
}
