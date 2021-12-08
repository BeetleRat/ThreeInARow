package ru.beetlerat.game.score;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ScorePanel extends JPanel {
    private Image turnsImg;
    private boolean isExists;

    private int score; // Очки
    private int turnsLeft; // Ходы
    private int scoreMultiply; // Множитель
    private int winScore;

    private int barWidth;
    private int barY;

    public ScorePanel() {
        isExists=true;
        winScore=1000;
        score=1000;
        turnsLeft=20;
        scoreMultiply=100;
        turnsImg=loadImage("Bell.png");
        // Установить размеры панели
        setPreferredSize(new Dimension(580,80));
        // Установка границ компонента красными жирностью 5
        //setBorder(BorderFactory.createLineBorder(Color.RED,5));
        barWidth=getPreferredSize().width/4;
        barY = getPreferredSize().height/4;
    }
    // Переопределить метод отрисовки компанента
    protected void paintComponent(Graphics brush){
        // ОБЯЗАТЕЛЬНО СНАЧАЛА ОТРИСОВЫВАЕМ САМ КОМПОНЕНТ ЧЕРЕЗ МЕТОД СУПЕРКЛАССА
        super.paintComponent(brush);
        // а затем рисуем на отрисованном компоненте
        // Отрисовываем счетчик очков
        brush.setColor(Color.BLACK);
        brush.drawRect(35, barY,barWidth,30);
        brush.setColor(Color.GREEN);
        // Подсчитываем на сколько счетчик очков заполнен
        Double percentScore=Math.ceil(((double)score/((double)winScore/100))*((double)(barWidth-1)/100));
        brush.fillRect(36,barY+1,percentScore.intValue(),29);
        brush.setColor(Color.BLACK);
        brush.setFont(new Font("1",Font.BOLD,10));
        brush.drawString(score+"/"+winScore,(35+barWidth)/2,barY+20);

        // Отрисовка оставшихся ходов
        if(turnsImg!=null){
            brush.drawImage(turnsImg,35*2+barWidth+50,barY-10,null);
        }
        brush.drawString(""+turnsLeft,35*2+barWidth+barWidth/2,barY+20);

        // Отрисовка множителя
        brush.drawRect(35*4+barWidth*2, barY-10,barWidth/4,55);
        brush.setColor(Color.PINK);
        // Подсчитываем на сколько счетчик очков заполнен
        Double percentMultiply=Math.ceil(scoreMultiply*((double)(55-1)/100));
        brush.fillRect(35*4+barWidth*2+1, barY+45-percentMultiply.intValue(),barWidth/4-1,percentMultiply.intValue());
        brush.setColor(Color.BLACK);
        brush.setFont(new Font("1",Font.BOLD,10));
        brush.drawString(scoreMultiply+"%",35*4+barWidth*2+10, barY+20);
    }

    public void setExists(boolean exists) {
        isExists = exists;
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
                img = ImageIO.read(imageFile).getScaledInstance(55, 55, Image.SCALE_AREA_AVERAGING);
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

    //Сеттеры
    public void setScore(int score) {
        this.score = score;
        repaint();
    }
    public void setTurnsLeft(int turnsLeft) {
        this.turnsLeft = turnsLeft;
        repaint();
    }
    public void setScoreMultiply(int scoreMultiply) {
        this.scoreMultiply = scoreMultiply;
        repaint();
    }

    public int getWinScore() {
        return winScore;
    }
}
