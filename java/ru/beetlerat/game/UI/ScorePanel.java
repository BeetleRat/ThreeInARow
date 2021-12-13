package ru.beetlerat.game.UI;

import ru.beetlerat.game.balls.BallView;
import ru.beetlerat.game.support.ImageLoader;

import javax.swing.*;
import java.awt.*;


public class ScorePanel extends JPanel implements Runnable {
    // Переменные паралельного потока
    private Thread thread;
    private boolean isExists;

    private Image turnsImg; // Изображение колокольчика
    private Image highPriorityBall; // Изображение шара с высоким приоритетом
    private Image lowPriorityBall; // Изображение шара с низким приоритетом
    private ImageLoader imageLoader;

    // Отображать ли изображения приоритетных шаров
    private boolean isPriorityVisible;

    private int score; // Очки
    private int turnsLeft; // Ходы
    private int scoreMultiply; // Множитель
    private final int winScore;

    private final int scoreMultiplyBarHeight;

    private Double fillScoreBar;
    private Double currentScoreBar;
    private int currentScore;

    private Double fillScoreMultiplyBar;
    private Double currentScoreMultiplyBar;
    private int currentScoreMultiply;

    private final int scoreBarWidth;
    private final int barY;


    public ScorePanel() {
        // Создаем новый паралельный поток "Score thread"
        thread = new Thread(this, "Score thread");
        // Установить размеры панели
        setPreferredSize(new Dimension(580, 200));
        setOpaque(false);// Сделать фон прозрачным

        imageLoader = new ImageLoader();
        winScore = 1000;
        setScore(1000);
        setScoreMultiply(100);
        currentScoreBar = 0.0;
        currentScoreMultiplyBar = 0.0;
        currentScore = 0;
        currentScoreMultiply = 0;
        turnsLeft = 20;

        turnsImg = imageLoader.loadImage("Bell.png", 60, 60);
        highPriorityBall = imageLoader.loadImage("yellowBallDark.png", 60, 60);
        lowPriorityBall = imageLoader.loadImage("blackBallDark.png", 60, 60);

        isExists = true;
        isPriorityVisible = true;

        scoreBarWidth = getPreferredSize().width / 4;
        barY = getPreferredSize().height / 3;
        scoreMultiplyBarHeight = 55;

        // Запускаем на исполнение поток "Score thread"
        thread.start();
    }

    // Переопределить метод отрисовки компанента
    protected void paintComponent(Graphics brush) {
        // ОБЯЗАТЕЛЬНО СНАЧАЛА ОТРИСОВЫВАЕМ САМ КОМПОНЕНТ ЧЕРЕЗ МЕТОД СУПЕРКЛАССА
        super.paintComponent(brush);
        // а затем рисуем на отрисованном компоненте

        Font brushFont=brush.getFont(); // Сохраняем исходный шрифт кисти
        // Устанавливаем свой шрифт
        brush.setFont(new Font(Font.SERIF, Font.BOLD, 10));

        // Отрисовываем счетчик очков
        // контур
        brush.setColor(Color.BLACK);
        brush.drawRect(35, barY, scoreBarWidth, 30);
        // заполненность
        brush.setColor(Color.GREEN);
        brush.fillRect(35 + 1, barY + 1, currentScoreBar.intValue(), 30 - 1);
        // цифры
        brush.setColor(Color.BLACK);
        brush.drawString(currentScore + "/" + winScore, (35 + scoreBarWidth) / 2, barY + 20);

        // Отрисовка оставшихся ходов
        if (turnsImg != null) {
            brush.drawImage(turnsImg, 230, barY - 10, null);
        }
        brush.drawString("" + turnsLeft, 255, barY + 20);

        // Отрисовка множителя
        // контур
        brush.drawRect(350, barY - 10, 40, scoreMultiplyBarHeight);
        // заполненность
        brush.setColor(Color.PINK);
        brush.fillRect(350 + 1, barY + 45 - currentScoreMultiplyBar.intValue(), 40 - 1, currentScoreMultiplyBar.intValue());
        // цифры
        brush.setColor(Color.BLACK);
        brush.drawString(currentScoreMultiply + "%", 365, barY + 20);

        // Отрисовка приоритетных шаров
        if (isPriorityVisible) {
            brush.drawImage(lowPriorityBall, 450, barY - 20, null);
            brush.drawImage(highPriorityBall, 480, barY - 20, null);
        }

        brush.setFont(brushFont);// Восстанавливаем исходный шрифт кисти
    }

    //Сеттеры
    public void setScore(int score) {
        this.score = score;
        this.fillScoreBar = Math.ceil(((double) score / ((double) winScore / 100)) * ((double) (scoreBarWidth - 1) / 100));
        repaint();
    }

    public void setTurnsLeft(int turnsLeft) {
        this.turnsLeft = turnsLeft;
        repaint();
    }

    public void setScoreMultiply(int scoreMultiply) {
        this.scoreMultiply = scoreMultiply;
        this.fillScoreMultiplyBar = Math.ceil(scoreMultiply * ((double) (55 - 1) / 100));
        repaint();
    }

    public void setPriorityVisible(boolean priorityVisible) {
        isPriorityVisible = priorityVisible;
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }

    public void setPriorityBalls(int highPriorityType, int lowPriorityType) {
        if (highPriorityType == lowPriorityType) {
            System.out.println("Ошибка приоритетных типов");
        } else {
            highPriorityBall = setPriorityBallImage(highPriorityType);
            lowPriorityBall = setPriorityBallImage(lowPriorityType);
        }
    }

    // Геттеры
    public int getWinScore() {
        return winScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getTurnsLeft() {
        return turnsLeft;
    }

    // Метод потока
    @Override
    public void run() {
        while (isExists) {
            // Ожидание 10 милисекунд
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Ошибка потока: " + e);
            }
            // Изменение на 1 всех показателей панели
            if (currentScoreBar < fillScoreBar) {
                currentScoreBar++;
            } else {
                if (currentScoreBar > fillScoreBar) {
                    currentScoreBar--;
                }
            }
            if (currentScoreMultiplyBar < fillScoreMultiplyBar) {
                currentScoreMultiplyBar++;
            } else {
                if (currentScoreMultiplyBar > fillScoreMultiplyBar) {
                    currentScoreMultiplyBar--;
                }
            }
            if (currentScore < score) {
                currentScore++;
            }
            if (currentScore > score) {
                currentScore--;
            }
            if (currentScoreMultiply > scoreMultiply) {
                currentScoreMultiply--;
            }
            if (currentScoreMultiply < scoreMultiply) {
                currentScoreMultiply++;
            }
            repaint();
        }
    }

    private Image setPriorityBallImage(int type) {
        switch (type) {
            case BallView.BLUE_BALL:
                return imageLoader.loadImage("blueBallDark.png", 60, 60);
            case BallView.ORANGE_BALL:
                return imageLoader.loadImage("orangeBallDark.png", 60, 60);
            case BallView.GREEN_BALL:
                return imageLoader.loadImage("greenBallDark.png", 60, 60);
        }
        return imageLoader.loadImage("blackBallDark.png", 60, 60);
    }
}
