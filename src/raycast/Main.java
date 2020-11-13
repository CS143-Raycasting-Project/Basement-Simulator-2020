package raycast;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener {
    private static boolean left, right, backwards, forwards, turnLeft, turnRight, render = false;
    private int playerX, playerY = 0;
    static Timer keyTimer = new Timer(10, new Main());
    private static DrawScene scene = new DrawScene(0, 0);

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(400,400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    turnLeft = true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    turnRight = true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_W) {
                    forwards = true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_A) {
                    left = true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_S) {
                    backwards = true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_D) {
                    right = true;
                }
            }
            public void keyTyped(KeyEvent e) {

            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    turnLeft = false;
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    turnRight = false;
                }
                else if (e.getKeyCode() == KeyEvent.VK_W) {
                    forwards = false;
                }
                else if (e.getKeyCode() == KeyEvent.VK_A) {
                    left = false;
                }
                else if (e.getKeyCode() == KeyEvent.VK_S) {
                    backwards = false;
                }
                else if (e.getKeyCode() == KeyEvent.VK_D) {
                    right = false;
                }
            }
        });
        f.add(scene);
        f.setResizable(false);
        f.setVisible(true);
        keyTimer.start();

    }
    public void actionPerformed(ActionEvent arg0) {
        if (!(left && right)) {
            if (left) {
                playerX --;
                render = true;
            }
            else if (right) {
                playerX ++;
                render = true;
            }
        }
        if (!(forwards && backwards)) {
            if (forwards) {
                playerY --;
                render = true;
            }
            else if (backwards) {
                playerY ++;
                render = true;
            }
        }
        if (render) {
            scene.setLocation(playerX, playerY);
            scene.renderFrame();
        }
        // System.out.println(playerX + ", " + playerY);
        render = false;
    }
}