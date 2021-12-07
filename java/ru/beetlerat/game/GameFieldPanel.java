package ru.beetlerat.game;



import ru.beetlerat.game.balls.Balls;

import javax.swing.*;
import java.awt.*;

public class GameFieldPanel extends JPanel {
    private Balls balls;


    GameFieldPanel(){
        // Установить размеры панели
        setSize(new Dimension(380,400));
        // Установка границ компонента красными жирностью 5
        setBorder(BorderFactory.createLineBorder(Color.RED,5));
        balls=new Balls(this);
    }
    // Переопределить метод отрисовки компанента
    protected void paintComponent(Graphics brush){
        // ОБЯЗАТЕЛЬНО СНАЧАЛА ОТРИСОВЫВАЕМ САМ КОМПОНЕНТ ЧЕРЕЗ МЕТОД СУПЕРКЛАССА
        super.paintComponent(brush);
        // а затем рисуем на отрисованном компоненте

        // Отрисовка игрового поля
        balls.drawBalls(brush);
    }
}
