package dsp.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TetrisGA extends ApplicationAdapter {
	SpriteBatch batch;
        BitmapFont font;
	Texture img;
        private static Game game;
        private static ShapeRenderer shapeRenderer;
        private static float gameUpdateRate = 0.05F;
        private static float timePassed;
	
	@Override
	public void create () {
                game = new Game();
                game.init();
                timePassed = 0;
                
                shapeRenderer = new ShapeRenderer();
                shapeRenderer.setAutoShapeType(true);
                
                batch = new SpriteBatch();
                
                font = new BitmapFont();
                font.setColor(Color.BLACK);
	}

	@Override
	public void render () {
                timePassed += Gdx.graphics.getDeltaTime();
                while(timePassed > gameUpdateRate){
                    timePassed = 0;
                    game.doStep();
                }
                
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                game.drawGame(shapeRenderer, font, batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
                font.dispose();
	}
}
