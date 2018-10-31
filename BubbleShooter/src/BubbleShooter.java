/**
 * This class contains the main methods used for launching the game at a 
 * specified frame size.
 * 
 * @author Tyler Fenske
 * @version 2016-12-06
 */

import javax.swing.SwingUtilities;

public class BubbleShooter {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    BubbleFrame frame = new BubbleFrame();
                    frame.setSize(602, 694);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setResizable(false);
                    
          
                }
            });
    }
}