/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.Graphics;

/** 
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public abstract class GameObj {
    /*
     * Current position of the object (in terms of graphics coordinates)
     *  
     * Coordinates are given by the upper-left hand corner of the object. This position should
     * always be within bounds.
     *  0 <= px <= maxX 
     *  0 <= py <= maxY 
     */
    private int px; 
    private int py;

    /* Size of object, in pixels. */
    private int size;

    /* Velocity: number of pixels to move every time move() is called. */
    private int vx;
    private int vy;

    /**
     * Constructor
     */
    public GameObj(int vx, int vy, int px, int py, int size) {
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.size  = size;
    }

    /*** GETTERS **********************************************************************************/
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }
    
    public int getVx() {
        return this.vx;
    }
    
    public int getVy() {
        return this.vy;
    }
    
    public int getSize() {
        return this.size;
    }

    /*** SETTERS **********************************************************************************/
    public void setPx(int px) {
        this.px = px;
    }

    public void setPy(int py) {
        this.py = py;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    /*** UPDATES AND OTHER METHODS ****************************************************************/
    
    /**
     * return if the object is about to intersect a brick/border in the next step
     * * Intersection is determined by comparing bounding boxes. If the  bounding boxes (for the next
     * time step) overlap, then an intersection is considered to occur.
     * 
     * if intersecting the border, return -1, -1
     * @param c
     * @return 
     */
    public boolean ifCollision(){
    	int thisNextX = this.px + this.vx;
        int thisNextY = this.py + this.vy;
    
        return (thisNextX >= 0
            && thisNextX + this.size <= GameCourt.COURT_WIDTH
            && thisNextY >= 40
            && thisNextY + this.size <= GameCourt.COURT_HEIGHT - 40);
    }
    
    /**
     * return if the four vertices of a object are all in a blank square
     * 
     * @param x - left x coord
     * @param y- up y coord 
     * @param x1- right x coord
     * @param y2- down y coord
     * @return 
     */
    public boolean movable(int x, int y, int x1, int y1){
    	if(x < 0 || x1 > 18 || y < 0 || y1 > 19){
    		return false;
    	}
        return GameCourt.maze[x][y] == 0 && GameCourt.maze[x1][y] == 0
    			&&GameCourt.maze[x][y1] == 0 && GameCourt.maze[x1][y1] == 0;
    }

    /**
     * the move method for the game object that are responsible for change their location and velocity in varies situations.
     * Subclass should override this method based on how their object should appear.
     */
    public abstract void move();
    
    /**
     * Default draw method that provides how the object should be drawn in the GUI. This method does
     * not draw anything. Subclass should override this method based on how their object should
     * appear.
     * 
     * @param g The <code>Graphics</code> context used for drawing the object. Remember graphics
     * contexts that we used in OCaml, it gives the context in which the object should be drawn (a
     * canvas, a frame, etc.)
     */
    public abstract void draw(Graphics g);
}