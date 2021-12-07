package ru.beetlerat.game.balls;

import javax.swing.*;
import java.awt.*;

public class YellowBall extends BallColor {


    // Конструктор
    public YellowBall(BallsGrid ballsGrid, JComponent canvas, int row, int column, int id) {
        // Конструктор суперкласса
        super(ballsGrid, canvas, row, column, id);
        // Установка цвета шара
        color=new Color(255,255,0);
        // Загрузка изображений
        darkIMG=loadImage("yellowBallDark.png");
        drawIMG=darkIMG;
        if(hasImage){
            lightIMG=loadImage("yellowBallLight.png");
        }
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
        return YELLOW_BALL;
    }

    @Override
    int getPrice() {
        return 0;
    }
    @Override
    void setBright(boolean isBright) {

        if(isBright){
            color=new Color(255,255,150);
            drawIMG=lightIMG;
        }
        else {
            color=new Color(255,255,0);
            drawIMG=darkIMG;
        }
    }
}
