package ru.beetlerat.game.balls;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// Класс
public abstract class BallView extends BallMovement {
    // Константы типов шаров
    public static final int YELLOW_BALL = 5;
    public static final int PINK_BALL = 4;
    public static final int GREEN_BALL = 3;
    public static final int ORANGE_BALL = 2;
    public static final int BLUE_BALL = 1;
    public static final int BLACK_BALL = 0;

    // Фабричный метод создания шара определенного типа
    public static BallMovement createBall(int ballType, BallsGrid anotherBallsGrid, JComponent canvas, int row, int column, int id) {
        switch (ballType) {
            case BLACK_BALL:
                return new BlackBall(anotherBallsGrid, canvas, row, column, id);
            case BLUE_BALL:
                return new BlueBall(anotherBallsGrid, canvas, row, column, id);
            case ORANGE_BALL:
                return new OrangeBall(anotherBallsGrid, canvas, row, column, id);
            case GREEN_BALL:
                return new GreenBall(anotherBallsGrid, canvas, row, column, id);
            case PINK_BALL:
                return new PinkBall(anotherBallsGrid, canvas, row, column, id);
            case YELLOW_BALL:
                return new YellowBall(anotherBallsGrid, canvas, row, column, id);
            default:
                System.out.println("Не существует шара типа: " + ballType);
                return null;
        }
    }

    // Цвет шара
    protected Color color;
    // Объекты изображений
    protected Image darkIMG;
    protected Image lightIMG;
    protected Image drawIMG;
    // Удалось ли загрузить изображение
    protected boolean hasImage;

    BallView(BallsGrid ballsGrid, JComponent canvas, int row, int column, int id) {
        super(ballsGrid, canvas, row, column, id);
        color = null;
        hasImage = false;
        lightIMG = null;
        darkIMG = null;
        drawIMG = null;
    }

    protected Image loadImage(String fileName) {
        Image img = null;
        try {
            File imageFile = null;
            // Получение файла из папки ресурсов
            if (getClass().getClassLoader().getResource(fileName) != null) {
                imageFile = new File(getClass().getClassLoader().getResource(fileName).getFile());
                // Очистка изображения, если оно было до этого
                if (img != null) {
                    img.flush();
                }
                // Загрузка изображения из файла в объект img
                img = ImageIO.read(imageFile).getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
                hasImage = true;
            } else {
                // Вывод в консоль сообщения об ошибке
                System.out.println("Cannot load image file: " + fileName);
                hasImage = false;
            }

        } catch (IOException exOb) {
            // Вывод в консоль сообщения об ошибке
            System.out.println("Cannot load image file: " + fileName);
            hasImage = false;
        }
        return img;
    }
}
