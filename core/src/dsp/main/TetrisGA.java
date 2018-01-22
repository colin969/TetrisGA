package dsp.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dsp.ga.GA;
import dsp.tetris.Game;
import dsp.tetris.Player;

public class TetrisGA extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
        BitmapFont font;
	Texture img;
        private static Game game;
        private static ShapeRenderer shapeRenderer;
        private static float gameUpdateRate = 0.2F;
        private static float timePassed;
        private static float[] test;
        private int lastGen;
        private boolean fullRender;
        private float savedUpdateRate;
        private boolean paused;
        
        private GA ga;
	
	@Override
	public void create () {
                ga = new GA();
                ga.init();
            
                game = new Game();
                game.init();
                timePassed = 0;
                //test = ga.getRandom();
                test = new float[]{-0.8744863F, 0.025707256F, 0.2459504F, -0.14321329F, -0.67774916F, -0.33289695F, -0.77588874F, -0.49761575F};
                lastGen = 1;
                
                game.resetGame(ga.startGame(), test);
                
                shapeRenderer = new ShapeRenderer();
                shapeRenderer.setAutoShapeType(true);
                
                batch = new SpriteBatch();
                
                font = new BitmapFont();
                font.setColor(Color.BLACK);
                
                fullRender = true;
                paused = false;
                
                Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
                timePassed += Gdx.graphics.getDeltaTime();
                
                if(!paused){
                    // Game ended, process results, start next game
                    if(game.gameEnded){
                        ga.returnResults(game.results);
                        if(ga.getGen() != lastGen){
                            test = ga.getBest();
                            lastGen = ga.getGen();
                            ga.printGen();
                            game.updateSeed();
                        }
                        game.resetGame(ga.startGame(), test);
                    }
                    
                    // Do game step
                    while(timePassed > gameUpdateRate){
                        timePassed = 0;
                        game.doStep();
                    }
                }
                
                
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                // Print running Generation and progress through population
                batch.begin();
                font.draw(batch, String.format("Generation\n%s", ga.getGen()), 220, 100);
                font.draw(batch, String.format("Individual\n%s of %s", ga.getGenNum(), ga.getGenNumMax()), 220, 60);
                if(paused)
                    font.draw(batch, "P A U S E D", 220, 200);
                batch.end();

                // Only render boards
                game.drawGame(shapeRenderer, font, batch, fullRender);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
                font.dispose();
	}
        
        @Override
        public boolean keyDown(int keycode) {
            // Adjust step change if shift is held
            float step = 0.05F;
            if(Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT)){
                step = 0.5F;
            }
            
            // Disable board rendering, puts to full speed - TODO : Tweaks to show rest of UI
            if(keycode == Keys.R){
                fullRender = !fullRender;
                if(fullRender){
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
