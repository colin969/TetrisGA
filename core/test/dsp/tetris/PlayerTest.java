package dsp.tetris;

import dsp.ga.GA;
import java.util.ArrayList;
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
public class PlayerTest {
    
    public PlayerTest() {
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
    public void loadCPU(){
        String testWeights = "25.01741	-44.13514	49.50907	-26.380072	-25.904919	1.0351926	-39.438038	35.81071	43.613655	-25.361193	4.715416	-3.838867	-20.558733	-10.126003	-10.89845	-39.833656";
        String[] realTestWeights = testWeights.split("\t");
        Player player = new Player(true, realTestWeights, 0);
        
        // Correct length, correct values
        assertTrue(player.getGenome().length == realTestWeights.length);
        assertTrue(player.getGenome()[0] == 25.01741F);
    }
    
    @Test
    public void nextMove() {
        // Weight all inputs equally for now
        float[] weights = new float[GA.NUM_WEIGHTS];
        for (int i = 0; i < GA.NUM_WEIGHTS; i++) {
            weights[i] = 1;
        }
        
        Player player = new Player(true, weights, false, 1);
        // Better action
        Action goodAction = new Action();
        // Seperate weight per clears, only use index 3 upwards 
        goodAction.clears = 4;
        goodAction.height = 1;
        goodAction.aggregateHeight = 1;
        goodAction.bumpiness = 1;
        
        // Action is worse, shouldn't be chosen
        Action badAction = new Action();
        badAction.clears = 3;
        badAction.height = 1;
        badAction.aggregateHeight = 1;
        badAction.bumpiness = 1;
        
        ArrayList<Action> actionList = new ArrayList();
        actionList.add(goodAction);
        actionList.add(badAction);
        
        Action result = player.nextMove(actionList);
        
        assertTrue(result.equals(goodAction));
        
    }
    
}
