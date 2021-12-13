package ru.beetlerat.game.UI.menu;


import javax.swing.*;

public class ApplicationMenu extends JMenuBar {
    // Элементы меню приложения
    private JMenu menu;
    private JMenuItem tutorialItem;
    private JMenuItem exitItem;

    private JFrame mainFrame;
    private Tutorial tutorial;

    public ApplicationMenu(JFrame parent){
        super();
        this.mainFrame=parent;
        createElements();
        addListeners();

        mainFrame.setJMenuBar(this);// Добавить меню к родителю
    }
    private void createElements(){
        menu=new JMenu("Меню");
        tutorialItem =new JMenuItem("Обучение");
        exitItem=new JMenuItem("Выйти");
        menu.add(tutorialItem);
        menu.add(exitItem);
        add(menu);
    }

    private void addListeners(){

        tutorialItem.addActionListener((actionEvent)->{
            tutorial=new Tutorial(mainFrame,"Обучение");
            tutorial.setVisible(true);
        });
        // По нажатию пункта меню завершить приложения
        exitItem.addActionListener((actionEvent)->System.exit(0));
    }


}
