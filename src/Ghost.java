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
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * circle of a specified color.
 */
public class Ghost extends GameObj {
    
    public static final int SIZE = 18;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = -1;

    private BufferedImage img;
    //modify the 3 state of the ghost: normal,
    //scared, ate
    private int state = 0;
    //route to get back after being ate
    private int[][] solutionMaze = new int[19][20];

    private int color = 0;
    public Ghost(int Xi, int Yi, int color) {
        super(INIT_VEL_X, INIT_VEL_Y, Xi, Yi, SIZE);
        this.color = color;
        setSolution(GameCourt.maze);
    }
    
    /**
     * change the state while fix their movements
     * @param the state of the ghost: 0: normal, 1: scared, 2: ate
     */
    public void setState(int states){
    	state = states;
    }
    
    /**
     * reset the solution everytime the ghost reached the final location marked by the solution blocks
     * @param s: the blank Construction maze(so can use it to mark solution next time when needed)
     */
    public void setSolution(int[][] s){
    	for(int i = 0; i < s.length; i++){
    		for(int j = 0; j < s[0].length; j++){
    			solutionMaze[i][j] = s[i][j];
    		}
    	}
    }
    
    /**
     * get the state of the ghost
     * @return the state of the ghost: 0: normal, 1: scared, 2: ate
     */
    public int getState(){
    	return state;
    }
    
    /**
     * Moves the object by its velocity.  
     */
    public void move() {
    	
	    if(state == 0 || state == 1){	 
	    	int thisNextX = super.getPx() + super.getVx();
	        int thisNextY = super.getPy() + super.getVy();
			//find the four vertices of the picture
	        int x = thisNextX/20;
	        int y = (thisNextY - 40)/20;  
	        int x1 = (thisNextX + SIZE)/20;
	        int y1 = (thisNextY - 40 + SIZE)/20; 
	       //prepare for the random movement
	        boolean movable = movable(x, y, x1, y1);
			boolean lr = directionalMove(-1, 0) || directionalMove(1, 0);
		    boolean ud = directionalMove(0, -1) || directionalMove(0, 1);
			//wall bounds
			if(movable){
		  	  	super.setPy(Math.min(Math.max(thisNextY, 40), GameCourt.COURT_HEIGHT - SIZE));
			  	super.setPx(Math.min(Math.max(thisNextX, 0), GameCourt.COURT_WIDTH - SIZE));
			}
			
	        //as long as it can move both vertically or horizontally
			//change direction randomly(or not) if at least one of x or y is empty
			if((!movable) || (lr && ud && (super.getPx() % 20 == 0 || super.getPy() % 20 == 0))){
				int rando = (int)(Math.random() * 4);
				if(rando == 1){
					super.setVx(-1);
					super.setVy(0);
				}else if (rando == 2 ){
					super.setVy(1);
					super.setVx(0);
				}else if (rando == 3 ){
					super.setVx(1);
					super.setVy(0);
				}else{
					super.setVy(-1);
					super.setVx(0);
				}
			}
			if(x == 0 && y ==9 && super.getVx() < 0){
				super.setPx(GameCourt.COURT_WIDTH);
			}else if(x == 18 && y ==9 && super.getVx() > 0){
				super.setPx(0);
			}
        }else if(state == 2){
        	//location(indice wise) of the current coordinates
        	int x = super.getPx()/20;
            int y = (super.getPy() - 40)/20;  
        	if(x == 9 && y == 8){//if the ghost got back to the center,
        		state = 0;		//set states back to normal
        		setSolution(GameCourt.maze); //reset the solution to use for next time
        	}else if(super.getPx() % 20 == 0 && super.getPy() % 20 == 0){
        								//only check to change direction when the whole picture fits in a unit
        								//(which means it can turn) and the next block leads to a non-solution block
        		solutionMaze[x][y] = 3;
        		nextV(x, y);
			}	
				super.setPy(Math.min(Math.max(super.getPy() + super.getVy(), 40), GameCourt.COURT_HEIGHT - SIZE));
			  	super.setPx(Math.min(Math.max(super.getPx() + super.getVx(), 0), GameCourt.COURT_WIDTH - SIZE));
				
		}
    }
    
    /**
     * check if possible for the ghost to move in a direction 
     *  %20 == 0 place for both x and y
     *  this is for when the ghost is mood 0/1
     * @param x x direction; 1)left 2)right 0) neither
     * @param y y direction; 1)up -1)down 0)neither
     */
    public boolean directionalMove(int xdi, int ydi){
    	int thisNextX = super.getPx() + 15 * xdi;
        int thisNextY = super.getPy() + 15 * ydi;
        
        int x = thisNextX/20;
        int y = (thisNextY - 40)/20;  
        int x1 = (thisNextX + SIZE)/20;
        int y1 = (thisNextY - 40 + SIZE)/20; 
        
        return (movable(x, y, x1, y1));
    }
    
    /**
	 * take in the current location(matrix indice) and current vertice and 
	 * return the direction change.
	 * Can disregard bound issues since will go in accordance to the solution block.
	 * this will go according to the current solution
	 * @param x - x coordinate of lower right corner
	 * @param y- y coordinate of upper left corner
	 * @param Vx- velocity X
	 * @param Vy- velocity Y
	 */
	public void nextV(int x, int y){
		
		//first check for if out of bound; then if the directional square is a ; then check if won't go against this direction
		if((x - 1 == Math.max(x - 1, 0)) && solutionMaze[Math.max(0, x - 1)][y] == 4 && super.getVx() <= 0){
			super.setVx(-10);
			super.setVy(0);
		}else if((y - 1 == Math.max(0, y - 1)) && solutionMaze[x][Math.max(0, y - 1)] == 4 && super.getVy() <= 0){
			super.setVx(0);
			super.setVy(-10);
		}else if((x + 1 == Math.min(x + 1, 18)) && solutionMaze[Math.min(x + 1, 18)][y] == 4 && super.getVx() >= 0){
			super.setVx(10);
			super.setVy(0);
		}else if((y + 1 == Math.min(19, y + 1)) && solutionMaze[x][Math.max(0, y + 1)] == 4 && super.getVy() >= 0){
			super.setVx(0);
			super.setVy(10);
		}//otherwise, reverse direction
		 else{
			 super.setVx(-1*super.getVx());
			 super.setVy(-1*super.getVy());
		}
	}
	
		/**
		 * return a solution maze; that find the nearest path between ghost and pac
		 * so the ghost will go in accordance to this path.
		 * @param x - x coordinate of lower right corner
		 * @param y- y coordinate of upper left corner
		 * @param Vx- velocity X
		 * @param Vy- velocity Y
		 * @return whether a certain path lead to a solution
		 */
		public boolean solution(int ghostX, int ghostY){
			//false if checking out of bound
			if(ghostX < 0 || ghostY < 0 || ghostX > 18 || ghostY > 19){
				return false;
			}
			
			// Try to solve the maze by continuing current path from position
	        // (row,col).  Return true if a solution is found.  The maze is
	        // considered to be solved if the path reaches the last cell.
			if (solutionMaze[ghostX][ghostY] == 0) {
	        	solutionMaze[ghostX][ghostY] = 4;      // add this cell to the path
	        
	        	
		    	//if reached pacman's position
		        if (ghostX == 9 && ghostY == 9){
		        	return true;
		            }
		        if (solution(ghostX - 1, ghostY)  ||     //as long as in some direction
		                solution(ghostX, ghostY - 1)  ||  //there is a solution
		                solution(ghostX + 1, ghostY)  || // and the puzzle have not been solved
		                solution(ghostX, ghostY + 1)){
		            return true;
		            }
		        // maze can't be solved from this cell, so backtrack out of the cell
		        solutionMaze[ghostX][ghostY] = 3;   // mark cell as having been visited
			}
			return false;	
		}	
	
    @Override
    public void draw(Graphics g) {
    	try {
    		if(state == 1){
    			img = ImageIO.read(new File("files/scaredGhost.png"));
    		}else if(state == 2){
    			img = ImageIO.read(new File("files/redEyes.png"));
    		}else if(color == 0){
    			img = ImageIO.read(new File("files/pinkGhost.png"));
        	}else if(color == 1){
            	img = ImageIO.read(new File("files/redGhost.png"));
            }else if(color == 2){
            	img = ImageIO.read(new File("files/orangeGhost.png"));
        	}else if(color == 3){
            	img = ImageIO.read(new File("files/greenGhost.png"));
        	}
    		
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    	g.drawImage(img, this.getPx(), this.getPy(), SIZE, SIZE, null);
    }
}