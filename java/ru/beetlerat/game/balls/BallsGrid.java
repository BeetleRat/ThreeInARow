package ru.beetlerat.game.balls;

import ru.beetlerat.game.support.BallsStack;
import ru.beetlerat.game.UI.ScorePanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// Класс отрисовки таблицы шаров
public class BallsGrid {

    private final JComponent canvas; // Панель, на которой будет производится отрисовка
    private final ScorePanel scorePanel; // Панель вывода очков
    private BallMovement ballsArray[][]; // Таблица шаров
    // Размеры таблицы шаров
    private final int rowTotal;
    private final int columnTotal;
    // Стэк запоминающий перестановку шаров в пределах одного хода
    private BallsStack reverseBallsStack;
    // Отображение хранящее в себе удаляемые комбинации
    private Map<Integer, List<BallMovement>> removeBalls;
    // Перетаскиваемый шар отрисовывается поверх других
    private BallMovement draggedBall;
    // Количество шаров стоящих на своих местах
    private int standBalls;

    private boolean isFieldAvailable;
    private boolean isEndTurnPhase;
    // Была ли собрана комбинация за этот ход
    private boolean isCorrectMove;

    private int score; // Очки
    private int turnsLeft; // Ходы
    private Double scoreMultiply; // Множитель

    private int highPriorityType; // Тип шара за который даются удвоенные очки
    private int lowPriorityType; // Тип шара за который даются уменшьшенные в 2 раза очки

    public BallsGrid(JPanel canvas, ScorePanel scorePanel) {
        // Сохраняем панель отрисовки
        this.canvas = canvas;
        // Сохраняем панель вывода очков
        this.scorePanel = scorePanel;
        this.reverseBallsStack = new BallsStack();
        this.removeBalls = new LinkedHashMap<>();
        // Устанавливаем размеры таблицы шаров
        this.rowTotal = 7;
        this.columnTotal = 9;
        // Создаем таблицу шаров
        ballsArray = new BallMovement[rowTotal][];
        for (int row = 0; row < rowTotal; row++) {
            ballsArray[row] = new BallMovement[columnTotal];
        }
        // Заполняем таблицу шарами
        startNewGame();
    }

    public void restartGame() {
        // Удаляем предыдущие шары
        for (int row = rowTotal - 1; row >= 0; row--) {
            for (int column = columnTotal - 1; column >= 0; column--) {
                ballsArray[row][column].setExists(false);
                ballsArray[row][column] = null;
            }
        }
        // Заного заполняем таблицу
        startNewGame();
    }

    private void startNewGame() {
        // Заполняем таблицу случайными шарами
        for (int row = 0; row < rowTotal; row++) {
            for (int column = 0; column < columnTotal; column++) {
                createRandomBall(row, column);
            }
        }
        // Устанавлива соседей для всех шаров из таблицы
        for (int row = 0; row < rowTotal; row++) {
            for (int column = 0; column < columnTotal; column++) {
                setNeighborsBalls(row, column);
            }
        }
        // Устанавливаем шар отрисовывающийся поверх других
        draggedBall = ballsArray[rowTotal - 1][columnTotal - 1];
        // Заного устанавливаем параметры счета
        standBalls = rowTotal * columnTotal; // Количество доступных шаров
        isFieldAvailable = true;
        isEndTurnPhase = false;
        score = 0;
        turnsLeft = 20;
        scoreMultiply = 1.0;
        isCorrectMove = false;
        // Выбираем шар с высоким и низким приоритетом
        Random randomInt = new Random(System.currentTimeMillis());
        int ballType = randomInt.nextInt(3) + 1;
        highPriorityType = ballType;
        while (ballType == highPriorityType) {
            ballType = randomInt.nextInt(3) + 1;
        }
        lowPriorityType = ballType;
        // Установить начальные значения на ScorePanel
        refreshScorePanel();
    }


    public void drawBalls(Graphics brush) {
        // Отрисовать каждый шар из таблицы шаров
        for (int row = rowTotal - 1; row >= 0; row--) {
            for (int column = columnTotal - 1; column >= 0; column--) {
                if (ballsArray[row][column] != null) {
                    ballsArray[row][column].paintBall(brush);
                }
            }
        }
        draggedBall.paintBall(brush); // Отрисовать перетаскиваемый шар поверх других
    }

    // Проверить доступно ли игровое поле для взаимодействия
    public synchronized void checkAvailable() {
        // Если все шары в сетке находятся на своих местах
        if (standBalls == columnTotal * rowTotal) {
            // Они только что туда пришли
            if (!isFieldAvailable) {
                // Разрешить пользователю использовать поле
                isFieldAvailable = true;
            }
            if (isEndTurnPhase) {
                checkCombinations(); // Проверить поле на комбинации
                endTurn();// Закончить ход
            }
        }
        // Если не все элементы на своих местах
        else {
            // Запретить пользователю изменять положение элементов
            isFieldAvailable = false;
        }
    }

    // Проверка комбинаций во всей табице шаров
    private void checkCombinations() {
        int row = rowTotal - 1;
        boolean isBallInCombination;
        while (row >= 0) {
            int column = columnTotal - 1;
            while (column >= 0) {
                isBallInCombination = false;
                // Получить множество из отображений
                Set<Map.Entry<Integer, List<BallMovement>>> set = removeBalls.entrySet();
                // Прогоняем полученное множество через for each
                for (Map.Entry<Integer, List<BallMovement>> SetIt : set) {
                    if (!isBallInCombination) {
                        for (BallMovement ballMovement : SetIt.getValue()) {
                            if (ballMovement.getColumn() == column && ballMovement.getRow() == row) {
                                isBallInCombination = true;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                // Если шара нет в списке комбинаций на удаление
                if (!isBallInCombination) {
                    checkCombinationForBall(row, column, false);
                }
                column--;
            }
            row--;
        }

    }

    public boolean checkCombinationForBall(int row, int column, boolean isChangedElement) {
        int combinationID = -1;
        if (!isChangedElement) { // Если проверяется комбинация не для элемента перетаскиваемого мышкой
            combinationID = removeBalls.size();
        }
        List<BallMovement> combination = new ArrayList<>();
        BallMovement currentBallMovement = ballsArray[row][column];

        // Если собралась комбинация из трех шаров подряд по столбцу
        if (columnStreak(row, column) > 2) {
            // Собираем комбинацию по столбцу
            int currentRow = row - 1;
            // Поиск одинаковых элементов снизу от текущего
            while (isThisABallOfThisType(currentBallMovement.getType(), currentRow, column)) {
                if (!ballsArray[currentRow][column].getBright()) {
                    combination.add(ballsArray[currentRow][column]);
                    ballsArray[currentRow][column].setBright(true);
                }
                currentRow--; // Шаг вниз
            }
            // Поиск одинаковых элементов сверху от текущего
            currentRow = row + 1;
            while (isThisABallOfThisType(currentBallMovement.getType(), currentRow, column)) {
                if (!ballsArray[currentRow][column].getBright()) {
                    combination.add(ballsArray[currentRow][column]);
                    ballsArray[currentRow][column].setBright(true);
                }
                currentRow++; // Шаг вверх
            }
        }

        // Если собралась комбинация из трех шаров подряд по строчке
        if (rowStreak(row, column) > 2) {
            // Собираем комбинацию по строке
            int currentColumn = column - 1;
            // Поиск одинаковых элементов слева от текущего
            while (isThisABallOfThisType(currentBallMovement.getType(), row, currentColumn)) {
                if (!ballsArray[row][currentColumn].getBright()) {
                    combination.add(ballsArray[row][currentColumn]);
                    ballsArray[row][currentColumn].setBright(true);
                }
                currentColumn--; // Шаг влево
            }
            // Поиск одинаковых элементов справа от текущего
            currentColumn = column + 1;
            while (isThisABallOfThisType(currentBallMovement.getType(), row, currentColumn)) {
                if (!ballsArray[row][currentColumn].getBright()) {
                    combination.add(ballsArray[row][currentColumn]);
                    ballsArray[row][currentColumn].setBright(true);
                }
                currentColumn++; // Шаг вправо
            }
        }
        if (combination.size() > 0) {
            // Добавляем к комбинации исходный элемент
            combination.add(ballsArray[row][column]);
            // Подсвечиваем его
            ballsArray[row][column].setBright(true);
            // Добавляем комбинацию в список комбинаций на удаление
            removeBalls.put(combinationID, combination);
            return true;
        }
        // Удалаем -1 элемент, если  собирали комбинацию с шариком, перетаскиваемым мышкой
        removeBalls.remove(combinationID);
        return false;

    }

    public void endTurn() {
        if (removeBalls.size() > 0) {
            isCorrectMove = true;
            destroyBalls();
            while (reverseBallsStack.getSize() > 0) {
                reverseBallsStack.pop();
            }
            // Запретить игроку перемещать шары, пока поле движется
            isFieldAvailable = false;
        } else {
            reverseSwap();
            isEndTurnPhase = false;
            // Если в этом ходу была собрана комбинация
            if (isCorrectMove) {
                turnsLeft--; // Засчитать ход
                refreshScorePanel();
                isCorrectMove = false;
                canvas.repaint();
            }
        }
    }

    public synchronized void destroyBalls() {
        if (removeBalls.size() > 0) {
            // Получить множество из отображений
            Set<Map.Entry<Integer, List<BallMovement>>> set = removeBalls.entrySet();
            // Прогоняем полученное множество через for each
            for (Map.Entry<Integer, List<BallMovement>> SetIt : set) {

                // В процессе удаления комбинаций подсчитываем количество очков полученных за комбинацию
                int amount = 0; // Шаров в комбинации
                int combinationScore = 0;
                int turnsScore = 0;
                double multiplyScore = 0;

                for (BallMovement ballMovement : SetIt.getValue()) {
                    if (ballMovement.isExists()) {
                        amount++;
                        switch (ballMovement.getType()) {
                            case BallView.YELLOW_BALL:
                                turnsScore++;
                                break;
                            case BallView.PINK_BALL:
                                multiplyScore = multiplyScore + 0.05;
                            case BallView.BLACK_BALL:
                            case BallView.BLUE_BALL:
                            case BallView.ORANGE_BALL:
                            case BallView.GREEN_BALL:
                                if (ballMovement.getType() == highPriorityType) {
                                    combinationScore = combinationScore + ballMovement.getPrice() * 2;
                                } else {
                                    if (ballMovement.getType() == lowPriorityType) {
                                        combinationScore = combinationScore + ballMovement.getPrice() / 2;
                                    } else {
                                        combinationScore = combinationScore + ballMovement.getPrice();
                                    }
                                }
                                break;
                        }
                        // Убираем шар из таблицы шаров
                        ballsArray[ballMovement.getRow()][ballMovement.getColumn()] = null;
                        // Уничтожаем шар
                        ballMovement.setExists(false);

                    } else {
                        System.out.println("Попытка уничтожения не существующего шара: " + ballMovement.getBallName() + ": " + ballMovement.getType() + "[" + ballMovement.getStartY() + ", " + ballMovement.getStartX() + "]");
                    }

                }
                // Подсчет очков за данную комбинацию
                scoreMultiply = scoreMultiply + multiplyScore * (amount - 2);
                if (scoreMultiply > 2) {
                    scoreMultiply = 2.0;
                }
                if (turnsScore >= 3) {
                    turnsLeft = turnsLeft + (turnsScore - 1);
                }
                Double toInt = (double) (combinationScore * amount);
                if (toInt > 0) {
                    toInt = toInt * scoreMultiply;
                }
                score = score + toInt.intValue();
                if (score < 0) {
                    score = 0;
                }
                // Вывести значение счета на ScorePanel
                refreshScorePanel();
            }
            removeBalls.clear(); // Очистить список шаров на удаление
            // Заменяем удаленные элемнты на вышестоящие
            for (int row = rowTotal - 1; row >= 0; row--) {
                for (int column = columnTotal - 1; column >= 0; column--) {
                    if (ballsArray[row][column] == null) {
                        dropTheBall(row - 1, column);
                    }
                }
            }
            // Установить соседей новым шарам
            for (int row = rowTotal - 1; row >= 0; row--) {
                for (int column = columnTotal - 1; column >= 0; column--) {
                    setNeighborsBalls(row, column);
                }
            }
        }
    }

    private synchronized void dropTheBall(int row, int column) {
        if (row < -1 || row >= rowTotal || column < 0 || column >= columnTotal) {
            System.out.println("Попытка уронить не существующий шар: [" + row + ", " + column + "]");
        } else {
            // Если роняется шар не из верхнего ряда
            if (row >= 0) {

                if (ballsArray[row][column] == null) {
                    // Уронить шар над ним
                    dropTheBall(row - 1, column);
                }
                // Уронить шар вниз
                ballsArray[row + 1][column] = ballsArray[row][column];
                ballsArray[row][column] = null;
                ballsArray[row + 1][column].setRow(row + 1);
            }
            // Если роняется шар из несуществующего верхнего ряда
            else {
                createRandomBall(0, column);
                // Устанавливаем завышенный Y для созданного шара, что бы создать эффект падения
                ballsArray[0][column].setY(ballsArray[0][column].getRowY() - ballsArray[0][column].getHeight());
            }
        }
    }

    public void reverseSwap() {
        while (reverseBallsStack.getSize() > 0) {
            int[] firstSwapElement = reverseBallsStack.pop();
            int[] secondSwapElement = reverseBallsStack.pop();
            swapBalls(firstSwapElement[0], firstSwapElement[1], secondSwapElement[0], secondSwapElement[1], true);
        }
    }

    // Поменять местами соседние элементы в табличке
    public void swapBalls(int row, int column, int row1, int column1, boolean isReverseSwap) {
        // Если элементы являются соседними
        if (isSameLine(row, column, row1, column1)) {
            // Удалить комбинацию передвигаемого шара из таблицы комбинаций(комбинации передвигаемого шара хранятся под индексом -1)
            deleteCombinationFromList(-1);
            // Поменять элементы местами
            BallMovement swap = ballsArray[row][column];

            ballsArray[row][column] = ballsArray[row1][column1];
            ballsArray[row][column].setColumn(column);
            ballsArray[row][column].setRow(row);

            ballsArray[row1][column1] = swap;
            ballsArray[row1][column1].setColumn(column1);
            ballsArray[row1][column1].setRow(row1);
            // Переустановить соседей обновленным элементам
            setNeighborsBalls(row, column);
            setNeighborsBalls(row1, column1);

            if (!isReverseSwap) {
                // Проверить существование комбинации для обычного шара
                checkCombinationForBall(row, column, false);
                // Проверить существование комбинации для передвигаемого шара
                checkCombinationForBall(row1, column1, true);

                // Накопить стек обмена
                if (reverseBallsStack.showTop()[0] != row1 || reverseBallsStack.showTop()[1] != column1) {
                    // Положить его в стек

                    reverseBallsStack.push(row, column);
                    reverseBallsStack.push(row1, column1);
                } else {
                    reverseBallsStack.pop();
                    reverseBallsStack.pop();
                }
            } else {
                // Очистка множества удаляемых шаров

                // Получить множество из отображений
                Set<Map.Entry<Integer, List<BallMovement>>> set = removeBalls.entrySet();
                // Прогоняем полученное множество через for each
                for (Map.Entry<Integer, List<BallMovement>> SetIt : set) {
                    Integer combinationID = SetIt.getKey();
                    deleteCombinationFromList(combinationID);
                }
            }
        }
    }

    private void deleteCombinationFromList(int combinationID) {
        if (removeBalls.get(combinationID) != null) {
            for (BallMovement ballMovement : removeBalls.get(combinationID)) {
                ballMovement.setBright(false); // Перед удалением погасить все шары в комбинации
            }
            removeBalls.remove(combinationID);
        }
    }

    // Проверка являются являются ли элементы сосденими
    private boolean isSameLine(int row, int column, int row1, int column1) {
        // Если один из элементов является недопустимым
        if (row < 0 || row >= rowTotal || column < 0 || column >= columnTotal || row1 < 0 || row1 >= rowTotal || column1 < 0 || column1 >= columnTotal) {
            return false;
        }
        // Если элементы являются соседними элементами по строке
        if (row == row1 && (column == column1 + 1 || column == column1 - 1)) {
            return true;
        }
        // Являются ли элементы соседними по столбцу
        return column == column1 && (row == row1 + 1 || row == row1 - 1);
    }

    // Является ли шар частью комбинации
    private boolean isStreak(int row, int column) {
        return rowStreak(row, column) > 2 || columnStreak(row, column) > 2;
    }

    private void createRandomBall(int row, int column) {
        int yellowBallChance = 2;
        int blackBallChance = 2;
        int pinkBallChance = 1;
        // Создание переменной случайного числа
        Random randomInt = new Random(System.currentTimeMillis());
        do {
            // Если в данной ячейке таблицы уже есть переменная
            if (ballsArray[row][column] != null) {
                // Удалить ее
                ballsArray[row][column].setExists(false);
            }
            // Добавляем случайный шар на поле
            int ballType = randomInt.nextInt(6);
            // Корректируем частоту выпадения особых шаров
            while (ballType == BallView.YELLOW_BALL || ballType == BallView.BLACK_BALL ||
                    ballType == BallView.PINK_BALL) {
                if (pinkBallChance <= 0) {
                    pinkBallChance = randomInt.nextInt(1);
                    break;
                } else {
                    ballType = randomInt.nextInt(6);
                    pinkBallChance--;
                }
                if (blackBallChance <= 0) {
                    blackBallChance = randomInt.nextInt(2);
                    pinkBallChance--;
                    break;
                } else {
                    ballType = randomInt.nextInt(6);
                    blackBallChance--;
                }
                if (yellowBallChance <= 0) {
                    yellowBallChance = randomInt.nextInt(2);
                    pinkBallChance--;
                    break;
                } else {
                    ballType = randomInt.nextInt(6);
                    yellowBallChance--;
                }
            }
            BallMovement newBallMovement =
                    BallView.createBall(ballType, this, this.canvas,
                                        row, column, row * columnTotal + column);
            ballsArray[row][column] = newBallMovement;
            // До тех пор пока случайный шар образовывает комбинацию
        } while (isStreak(row, column));
    }

    private boolean isBallExistsInGrid(int row, int column) {
        if (row < 0 || row >= rowTotal) {
            return false;
        }
        if (ballsArray[row] == null) {
            return false;
        }
        if (column < 0 || column >= columnTotal) {
            return false;
        }
        if (ballsArray[row][column] == null) {
            return false;
        }
        return true;
    }

    private boolean isThisABallOfThisType(int type, int row, int column) {
        // Проверка, а существует ли такой шарик
        if (isBallExistsInGrid(row, column)) {
            // Вернуть, совпадает ли тип шарика с входящим типом
            return ballsArray[row][column].getType() == type;
        }
        return false;
    }

    // Подсчет количества одинаковых шариков идущих подряд в строке
    private int rowStreak(int row, int column) {
        // Если такой шарик существует
        if (isBallExistsInGrid(row, column)) {
            BallMovement currentBallMovement = ballsArray[row][column];
            int currentColumn = column - 1;
            int Sch = 1;  // Количество одинаковых шариков идущих подряд в строке
            // Поиск одинаковых элементов слева от текущего
            while (isThisABallOfThisType(currentBallMovement.getType(), row, currentColumn)) {
                Sch++; // Увеличиваем количество одинаковых шариков идущих подряд в строке
                currentColumn--; // Шаг влево
            }
            // Поиск одинаковых элементов справа от текущего
            currentColumn = column + 1;
            while (isThisABallOfThisType(currentBallMovement.getType(), row, currentColumn)) {
                Sch++; // Увеличиваем количество одинаковых шариков идущих подряд в строке
                currentColumn++; // Шаг вправо
            }
            return Sch;
        }
        return 0; // Если такого шарика не существует
    }

    // Подсчет количества одинаковых шариков идущих подряд в столбце
    private int columnStreak(int row, int column) {
        // Если такой шарик существует
        if (isBallExistsInGrid(row, column)) {
            BallMovement currentBallMovement = ballsArray[row][column];
            int currentRow = row - 1;
            int Sch = 1;  // Количество одинаковых шариков идущих подряд в столбце
            // Поиск одинаковых элементов снизу от текущего
            while (isThisABallOfThisType(currentBallMovement.getType(), currentRow, column)) {
                Sch++; // Увеличиваем количество одинаковых шариков идущих подряд в строке
                currentRow--; // Шаг вниз
            }
            // Поиск одинаковых элементов сверху от текущего
            currentRow = row + 1;
            while (isThisABallOfThisType(currentBallMovement.getType(), currentRow, column)) {
                Sch++; // Увеличиваем количество одинаковых шариков идущих подряд в строке
                currentRow++; // Шаг вверх
            }
            return Sch;
        }
        return 0; // Если такого шарика не существует
    }


    public synchronized void incStandBalls() {
        standBalls++;
    }

    public synchronized void decStandBalls() {
        standBalls--;
    }

    private void refreshScorePanel() {
        // Выводим очки на панель очков
        scorePanel.setScore(score);
        scorePanel.setTurnsLeft(turnsLeft);
        // Преобразуем множитель в проценты
        Double percentMultiply = Math.ceil((scoreMultiply - 1) * 100);
        scorePanel.setScoreMultiply(percentMultiply.intValue());
        scorePanel.setPriorityBalls(highPriorityType, lowPriorityType);
    }

    // Сеттеры
    private void setNeighborsBalls(int row, int column) {
        if (row < 0 || row >= rowTotal || column < 0 || column >= columnTotal) {
            System.out.println("Не корректные данные установки соседних шаров: " + row + "; " + column);
        } else {
            if (ballsArray[row][column] == null) {
                System.out.println("Попытка установить соседей удаленному шару: " + row + "; " + column);
            } else {
                // Устанавливаем выбранный шар
                BallMovement currentBallMovement = ballsArray[row][column];
                // Устанавливаем всех соседей для выбранного шара
                currentBallMovement.setRightBall(getBall(row, column + 1));
                currentBallMovement.setLeftBall(getBall(row, column - 1));
                currentBallMovement.setUpBall(getBall(row - 1, column));
                currentBallMovement.setDownBall(getBall(row + 1, column));
            }
        }
    }

    public void setDraggedBall(int row, int column) {
        draggedBall = ballsArray[row][column];
    }

    public void setEndTurnPhase(boolean endTurnPhase) {
        isEndTurnPhase = endTurnPhase;
    }

    // Геттеры
    public boolean isFieldAvailable() {
        return isFieldAvailable;
    }

    public int getScore() {
        return score;
    }

    public int getTurnsLeft() {
        return turnsLeft;
    }

    public Double getScoreMultiply() {
        return scoreMultiply;
    }

    public synchronized BallMovement getBall(int row, int column) {
        if (row < 0 || row >= rowTotal || column < 0 || column >= columnTotal) {
            return null;
        }
        return ballsArray[row][column];
    }
}
