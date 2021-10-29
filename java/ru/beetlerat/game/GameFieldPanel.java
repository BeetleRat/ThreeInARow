package ru.beetlerat.game;

import ru.beetlerat.game.balls.Ball;

import javax.swing.*;
import java.awt.*;

public class GameFieldPanel extends JPanel {
    private Ball balls[];


    GameFieldPanel(){
        // Установить размеры панели
        setSize(new Dimension(380,380));
        // Установка границ компонента красными жирностью 5
        setBorder(BorderFactory.createLineBorder(Color.RED,5));

        balls=new Ball[10];
        for(int i=0;i<10;i++){
            balls[i]=new Ball(this,15+i*35,100);
        }
    }
    // Переопределить метод отрисовки компанента
    protected void paintComponent(Graphics brush){
        // ОБЯЗАТЕЛЬНО СНАЧАЛА ОТРИСОВЫВАЕМ САМ КОМПОНЕНТ ЧЕРЕЗ МЕТОД СУПЕРКЛАССА
        super.paintComponent(brush);
        // а затем рисуем на отрисованном компоненте

        // Отрисовка игрового поля
        for (Ball ball:balls){
            ball.paintBall(brush);
        }
    }
}
