package ru.beetlerat.game;

import javax.swing.*;
import java.awt.*;

public class MoveScore implements Runnable {
    private Thread thread;

    private JComponent canvas;

    private int fontSize;
    private Color fontColor;

    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    private int steps;
    private int currentStep;

    private int score;

    public MoveScore(JComponent canvas, int fromX, int fromY, int toX, int toY, int score){
        this.thread=new Thread(this);
        this.canvas=canvas;
        this.fromX=fromX;
        this.fromY=fromY;
        this.toX=toX;
        this.toY=toY;
        this.steps=figure();
        this.score=score;
        this.fontColor=new Color(0,0,0);
        this.fontSize=1;
        currentStep=1;
        this.thread.start();

    }
    @Override
    public void run() {
        while (fromX!=toX||fromY!=toY){
            canvas.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Graphics brush=canvas.getGraphics();
            Font brushFont=brush.getFont();
            Color brushColor=brush.getColor();
            brush.setFont(new Font("1",Font.PLAIN,fontSize));
            brush.setColor(fontColor);
            brush.drawString(String.valueOf(score),fromX,fromY);

            if(fromY>toY){
                fromY--;
            }
            else {
                if(fromY<toY){
                    fromY++;
                }
            }
            if(fromX>toX){
                fromX--;
            }
            else {
                if(fromX<toX){
                    fromX++;
                }
            }
            currentStep++;
            if(currentStep<steps/2){
                fontSize++;
                if(fontSize>10){
                    fontSize=10;
                }
                fontColor.brighter();
            }
            else {
                fontSize--;
                if(fontSize<1){
                    fontSize=1;
                }
                fontColor.darker();
            }
            brush.setFont(brushFont);
            brush.setColor(brushColor);

        }
    }

    private int figure(){
        int x=fromX;
        int y=fromY;
        int sch=0;
        while (x!=toX||y!=toY){
            sch++;
            if(y>toY){
                y--;
            }
            else {
                if(y<toY){
                    y++;
                }
            }
            if(x>toX){
                x--;
            }
            else {
                if(x<toX){
                    x++;
                }
            }
        }
        return sch;
    }


}
