
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;


/**
 *@author Victoria Suwardiman & Andrew Dammann
 *@course CSC 2053
 *@instructor Dr. Helwig
 *@project Project 4: Brickbreaker
 */
public class ControlPanel extends JPanel {

    //declare buttons & button handler
    private JButton restart, pause, help;
    ButtonHandler action;

    //declare key listener
    DirectionListener direction;

    //declare panels
    private TextfieldPanel text;
    private ImagePanel image;

    //declare bricks array, ball, paddle
    private Brick[] bricks;
    private Ball ball;
    private Paddle paddle;

    //declare number formatter to display time
    NumberFormat formatter;

    //declare timer, timer handler, time delay
     Timer mainTimer;
     TimerHandler timer;
     final int TIMER_DELAY = 10;
     
     //declare time, score, lives
     int time, timeDisplay, score, count, lives = 3;


     
     /**
      * constructor takes in copies of the TextfieldPanel and ImagePanel as well as
     copies of the brick array, ball, & paddle
      * @param text
      * @param image
      * @param bricks
      * @param ball
      * @param paddle
      */
     public ControlPanel(TextfieldPanel text, ImagePanel image, Brick[] bricks, Ball ball, Paddle paddle ){

        //instantiating panels and objects
        this.bricks = bricks;
        this.ball = ball;
        this.paddle = paddle;
        this.text = text;
        this.image = image;

        //instantiate action listeners
        action = new ButtonHandler();
        direction = new DirectionListener();
        timer = new TimerHandler();
        
        //instantiate formatter for time
        formatter = new DecimalFormat("00");
       
        //create timer
        mainTimer = new Timer(TIMER_DELAY, timer);
        time = 0;
        timeDisplay = 0;
        
        //add key listener
        image.addKeyListener (direction);
        image.requestFocusInWindow();
        
        //create buttons and add button listeners
        restart = new JButton("RESTART");
        restart.addActionListener(action);
        pause = new JButton("PAUSE");
        pause.addActionListener(action);
        help = new JButton("HELP");
        help.addActionListener(action);
        
        //setting up panel with grid layout
        this.setLayout(new GridLayout(3,1));
        this.add(help);
        this.add(restart);
        this.add(pause);

    }

    //button listener for restart and pause buttons
    class ButtonHandler implements ActionListener{

        public void actionPerformed(ActionEvent e){

            if(e.getActionCommand().equals("RESTART")){
               
                //resets every brick to being not hit
                for(int i = 0; i < bricks.length; i++){
                    bricks[i].setHit(false);
                }
                
                //resets every textfield and variable back to 0
                lives = 3;
                score = 0;
                time = 0;
                timeDisplay = 0;
                text.setTime("" + formatter.format(timeDisplay/60) + ":" +
                 formatter.format(timeDisplay%60));
                text.setLives("" + lives);
                text.setScore("" + score);
                
                //resets position of ball and paddle to initial position
                ball.setX(400);
                ball.setY(698);
                ball.setXVelocity(0);
                ball.setYVelocity(0);
                paddle.setX(335);
                paddle.setY(720);
                
                //stops timer resets up image panel
                mainTimer.stop();
                image.requestFocusInWindow();
                image.updateBoard();
            }
            else if(e.getActionCommand().equals("PAUSE")){
                
                //stops timer
                if(mainTimer.isRunning()){
                  mainTimer.stop();
                  image.requestFocusInWindow();}
                else{
                  mainTimer.start();
                  image.requestFocusInWindow();
                }
            }
            else if(e.getActionCommand().equals("HELP"))
            {
                JOptionPane.showMessageDialog (null,  "Press space bar to start the game, hit the ball with your paddle and try to get rid of all the bricks", "INSTRUCTIONS", JOptionPane.PLAIN_MESSAGE );
            }
        }

    }

    class DirectionListener implements KeyListener{

        public void keyPressed(KeyEvent event){
           switch(event.getKeyCode()){
                case KeyEvent.VK_LEFT:
                   //sets paddle left boundary and sets position of paddle
                   if(paddle.getX() > 0 && mainTimer.isRunning()) {
                   paddle.setX(paddle.getX() - 16);
                   }
                   break;
                case KeyEvent.VK_RIGHT:
                   //sets paddle right boundary and sets position of paddle 
                   if(paddle.getX() + 150 < 800 && mainTimer.isRunning()) {
                   paddle.setX(paddle.getX() + 16);
                   }
                   break;
                case KeyEvent.VK_SPACE:
                   //starts timer and releases the ball
                   mainTimer.start();
                   if(ball.getX() == 400 && ball.getY() == 698){
                   ball.setYVelocity(-5);
                   ball.setXVelocity(-5);}
                   break;
            }
        }

        public void keyTyped(KeyEvent event) {}
        public void keyReleased(KeyEvent event) {}
    }

    class TimerHandler implements ActionListener{

        public void actionPerformed(ActionEvent e) {

            //time is increased once timer begins
            time++;
            //used for displaying time
            if(time % 10 == 0){
                timeDisplay++;
            }
            
            //sets text fields
            text.setTime("" + formatter.format(timeDisplay/60) + ":" +
                 formatter.format(timeDisplay%60));
            text.setScore("" + score);
            text.setLives("" + lives);
            
            //checks if ball has hit anything and sets velocities; repaint
            checkContact();
            ball.setX(ball.getX() + ball.getXVelocity());
            ball.setY(ball.getY() + ball.getYVelocity());
            image.updateBoard();
        }

    }

    private void checkContact()
    {
        //sets boundaries for ball in the horizontal direction
        if(ball.getX() + 20 >= 800 || ball.getX() <= 0)
        {
            ball.setXVelocity(-1 * ball.getXVelocity());
        }
        //sets bounndary for upper wall in vertical direction
        if(ball.getY() < 0)
        {
            ball.setYVelocity(-1 * ball.getYVelocity());
        }
        //checks if ball touches paddle; if so allows ball to go in reverse y velocity
        if(ball.getY() + 20 >= paddle.getY() && (ball.getX() >= paddle.getX() && ball.getX() <= (paddle.getX() + 150)))
        {
            if(ball.getY() + 20 <= paddle.getY() + 9){
            ball.setYVelocity(-1 * ball.getYVelocity());
            }
        }

        //if the ball drops below the paddle loses a life and resets to initial position
        if(ball.getY() + 20 > paddle.getY() + 20){
            lives--;
            text.setLives("" + lives);
            mainTimer.stop();
            ball.setX(400);
            ball.setY(698);
            ball.setXVelocity(0);
            ball.setYVelocity(0);
            paddle.setX(335);
            paddle.setY(720);
        }

       //checks if ball hits a brick; if so resets x velocity and sets brick to being hit; update score
        for(int i = 0; i < bricks.length; i++)
        {
            if(ball.getX() + 20 >= bricks[i].getX() && ball.getX() <= bricks[i].getX() + 100)
            {
                if(ball.getY() + 20 >= bricks[i].getY() && ball.getY() <= bricks[i].getY() + 45)
                {
                    if(!bricks[i].isHit())
                    {
                        if(ball.getX() + 20 - ball.getXVelocity() <= bricks[i].getX() || ball.getX() - ball.getXVelocity() >= bricks[i].getX() + 100)
                        {
                            ball.setXVelocity(-1 * ball.getXVelocity());
                            bricks[i].setHit(true);
                            score += 50;
                        }
                        else
                        {
                        ball.setYVelocity(-1 * ball.getYVelocity());
                        bricks[i].setHit(true);
                        score += 50;
                        }
                    }
                }
            }
            if(bricks[i].isHit())
            {
                count++;
            }
        }
        
        //if all bricks have been hit then stop the timer
        if(count == bricks.length)
        {
            mainTimer.stop();
        }
        //if loses all lives resets everything
        if(lives == 0){
            mainTimer.stop();
            for(int i = 0; i < bricks.length; i++){
                bricks[i].setHit(false);
            }
            lives = 3;
            time = 0;
            text.setLives("" + lives);
            score = 0;
            timeDisplay = 0;

            text.setTime("" + formatter.format(timeDisplay/60) + ":" +
                 formatter.format(timeDisplay%60));
            text.setScore("" + score);
            
            ball.setX(400);
            ball.setY(698);
            ball.setXVelocity(0);
            ball.setYVelocity(0);
            paddle.setX(335);
            paddle.setY(720);

        }

        count = 0;

    }
    

}
