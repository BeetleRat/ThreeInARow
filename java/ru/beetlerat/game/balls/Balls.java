package ru.beetlerat.game.balls;

import javax.swing.*;
import java.awt.*;

public class Balls {
    private JPanel canvas;
    private Ball ballsArray[];
    private int n;

    public Balls(JPanel canvas) {
        this.canvas =canvas;
        n=10;
        ballsArray=new Ball[n];
        for(int i=0;i<n;i++){
            ballsArray[i]=new Ball(this,canvas,15+i*35,100,i);
        }
    }
    public void drawBalls(Graphics brush){
        for (Ball ball:ballsArray){
            ball.paintBall(brush);
        }
    }
    boolean isIntersect(int ballID){
        for(int i=0;i<ballID;i++){
            if(ballsArray[i].isIntersect(ballsArray[ballID])){
                return true;
            }
        }
        for(int i=ballID+1;i<n;i++){
            if(ballsArray[i].isIntersect(ballsArray[ballID])){
                return true;
            }
        }
        return false;
    }
}
