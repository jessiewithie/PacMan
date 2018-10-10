/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	//the board; made public cuz want the game objs to access it
	public static final int[][] maze = new int[19][20];
	
    // the state of the game logic
	private Pacman pac; // the pacman, keyboard control
	private Ghost[] ghost = new Ghost[4]; // the ghosts
    private BufferedImage cherImg; //images for cherry, key, and pac
    private BufferedImage keyImg;	//as part of the screen
    private BufferedImage pacLifeImg;
    
    private LinkedList<obj> objts = new LinkedList<obj>(); // can be either cherry or key randomly
    private int cherryCount = 0; //count amount of cherries you have
    private int keyCount = 0; //count amount of keys you have
    private int counter; //time counter

    public boolean playing = false; // whether the game is running 
    private boolean pause = false; //pause the game?
    private boolean instruction = false; //show instruction?
    private boolean record = false; //whether let user enter game 
    private boolean displayScore = false; //display scores?
    private boolean scaryMood = false; //turns the ghosts scaried(mood 1)
    private int scaryMoodCounter = 2000; //timer counter for scary Mood
    									
    
    private Map<Integer, TreeSet<String>> scores = new TreeMap<Integer, 
    							TreeSet<String>>(Collections.reverseOrder());//store a list of names and scores.
    private int life; // amount of life the character gets
    
    private String name = ""; //name of the player
    private boolean ename = false;	//enable the mood of enter name?
    private boolean warnName = false; //if user inpute non-letters in their name or too many letters
    									// give true
    
    // Game constants
    public static final int COURT_WIDTH = 380;
    public static final int COURT_HEIGHT = 440;
    public static final int SQUARE_VELOCITY = 1;
    
    //direction of the character going
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 10;
    
    

    public GameCourt(JLabel status){
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);
		
        //fill the maze
  		for(int i = 0; i < maze.length; i++){
  			for(int j = 0; j < maze[0].length; j++){
  				maze[i][j] = 1;
  			}
  		}
  		
  		for(int i = 0; i < maze.length; i++){
  			maze[i][3] = 0;
  			maze[i][19] = 0;
  			
  			if(i != 9){
  				maze[i][0] = 0;
  				maze[i][13] = 0;
  			}
  			
  			if(!(i == 5 || i == 9 || i == 13)){
  				maze[i][5] = 0;
  				maze[i][17] = 0;
  			}
  			
  			if(!(i == 3 || i == 15)){
  				maze[i][15] = 0;
  			}
  			//
  			if(!(i == 7 || i == 11)){
  				maze[i][9] = 0;
  			}
  		}
  		
  		for(int i = 1; i < 17; i++){
  			maze[4][i] = 0;
  			maze[14][i] = 0;
  		}
  		
  		for(int i = 7; i < 13; i++){
  			maze[6][i] = 0;
  			maze[12][i] = 0;
  		}
  		
  		for(int i = 7; i < 12; i++){
  			maze[i][7] = 0;
  			maze[i][11] = 0;
  		}
  		
  		for(int i = 1; i < 5; i++){
  			maze[0][i] = 0;
  			maze[18][i] = 0;
  		}
  		maze[0][14] = 0;
  		maze[0][18] = 0;
  		maze[2][16] = 0;
  		maze[6][4] = 0;
  		maze[6][16] = 0;
  		maze[8][1] = 0;
  		maze[8][2] = 0;
  		maze[8][6] = 0;
  		maze[8][14] = 0;
  		maze[8][18] = 0;
  		maze[9][8] = 0;
  		maze[10][1] = 0;
  		maze[10][2] = 0;
  		maze[10][6] = 0;
  		maze[10][14] = 0;
  		maze[10][18] = 0;
  		maze[12][4] = 0;
  		maze[12][16] = 0;
  		maze[16][16] = 0;
  		maze[18][14] = 0;
  		maze[18][18] = 0;

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
               warnName = false;
            	if (e.getKeyCode() == KeyEvent.VK_LEFT && pac.change(-1, 0)) {
            		left = true;
            		right = false;
                    up = false;
                    down = false;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && pac.change(1, 0)) {
                    right = true;
                    up = false;
                    down = false;
                    left = false;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && pac.change(0, 1)) {
                	right = true;
                    up = false;
                    down = true;
                    left = false;
                } else if (e.getKeyCode() == KeyEvent.VK_UP && pac.change(0, -1)) {
                	right = true;
                    up = true;
                    down = false;
                    left = false;
                }else if (e.getKeyCode() == KeyEvent.VK_SPACE && playing){
                		pause = !pause;
                }else if (ename){
                	//allowed to have spaces and name length < 12(entering wise)
                	if((Character.isLetter(e.getKeyCode()) || e.getKeyCode() == KeyEvent.VK_SPACE) && name.length() < 12){
                		name += e.getKeyChar();
                		//Used 8 as my delete keycode??? hopefully should work
            		}else if(e.getKeyCode() == 8 && name.length() > 0){
                		name=name.substring(0, name.length()-1);
                	}//if name length() > 10, 
                	if(name.length()>10 && e.getKeyCode() != KeyEvent.VK_SHIFT){
                		warnName = true;
                	}else if(e.getKeyCode() == KeyEvent.VK_ENTER && warnName == false){
                		ename = false;
                		record = false;
                		displayScore = true; //display score after user input
            		try {
            			writeScore();
						readScores();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	}
                	
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });
        
        try {
        	cherImg = ImageIO.read(new File("files/cherry.png"));
        	pacLifeImg = ImageIO.read(new File("files/packman.png"));
        	keyImg = ImageIO.read(new File("files/key.png"));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    
    public boolean playing(){
    	return playing;
    }
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        pac = new Pacman();
        ghost[0] = new Ghost(180, 220, 0);
        ghost[1] = new Ghost(160, 220, 1);
        ghost[2] = new Ghost(180, 200, 2);
        ghost[3] = new Ghost(200, 220, 3);       
        objts = new LinkedList<obj>();
        cherryCount = 0;
        keyCount = 0;
        counter = 0;
        instruction = false;
        
        playing = true;
        pause = false;
        record = false;
        displayScore = false;
        scaryMood = false; 
        scaryMoodCounter = 2000;
        scores = new TreeMap<Integer, TreeSet<String>>(Collections.reverseOrder());
        name = "";
        ename = false;
        warnName = false;

        //make note on life
        life = 3;
        
        right = false;
        left = false;
        up = false;
        down = false;
       
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    /**
     * (Re-)set the game to its initial set up but keep all the states.
     */
    public void loseLife() {
    	
    	try { Thread.sleep(1000); } // wait a bit before updating everything
        catch (InterruptedException e) { }
    	
    	pac = new Pacman();
        ghost[0] = new Ghost(180, 220, 0);
        ghost[1] = new Ghost(160, 220, 1);
        ghost[2] = new Ghost(180, 200, 2);
        ghost[3] = new Ghost(200, 220, 3);       
        counter = 0;
        instruction = false;
        
        playing = true;
        pause = false;
        displayScore = false;
        scaryMood = false; 
        scaryMoodCounter = 2000;
        
        right = false;
        left = false;
        up = false;
        down = false;
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing && (!pause)) {
            // advance the characters in their current direction.
        	pac.move();
        	for(Ghost g : ghost){
	            g.move();
	            if(pac.intersects(g)){
	            	//normal mood then life down and stuff
	            	if(g.getState() == 0){
	            		life --;
	            		playing = false;
	            		loseLife();
            		}else if(g.getState() == 1){ //eat the ghost!
            			cherryCount += 4;
            			g.setPx(g.getPx() - g.getPx()%20);
            			g.setPy(g.getPy() - g.getPy()%20); //set to a %20 = 0 location
            			g.setState(2); //ghost transfer to state 2-the eyes!
            			//generate a solution in maze
            			g.solution(g.getPx()/20, (g.getPy() - 40)/20);
            		}
	            }
	            
        	}
        //start counter when scaryMood is on
        if (scaryMood){
        	scaryMoodCounter --;
        }
        //at the end of scary mood, reboot the counter
        if(scaryMoodCounter == 0 && scaryMood == true){
        	scaryMood = false;
        	for(Ghost g : ghost){ //change the scared ghosts back to their normal state
        		if(g.getState() == 1){
        			g.setState(0);
        		}
        	}
        }
        //move the pacman around
        pac.setVx(0);
        pac.setVy(0);
		if (down==true){
            pac.setVy(SQUARE_VELOCITY);
		}else if(up==true){
            pac.setVy(-SQUARE_VELOCITY);
		}else if(left==true){
            pac.setVx(-SQUARE_VELOCITY);
		}else if(right==true){
            pac.setVx(SQUARE_VELOCITY);
		}
    		
		//making/take objts based on counter!
		counter ++;
		if(counter % 600 == 500 && !objts.isEmpty()){
			objts.removeFirst();
		}
		if(counter % 420 == 400){
			objts.add(new obj());
		}
		for(obj c : objts){
			if(c.intersect(pac)){
				if(c.getType() == 0){
					cherryCount += c.objCount();
				}else if(c.getType() == 1){
					keyCount += c.objCount();
    				for(Ghost g: ghost){
    					if(g.getState() == 0){
    						g.setState(1);
    					}
    				}
    				scaryMood = true;
    				scaryMoodCounter = 2000;//in the case of eating another one
    										//before the last one ended
				}
			}
		}
        //if no life then go to record game result
        if(life <= 0){
        	playing = false;
        	//may be some animations to show??? ehhh let's not
        	record = true;
        }

            // update the display
            repaint();
        }
    }
    
    /**
     * control the instruction button
     */
    public void instruction(){
    	if(!displayScore && life > 0){
	    	if(playing && instruction){
	    	pause = false;
	    	}else{
	    		pause = true;
	    	}
	    	instruction = !instruction;
    	}
    }
    
    /**
     * control the high score button
     */
    public void highScore(){
    	if(!instruction && life > 0){
	    	if(playing && (displayScore)){
	    	pause = false;
	    	}else{
	    		pause = true;
	    	}
	    	displayScore = !displayScore;
    	}
    }

    /**
     * read the score into the list so can print the top few scores
     * @throws IOException
     */
    public void readScores() throws IOException{
    	BufferedReader f = new BufferedReader(new FileReader("files/scores.txt"));
    	scores = new TreeMap<Integer, TreeSet<String>>(Collections.reverseOrder());
    	String s = "";
    	String name = "";
    	int score = 0;
    	while( (s = f.readLine()) != null){
    		name = s.substring(0, s.lastIndexOf(" "));
    		score = Integer.parseInt(s.substring(s.lastIndexOf(" ") + 1, s.length()));
    		if(scores.containsKey(score)){
    			scores.get(score).add(name);
    		}else{
    			TreeSet<String> ts = new TreeSet<String>();
    			ts.add(name);
    			scores.put(score,  ts);
    		}
    	}
    	f.close();
    }
    
    /**
     * write the score of the round
     * @throws IOException
     */
    public void writeScore() throws IOException{
        FileWriter out = (new FileWriter("files/scores.txt", true));
        out.write(name + " " + (cherryCount + keyCount *2) + "\n");
        out.close();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, COURT_WIDTH, COURT_HEIGHT);
        //draw the maze
        for(int i = 0; i < maze.length ; i++){
			for(int j = 0; j < maze[0].length ; j++){
				if (maze[i][j] == 0){
		    		g.setColor(Color.BLACK);
		            g.fillRect(i*20, j*20 + 40, 20, 20);
		    	}else if (maze[i][j] == 1){
		    		g.setColor(new Color(25, 34, 200));
		            g.fillRect(i*20, j*20 + 40, 20, 20);
		    	}
			}
		}
        
        g.setColor(Color.yellow);
        if(scaryMood){
        g.drawString(" Remain: " + scaryMoodCounter/40, 140, 27);
        }
        //key counter
        g.drawImage(keyImg, 240, 10, 20, 20, null);
        g.drawString(" X " + keyCount, 265, 25);
        //cherry counter
        g.drawImage(cherImg, 300, 10, 20, 20, null);
        g.setColor(Color.RED);
        g.drawString(" X " + cherryCount, 325, 25);
        //represent amount of life
        g.drawString("Life", 10, 30);
        if(life > 0){
        	for(int i = 0; i < life; i++){
        		g.drawImage(pacLifeImg, 45 + 30 * i, 15, 17, 17, null);
        	}
        }
        
        pac.draw(g);
        
        for(Ghost gh : ghost){
        	gh.draw(g);
        }

        //paint the objects!!
        for(obj c : objts){
        	c.draw(g);
        }
        
        //random court lines
        g.drawLine(0, 40, COURT_WIDTH - 1, 40);
        g.drawLine(0, COURT_HEIGHT - 1, COURT_WIDTH - 1, COURT_HEIGHT - 1);
        g.drawLine(1, 0, 1, 200);
        g.drawLine(1, 250, 1, COURT_HEIGHT - 1);
        g.drawLine(COURT_WIDTH - 1, 0, COURT_WIDTH - 1, 200);
        g.drawLine(COURT_WIDTH - 1, 250, COURT_WIDTH - 1, COURT_HEIGHT - 1);
        
        //when recording scores
        if(record){
        	g.setColor(new Color(25, 34, 200));
        	g.fillRect(30, 30, 320, 420);
        	g.setColor(Color.black);
        	g.drawLine(30, 30, 350, 30);
        	g.drawLine(30, 30, 30, 450);
        	g.drawLine(350, 450, 350, 30);
        	g.drawLine(30, 450, 350, 450);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        	g.drawString("please enter your", 60, 100);
        	g.drawString("name below", 100, 140);
        	
        	
        	ename = true;
        	g.setColor(Color.white);
        	g.drawString(name, 100, 180);
        	
			if(warnName){
				g.setColor(Color.red);
				g.setFont(new Font("Comic Sans MS", Font.BOLD, 10));
				g.drawString("names should be no more than 10 characters", 80, 210);   
				g.drawString("and can only contain letters and spaces", 80, 230);   
			        	}
        }
        
        if(displayScore){
        	g.setColor(new Color(25, 34, 200));
        	g.fillRect(30, 30, 320, 420);
        	g.setColor(Color.black);
        	g.drawLine(30, 30, 350, 30);
        	g.drawLine(30, 30, 30, 450);
        	g.drawLine(350, 450, 350, 30);
        	g.drawLine(30, 450, 350, 450);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        	g.drawString("TOP SCORERS", 60, 100);
        	
        	try {
				readScores();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        	int counter = 0; //count how many scoers have been displayed
        	for(Integer s: scores.keySet()){
        		for(String name : scores.get(s)){
        			g.drawString(name, 60, counter * 25 + 130);
        			g.drawString(Integer.toString(s), 300, counter * 25 + 130);
        			counter ++;
        			if(counter >= 10){
        				break;
        			}
        		}
        	}
        	if(life == 0){
        		g.setColor(Color.red);
        		g.setFont(new Font("Comic Sans MS", Font.ITALIC, 15));
            	g.drawString("Click on restart to", 123, 380);
            	g.drawString("restart the game", 124, 397);
        	}
        	
        }
        
        if(instruction){
        	g.setColor(new Color(25, 34, 200));
        	g.fillRect(30, 30, 320, 420);
        	g.setColor(new Color(150, 240, 150));
        	g.drawLine(30, 30, 350, 30);
        	g.drawLine(30, 30, 30, 450);
        	g.drawLine(350, 450, 350, 30);
        	g.drawLine(30, 450, 350, 450);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        	g.drawString("Instructions", 100, 100);
        	
        	g.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        	g.drawString("1. Arrow keys to move around.", 60, 140);
        	g.drawString("2. Space key to stop.", 60, 160);
        	g.drawString("3. Stay away from the ghost.", 60, 180);
        	g.drawString("4. Try to each as many cherries", 60, 200);
        	g.drawString("and keys as possible.", 80, 220);
        	g.drawString("5. You only have 3 lives", 60, 240);
        	g.drawString("6. You cannot cross the walls", 60, 260);
        	g.drawString("7. If you found a key, you can", 60, 280);
        	g.drawString("be ghost predators. The killed ghost", 80, 300);
        	g.drawString("goes back to their den and revive.", 80, 320);
        	g.drawString("8. you can never win, but stay alive", 60, 340);
        	g.drawString("for as long as possible.", 80, 360);
        	g.setFont(new Font("Comic Sans MS", Font.ITALIC, 15));
        	g.drawString("Click on instructions to", 113, 380);
        	g.drawString("get back to the game", 114, 397);
        }
        repaint();
    }
    

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
     
}