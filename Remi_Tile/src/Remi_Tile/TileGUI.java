/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 09/13/2018
 *
 * TileGUI objects load images for all aspects of the game. TileGUIs are used
 * to represent all graphical images on the display.
 * TileGUI's are stored in parallel to the hand array of Tiles for simplicity
 * in display.
 */

package Remi_Tile;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;


public class TileGUI {

    private Image img;
    private ImageView imgView;

    private Color color;
    private int rank;

    private String fileName;


    /**
     * Constructor for TileGUI.
     * Params: None
     * Returns: Nothing
     */
    public TileGUI(Color color, int rank){
        this.color = color;
        this.rank = rank;

        createTileGUI();
    }


    /**
     * Creates the TileGUI object based on class instantiated params.
     * Params: None
     * Returns: Nothing
     */
    private void createTileGUI(){
        try{

            fileName = findFileName();


            InputStream is = ClassLoader.class.getResourceAsStream(fileName);


            img = new Image(is);
            imgView = new ImageView(img);


        }catch (Exception e){
            System.out.println("Fill not Found " + fileName);
        }
    }


    /**
     * Returns the image object of the loaded image.
     * Params: None
     * Returns: Image
     */
    public Image getTileImage(){
        return img;
    }

    /**
     * Based on the params of the class, returns appropriate image file.
     * Params: None
     * Returns: String
     */
    private String findFileName() {
        switch (rank) {
            case -1:
                if(color == Color.BLANK){
                    fileName = "/resources/blank.png";
                }else if (color == Color.DISCARD){
                    fileName = "/resources/discard.png";
                }else if (color == Color.POOL){
                    fileName = "/resources/pool.png";
                }else if(color == Color.BACK){
                    fileName = "/resources/back.png";
                }else if(color == Color.RULES){
                    fileName = "/resources/rules.PNG";
                }else if(color == Color.BACKGROUND){
                    fileName = "/resources/background.png";
                }
                break;
            case 0:
                fileName = "/resources/joker.png";
                break;
            case 1:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue1.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green1.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red1.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow1.png";
                }
                break;
            case 2:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue2.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green2.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red2.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow2.png";
                }
                break;
            case 3:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue3.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green3.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red3.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow3.png";
                }
                break;
            case 4:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue4.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green4.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red4.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow4.png";
                }
                break;
            case 5:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue5.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green5.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red5.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow5.png";
                }
                break;
            case 6:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue6.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green6.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red6.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow6.png";
                }
                break;
            case 7:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue7.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green7.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red7.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow7.png";
                }
                break;
            case 8:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue8.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green8.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red8.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow8.png";
                }
                break;
            case 9:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue9.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green9.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red9.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow9.png";
                }
                break;
            case 10:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue10.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green10.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red10.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow10.png";
                }
                break;
            case 11:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue11.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green11.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red11.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow11.png";
                }
                break;
            case 12:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue12.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green12.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red12.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow12.png";
                }
                break;
            case 13:
                if (color == Color.BLUE) {
                    fileName = "/resources/blue13.png";
                } else if (color == Color.GREEN) {
                    fileName = "/resources/green13.png";
                } else if (color == Color.RED) {
                    fileName = "/resources/red13.png";
                } else if (color == Color.YELLOW) {
                    fileName = "/resources/yellow13.png";
                }
                break;
        }
        return fileName;
    }

    /**
     * Returns color of TileGUI object
     * Params: None
     * Returns: Color
     */
    public Color getColor(){
        return color;
    }

    /**
     * Returns rank of TileGUI object
     * Params: None
     * Returns: Int
     */
    public int getRank(){
        return rank;
    }

    /**
     * Returns ImageView of image loaded.
     * Params: None
     * Returns: ImageView
     */
    public ImageView getImgView(){
        return imgView;
    }
}
