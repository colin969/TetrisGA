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
import java.util.LinkedList;
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
        private static int miniBlockSize = 9;
        
        private int xAnchor;
        private int yAnchor;
        private Random pieceGen;
    
	private Color[][] board;
        private Color background = Color.LIGHT_GRAY;
        private Color[] clearRow;
        private boolean isAlive;
        private int queuedGarbage;
        private int garbageHole;
        private Random garbageRand;

	public Tetromino activePiece;
        public Queue<Tetromino> nextPieces;
        public Point pieceOrigin;
        public int pieceRot;
        public int comboLevel;
        public int garbageLevel;

	private int pieceNumber;

	private int score;

	private int gravityLvl;

	private Player player;

	public Board(Player player, int seed, int xAnchor, int yAnchor) {
            board =  new Color[10][24];
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 24; y++) {
                    board[x][y] = background;
                }
            }
            
            this.garbageRand = new Random();
            this.garbageHole = garbageRand.nextInt(10);
            this.queuedGarbage = 0;
            this.garbageLevel = 0;
            this.comboLevel = 0;
            this.pieceNumber = 0;
            this.score = 0;
            this.isAlive = true;
            this.xAnchor = xAnchor;
            this.yAnchor = yAnchor;
            this.player = player;
            pieceGen = new Random(seed);
            nextPieces = new LinkedList();
            nextPieces.add(Game.tetrominoes[(int) Math.floor(pieceGen.nextDouble() * 7)]);
            nextPieces.add(Game.tetrominoes[(int) Math.floor(pieceGen.nextDouble() * 7)]);
            nextPieces.add(Game.tetrominoes[(int) Math.floor(pieceGen.nextDouble() * 7)]);
            
	}

	public void doStep() {
            if(activePiece == null){
                addPiece();
            } else {
                this.softDrop();
            }
            
            if(player.getCpu() && isAlive){
                Action action = player.nextMove(this.cpuGetAllActions());
                if(action.valid){
                    this.cpuDrop(action.origin, action.rot);
                }
            }
	}

	private void addPiece() {
            activePiece = nextPieces.remove();
            nextPieces.add(Game.tetrominoes[(int) Math.floor(pieceGen.nextDouble() * 7)]);
            
            pieceOrigin = new Point(3,20);
            pieceRot = 0;
            
            // Failure!
            if(collides(3,20,pieceRot)){
                this.isAlive = false;
            }
            
            pieceNumber++;
            
	}

	public void rotate() {

	}

	public void hardDrop() {
            
	}

	public void softDrop() {
            if(!collides(pieceOrigin.x, pieceOrigin.y - 1, pieceRot)){
                pieceOrigin.y = pieceOrigin.y - 1;
            } else {
                lockPiece();
            }
	}

	public void lockPiece() {
            for(Point p : activePiece.point[pieceRot]){
                board[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = activePiece.color;
            }
            
            int lines = clearLines();
            if(lines > 0){
                // 100, 300, 500, 800
                score += (comboLevel * 50) + 100 + (200 * (lines-1));
                // 0, 1, 2, 4
                garbageLevel = (lines - 1) + (lines / 4);
                // Garbage cancelling
                if(queuedGarbage > 0){
                    queuedGarbage -= garbageLevel;
                    garbageLevel = Math.abs(queuedGarbage);
                }
                comboLevel++;
                
            } else {
                while(queuedGarbage > 0){
                    // Move up a line for each line of garbage
                    // 9/10 chance to change garbage hole
                    garbageHole = garbageRand.nextInt(10);
                    for(int oldRow = 23; oldRow >= 1; oldRow--){
                        for(int col = 0; col < 10; col++){
                            board[col][oldRow] = board[col][oldRow-1];
                        }
                    }
                    for(int col = 0; col < 10; col++){
                        if(col != garbageHole)
                            board[col][0] = Color.GRAY;
                        else
                            board[col][0] = background;
                    }
                    queuedGarbage--;
                }
                comboLevel = 0;
            }
            
            activePiece = null;
	}
        
        public int clearLines(){
            boolean clear;
            int clears = 0;
            for(int row = pieceOrigin.y; row < pieceOrigin.y + 4; row++){
                if(row >= 0){
                    clear = true;
                    for(int col = 0; col < 10; col++){
                        if(board[col][row] == background){
                            clear = false;
                            break;
                        }
                    }
                    if(clear){
                        clears++;
                        int nextRow = row;
                        for(int oldRow = nextRow + 1; oldRow < 24; oldRow++){
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
        
        public int boardCheckClears(int col, int placedY, Point[] piece) {
            int clears = 0;
            
            // Only check possible changed rows
            for(int plusY = 0; plusY < 4; plusY++){
                int y = plusY + placedY;
                boolean clear = true;
                if(y >= 0){
                    for(int x = 0; x < 10; x++){

                        if(board[x][y] == background){
                            for(Point p : piece){
                                if(p.x + col == x && p.y + placedY == y)
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
        
        public int boardCheckHoles(Point placement, Point[] piece){
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
                int y = placement.y + p.y - 1;
                if(y >= 0){
                    if(board[placement.x + p.x][y] == background){
                        holes++;
                    }
                }
            }
            
            return holes;
        }
        
        public boolean collides(int newX, int newY, int rot){
            Point[] points = activePiece.point[rot];
            for(Point p : points){
                // Edge check
                int x = newX + p.x;
                int y = newY + p.y;
                if(x < 0 || x > 9 || y < 0)
                    return true;
                // Piece check
                if(board[x][y] != background){
                    return true;
                }
            }
            return false;
        }
        
        // Check if a move is in bounds of the board
        public boolean getValid(int col, int row, Point[] points){
            Point origin = new Point(col, row);
            int x;
            for(Point p : points){
                x = origin.x + p.x;
                if(x < 0 || x >= 10){
                    return false;
                }
                
            }
            return true;
        }
        
        // Get lowest Y origin value for column
        public int getLowestValid(int col, int row, Point[] points){
            Point origin = new Point(col, row);
            int x;
            int lowest = row;
            for(int y = row; y >= -2; y--){
                for(Point p : points){
                    x = origin.x + p.x;
                    if(y + p.y < 0)
                        return lowest;
                    if(board[x][y + p.y] != background){
                        return lowest;
                    }
                }
                lowest = y;
            }
            return lowest;
        }
        
        // Return an array of Column by Rotation values for the CPU player to choose from
	public Action[][] cpuGetAllActions() {
                Action[][] actions = new Action[11][4];
                for(int col = -2; col < 9; col++){
                    for(int rot = 0; rot < 4; rot++){
                        Action action = new Action();
                        // Make sure action is in bounds before finding lowest point to use
                        action.valid = getValid(col, 20, activePiece.point[rot]);
                        if(action.valid){
                            int y = getLowestValid(col, 20, activePiece.point[rot]);
                            action.origin = new Point(col, y);
                            
                            // Get number of line clears
                            action.clears = boardCheckClears(col, y, activePiece.point[rot]);
                            
                            // Get number of holes it would produce
                            action.holes = boardCheckHoles(new Point(col, y), activePiece.point[rot]);
                            
                            // Get stack height of choice
                            action.height = 0;
                            for(Point p : activePiece.point[rot]){
                                int tempY = p.y + y;
                                if(tempY > action.height)
                                    action.height = tempY;
                            }
                        }
                        actions[col+2][rot] = action;
                    }
                }
                return actions;
	}

        // Drop by a CPU, guaranteed valid move.
	public void cpuDrop(Point origin, int rot) {
            
            // TODO: Check for collision on Y > 20
            
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
            for(Tetromino tetromino : nextPieces){
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

}

