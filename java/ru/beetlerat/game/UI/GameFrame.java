package ru.beetlerat.game.UI;

import ru.beetlerat.game.UI.menu.ApplicationMenu;
import ru.beetlerat.game.support.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class GameFrame {
    private JFrame mainFrame; // Отображаемая форма
    private ImageIcon ratImage; // Изображение крысы
    private JPanel gameAndScore;
    private WinLoseGamePanel winLoseGamePanel;
    private ScorePanel scorePanel;

    public GameFrame() {
        // Создать новый контейнер типа JFrame с подписью Игра три в ряд
        mainFrame = new JFrame("Игра три в ряд");
        // Задать размеры формы
        mainFrame.setSize(1200, 730);
        mainFrame.setResizable(false); // Запретить пользователю менять размеры
        // Установка заднего фона
        mainFrame.setContentPane(new JLabel(new ImageLoader().loadIconImage("BG.png")));
        // Установить действие при закрытии формы - завершить приложение
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Установить сеточный диспетчер компановки 1 строка 2 столбца
        mainFrame.setLayout(new GridLayout(1, 2));
        // Создать меню
        new ApplicationMenu(mainFrame);

        // Упаковываем WinLoseGamePanel и SorePanel в панель gameAndScore
        gameAndScore = new JPanel();
        gameAndScore.setOpaque(false);// Сделать фон прозрачным
        scorePanel = new ScorePanel();
        winLoseGamePanel=new WinLoseGamePanel(scorePanel);
        gameAndScore.add(winLoseGamePanel);
        gameAndScore.add(scorePanel);
        // Добавляем панель gameAndScore на форму в 1 строку 1 столбец
        mainFrame.add(gameAndScore);

        // Загружаем изображение крысы
        ratImage = new ImageLoader().loadIconImage("Mage.gif");
        // Добавляем изображение крысы на форму в 1 строку 2 столбец
        if (ratImage != null) {
            JLabel jLabel = new JLabel();
            jLabel.setIcon(ratImage);
            mainFrame.add(jLabel);
        } else {
            mainFrame.add(new Label("Тут будет персонаж"));
        }

        // Отобразить форму
        mainFrame.setVisible(true);
    }
}
