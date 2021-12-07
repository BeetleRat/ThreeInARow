package ru.beetlerat.game.balls;

import javax.swing.*;
import java.awt.*;

public class YellowBall extends Ball{
    // Цвет шара
    private Color color;
    // Очки за шар
    private int price;
    // Конструктор
    public YellowBall(Balls anotherBalls, JPanel canvas, int row, int column, int id) {
        // Конструктор суперкласса
        super(anotherBalls, canvas, row, column, id);
        // Установка цвета шара
        color=new Color(255,255,0);
        // Установка количества очков за шар
        price=2;
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
        return YELLOW_BALL;
    }

    @Override
    int getPrice() {
        return price;
    }
    @Override
    void setBright(boolean isBright) {
        if(isBright){
            color=new Color(255,255,150);
        }
        else {
            color=new Color(255,255,0);
        }
    }
}
