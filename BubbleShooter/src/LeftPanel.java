/**
 * Class that defines the left panel of the game's GUI.
 * 
 * @author Tyler Fenske
 * @version 2016-12-06
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LeftPanel extends JPanel {

    BufferedImage leftPanel;
    
    /** When called, creates leftpanel image in left GUI JPanel */
    public LeftPanel(){
        leftPanel = loadImage("leftpanel.png", leftPanel);
    }
    
    /** Overrides paintComonent to display custom graphics */
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        
        g.drawImage(leftPanel, 0, 0, null);
        
    }
    
    /** Loads image */
    private BufferedImage loadImage (String imageName, BufferedImage image){
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream(imageName);
        try{
            image = ImageIO.read(in);
        }catch (IOException ex){
            System.err.println("Could not load: " + imageName);
        }
        return image;
    }
}
