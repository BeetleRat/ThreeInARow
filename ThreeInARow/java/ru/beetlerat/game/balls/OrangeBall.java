package ru.beetlerat.game.balls;

import javax.swing.*;
import java.awt.*;

public class OrangeBall extends Ball{
    // Цвет шара
    private Color color;
    // Очки за шар
    private int price;
    // Конструктор
    public OrangeBall(Balls anotherBalls, JPanel canvas, int row, int column, int id) {
        // Конструктор суперкласса
        super(anotherBalls, canvas, row, column, id);
        // Установка цвета шара
        color=new Color(255,175,0);
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
        return ORANGE_BALL;
    }
    @Override
    int getPrice() {
        return price;
    }
    @Override
    void setBright(boolean isBright) {
        if(isBright){
            color=new Color(255,200,150);
        }
        else {
            color=new Color(255,175,0);
        }
    }
}
