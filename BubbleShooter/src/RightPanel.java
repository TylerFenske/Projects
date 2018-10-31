/**
 * Class that defines the right panel of the game's GUI.
 * 
 * @author Tyler Fenske
 * @version 2016-12-06
 */

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class RightPanel extends JPanel {

    BufferedImage rightPanel;
    
    /** When called, creates rightpanel image in right GUI JPanel */
    public RightPanel(){
        setLayout(new BorderLayout());
        
        rightPanel = loadImage("rightpanel.png", rightPanel);
    }
    
    /** Overrides paintComonent to display custom graphics */
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        
        g.drawImage(rightPanel, 0, 0, null);
        
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
