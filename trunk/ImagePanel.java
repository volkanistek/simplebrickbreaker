
import java.awt.*;
import javax.swing.*;


/**
 *@author Victoria Suwardiman & Andrew Dammann
 *@course CSC 2053
 *@instructor Dr. Helwig
 *@project Project 4: Brickbreaker
 */
public class ImagePanel extends JPanel{

    //declares array of bricks, ball, & paddle to be drawn
    private Brick[] bricks;
    private Ball ball;
    private Paddle paddle;

    //constructor of panel takes in copies of the bricks array, ball, & paddle
    public ImagePanel(Brick[] bricks, Ball ball, Paddle paddle){

        this.bricks = bricks;
        this.ball = ball;
        this.paddle = paddle;

        //sets border of panel and background color
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 4));
        setBackground(Color.YELLOW);

        //sets focusable of panel to allow key press
        this.setFocusable(true);

        //paints initial position upon instantiation
        repaint();
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        //paints  bricks only if they are not hit
        for(int i = 0; i < bricks.length; i++){
            if(!bricks[i].isHit()){
                g.setColor(Color.ORANGE);
                g.fillRect(bricks[i].getX(), bricks[i].getY(), 100, 45);
                g.setColor(Color.BLACK);
                g.drawRect(bricks[i].getX(), bricks[i].getY(), 100, 45);
            }
        }

        //paints paddle
        g.setColor(Color.DARK_GRAY);
        g.fillRect(paddle.getX(), paddle.getY(), 150, 20);
        g.setColor(Color.BLUE);
        g.drawRect(paddle.getX(), paddle.getY(), 150, 20);

        //paints ball
        g.setColor(Color.BLACK);
        g.fillOval(ball.getX(), ball.getY(), 20, 20);
    }

    //method that calls the repaint method
    public void updateBoard(){
        repaint();
    }

}
