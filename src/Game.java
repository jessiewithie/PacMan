/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("PACK MAN");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.CENTER);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status, BorderLayout.NORTH);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // set the control panel on the bottom
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);
        control_panel.setBackground(Color.black);
        
        //restart button
        final JButton start = new JButton("Restart");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(start);
        start.requestFocusInWindow();
        start.setForeground(new Color(25, 34, 200));
        
        //instruction button
        final JButton instruction = new JButton("Instructions");
        instruction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.instruction();
            }
        });
        control_panel.add(instruction);
        instruction.setFocusable(false);
        instruction.setForeground(new Color(25, 34, 200));
        
        //high score button
        final JButton highScore = new JButton("High Scores");
        highScore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               court.highScore();
           }
       });
        control_panel.add(highScore);
        highScore.setFocusable(false);
        highScore.setForeground(new Color(25, 34, 200));

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}