package dsp.tetris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dsp.main.TetrisGA;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.width = 600;
                config.height = 500;
                config.vSyncEnabled = false;
                config.foregroundFPS = 0;
                config.backgroundFPS = 0;
                new LwjglApplication(new TetrisGA(), config);
	}
}
