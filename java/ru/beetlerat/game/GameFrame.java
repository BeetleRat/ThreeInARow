package ru.beetlerat.game;

import javax.swing.*;
import java.awt.*;

public class GameFrame {
    private JFrame mainFrame; // Отображаемая форма
    private JTabbedPane multiPanel; // Панель с вкладками

    GameFrame(){
        // Создать новый контейнер типа JFrame с подписью Игра три в ряд
        mainFrame=new JFrame("Игра три в ряд");
        // Задать размеры формы
        mainFrame.setSize(500,500);
        // Установить действие при закрытии формы - завершить приложение
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Установить граничный диспетчер компановки
        mainFrame.setLayout(new FlowLayout());

        // Создать панель с вкладками
        multiPanel=new JTabbedPane();
        // Установить размеры панели с вкладками
        multiPanel.setPreferredSize(new Dimension(400,430));
        // Заполнить панель с вкладками
        fillTabbedPane();

        // Добавить панель с вкладками на форму
        mainFrame.add(multiPanel,BorderLayout.CENTER);
        mainFrame.add(new Label("Тут будет панель инструментов"));
        mainFrame.add(new Label("Тут будет счет и бонусы"));
        mainFrame.add(new Label("Тут будет персонаж"));
        mainFrame.add(new Label("Тут я еще не придумал"));
        // Отобразить форму
        mainFrame.setVisible(true);
    }

    // Заполнить панель с вкладками
    private void fillTabbedPane() {
        // На каждую вкладку TabbedPane можно занести только 1 элемент
        // Что бы обойти это ограничение
        // мы будем добавлять туда класс панели с элементами JPanel
        // Можно просто создать объект класса JPanel,
        // настроить его, забить элементами и добавить в JTabbedPane
        // Но с точки зрения читаемости лучше для каждой панели создать свой класс,
        // и внутри этого класса создать, настроить и добавить элементы на панель

        // Добавляем палень игрового поля на прокручиваемую панель
        JScrollPane scrollGameFieldPanel=new JScrollPane(new GameFieldPanel());
        // Задать размер прокручиваемой панели
        scrollGameFieldPanel.setPreferredSize(new Dimension(380,400));
        multiPanel.addTab("Game field",scrollGameFieldPanel);

    }
}
