package some;

/*
Author : DHRUVIL SURESH PATEL

Game::
        Just you need toescpe from the bots that are searching for you
        you cannot go out of the borders
        you are out only if any bot catches you
controls::
        w - up
        a - left
        s - down
        d - right
*/

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Escape extends JFrame implements KeyListener{

    
    int height = 500 , width = 500 , x , y , factor = 10 , noOfBots , score , level;
    char pressed = 'd' , prev = 'd';
    String player = "" , bot = "";
    ArrayList<Integer> xbody = new ArrayList<>() , ybody = new ArrayList<>();
    
    void run(Escape pm){
        String lev = JOptionPane.showInputDialog("Enter level: \n please choose levelfrom 1 to level 10\n"
                + "upto 3 are very easy recommended not to choose them just added for design purpose..");
        level = Integer.parseInt(lev);
        pm.setSize(height + 20 , width + 20);
        pm.setVisible(true);
        pm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pm.initialize();
        pm.addKeyListener(pm);
    }
    
    public void paintComponent(){}
    
    public void paint(Graphics g){
        Dimension d = getSize();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.BLACK);
        
        //player's next move
        int prevx = x , prevy = y;
        if (pressed == 'd') {
            if (prev == 'a') {
                pressed = 'a';
                x -= factor;
            } else {
                x += factor;
            } 
        } else if (pressed == 'w') {
            if (prev == 's') {
                pressed = 's';
                y += factor;
            } else {
                y -= factor;
            }
        } else if (pressed == 'a') {
            if (prev == 'd') {
                pressed = 'd';
                x += factor;
            } else {
                x -= factor;
            }
        } else if (pressed == 's') {
            if (prev == 'w') {
                pressed = 'w';
                y -= factor;
            } else {
                y += factor;
            }
        }
        
        if(x<0){
            x = 0;
        }
        if(y<0){
            y = 0;
        }
        if(x>=width){
            x = width-factor;
        }
        if(y>=height){
            y = height - factor;
        }
        
        g.drawString(player, x, y);
        
        //bot's next move
        boolean caught = false;
        for(int i=0 ; i<xbody.size() ; i++){
            int j = xbody.get(i).intValue() , k = ybody.get(i).intValue();
            int[] a = next(j , k , i);
            xbody.set(i, a[0]);
            ybody.set(i, a[1]);
            if(x==a[0] && y==a[1]){
                caught = true;
            }
        }
        
        //print bots
        for(int i=0 ; i<xbody.size() ; i++){
            g.drawString(bot, xbody.get(i).intValue(), ybody.get(i).intValue());
        }
        
        if(caught){
            JOptionPane.showMessageDialog(null, "OUT!!!!!!! fuck off!!!!\nYour Score is " + score);
            System.exit(0);
        }
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Escape.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        prev = pressed;
        score++;
        repaint();
    }
    
    void initialize(){
        x = random(0 , width/factor) * factor;
        y = random(0 , height/factor) * factor;
        
        if(factor==10){
            player = "@";
            bot = "#";
        }
        else if(factor==5){
            player = "+";
            bot = "*";
        }
        
        switch(level){
            case 1:
                noOfBots = 2;
                break;
            case 2:
                noOfBots = 4;
                break;
            case 3:
                noOfBots = 6;
                break;
            case 4:
                noOfBots = 8;
                break;
            case 5:
                noOfBots = 10;
                break;
            case 6:
                noOfBots = 12;
                break;
            case 7:
                noOfBots = 16;
                break;
            case 8:
                noOfBots = 18;
                break;
            case 9:
                noOfBots = 20;
                break;
            case 10:
                noOfBots = 25;
                break;
        }
        
        for(int i=0 ; i<noOfBots ; i++){
            int j = random(0 , width/factor) * factor , k = random(0 , height/factor) * factor;
            while(check(j , k , -1) || (j==x && k==y)){
                j = random(0 , width/factor) * factor;
                k = random(0 , height/factor) * factor;
            }
            xbody.add(j);
            ybody.add(k);
        }
    }
    
    boolean check(int xi , int yj , int n){  
        //to check if point inside body of any bot or not
        // n to skip checking for a particular bot
        for(int i=0 ; i<xbody.size() ; i++){
            if(i==n){
                continue;
            }
            int j = xbody.get(i).intValue() , k = ybody.get(i).intValue();
            if(xi==j && yj==k){
                return true;
            }
        }
        
        return false;
    }
    
    int random(int i, int j) {
        Random r = new Random();
        int num = r.nextInt((j - i) + 1) + i;
        return num;
    }
    
    int[] next(int i , int j , int n){
        double dist = dis(i , j , x , y);
        int xi = i , yj = j;
        boolean[] a = new boolean[4];
        while(true){
            int decision = random(0 , 3) , prevx = xi , prevy = yj;
            switch(decision){
                case 0:
                    xi += factor;
                    break;
                case 1:
                    yj += factor;
                    break;
                case 2:
                    xi -= factor;
                    break;
                case 3:
                    yj -= factor;
            }
            
            boolean b = true;
            for(int o=0 ; o<4 ; o++){
                if(!a[o]){
                    b = false;
                    break;
                }
            }
            
            if(b){
                return new int[]{i , j};
            }
            
            a[decision] = true;
            
            if(check(xi , yj , n) || xi<0 || yj<0 || xi>width || yj>height){
                xi = prevx;
                yj = prevy;
                continue;
            }
            
            double newDist = dis(xi , yj , x , y);
            if(newDist>dist){
                xi = prevx;
                yj = prevy;
                continue;
            }
            
            return new int[]{xi , yj};
        }
    }
    
    double dis(int i , int j , int a , int b){
        return Math.sqrt( ((b - j) * (b - j)) + ( (a - i) * (a - i)) );
    }
    
    public void keyReleased(KeyEvent e){}
    public void keyPressed(KeyEvent e){}
    public void keyTyped(KeyEvent e){
        pressed = e.getKeyChar();
    }
}
