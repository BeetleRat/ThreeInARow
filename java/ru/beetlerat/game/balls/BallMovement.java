package ru.beetlerat.game.balls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Абстрактный класс
// выполняющийся в паралельном потоке
// реализующий перемещение шра по игровому полю
abstract class BallMovement implements Runnable {

    // Объект потока
    private final Thread thread;
    // Панель в которой отрисовывается шар
    private JComponent canvas;
    // Классы взаимодействия с другими шариками
    private BallsGrid ballsGrid;
    private BallMovement upBallMovement;
    private BallMovement downBallMovement;
    private BallMovement leftBallMovement;
    private BallMovement rightBallMovement;

    // Параметры шара
    private int ballName;
    // Строка и столбец в таблице шаров
    private int row;
    private int column;
    // Координаты строки и столбца в таблице шаров
    private int rowY;
    private int columnX;
    private int startX; // х в котором шар был взят мышкой
    private int startY; // y в котором шар был взят мышкой
    // Координаты отрисовки шара и его размеры
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    // Границы игрового поля
    private int floor;
    private int ceil;
    private int rightBorder;
    private int leftBorder;
    // Отступы от края игрового поля
    private int paddingX;
    private int paddingY;

    private boolean isExists;
    protected boolean isBallDragged;
    protected boolean isTransported;// Перемещается ли данный шар другим шаром
    private int isBallAvailable; // 0-шар еще не двигался 1-шар уже пришел в нужную точку 2 - шар еще движется в нужную точку
    private int isYChange; // 0 -шар стоит на месте/ 1-шар перемещается по Y/ -1 - шар перемещается по Х


    // Конструктор
    BallMovement(BallsGrid ballsGrid, JComponent canvas, int row, int column, int id) {
        // Имя шарика
        this.ballName = id;
        // Установка базовых параметров шара
        this.width = 60;
        this.height = 60;
        // Создание паралельного потока
        thread = new Thread(this, "Ball " + ballName + " thread");
        // Сохранение панели в которой отрисовывается шар
        this.canvas = canvas;
        // Сохранение игрового поля в котором находится шар
        this.ballsGrid = ballsGrid;
        // Обнуление соседей шара
        this.upBallMovement = null;
        this.downBallMovement = null;
        this.leftBallMovement = null;
        this.rightBallMovement = null;

        // Установка границ шара
        this.floor =this.canvas.getPreferredSize().height -  height;
        this.ceil =0+ height / 2;

        this.rightBorder = this.canvas.getPreferredSize().width - width;
        this.leftBorder = 0 + width / 2;
        // Установка отступов от края игрового поля
        this.paddingX = 35;
        this.paddingY = 35;


        // Установка изночальных координат по положению шара в таблице шаров
        setRow(row);
        setColumn(column);
        this.y = rowY;
        this.x = columnX;
        this.startX = columnX;
        this.startY = rowY;

        this.isExists = true; // Шар создан - значит существует
        // При создании шар никуда не перемещается
        this.isBallDragged = false;
        this.isTransported = false;
        this.isBallAvailable = 0; // Шар еще не двигался
        this.isYChange = 0;// Шар стоит на месте

        // Добавление слушателей
        addListeners();

        // Запуск парралельного потока
        thread.start();
    }

    // Добавление слушателей
    private void addListeners() {
        // Добавление слушателя мыши через адаптер
        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouse) {
                // Шарик отрисовыватся по координатам слева сверху, а мышкой мы должны перетаскивать его за центр.
                // По этому для удобства приводим координаты левого верхнего края к центральным координатам
                int centerX = x + width / 2;
                int centerY = y + height / 2;
                // Если при нажатии на мышь курсор находится внутри шара и таблица шаров доступна для изменения
                if (mouse.getX() > (centerX - width / 2) && mouse.getX() < (centerX + width / 2) &&
                        mouse.getY() > (centerY - height / 2) && mouse.getY() < (centerY + height / 2) &&
                        ballsGrid.isFieldAvailable()) {
                    isBallDragged = true;
                    // Запоминаем координаты из которых взят шар
                    startX = columnX;
                    startY = rowY;
                    // Установить текущий шар отрисовываемым поверх других
                    ballsGrid.setDraggedBall(row, column);
                    //System.out.println("Взят шар: "+ballName+" типа: "+getType()+" startX: "+startX+" startY: "+startY);
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouse) {
                if (isBallDragged) {
                    isBallDragged = false;
                    isBallAvailable = 0; // Шар еще не двигался
                    ballsGrid.setEndTurnPhase(true); // Запустить процесс конца хода
                }
                if (isTransported) {
                    isTransported = false;
                    isBallAvailable = 0; // Шар еще не двигался
                }

            }

            @Override
            public void mouseDragged(MouseEvent mouse) {
                if (isBallDragged) {
                    // Шарик отрисовыватся по координатам слева сверху, а мышкой мы должны перетаскивать его за центр.
                    // По этому для удобства приводим центральные координаты к координатам левого верхнего края
                    int mouseX = mouse.getX() - width / 2;
                    int mouseY = mouse.getY() - height / 2;
                    // Изменяем координаты самого шара, на координаты соответствующие текущему положению курсора
                    changeCoords(mouseX, mouseY);
                    // Проверяем не вылезли ли x и y за границы игрового поля
                    checkBorders();
                    // Изменяем координаты соседей шара, на координаты соответствующие текущему положению самого шара
                    moveNeighbor();

                    canvas.repaint();// Перерисовываем изображение согласно новым координатам
                }
            }

            // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ АНОНИМНОГО КЛАССА
            private void moveNeighbor() {
                int rangeX = 0; // Положительное расстояние на которое передвинулся шар по х
                int signX = 0; // -1 движемся вправо/ 1 движемся влево / 0 перемещение по х не происходит
                int rangeY = 0; // Положительное расстояние на которое передвинулся шар по Y
                int signY = 0;    // -1 движемся вниз/ 1 движемся вверх / 0 перемещение по y не происходит
                BallMovement neighbor = null; // Передвигаемый сосед
                // Если шар передвигается вправао
                if (x > columnX) {
                    // Вычисляем положительное расстояние на которое передвинулся шар
                    rangeX = Math.abs(x - columnX);
                    neighbor = rightBallMovement;
                    signX = -1;
                } else {
                    // Если шар передвигается влево
                    if (x < columnX) {
                        // Вычисляем положительное расстояние на которое передвинулся шар
                        rangeX = Math.abs(x - columnX);
                        neighbor = leftBallMovement;
                        signX = 1;
                    } else {
                        // Если шар передвигается вниз
                        if (y > rowY) {
                            // Вычисляем положительное расстояние на которое передвинулся шар
                            rangeY = Math.abs(y - rowY);
                            neighbor = downBallMovement;
                            signY = -1;

                        } else {
                            // Если шар передвигается вверх
                            if (y < rowY) {
                                // Вычисляем положительное расстояние на которое передвинулся шар
                                rangeY = Math.abs(y - rowY);
                                neighbor = upBallMovement;
                                signY = 1;
                            }
                        }
                    }
                }
                // Если существует передвигаемый сосед
                if (neighbor != null) {
                    // Устанавливаем ему флаг перемещения
                    neighbor.setTransported(true);
                    // Если происходит перемещение по х
                    if (signX != 0) {
                        // Если шар встал на место соседа, или дальше
                        if ((-1) * signX * neighbor.getColumnX() + signX * x <= 0) {
                            // Поменять шар и соседа местами в таблице шаров
                            ballsGrid.swapBalls(row, column, neighbor.getRow(), neighbor.getColumn(), false);
                        } else {
                            // Иначе изменить координаты соседа на сдвиг от его положения на range в сторону шара
                            neighbor.setX(neighbor.getColumnX() + signX * rangeX);
                        }
                    }
                    // Если происходит перемещение по х
                    if (signY != 0) {
                        // Если шар встал на место соседа, или дальше
                        if ((-1) * signY * neighbor.getRowY() + signY * y <= 0) {
                            // Поменять шар и соседа местами в таблице шаров
                            ballsGrid.swapBalls(row, column, neighbor.getRow(), neighbor.getColumn(), false);
                        } else {
                            // Иначе изменить координаты соседа на сдвиг от его положения на range в сторону шара
                            neighbor.setY(neighbor.getRowY() + signY * rangeY);
                        }
                    }
                }
            }

            private void changeCoords(int mouseX, int mouseY) {
                // Расстояние пройденное шаром от точки старта до положения мышы
                int xRange;
                int yRange;
                if (mouseX > startX) {
                    xRange = mouseX - startX;
                } else {
                    xRange = startX - mouseX;
                }
                if (mouseY > startY) {
                    yRange = mouseY - startY;
                } else {
                    yRange = startY - mouseY;
                }
                // Если шар стоит на месте
                if (isYChange == 0) {
                    // Проверяем изменяют ли его горизонтальное положение
                    if (xRange > yRange + 1) {
                        isYChange = -1;
                        x = mouseX;
                        y = startY;
                    }
                    // Или же изменяют его вертикальное положение
                    if (xRange < yRange - 1) {
                        isYChange = 1;
                        x = startX;
                        y = mouseY;
                    }
                } else {
                    // Если шар перемещается по Y
                    if (isYChange == 1) {
                        // Пришел ли шар в положение из которого был взят
                        if (yRange <= 2) {
                            isYChange = 0;// Шар стоит на месте
                            ballsGrid.reverseSwap(); // Вернуть на место все перемещенные в этом ходу шары
                            // Установить изначальные координаты отрисовки шара
                            x = startX;
                            y = startY;
                        } else {
                            // Сохранить координату x и изменить y
                            x = startX;
                            y = mouseY;
                        }
                        // Если шар перемещается по Y
                    } else {
                        // Пришел ли шар в положение из которого был взят
                        if (xRange <= 2) {
                            isYChange = 0;// Шар стоит на месте
                            ballsGrid.reverseSwap(); // Вернуть на место все перемещенные в этом ходу шары
                            // Установить изначальные координаты отрисовки шара
                            x = startX;
                            y = startY;
                        } else {
                            // Сохранить координату y и изменить x
                            x = mouseX;
                            y = startY;
                        }
                    }
                }
            }

            private void checkBorders() {
                // Проверка помещаются ли x и y в границы игрового поля
                if (x > rightBorder) {
                    x = rightBorder;
                }
               if (x < leftBorder) {
                    x = leftBorder;
                }
                if (y < ceil) {
                    y = ceil;
                }
                if (y > floor) {
                    y = floor;
                }
            }
        };

        // Добавление слушателя для мыши
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);
    }

    // Метод выполняемый в паралельном потоке
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5); // Ожидание 5 милисекунд

            } catch (InterruptedException e) {
                System.out.println("Ошибка потока: " + this.thread.getName() + "  " + e);
            }
            // Если шар не перемещается мышкой
            if (!isBallDragged && !isTransported) {
                // Если он стоит на нужном месте
                if (y == rowY && x == columnX) {
                    // Если шар двигался и только что встал в нужное место
                    if (isBallAvailable == 2) {
                        isBallAvailable = 1;// Шар уже пришел в нужную точку
                        ballsGrid.incStandBalls(); // Увеличить в таблице количество шаров на своем месте
                        ballsGrid.checkAvailable(); // Проверить все ли шары в таблице на своем месте
                        // Если шара уже не существует
                        if (!isExists) {
                            break;  // Выйти из цикла потока
                        }
                    }
                    // Если шар не двигался
                    if (isBallAvailable == 0) {
                        isBallAvailable = 1;// Шар уже пришел в нужную точку
                        ballsGrid.checkAvailable();// Проверить все ли шары в таблице на своем месте
                        // Если шара уже не существует
                        if (!isExists) {
                            break;  // Выйти из цикла потока
                        }
                    }

                }
                // Если шар не находится на своем месте
                else {
                    // Если шар не двигался
                    if (isBallAvailable == 0) {
                        isBallAvailable = 2;// Шар движется в нужную точку
                        ballsGrid.decStandBalls(); // Уменьшить в таблице количество шаров на своем месте
                        ballsGrid.checkAvailable(); // Проверить что не все шары в таблице на своем месте
                    }
                    // Смещение на 1 по х и по y в сторону нужной точки
                    if (x > columnX) {
                        x--;
                        canvas.repaint();
                    }
                    if (x < columnX) {
                        x++;
                        canvas.repaint();
                    }
                    if (y > rowY) {
                        y--;
                        canvas.repaint();
                    }
                    if (y < rowY) {
                        y++;
                        canvas.repaint();
                    }
                }
            }
        }

    }

    // Геттеры
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getBallName() {
        return ballName;
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public int getRowY() {
        return rowY;
    }
    public int getColumnX() {
        return columnX;
    }
    public int getStartX() {
        return startX;
    }
    public int getStartY() {
        return startY;
    }
    public boolean isExists() {
        return isExists;
    }

    // Сеттеры
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setBallName(int ballName) {
        this.ballName = ballName;
    }
    public void setTransported(boolean transported) {
        isTransported = transported;
    }
    public void setUpBallMovement(BallMovement upBallMovement) {
        this.upBallMovement = upBallMovement;
    }
    public void setDownBallMovement(BallMovement downBallMovement) {
        this.downBallMovement = downBallMovement;
    }
    public void setLeftBallMovement(BallMovement leftBallMovement) {
        this.leftBallMovement = leftBallMovement;
    }
    public void setRightBallMovement(BallMovement rightBallMovement) {
        this.rightBallMovement = rightBallMovement;
    }
    public void setRow(int row) {
        this.row = row; // Сохраняем строку
        // Пересчитываем координаты текущей строки
        this.rowY = paddingY + this.row * height;
        // Если мы установили новую строку из-вне,
        // значит шар изменил свое положение в таблице,
        // значит он не может больше быть перетаскиваемым,
        // так как перетаскивался шар с дргуого места в таблице
        this.isTransported = false;

    }
    public void setColumn(int column) {
        this.column = column; // Сохраняем стобец
        // Пересчитываем координаты текущего столбца
        columnX = paddingX + this.column * width;
        // Если мы установили новую строку из-вне,
        // значит шар изменил свое положение в таблице,
        // значит он не может больше быть перетаскиваемым,
        // так как перетаскивался шар с дргуого места в таблице
        this.isTransported = false;
    }
    public void setExists(boolean exists) {
        isExists = exists;
        // Если шар больше не существует
        if (!isExists) {
            // Выкидываем его за поле, что бы перед уничтожением он успел увеличить в таблице количество шаров на своем месте
            // но при этом пользователь не мог случайно с ним взаимодействовать
            this.upBallMovement = null;
            this.downBallMovement = null;
            this.leftBallMovement = null;
            this.rightBallMovement = null;
            this.width = 0;
            this.height = 0;
            this.paddingX = 0;
            this.paddingY = 0;
            this.startY=row;
            this.startX=column;
            setRow(-100);
            setColumn(-100);
            this.y = rowY;
            this.x = columnX;
        }
    }



    abstract void paintBall(Graphics brush);

    abstract int getType();

    abstract int getPrice();

    abstract boolean getBright();

    abstract void setBright(boolean isBright);



}
