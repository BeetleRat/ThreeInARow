package ru.beetlerat.game.UI.menu;

import ru.beetlerat.game.support.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Tutorial extends JDialog {
    private JLabel titleLabel;
    private JLabel imageLabel;
    private JTextArea textArea;
    private JButton nextButton;
    private JButton previousButton;

    private List<String> titleText;
    private List<ImageIcon> imageList;
    private List<String> tutorialText;

    private final int totalPage;
    private int currentPage;
    private ImageLoader imageLoader;

    public Tutorial(JFrame parent, String title) {
        // Создание диалогового окна от родительского окна parent
        // с заголовком title
        // созданное окно запрещает взаимодействие с основным
        super(parent, title, true); // Вызов конструктора суперкласса
        imageLoader = new ImageLoader();
        // Установить действие при закрытии формы - сркыть окно
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Установить размеры диалогового окна
        setSize(new Dimension(600, 600));
        imageLoader = new ImageLoader();
        // Установить диспетчер компановки
        setLayout(new FlowLayout());
        setResizable(false);// Запретить изменять размер окна

        totalPage = 11;
        currentPage = 1;

        createTutorialLists();
        createElements();
        addListeners();
        addElementsToPanel();
    }

    private void createTutorialLists() {
        tutorialText = new LinkedList<>();
        imageList = new LinkedList<>();
        titleText = new LinkedList<>();

        titleText.add("Обучение основам игры. ");
        imageList.add(imageLoader.loadIconImage("tutor1.png"));
        tutorialText.add("Здравствуй дорогой игрок.\n" +
                "Раз ты зашел в этот раздел, то вероятно тебе интересно узнать, как же в это играть.\n" +
                "Сейчас расскажу.\n" +
                "Для перехода по страничкам обучения используй кнопки \"Дальше\" и \"Назад\".");

        titleText.add("Игровое поле. ");
        imageList.add(imageLoader.loadIconImage("tutor2.png"));
        tutorialText.add("И так. Игра происходит на игровом поле. Оно выделенно красным на картинке.");

        titleText.add("Взаимодействие с шариками. ");
        imageList.add(imageLoader.loadIconImage("tutor3.png"));
        tutorialText.add("Для взаимодействия с шариками возьми один из них зажав левую кнопку мыши, и перетащи.");

        titleText.add("Правила перетаскивания шарика. ");
        imageList.add(imageLoader.loadIconImage("tutor4.png"));
        tutorialText.add("Шарик можно перетаскивать на любое количество клеток по вертикали\n" +
                "ИЛИ на любое количество клеток по горизонтали.\n" +
                "По диагонали перетаскивать шарик нельзя.");

        titleText.add("Конец хода и получение очков. ");
        imageList.add(imageLoader.loadIconImage("tutor5.png"));
        tutorialText.add("Шарики перетаскиваются с целью собрать комбинацию из хотя бы трех шариков одного типа.\n" +
                "Когда вы перетаскивая шарик собираете комбинацию, то шарики в комбинации подсвечиваются.\n" +
                "В таком случае вы можете отпустить левую кнопку мыши\n" +
                "и подсвеченые шарики исчезнут, а вы получите очки за комбинацию.\n" +
                "Чем больше шариков в комбинации, тем больше очков вы получите.\n" +
                "Не забывайте, что убрав шарики поле придет в движение.\n" +
                "Шарики сверху упадут вниз и при падении так же могут составить комбинацию,\n" +
                "которая так же вам засчитается.");

        titleText.add("Типы шариков ");
        imageList.add(imageLoader.loadIconImage("tutor6.png"));
        tutorialText.add("Есть несколько типов шариков.\n" +
                "Отличаются они результатом, который вы получите собрав комбинацию из таких шариков.\n" +
                "Синий, ораньжевый и зеленый шарики - обычные. Собираете комбинацию получаете очки.\n" +
                "Розовые шарики дают мало очков, но зато увеличивают общий множитель очков.\n" +
                "Желтые шарики совсем не дают очков,\n" +
                "но зато увеличивают количество ходов оставшихся до конца игры.\n" +
                "Черные шарики - это плохие шарики. Они уменьшают ваши очки.\n" +
                "Чем большую комбинацию черных шариков вы соберете,\n" +
                "тем больше очков у вас отнимется.\n" +
                "Старайтесь не собирать комбинации этих шариков.");

        titleText.add("Панель очков. ");
        imageList.add(imageLoader.loadIconImage("tutor7.png"));
        tutorialText.add("На панели очков отображается ваш прогресс.\n" +
                "Здесь вы можете видеть, сколько очков вы набрали и сколько еще осталось.\n" +
                "Так же тут показывается количество оставшихся ходов,\n" +
                "множитель и какой шар в данной игре усиленный, а какой ослабленный." );

        titleText.add("Правила победы. ");
        imageList.add(imageLoader.loadIconImage("tutor8.png"));
        tutorialText.add("Для победы необходимо набрать 1000 очков.\n" +
                "Собирайте большие комбинации, чтобы получить больше очков.");

        titleText.add("Правила поражения. ");
        imageList.add(imageLoader.loadIconImage("tutor9.png"));
        tutorialText.add("В начале игры у вас есть 20 ходов.\n" +
                "После каждого перетаскивания шара, при котором собралась комбинация у вас отнимается 1 ход.\n" +
                "Если шары упали сами и собрались в комбинацию ход не отнимается.\n" +
                "Так же если вы перетащили шар, но отпустили его не собрав комбинацию,\n" +
                "ход тоже не засчитывается\n" +
                "Что бы увеличить количество ходов собирайте желтые шарики.\n" +
                "Когда количество ходов дойдет до нуля - вы проиграете.");

        titleText.add("Множитель. ");
        imageList.add(imageLoader.loadIconImage("tutor10.png"));
        tutorialText.add("Изначально множителя нет.\n" +
                "Если вы будете собирать комбинации из розовых шариков, множитель начнет увеличиваться.\n" +
                "При максимальном множителе 100%\nвсе собранные вами комбинации приносят в 2 раза больше очков.\n" +
                "Это не касается особых шаров.");

        titleText.add("Слабый и сильный шар. ");
        imageList.add(imageLoader.loadIconImage("tutor11.png"));
        tutorialText.add("В начале каждой игры определяются ослабленный и усиленный шары.\n" +
                "Ослабленный шар приносит в 2 раза меньше очков, за собранную комбинацию.\n" +
                "В то время как усиленный шар приносит в 2 раза больше очков за собранную комбинацию.");
    }

    private void createElements() {
        titleLabel = new JLabel(titleText.get(currentPage - 1) + currentPage + "/" + totalPage);
        imageLabel = new JLabel(imageList.get(currentPage - 1));
        textArea = new JTextArea(tutorialText.get(currentPage - 1));

        // Настройка textArea
        textArea.setFont(new Font(Font.DIALOG,Font.BOLD,12)); // Устанавливаем шрифт
        textArea.setDisabledTextColor(Color.BLACK); // Цвет текста
        textArea.setOpaque(false); // Делаем фон прозрачным
        // Запрещаем пользователю взаимодействие с textArea
        textArea.setEnabled(false);
        textArea.setEditable(false);

        nextButton = new JButton("Дальше");
        previousButton = new JButton("Назад");
        previousButton.setEnabled(false);
    }

    private void addElementsToPanel() {
        // Создаем панель для текста обучения
        JPanel textPanel = new JPanel();
        // Текст может быть разных размеров,
        // что бы при изменении размера текста обучения не летел диспетчер компановки
        // создаем панель фиксированного размера, отображающую текст обучения
        textPanel.setPreferredSize(new Dimension(600, 170));
        textPanel.add(textArea);
        // Создаем панель кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(previousButton);
        buttonPanel.add(new JLabel("     "));
        buttonPanel.add(nextButton);

        // Добавляем элементы на диалоговое окно
        add(titleLabel);
        add(imageLabel);
        add(textPanel);
        add(buttonPanel);
    }

    private void addListeners() {
        // Добавить слушателя нажатия на кнпку nextButton
        nextButton.addActionListener((actionEvent) -> {
            if (currentPage == totalPage) {
                dispose(); // Закрыть окно
            } else {
                currentPage++;
                if (currentPage > 1) {
                    previousButton.setEnabled(true);
                }
                if (currentPage == totalPage) {
                    nextButton.setText("Понятно");
                }
                refreshPage();
            }
        });

        // Добавить слушателя нажатия на кнпку previousButton
        previousButton.addActionListener((actionEvent) -> {
            currentPage--;
            if (currentPage == 1) {
                previousButton.setEnabled(false);
            }
            if (currentPage < totalPage) {
                nextButton.setText("Дальше");
                refreshPage();
            }
        });
    }

    private void refreshPage() {
        // Обновить заголовок, согласно текущей странице
        titleLabel.setText(titleText.get(currentPage - 1) + currentPage + "/" + totalPage);
        // Обновить картинку, согласно текущей странице
        imageLabel.setIcon(imageList.get(currentPage - 1));
        // Обновить текст обучения, согласно текущей странице
        textArea.setText(tutorialText.get(currentPage - 1));
    }
}
