/** 
 *  Title of project
 * 
 *  Date of completion
 * 
 *  This program was created under the collaboration of Nathan Grimsey, Eric Lumpkin, Dylan Gibbons-Churchward, and Matthew McGuinn
 *  for Martin Hock's CS143 class in the Fall quarter of 2020.
 * 
 *  This code may be found at https://github.com/CS143-Raycasting-Project/Raycast along with documentation.
 */

package raycast;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

@SuppressWarnings("serial")
public class Scene extends JPanel {
    //Points were causing rounding issues, so I just made the coords 2 separate doubles.
    private double playerX;
    private double playerY;
    public static int playerRotation = 0; //This is in degrees so that I can just use an int.
    public static Maze maze = new Maze(Main.mazeSize, Main.mazeSize);
    private static BufferedImage miniMap = maze.getMiniMap();
    private int[][] mazeWalls = maze.getMaze();
    private int rayCastScreenPixelColumns = Main.windowX;
    public Scene(double x, double y) {
        this.playerX = x;
        this.playerY = y;
    }
    public void move(Main.Movement direction) { //I use some simple trig here to change how the movement is done depending on rotation.
        switch (direction) {
            /** player degrees of rotation reference
             * 
             *     -135  -90  -45  
             *    +-180   P    0
             *     +135  +90  +45
             */
        case L:
            Main.playerVector = rotateVector(180);
            break;
        case R:
            Main.playerVector = rotateVector(0);
            break;
        case F:
            Main.playerVector = rotateVector(-90);
            break;
        case B:
            Main.playerVector = rotateVector(90);
            break;
        case BL:
            Main.playerVector = rotateVector(135);
            break;
        case BR:
            Main.playerVector = rotateVector(45);
            break;
        case FL:
            Main.playerVector = rotateVector(-135);
            break;
        case FR:
            Main.playerVector = rotateVector(-45);
            break;
        }
        playerX += Main.playerVector[0];
        playerY += Main.playerVector[1];
    }

    private static double[] rotateVector(double rotation) {
        double[] rotatedVector = {0, 0}; // {x, y}
        rotatedVector[0] = Main.moveSpeed * Math.cos(Math.toRadians(playerRotation + rotation));
        rotatedVector[1] = Main.moveSpeed * Math.sin(Math.toRadians(playerRotation + rotation));
        return rotatedVector;
    }

    public void rotate(int angle) {
        playerRotation += angle;
    }

    public void renderFrame() {
        repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        //Used for timing the length it takes to render a frame
        double start = System.nanoTime();
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); /* This is antialiasing. We can turn this on later if necessary */
        g2d.setColor(Color.WHITE);
        Ray pixel;
        double collision;
        int columnHeight;
        //This does the collision calculations and renders the scene in 3D
        for (int x = 0; x < rayCastScreenPixelColumns; x++) {
            double cameraX = 2 * x / (double)rayCastScreenPixelColumns - 1;
            pixel = new Ray(playerY / (double)Main.cellSize, playerX / (double)Main.cellSize, Math.toRadians(180-playerRotation), cameraX);
            collision = pixel.findCollision();
            //How tall the column of pixels will be at x. We use the inverse of the collision distance because as the distance increases,
            //the height of the column should decrease. This is then multiplied by the window height and scaled by 10
            columnHeight = (int)(1 / collision / Main.cellSize * Main.windowY * 10);
            if(255 - (int)(collision * 15) >= 0) { //This if statement makes sure that the lowest brightness a color can be is black
                g2d.setColor(new Color(255 - (int)(collision * 15), 0, 0));
            }
            else {
                g2d.setColor(Color.BLACK);
            }
            //This draws the column of pixels on the x value; it's on based on the distance from the collision
            g2d.drawLine(x, Main.windowY / 2 - columnHeight, x, Main.windowY / 2 + columnHeight);
            //as of right now you need to switch x and y, i dont know why. you also need to subtract player rotation from 180 degrees
            //and turn it to radians
        }
        g2d.setColor(Color.ORANGE);

        /*  THIS STUFF LOOKS LIKE A MESS. In reality, it's a bunch of graphical stuff, so there are a lot of numbers that help determine the scale
            of each GUI element. You don't need to understand exactly how the coordinates are determines, because it has to do with what looked good
            on the screen, but it should be pretty easy to understand what each line does. Basically, just don't worry about the parameters so long
            as you understand what each line does; I think you'd probably go crazy trying to figure everything out. */

        //Draws the circle that makes the border of the minimap
        g2d.fillOval(Main.windowX / 64 - Main.windowX / 256, Main.windowX / 64 - Main.windowX / 256, Main.windowX / 5 + Main.windowX / 128, Main.windowX / 5 + Main.windowX / 128);
        //This is how I'm able to rotate and move the minimap inside a bounded circle. It allows you to map a BufferedImage to a rectangle, and then paint a shape with the part of the image that would be there.
        TexturePaint miniMapPaint = new TexturePaint(miniMap, new Rectangle((int)(playerX - Main.windowX - Main.cellSize * 4) / 2 + Main.windowX / 5 / 2 + Main.windowX / 64, (int)(playerY - Main.windowX - Main.cellSize * 4) / 2 + Main.windowX / 5 / 2 + Main.windowX / 64, -(Main.windowX + Main.cellSize * 8) / 2, -(Main.windowX + Main.cellSize * 8) / 2));
        //Sets the paint used by g2d to the TexturePaint of the miniMap image
        g2d.setPaint(miniMapPaint);
        //This controls the rotation of the miniMap image, and it is centered on the center point of the circle that bounds it
        g2d.rotate(Math.toRadians(180 - playerRotation), Main.windowX / 5 / 2 + Main.windowX / 64, Main.windowX / 5 / 2 + Main.windowX / 64);
        //This is what actually draws the miniMap image in a circle
        g2d.fillOval(Main.windowX / 64, Main.windowX / 64, Main.windowX / 5, Main.windowX / 5);
        g2d.setColor(Color.RED);
        //This reverses the rotation needed for the miniMap so that a static player icon can be drawn at the center of the miniMap
        g2d.rotate(-Math.toRadians(180 - playerRotation), Main.windowX / 5 / 2 + Main.windowX / 64, Main.windowX / 5 / 2 + Main.windowX / 64);
        g2d.fillRect(Main.windowX / 5 / 2 + Main.windowX / 64 - Main.cellSize / 8, Main.windowX / 5 / 2 + Main.windowX / 64 - Main.cellSize / 8, Main.cellSize / 4, Main.cellSize / 4);
        //Used for timing the length it takes to render a frame
        double end = System.nanoTime();
        //System.out.println((double)(end - start)/1000000); //with 4000 rays it should take between 0.8 and 1.3 MILLISECONDS per frame
    }

}
