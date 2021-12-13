package ru.beetlerat.game.balls;

import javax.swing.*;
import java.awt.*;

public class BlackBall extends BallView {
    // Цвет шара
    private Color color;
    // Очки за шар
    private int price;
    // Конструктор
    public BlackBall(BallsGrid ballsGrid, JComponent canvas, int row, int column, int id) {
        // Конструктор суперкласса
        super(ballsGrid, canvas, row, column, id);
        // Установка цвета шара
        color=new Color(0,0,0);
        // Загрузка изображений
        darkIMG=imageLoader.loadImage("blackBallDark.png",width,height);
        drawIMG=darkIMG;
        if(darkIMG!=null){
            lightIMG=imageLoader.loadImage("blackBallLight.png",width,height);
        }
        // Установка количества очков за шар
        price=-2;
    }

    // Отрисовка шара
    @Override
    void paintBall(Graphics brush) {
        if(isExists()){
            // Сохранить цвет кисти
            Color brushColor = brush.getColor();
            // Отрисовать шар нужного цвета
            brush.setColor(color);
            if (drawIMG!=null) {
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
        return BLACK_BALL;
    }

    @Override
    int getPrice() {
        return price;
    }
    @Override
    boolean getBright() {
        return bright;
    }

    @Override
    void setBright(boolean isBright) {
        this.bright=isBright;
        if(isBright){
            color=new Color(150,0,150);
            drawIMG=lightIMG;
        }
        else {
            color=new Color(0,0,0);
            drawIMG=darkIMG;
        }
    }
}
