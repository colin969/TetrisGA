/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsp.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author Colin Berry
 */
public class Board {
    
    private static int boardWidth = 200;
    private static int boardHeight = 440;
    private static int blockSize = 20;
    private static int boardHeightCells = 40;

    private int xAnchor;
    private int yAnchor;
    private Random pieceGen;

    private Color[][] board;
    private Color background = Color.LIGHT_GRAY;
    private Color permaGarbage = Color.DARK_GRAY;
    private int permaGarbageRows;
    private boolean isAlive;
    private int queuedGarbage;
    private int garbageHole;
    private Random garbageRand;

    private Tetromino activePiece;
    private Queue<Tetromino> nextPieces;
    private Tetromino holdPiece;
    private Point pieceOrigin;
    private int pieceRot;
    private int comboLevel;
    private int garbageLevel;
    private int results;
    private int linesClear;
    private int step;

    private int score;

    private Player player;

    public Board(Player player, int seed, int xAnchor, int yAnchor) {
        step = 1;
        linesClear = 0;
        permaGarbageRows = 0;

        board =  new Color[10][boardHeightCells];
        results = 1;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < boardHeightCells; y++) {
                board[x][y] = background;
            }
        }

        this.garbageRand = new Random();
        this.garbageHole = garbageRand.nextInt(10);
        this.queuedGarbage = 0;
        this.garbageLevel = 0;
        this.comboLevel = 0;
        this.score = 0;
        this.isAlive = true;
        this.xAnchor = xAnchor;
        this.yAnchor = yAnchor;
        this.player = player;

        pieceGen = new Random(seed);
        nextPieces = new LinkedList();
        List<Tetromino> toAdd = Arrays.asList(Game.tetrominoes);
        Collections.shuffle(toAdd, pieceGen);
        nextPieces.addAll(toAdd);
        holdPiece = nextPieces.remove();
    }

    public void doStep() {
        step++;
        if(activePiece == null){
            addPiece();
        } else {
            this.softDrop();
        }

        if(player.getCpu() && isAlive){
            if(holdPiece == null){
                this.hold();
                return;
            }
            ArrayList<Action> actions = cpuGetAllActions();
            if(!actions.isEmpty()){
                Action action = player.nextMove(actions);
                if(action != null){
                    this.cpuDrop(action.origin, action.rot, action.swap);
                }
            } else {
                this.isAlive = false;
                this.results = 0;
            }
        }
    }

    private void addPiece() {
        activePiece = nextPieces.remove();
        if(nextPieces.size() < 3){
            List<Tetromino> toAdd = Arrays.asList(Game.tetrominoes);
            Collections.shuffle(toAdd, pieceGen);
            nextPieces.addAll(toAdd);
        }

        pieceOrigin = new Point(3,20);
        pieceRot = 0;

        // Failure!
        if(collides(new Point(3,20), pieceRot)){
            this.isAlive = false;
            this.results = 1;
        }

    }

    public void rotate() {

    }
    
    // Returns new rotation and new lowest origin
    public Object[] getRotationTranslation(Tetromino piece, int rot, boolean right, Point origin, Point[][] kickMatrix){
        int rotIndex = (rot * 2) + (right ? 0 : -1);
        int newRot = rot + (right ? 1 : -1);
        
        // Spawn rot left is only one out of bounds
        if(rotIndex == -1)
            rotIndex = 7;

        // Move new rotation in bounds
        if(newRot < 0)
            newRot = 3;
        else if(newRot > 3)
            newRot = 0;        
        
        for(int test = 0; test < 5; test++){
            Point trans = kickMatrix[rotIndex][test];
            Point newOrigin = new Point(origin.x + trans.x, origin.y + trans.y);
            if(!collides(piece.point[newRot], newOrigin)){
                // Find any drop from here
                int y = this.getLowestValid(piece.point[newRot], newOrigin);
                newOrigin.y = y;
                
                return new Object[]{newRot, newOrigin};
            }
        }
        
        return new Object[]{-1};
    }

    public void hardDrop() {

    }

    public void softDrop() {
        if(!collides(new Point(pieceOrigin.x, pieceOrigin.y - 1), pieceRot)){
            pieceOrigin.y = pieceOrigin.y - 1;
        } else {
            lockPiece();
        }
    }

    private void lockPiece() {
        for(Point p : activePiece.point[pieceRot]){
            board[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = activePiece.color;
        }

        int lines = clearLines();
        linesClear += lines;
        if(lines > 0){
            // 100, 300, 500, 800
            score += (comboLevel * 50) + 100 + (200 * (lines-1));
            // 0, 1, 2, 4
            garbageLevel = (lines - 1) + (lines / 4);
            // Garbage cancelling
            if(queuedGarbage > 0){
                queuedGarbage -= garbageLevel;
                if(queuedGarbage < 0)
                    garbageLevel = Math.abs(queuedGarbage);


            }
            comboLevel++;

        } else {
            garbageHole = garbageRand.nextInt(10);
            while(queuedGarbage > 0){
                // Move up a line for each line of garbage
                // 9/10 chance to change garbage hole
                for(int oldRow = 23; oldRow >= permaGarbageRows+1; oldRow--){
                    for(int col = 0; col < 10; col++){
                        board[col][oldRow] = board[col][oldRow-1];
                    }
                }
                for(int col = 0; col < 10; col++){
                    if(col != garbageHole)
                        board[col][permaGarbageRows] = Color.GRAY;
                    else
                        board[col][permaGarbageRows] = background;
                }
                queuedGarbage--;
            }
            comboLevel = 0;
        }

        activePiece = null;
    }

    private int clearLines(){
        boolean clear;
        int clears = 0;
        for(int row = pieceOrigin.y; row < pieceOrigin.y + 4; row++){
            if(row >= 0){
                clear = true;
                for(int col = 0; col < 10; col++){
                    if(board[col][row] == background || board[col][row] == permaGarbage){
                        clear = false;
                        break;
                    }
                }
                if(clear){
                    clears++;
                    int nextRow = row;
                    for(int oldRow = nextRow + 1; oldRow < boardHeightCells; oldRow++){
                        for(int col = 0; col < 10; col++){
                            board[col][nextRow] = board[col][oldRow];
                        }
                        nextRow++;
                    }
                    for(int col = 0; col < 10; col++){
                        board[col][23] = background;
                    }
                    row--;
                }
            }
        }
        return clears;
    }

    private int boardCheckClears(Point[] piece, Point origin) {
        int clears = 0;

        // Only check possible changed rows
        for(int plusY = 0; plusY < 4; plusY++){
            int y = plusY + origin.y;
            boolean clear = true;
            if(y >= 0){
                for(int x = 0; x < 10; x++){
                    // If space not filled, check if piece fits space (if not, no clear!)
                    if(board[x][y] == background){
                        for(Point p : piece){
                            if(p.x + origin.x == x && p.y + origin.y == y)
                                break;
                            clear = false;
                            break;
                        }
                        if(!clear)
                            break;
                    }
                }
                if(clear)
                    clears++;
            }
        }

        return clears;
    }

    private int boardCheckHoles(Point[] piece, Point origin){
        Point[] topPieces = new Point[4];
        int topPieceCount = 0;

        // Only check for holes under lowest piece for each column
        for(Point p : piece){
            boolean top = true;
            for(int compare = 0; compare < topPieceCount; compare++){
                if(topPieces[compare].x == p.x){
                    if(p.y < topPieces[compare].y){
                        topPieces[compare] = p;
                    }
                    top = false;
                    break;
                }
            }
            if(top){
                topPieces[topPieceCount] = p;
                topPieceCount++;
            }
        }

        int holes = 0;

        for(int num = 0; num < topPieceCount; num++){
            Point p = topPieces[num];
            int y = origin.y + p.y - 1;
            if(y >= 0){
                if(board[origin.x + p.x][y] == background){
                    holes++;
                }
            }
        }

        return holes;
    }

    private boolean collides(Point origin, int rot){
        return collides(activePiece.point[rot], origin);
    }
    
    private boolean collides(Point[] piece, Point origin){
        for(Point p : piece){
            // Edge check
            int x = origin.x + p.x;
            int y = origin.y + p.y;
            if(x < 0 || x > 9 || y < 0)
                return true;
            // Piece check
            if(board[x][y] != background){
                return true;
            }
        }
        return false;
    }

    // Get lowest Y origin value for column
    private int getLowestValid(Point[] piece, Point origin){
        int lowest = origin.y;
        for(int y = lowest; y >= -2; y--){
            if(!collides(piece, new Point(origin.x, y))){
                lowest = y;
            } else {
                return lowest;
            }
        }
        return lowest;
    }

    // Return an array of Column by Rotation values for the CPU player to choose from
    private ArrayList<Action> cpuGetAllActions() {
        ArrayList<Action> validActions = new ArrayList();
        validActions.addAll(getActionsForPiece(activePiece, false));
        validActions.addAll(getActionsForPiece(holdPiece, true));
        return validActions;
    }

    private void scoreAction(Action action, Point[] piece, Point origin, int[] colHeights){
        action.origin = origin;

        // Get number of line clears
        action.clears = boardCheckClears(piece, origin);

        // Get number of holes it would produce
        action.holes = boardCheckHoles(piece, origin);

        int[] colChange = new int[10];
        for(int i = 0; i < 10; i++)
            colChange[i] = 0;

        // Calculate any changes to the current heights of the board
        action.height = 0;
        for(Point p : piece){
            int tempY = p.y + origin.y;
            if(tempY > action.height)
                action.height = tempY;
            if(colChange[action.origin.x + p.x] < tempY+1)
                colChange[action.origin.x + p.x] = tempY+1;
        }

        int aggregateHeight = 0;
        int bumpiness = 0;
        for(int colHeight = 0; colHeight < 10; colHeight++){
            int firstHeight = colChange[colHeight] == 0 ? colHeights[colHeight] : colChange[colHeight];
            if(colHeight < 9){
                int secondHeight = colChange[colHeight+1] == 0 ? colHeights[colHeight+1] : colChange[colHeight+1];
                bumpiness += Math.abs(firstHeight - secondHeight);
            }
            aggregateHeight += firstHeight;
        }
        action.aggregateHeight = aggregateHeight;
        action.bumpiness = bumpiness;
    }
    
    private ArrayList<Action> getActionsForPiece(Tetromino piece, boolean swap){
        // Get height of all columns on the board
        int[] colHeights = new int[10];
        int curHeight;
        for(int col = 0; col < 10; col++){
            curHeight = 0;
            for(int y = 0; y < 22; y++){
                if(board[col][y] != background){
                    curHeight = y+1;
                }
            }
            colHeights[col] = curHeight;
        }

        ArrayList<Action> validActions = new ArrayList();

        for(int col = -2; col < 9; col++){
            for(int rot = 0; rot < 4; rot++){
                Action action = new Action();
                action.rot = rot;
                // Make sure action is in bounds before finding lowest point to use
                if(!collides(piece.point[rot], new Point(col, 20))){
                    int y = getLowestValid(piece.point[rot], new Point(col, 20));
                    action.swap = swap;
                    scoreAction(action, piece.point[rot], new Point(col, y), colHeights);
                    validActions.add(action);
                    
                    Point[][] rotMatrix;
                    // Get the right rotation matrix for the piece
                    if(activePiece.letter.equals("I"))
                        rotMatrix = Game.rotMatrixI;
                    else
                        rotMatrix = Game.rotMatrixRest;
                    
                    // Check left and right rotations from this position
                    // LEFT
                    Object[] vals = getRotationTranslation(piece, rot, false, new Point(col, y), Game.rotMatrixRest);
                    if((Integer) vals[0] >= 0){
                        Action rotAction = new Action();
                        int newRot = (Integer) vals[0];
                        Point newOrigin = (Point) vals[1];

                        rotAction.rot = newRot;
                        rotAction.swap = swap;
                        scoreAction(rotAction, piece.point[newRot], newOrigin, colHeights);
                        validActions.add(rotAction);
                    }
                    // RIGHT
                    vals = getRotationTranslation(piece, rot, true, new Point(col, y), Game.rotMatrixRest);
                    if((Integer) vals[0] >= 0){
                        Action rotAction = new Action();
                        int newRot = (Integer) vals[0];
                        Point newOrigin = (Point) vals[1];

                        rotAction.rot = newRot;
                        rotAction.swap = swap;
                        scoreAction(rotAction, piece.point[newRot], newOrigin, colHeights);
                        validActions.add(rotAction);
                    }
                }
            }
        }
        return validActions;
    }

    // Drop by a CPU, guaranteed valid move.
    private void cpuDrop(Point origin, int rot, boolean swap) {

        if(swap){
            Tetromino swapPiece = activePiece;
            activePiece = holdPiece;
            holdPiece = swapPiece;
        }

        pieceOrigin = origin;
        pieceRot = rot;
        lockPiece();
    }

    // Draw board
    public void draw(ShapeRenderer renderer){

        // Draw Current Pieces
        renderer.set(ShapeType.Filled);
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[x].length; y++){
                renderer.setColor(board[x][y]);
                renderer.rect(xAnchor + (x * 20),
                        yAnchor + (y * 20),
                        blockSize,
                        blockSize);
            }
        }


        // Draw Active Piece
        if(activePiece != null){
            renderer.setColor(activePiece.color);
            for(Point p : activePiece.point[pieceRot]){
                renderer.rect(xAnchor + ((pieceOrigin.x + p.x) * 20),
                        yAnchor + ((pieceOrigin.y + p.y) * 20),
                        blockSize,
                        blockSize);
            }
        }

        // Draw Upcoming Pieces
        Point offset = new Point(xAnchor + boardWidth + 20, yAnchor + boardHeight - 130);
        Iterator<Tetromino> iter = nextPieces.iterator();
        for(int next = 0; next < 3; next++){
            Tetromino tetromino = iter.next();
            renderer.setColor(tetromino.color);
            for(Point p : tetromino.point[0]){
                renderer.rect(offset.x + (p.x * 20),
                        offset.y + (p.y * 20),
                        blockSize,
                        blockSize);
            }
            offset.y -= 20 * 5;
        }

        // Draw Board Edges
        renderer.set(ShapeType.Line);
        renderer.setColor(Color.BLACK);
        renderer.rect(xAnchor, yAnchor, boardWidth, boardHeight);
    }

    public boolean isAlive(){
        return this.isAlive;
    }

    public int getScore() {
        return score;
    }

    public void queueGarbage(int garbageLevel) {
        this.queuedGarbage += garbageLevel;
    }

    public int getResults() {
        results = this.score;
        
        // Differentiate between very fast losers (positive height weight) and other individuals by penalising weight very slightly.
        int height = 0;
        int curHeight;
        for(int col = 0; col < 10; col++){
            curHeight = 0;
            for(int y = 0; y < boardHeightCells; y++){
                if(board[col][y] != background){
                    curHeight = y+1;
                }
            }
            height += curHeight;
        }
        results -= height;
        
        if(isAlive)
            results += 1000000/step;
        else
            results -= 1000000/step;
        return results;
    }

    private void hold() {
        if(holdPiece == null){
            holdPiece = activePiece;
            addPiece();
        } else {
            Tetromino swapPiece = activePiece;
            activePiece = holdPiece;
            holdPiece = swapPiece;
        }
    }
    
    public void addPerma(){
        // Move up a line for each line of garbage

        for(int oldRow = 23; oldRow >= 1; oldRow--){
            for(int col = 0; col < 10; col++){
                board[col][oldRow] = board[col][oldRow-1];
            }
        }
        for(int col = 0; col < 10; col++){
            board[col][0] = permaGarbage;
        }
        
        permaGarbageRows++;
    }

    public Player getPlayer() {
        return player;
    }
    
    public int getGarbageLevel() {
        return garbageLevel;
    }

    public void setGarbageLevel(int garbageLevel) {
        this.garbageLevel = garbageLevel;
    }
    
    public int getLinesCleared(){
        return linesClear;
    }

}

