package ru.beetlerat.game.balls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Ball implements Runnable {
    // Объект потока
    private Thread thread;
    // Панель в которой отрисовывается шар
    private JPanel canvas;
    // Класс взаимодействия с другими шариками
    Balls anotherBalls;


    private int ballName;

    // Параметры шара
    private int x;
    private int y;
    private int width;
    private int height;

    // Границы игрового поля
    private int floor;
    private int ceil;
    private int rightBorder;
    private int leftBorder;


    private boolean isBallExist;
    private boolean isBallDragged;


     Ball(Balls anotherBalls, JPanel canvas, int x, int y,int id){
        // Имя шарика
        ballName=id;
        // Создание паралельного потока
        thread=new Thread(this,"Ball "+ballName+" thread");
        // Сохранение панели в которой отрисовывается шар
        this.canvas=canvas;
        this.anotherBalls=anotherBalls;
        // Установка базовых параметров шара
        this.x=x;
        this.y=y;
        width=30;
        height=30;
        // Установка границ шара
        floor=this.canvas.getSize().height-height-20;
        ceil=0+height/2;
        rightBorder=this.canvas.getSize().width-width;
        leftBorder=0+width/2;

        isBallExist=true;
        isBallDragged =false;
        // Добавление слушателей
        addListeners();

        // Запуск парралельного потока
        thread.start();
    }

    // Добавление слушателей
    private void addListeners(){
        // Добавление слушателя мыши через адаптер
        MouseAdapter mouseListener= new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getX()>x&&e.getX()<x+width&&e.getY()<y+height&&e.getY()>y){
                    isBallDragged =true;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getX()>x&&e.getX()<x+width&&e.getY()<y+height&&e.getY()>y){
                    isBallDragged =true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isBallDragged =false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if(isBallDragged){
                    if(e.getX()-width/2>leftBorder&&e.getX()-width/2<rightBorder){
                        x=e.getX()-width/2;
                    }
                    if(e.getY()-height/2>ceil&&e.getY()-height/2<floor){
                        y=e.getY()-height/2;
                    }
                    canvas.repaint();
                }
            }
        };
        // Добавление слушателя для мыши
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);
    }

    // Метод выполняемый в паралельном потоке
    @Override
    public void run() {
        // До тех пор, пока шарик существует
        while (isBallExist) {
            try {
                // Приостановка потока для плавности
                Thread.sleep(10);
                // Падение шарика вниз
                if (y < floor&&!isBallDragged&&!anotherBalls.isIntersect(ballName)) {
                    y++;
                    canvas.repaint();
                }
            } catch (InterruptedException e) {
                System.out.println("Ошибка в потоке: "+e);
            }
        }
        // После уничтожения шарика перерисовать панель без него
        canvas.repaint();
    }

    // Геттеры
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getBallName() {
        return ballName;
    }

    // Сеттеры
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setBallName(int ballName) {
        this.ballName = ballName;
    }

    // Уничтожение шарика
    void destroyBall(){
        isBallExist=false;
        x=-1;
        y=-1;
        width=0;
        height=0;
    }
    // Отрисовка шарика
    void paintBall(Graphics brush){
        brush.fillOval(x,y,width,height);
    }

    boolean isIntersect(Ball anotherBall){

         int thisLeftX=x-width/2;
         int thisRightX=x+width/2;
         int thisFloorY=y+height/2;
         int thisCeilY=y-height/2;
         if(
                 isPointInsideTheBall(thisLeftX,thisCeilY,anotherBall) ||
                 isPointInsideTheBall(thisRightX,thisCeilY,anotherBall)||
                 isPointInsideTheBall(thisRightX,thisFloorY,anotherBall)||
                 isPointInsideTheBall(thisLeftX,thisFloorY,anotherBall)
         ){
             return true;
         }


         return false;
     }

     private boolean isPointInsideTheBall(int x,int y, Ball ball){
         final int anotherLeftX=ball.getX()-ball.getWidth()/2;
         final int anotherRightX=ball.getX()+ball.getWidth()/2;
         final int anotherFloorY=ball.getY()+ball.getHeight()/2;
         final int anotherCeilY=ball.getY()-ball.getHeight()/2;
         // System.out.println("anotherLeftX: "+anotherLeftX+" anotherRightX: "+anotherRightX+" anotherFloorY: "+anotherFloorY+" anotherCeilY: "+anotherCeilY+"; x:"+x+" y:"+y);
         if(x<anotherRightX&&x>anotherLeftX&&y>anotherCeilY&&y<anotherFloorY){
             //System.out.println("Ball "+ball.getBallName()+" is intersect "+ballName);
             return true;
         }
         return false;
     }

}
