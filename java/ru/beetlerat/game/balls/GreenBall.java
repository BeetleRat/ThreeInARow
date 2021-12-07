package ru.beetlerat.game.balls;

import javax.swing.*;
import java.awt.*;

public class GreenBall extends BallColor {
    // Цвет шара
    private Color color;
    // Очки за шар
    private int price;
    // Конструктор
    public GreenBall(BallsGrid ballsGrid, JComponent canvas, int row, int column, int id) {
        // Конструктор суперкласса
        super(ballsGrid, canvas, row, column, id);
        // Установка цвета шара
        color=new Color(0,255,0);
        // Загрузка изображений
        darkIMG=loadImage("greenBallDark.png");
        drawIMG=darkIMG;
        if(hasImage){
            lightIMG=loadImage("greenBallLight.png");
        }
        // Установка количества очков за шар
        price=2;
    }

    // Отрисовка шара
    @Override
    void paintBall(Graphics brush) {
        if(isExists()){
            // Сохранить цвет кисти
            Color brushColor = brush.getColor();
            // Отрисовать шар нужного цвета
            brush.setColor(color);
            if (hasImage) {
                brush.drawImage(drawIMG, x, y, null);
            } else {
                brush.fillOval(x, y, width, height);
            }
            // Вернуть кисти прежний цвет
            brush.setColor(brushColor);
        }
    }

    @Override
    int getType() {
        return GREEN_BALL;
    }
    @Override
    int getPrice() {
        return price;
    }
    @Override
    void setBright(boolean isBright) {
        if(isBright){
            color=new Color(150,255,150);
            drawIMG=lightIMG;
        }
        else {
            color=new Color(0,255,0);
            drawIMG=darkIMG;
        }
    }
}
