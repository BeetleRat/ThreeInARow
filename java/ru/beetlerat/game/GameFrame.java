package ru.beetlerat.game;

import ru.beetlerat.game.score.ScorePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameFrame {
    private JFrame mainFrame; // Отображаемая форма
    private JTabbedPane multiPanel; // Панель с вкладками
    private ImageIcon ratImage;
    private Image backgroundImage;


    GameFrame(){
        // Создать новый контейнер типа JFrame с подписью Игра три в ряд
        mainFrame=new JFrame("Игра три в ряд");
        // Задать размеры формы
        mainFrame.setSize(1200,730);
        mainFrame.setResizable(false);
        // Установить действие при закрытии формы - завершить приложение
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Установить граничный диспетчер компановки
        mainFrame.setLayout(new GridLayout(1,2));

        mainFrame.setBackground(new Color(132,101,179));




        // Добавить панель с вкладками на форму
        JPanel gameAndScore=new JPanel();
        ScorePanel mySorePanel=new ScorePanel();
        gameAndScore.add(new GameFieldPanel(mySorePanel));
        gameAndScore.add(mySorePanel);

        mainFrame.add(gameAndScore);
        ratImage = loadIconImage("Mage.gif");
        if(ratImage !=null){
            JLabel jLabel=new JLabel();
            jLabel.setIcon(ratImage);
            mainFrame.add(jLabel);
        }
        else {
            mainFrame.add(new Label("Тут будет персонаж"));
        }

        // Отобразить форму
        mainFrame.setVisible(true);
    }

    protected ImageIcon loadIconImage(String fileName) {
        ImageIcon img = null;

            File imageFile = null;
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
                img = ImageIO.read(imageFile).getScaledInstance(1200, 730, Image.SCALE_AREA_AVERAGING);
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


}
