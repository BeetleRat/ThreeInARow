package ru.beetlerat.game.support;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

    public Image loadImage(String fileName,int width,int height) {
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
            } else {
                // Вывод в консоль сообщения об ошибке
                System.out.println("Cannot load image file: " + fileName);
            }

        } catch (IOException exOb) {
            // Вывод в консоль сообщения об ошибке
            System.out.println("Cannot load image file: " + fileName);
        }
        return img;
    }

    public ImageIcon loadIconImage(String fileName) {
        ImageIcon img = null;
        File imageFile;
        // Получение файла из папки ресурсов
        if (getClass().getClassLoader().getResource(fileName) != null) {
            imageFile = new File(getClass().getClassLoader().getResource(fileName).getFile());

            // Загрузка изображения из файла в объект img
            img = new ImageIcon(imageFile.getAbsolutePath());
        } else {
            // Вывод в консоль сообщения об ошибке
            System.out.println("Cannot find image file: " + fileName);
        }
        return img;
    }
}
