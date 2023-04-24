package com.tryingout.BrickBreaker;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = true;
    private int score = 0;
    
    private int totalBricks = 21;
    
    private final Timer timer;
    private final int delay = 60;
    
    private int playerX = 310;
    
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    
    private MapGenerator map;
    
    public GamePlay(){
        map= new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }
    
    @Override
    public void paint(Graphics g) {
        //background color
        g.setColor(Color.DARK_GRAY);
        g.fillRect(1,1,692,592);
        
        map.draw((Graphics2D)g);
        
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);
        
        g.setColor(Color.green);
        g.fillRect(playerX, 550,100,12);
        
        //ball color
        g.setColor(Color.magenta);
        g.fillOval(ballposX, ballposY, 20, 20);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("MV Boli",Font.BOLD, 25));
        g.drawString("Score: "+score,520,30);
        
        if(totalBricks <= 0){
            //if total bricks is 0 you win.
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.WHITE);
            g.setFont(new Font("MV Boli",Font.BOLD,35));
            g.drawString("You Won! Final Score: "+score,150,300);
            
            g.setFont(new Font("MV Boli",Font.BOLD,25));
            g.drawString("Please press Enter to restart.",150,350);
        }
        
        if(ballposY > 570){
            //if ball goes below the paddle then you lost.
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.WHITE);
            g.setFont(new Font("MV Boli",Font.BOLD,35));
            g.drawString("Game Over! Score: "+score, 150,300);
            
            g.setFont(new Font("MV Boli",Font.BOLD,25));
            g.drawString("Please press Enter to restart.",150,350);
        }
        g.dispose();
    }
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        timer.start();
        
        if(play){
            //ball-pedal interaction
            if(new Rectangle(ballposX, ballposY,20,20).intersects(new Rectangle(playerX,550,100,8))) {
                ballYdir = -ballYdir;
            }
            
            for(int i=0; i<map.map.length; i++){ //ball brick interaction
                for(int j=0; j<map.map[0].length; j++){ //map.map[0].length is number of columns
                    if(map.map[i][j]>0){
                        int brickX = j*map.brickWidth+80;
                        int brickY = i*map.brickHeight+50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        
                        Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX,ballposY,20,20);
                        Rectangle brickRect = rect;
                        
                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score+=5;
                            
                            if(ballposX+19 <= brickRect.x || ballposX+1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            }
                            else
                                ballYdir = -ballYdir;
                        }
                        
                    }
                }
            }
        }
        
        ballposX += ballposX;
        ballposY += ballposY;
        if(ballposX < 0){ //if the ball hits the left wall then it bounces back
            ballXdir = -ballXdir;
        }
        if(ballposY < 0){ //if the ball hits the top wall then it bounces back
            ballYdir = -ballYdir;
        }
        if(ballposX < 640){ //if the ball hits the right wall then it bounces back
            ballXdir = -ballXdir;
        }
    repaint();
    }

    @Override
    public void keyTyped(KeyEvent arg0){
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) { //if right arrow is pressed the paddle moves to right
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (arg0.getKeyCode() == KeyEvent.VK_LEFT) { //if left arrow is pressed the paddle moves to left
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if(arg0.getKeyCode() == KeyEvent.VK_ENTER) { //if enter key is pressed then game starts
            if(!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3,7);
                
                repaint();
            }
        }
    }
    public void moveRight(){ //paddle moves right by 50 pixels
        play=true;
        playerX += 50;
    }
    
    public void moveLeft(){ //paddle moves left by 50 pixels
        play = true;
        playerX -= 50;
    }
    
    @Override
    public void keyReleased(KeyEvent arg0){
    }
}