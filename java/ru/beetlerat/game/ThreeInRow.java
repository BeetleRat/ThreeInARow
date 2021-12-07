package ru.beetlerat.game;

import javax.swing.*;

public class ThreeInRow {
    public static void main(String args[]){
        // Создать форму в потоке диспетчерезации событий через анонимный класс
        SwingUtilities.invokeLater(new Runnable() {
            // Перегрузка методов интерфейса
            public void run() {
                // Создание формы в парралельном потоке Swing
                new GameFrame();
            }
        });
    }
}
