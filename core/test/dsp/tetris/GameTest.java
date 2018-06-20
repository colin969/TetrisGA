package dsp.tetris;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Colin Berry
 */
public class GameTest {
    
    public GameTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void saveGameData(){
        Game game = new Game();
        game.init();
        game.resetGame(new Player(false, false, 0), new Player(false, false, 1));
        
        game.getPlayerBoard(0).setStep(500);
        game.getPlayerBoard(1).setStep(250);
        
        Calendar cal = Calendar.getInstance();
        File file = new File(String.format("./game_%s-%s-%s.csv", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));
        
        game.openGameData();
        game.saveGameData();
        game.closeGameData();
        
        assertTrue(file.exists());
        
        BufferedReader reader = null;
        
        try{
            reader = new BufferedReader(new FileReader(file));
            assertTrue(reader.readLine().equals("Run,Steps"));
            assertTrue(reader.readLine().equals("1,500"));
            reader.close();
        } catch (IOException e){
        } finally {
            file.delete();
        } 
    }
    
}
