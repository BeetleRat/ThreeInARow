package ru.beetlerat.game.balls;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// Класс
public abstract class BallColor extends BallMovement {
    // Цвет шара
    protected Color color;
    // Объекты изображений
    protected Image darkIMG;
    protected Image lightIMG;
    protected Image drawIMG;
    // Удалось ли загрузить изображение
    protected boolean hasImage;

    BallColor(BallsGrid ballsGrid, JComponent canvas, int row, int column, int id) {
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
