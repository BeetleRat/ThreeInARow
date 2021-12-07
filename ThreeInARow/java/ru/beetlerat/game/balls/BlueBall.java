package ru.beetlerat.game.balls;

import javax.swing.*;
import java.awt.*;

public class BlueBall extends Ball{
    // Цвет шара
    private Color color;
    // Очки за шар
    private int price;
    // Конструктор
    public BlueBall(Balls anotherBalls, JPanel canvas, int row, int column, int id) {
        // Конструктор суперкласса
        super(anotherBalls, canvas, row, column, id);
        // Установка цвета шара
        color=new Color(0,0,255);
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
        return BLUE_BALL;
    }
    @Override
    int getPrice() {
        return price;
    }
    @Override
    void setBright(boolean isBright) {
        if(isBright){
            color=new Color(150,150,255);
        }
        else {
            color=new Color(0,0,255);
        }
    }
}
