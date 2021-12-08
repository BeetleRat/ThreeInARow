package ru.beetlerat.game;



import ru.beetlerat.game.balls.BallsGrid;
import ru.beetlerat.game.score.ScorePanel;

import javax.swing.*;
import java.awt.*;

public class GameFieldPanel extends JPanel {
    private final int GAME_ON=0;
    private final int YOU_LOSE=-1;
    private final int YOU_WIN=1;

    private BallsGrid ballsGrid;
    private ScorePanel scorePanel;
    //private JButton restartButton;

    private int drawState;


    public GameFieldPanel(ScorePanel scorePanel){
        // Установить размеры панели
        setPreferredSize(new Dimension(580,600));
        this.scorePanel=scorePanel;
        /*createElements();
        addListeners();*/
        newGame();
    }
/*
    private void createElements(){
        restartButton=new JButton("Начать сначала");
    }
    private void addListeners(){
        // По нажатию на кнопку начать новую игру
        restartButton.addActionListener((actionEvent)->newGame());
    }*/

    // Переопределить метод отрисовки компанента
    protected void paintComponent(Graphics brush){
        // ОБЯЗАТЕЛЬНО СНАЧАЛА ОТРИСОВЫВАЕМ САМ КОМПОНЕНТ ЧЕРЕЗ МЕТОД СУПЕРКЛАССА
        super.paintComponent(brush);
        // а затем рисуем на отрисованном компоненте

        Font currentFont=brush.getFont();
        switch (drawState){
            case GAME_ON:
                // Отрисовка игрового поля
                ballsGrid.drawBalls(brush);
                if(scorePanel!=null){
                    if(ballsGrid.getTurnsLeft()==0){
                        setDrawState(YOU_LOSE);
                    }else {
                        if(ballsGrid.getScore()>=scorePanel.getWinScore()){
                            setDrawState(YOU_WIN);
                        }
                    }
                }
                break;
            case YOU_LOSE:
                brush.setFont(new Font("1",Font.BOLD,20));
                brush.drawString("Вы проиграли",300,300);
                //add(restartButton);
                break;
            case YOU_WIN:
                brush.setFont(new Font("1",Font.BOLD,20));
                brush.drawString("Вы победили",300,300);
                //add(restartButton);
                break;
        }
        brush.setFont(currentFont);
    }

    private void newGame() {
        drawState=GAME_ON;
        ballsGrid =new BallsGrid(this,scorePanel);
    }

    public void setDrawState(int drawState) {
        this.drawState = drawState;
        //removeAll();
        repaint();
    }
}
