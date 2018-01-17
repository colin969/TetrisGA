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

public class TetrisGA extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
        BitmapFont font;
	Texture img;
        private static Game game;
        private static ShapeRenderer shapeRenderer;
        private static float gameUpdateRate = 0.2F;
        private static float timePassed;
        private static float[] test;
        private int gameNumber;
        private int lastGen;
        private boolean partialRender;
        private float savedUpdateRate;
        
        private GA ga;
	
	@Override
	public void create () {
                ga = new GA();
                ga.init();
            
                game = new Game();
                game.init();
                timePassed = 0;
                gameNumber = 1;
                test = ga.getRandom();
                lastGen = 0;
                
                game.resetGame(ga.startGame(), test);
                
                shapeRenderer = new ShapeRenderer();
                shapeRenderer.setAutoShapeType(true);
                
                batch = new SpriteBatch();
                
                font = new BitmapFont();
                font.setColor(Color.BLACK);
                
                partialRender = false;
                
                Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
                timePassed += Gdx.graphics.getDeltaTime();
                
                if(game.gameEnded){
                    ga.returnResults(game.results);
                    game.resetGame(ga.startGame(), test);
                    gameNumber++;
                    if(ga.getGen() % 5 == 0 && ga.getGen() != lastGen){
                        test = ga.getRandom();
                        lastGen = ga.getGen();
                    }
                }
                
                while(timePassed > gameUpdateRate){
                    timePassed = 0;
                    game.doStep();
                }
                
                
                    Gdx.gl.glClearColor(1, 1, 1, 1);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                    
                    batch.begin();
                    font.draw(batch, String.format("Generation\n%s", ga.getGen()), 220, 100);
                    font.draw(batch, String.format("Individual\n%s of %s", ga.getGenNum(), ga.getGenNumMax()), 220, 60);
                    batch.end();
                    
                    if(!partialRender)
                        game.drawGame(shapeRenderer, font, batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
                font.dispose();
	}
        
        @Override
        public boolean keyDown(int keycode) {
            float step = 0.05F;
            if(Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT)){
                step = 0.25F;
            }
            
            if(keycode == Keys.P){
                partialRender = !partialRender;
                if(partialRender){
                    savedUpdateRate = gameUpdateRate;
                    gameUpdateRate = 0;
                } else {
                    gameUpdateRate = savedUpdateRate;
                }
            }
            
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
