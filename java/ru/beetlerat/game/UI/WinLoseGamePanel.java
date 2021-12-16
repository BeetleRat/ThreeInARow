package ru.beetlerat.game.UI;

import ru.beetlerat.game.support.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class WinLoseGamePanel extends JPanel implements Runnable {
    // Константы
    private final int GAME_ON = 0;
    private final int YOU_WIN = 1;
    private final int YOU_LOSE = -1;

    // Потоковые переменные
    private Thread thread;
    private boolean isExists;

    // Состояние панели.
    // Во время изменения состояния поток должен среагировать на это изменение.
    // По этому сначала изменяется newState, поток на это реагирует, и после реакции изменяет oldState
    private int oldState;
    private int newState;

    private ImageLoader imageLoader;
    private ImageIcon winImage;
    private ImageIcon loseImage;
    private ImageIcon gameName;
    private ImageIcon restartImage;
    private ImageIcon exitImage;

    // Объекты панели
    private GameFieldPanel gameFieldPanel;
    private ScorePanel scorePanel;
    private JPanel restartPanel;
    private JLabel announceLabel;
    private JButton restartButton;
    private JButton exitButton;
    // Карточный диспетчер компановки
    private CardLayout windows;

    public WinLoseGamePanel(ScorePanel scorePanel) {
        // Создаем новый пралельный поток "Win Lose Game Thread"
        thread = new Thread(this, "Win Lose Game Thread");
        // Установить размеры панели
        setPreferredSize(new Dimension(600, 500));
        setSize(new Dimension(600, 500));
        setOpaque(false); // Сделать фон прозрачным
        // Создаем новый диспетчер карточной компановки
        windows = new CardLayout();
        setLayout(windows);

        // Сохраняем входной параметр в переменную класса
        this.scorePanel = scorePanel;
        // Создаем игровое поле используя входной параметр
        this.gameFieldPanel = new GameFieldPanel(scorePanel);
        // Загружаем нужные изображения
        imageLoader = new ImageLoader();
        winImage = imageLoader.loadIconImage("Win.png");
        loseImage = imageLoader.loadIconImage("Lose.png");
        gameName = imageLoader.loadIconImage("gameName.png");
        exitImage=imageLoader.loadIconImage("exitButton.png");
        restartImage =imageLoader.loadIconImage("restartButton.png");

        createElements();
        addListeners();
        addElementsToPanel();

        // Переключиться на карту конца игры(изначально restartPanel выглядит как главное меню)
        endGame();
        // Устанавливаем состояния вывода карты Restart panel
        newState = YOU_WIN;
        oldState = newState;

        isExists = true;
        // Запустить поток на исполнение
        thread.start();
    }

    private void createElements() {
        announceLabel = new JLabel(gameName);
        announceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        announceLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        restartButton = new JButton(restartImage);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        restartButton.setPreferredSize(new Dimension(550,54));
        restartButton.setBackground(new Color(0,0,0,0));
        restartButton.setBorderPainted(false);

        exitButton = new JButton(exitImage);
        exitButton.setBackground(new Color(0,0,0,0));
        exitButton.setBorderPainted(false);

        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        exitButton.setPreferredSize(new Dimension(550,54));


        // Создать панель restartPanel
        restartPanel = new JPanel();
        // Установить вертикальный диспетчер компановки

        restartPanel.setLayout(new FlowLayout());
        restartPanel.setOpaque(false);// Сделать фон прозрачным

        // Упоковать элементы в restartPanel разделяя их пустыми JLabel
        restartPanel.add(announceLabel);
        restartPanel.add(restartButton);
        restartPanel.add(new JLabel("                                                                                                                                                                       "));
        restartPanel.add(exitButton);
    }

    private void addListeners() {
        // Добавить слушателя нажатия кнопки restartButton
        restartButton.addActionListener((actionEvent) -> {
                    // По нажатию кнопки
                    gameFieldPanel.restartGameField(); // Перезагрузить игровое поле
                    newGame(); // Переключиться на карту новой игры
                }
        );
        // Добавить слушателя нажатия кнопки exitButton
        // по нажатию кнопки завершить программу
        exitButton.addActionListener((actionEvent) -> System.exit(0));
    }

    private void addElementsToPanel() {
        // Добавляем restartPanel на карту с названием Restart panel
        add(restartPanel, "Restart panel");
        // Добавляем игровое поле на карту с названием Game panel
        add(gameFieldPanel, "Game panel");
    }

    // Переключиться на карту новой игры
    private void newGame() {
        // Отображать приоритетные шары на панели счета
        scorePanel.setPriorityVisible(true);
        windows.show(this, "Game panel");// Переключиться на карту Game panel
        newState = GAME_ON;
    }

    // Переключиться на карту выйгрыш/проигрыш/меню
    private void endGame() {
        // Не отображать приоритетные шары
        scorePanel.setPriorityVisible(false);
        windows.show(this, "Restart panel");// Переключиться на карту Restart panel
    }

    // Метод потока
    @Override
    public void run() {
        while (isExists) {
            // Опрашивать состояние каждую милисекунду
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (scorePanel != null) {
                // Если закончились ходы
                if (scorePanel.getTurnsLeft() <= 0) {
                    newState = YOU_LOSE;
                }
                // Если счет стал больше победного счета
                if (scorePanel.getCurrentScore() >= scorePanel.getWinScore()) {
                    newState = YOU_WIN;
                }
            }

            // Если изменилось состояние панели
            if (newState != oldState) {
                switch (newState) {
                    case GAME_ON:
                        newGame(); // Переключиться на карту новой игры
                        oldState = GAME_ON;
                        break;
                    case YOU_WIN:
                        // Установить в announceLabel победное изображение
                        announceLabel.setIcon(winImage);
                        endGame();// Переключиться на карту выйгрыш
                        oldState = YOU_WIN;
                        break;
                    case YOU_LOSE:
                        // Установить в announceLabel изображение проигрыша
                        announceLabel.setIcon(loseImage);
                        endGame();// Переключиться на карту проигрыш
                        oldState = YOU_LOSE;
                        break;
                }
            }
        }
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }
}
