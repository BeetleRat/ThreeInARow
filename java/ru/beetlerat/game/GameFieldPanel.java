package ru.beetlerat.game;

import javax.swing.*;
import java.awt.*;

public class GameFieldPanel extends JPanel {

    GameFieldPanel(){
        // Установить размеры панели
        setSize(new Dimension(380,380));
        // Установка границ компонента красными жирностью 5
        setBorder(BorderFactory.createLineBorder(Color.RED,5));

    }
    // Переопределить метод отрисовки компанента
    protected void paintComponent(Graphics brush){
        // ОБЯЗАТЕЛЬНО СНАЧАЛА ОТРИСОВЫВАЕМ САМ КОМПОНЕНТ ЧЕРЕЗ МЕТОД СУПЕРКЛАССА
        super.paintComponent(brush);
        // а затем рисуем на отрисованном компоненте

        // Отрисовка игрового поля
        brush.fillOval(100,100,10,10);

    }
}
