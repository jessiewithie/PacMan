/* CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The cherries will start randomly appear on the court and disappear randomly if the user does not pick it up
 */
public class obj{
    
    public static final int SIZE = 19;

    private BufferedImage img;
    //location - right about the coordinate
    private int x = 1;
    private int y = 1;
    private int type = (int) Math.random() * 2; //type of obj: 0 for cherry, 1 for key
    
    private boolean notTaken;

    public obj() {
    	double tp = Math.random() * 2;
    	type = (int) tp;
        try {
        	if(type == 0){
        		img = ImageIO.read(new File("files/cherry.png"));
        	}else if (type == 1){
        		img = ImageIO.read(new File("files/key.png"));
        	}
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        while(GameCourt.maze[x][y] == 1){
        	x = (int) (Math.random() * 19);
        	y = (int) (Math.random() * 20);
        }
        notTaken = true;
        
    }
    
    public int getType(){
    	return type;
    }
    
    /**
     * if intersect with Pacman, then taken
     */
    public boolean intersect(Pacman p) {
    	return (20 * x + SIZE >= p.getPx()
                && 20 * (y + 2) + SIZE >= p.getPy()
                && p.getPx() + p.getSize() >= 20 * x 
                && p.getPy() + p.getSize() >= 20 * (y + 2)
                && notTaken());
    }
    
    /**
     * help to count  obj
     * @return if the obj haven't been taken before, then return 1, else, return 0
     */
    public int objCount(){
    	if(notTaken){
    		notTaken = false;
    		return 1;
    	}
    	return 0;
    }
    
    /**
     * keep track of the obj's taken state
     * @return if the obj was taken
     */
    public boolean notTaken(){
    	return notTaken;
    }

    /**
     * draw the obj if they are still wait to be taken
     * @param g
     */
    public void draw(Graphics g) {
    	if(notTaken){
    	g.drawImage(img, 20 * x, 20 * (y + 2), SIZE, SIZE, null);
    	}
    }
}