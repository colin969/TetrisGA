/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.ga;

import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
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
public class GATest {
    
    public static GA ga;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        ga = new GA();
        ga.init();
    }
    
    @After
    public void tearDown() {
        ga.closeCSV();
    }

    // startGame should return a set of weights, each randomly between -1 and 1
    @Test
    public void startGame(){
        float[] weights = ga.startGame();
        assertTrue(weights.length == GA.NUM_WEIGHTS);
        
        // Check if bound between -1 and 1
        for (int i = 0; i < GA.NUM_WEIGHTS; i++) {
            assertTrue(weights[i] >= -1 && weights[i] <= 1);
        }
        
        // 1 in 33554432 chance of being the same
        assertTrue(weights[0] != weights[1]);
        // 1 in (1.13 * 10^15) chance of being the same twice
        assertTrue(weights[2] != weights[3]);
    }
    
    // Generation should change after 10 returns, to ensure evaluation and swapping is taking place
    @Test
    public void returnResults(){
        assertTrue(ga.getGen() == 1);
        for (int i = 0; i < GA.POP_SIZE; i++) {
            ga.startGame();
            ga.returnResults(0);
        }
        assertTrue(ga.getGen() == 2);
    }
    
    // Ensures a CSV is present at the end
    @Test
    public void printToCSV(){
        ga.printToCSV();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
            
        File file = new File(String.format("./training_%s-%s.csv", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));
        ga.closeCSV();
        assertTrue(file.exists());
        if(file.exists()){
            // Clean up test file
            file.delete();
        }
    }
    
}
