package ru.beetlerat.game.UI;


import ru.beetlerat.game.balls.BallsGrid;
import ru.beetlerat.game.support.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class GameFieldPanel extends JPanel {

    private BallsGrid ballsGrid; // Таблица шаров

    public GameFieldPanel(ScorePanel scorePanel) {
        // Установить размеры панели
        setPreferredSize(new Dimension(580, 470));
        setOpaque(false);// Сделать фон прозрачным
        // Создать новую таблицу шаров, отрисовываемую на данной панели
        // и использующую scorePanel для вывода очков
        ballsGrid = new BallsGrid(this, scorePanel);
    }

    public void restartGameField() {
        ballsGrid.restartGame();
    }

    // Переопределить метод отрисовки компанента
    protected void paintComponent(Graphics brush) {
        // ОБЯЗАТЕЛЬНО СНАЧАЛА ОТРИСОВЫВАЕМ САМ КОМПОНЕНТ ЧЕРЕЗ МЕТОД СУПЕРКЛАССА
        super.paintComponent(brush);
        // а затем рисуем на отрисованном компоненте

        // Отрисовка игрового поля
        ballsGrid.drawBalls(brush);
    }
}
