/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A game object displayed using an image.
 * 
 * Note that the image is read from the file when the object is constructed, and that all objects
 * created by this constructor share the same image data (i.e. img is static). This is important for
 * efficiency: your program will go very slowly if you try to create a new BufferedImage every time
 * the draw method is invoked.
 */
public class Pacman extends GameObj {
    public static final String IMG_FILE = "files/packman.png";
    public static final String IMG_FILE2 = "files/packr.png";
    public static final String IMG_FILE3 = "files/packu.png";
    public static final String IMG_FILE4 = "files/packd.png";
    public static final int SIZE = 15;
    public static final int INIT_POS_X = 180;
    public static final int INIT_POS_Y = 260;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private static BufferedImage img;

    public Pacman() {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE);

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    /**
     * check if all four vertices of the current picture is in blank
     * @param x- the x position
     * @param y- the y position
     * @return if all 4 corners of the picture is in blank space(where
     * they are supposed to be)
     */
    public boolean inBlank(int x, int y){
    	int x1 = x/20;
    	int y1 = (y - 40)/20;
    	int x2 = (x + SIZE)/20;
    	int y2 = (y1 + SIZE - 40)/20;
    	
    	return movable(x1, y1, x2, y2);
    }
    
    /**
     * determine if the direction change if valid(like if turning won't make pac run into a wall)
     * @param x- the new Vx of changing direction
     * @param y- the new Vy of changing direction
     * @return determine whether to change direction
     */
    public boolean change(int x, int y){
    	int thisNextX = super.getPx() + 15 * x;//I made it 15 just to over see it
    	int thisNextY = super.getPy() + 15 * y;
    	
    	int x1 = thisNextX/20;
        int y1 = (thisNextY - 40)/20;  
        int x2 = (thisNextX + SIZE - 1)/20;
        int y2 = (thisNextY - 40 + SIZE - 1)/20;  
    	
		return movable(x1, y1, x2, y2);
    	
    }
    
    /**
     * Moves the object by its velocity.  Ensures that the object does not go outside its bounds by
     * clipping.
     * 
     */
    public void move() {
    	int thisNextX = super.getPx() + super.getVx();
        int thisNextY = super.getPy() + super.getVy();
        //x and y be squares containing those four indices.
        int x = thisNextX/20;
        int y = (thisNextY - 40)/20;  
        int x1 = (thisNextX + SIZE)/20;
        int y1 = (thisNextY - 40 + SIZE)/20;  
        
        //if after movement all four vertices inside blank, then allow movement!
        //checkbound then check movements
		if(movable(x, y, x1, y1)){
	  	  	super.setPy(Math.min(Math.max(thisNextY, 40), GameCourt.COURT_HEIGHT - SIZE));
		  	super.setPx(Math.min(Math.max(thisNextX, 0), GameCourt.COURT_WIDTH - SIZE));
		}
    }
    
    /**
     * Determine whether this game object is currently intersecting another object.
     * 
     * Intersection is determined by comparing bounding boxes. If the bounding boxes overlap, then
     * an intersection is considered to occur.
     * 
     * @param that The other object
     * @return Whether this object intersects the other object.
     */
    public boolean intersects(Ghost that) {
        return (super.getPx() + super.getSize() >= that.getPx()
            && super.getPy() + super.getSize() >= that.getPy()
            && that.getPx() + that.getSize() >= super.getPx()
            && that.getPy() + that.getSize() >= super.getPy());
    }
    
    
    @Override
    public void draw(Graphics g) {
    	try {
	    	if(super.getVx() > 0){
	    		img = ImageIO.read(new File(IMG_FILE2));
	    	}else if (super.getVx() < 0){
	    		img = ImageIO.read(new File(IMG_FILE));
	    	}else if(super.getVy() > 0){
	    		img = ImageIO.read(new File(IMG_FILE4));
	    	}else if (super.getVy() < 0){
	    		img = ImageIO.read(new File(IMG_FILE3));
	    	}
    	} catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        g.drawImage(img, this.getPx(), this.getPy(), this.getSize(), this.getSize(), null);
    }
}
