package dsp.tetris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dsp.tetris.TetrisGA;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.width = 600;
                config.height = 500;
		new LwjglApplication(new TetrisGA(), config);
	}
}
