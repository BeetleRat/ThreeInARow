package ru.beetlerat.game.balls;

import javax.swing.*;
import java.awt.*;

public class PinkBall extends Ball{
    // Цвет шара
    private Color color;
    // Очки за шар
    private int price;
    // Конструктор
    public PinkBall(Balls anotherBalls, JPanel canvas, int row, int column, int id) {
        // Конструктор суперкласса
        super(anotherBalls, canvas, row, column, id);
        // Установка цвета шара
        color=new Color(255,0,225);
        // Установка количества очков за шар
        price=1;
    }

    // Отрисовка шара
    @Override
    void paintBall(Graphics brush) {
        // Сохранить цвет кисти
        Color brushColor=brush.getColor();
        // Отрисовать шар нужного цвета
        brush.setColor(color);
        brush.fillOval(x,y,width,height);
        // Вернуть кисти прежний цвет
        brush.setColor(brushColor);
    }

    @Override
    int getType() {
        return PINK_BALL;
    }
    @Override
    int getPrice() {
        return price;
    }

    @Override
    void setBright(boolean isBright) {
        if(isBright){
            color=new Color(255,150,225);
        }
        else {
            color=new Color(255,0,225);
        }
    }
}
