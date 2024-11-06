import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeOutside extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int playerX = 150;
    private int playerY = 200;
    private final int FIELD_WIDTH = 800; // Фиксированная ширина игрового поля
    private final int FIELD_HEIGHT = 600; // Фиксированная высота игрового поля
    private final int OBJECT_SIZE = 50; // Фиксированный размер объектов
    private int[][] positions;
    private LastDirection lastDirection = LastDirection.RIGHT;
    private Direction direction = Direction.RIGHT;
    private Snake head;
    private Snake tail;

    public SnakeOutside() {
        timer = new Timer(1000, this); // Создание таймера для обновления игры
        timer.start();
        setFocusable(true);
        addKeyListener(this); // Добавление слушателя клавиатуры
        setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT)); // Устанавливаем предпочтительный размер для панели
    }

    private enum LastDirection {
        UP, RIGHT, DOWN, LEFT
    }

    private enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    private class Snake {
        int posX;
        int posY;

        Snake(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }
    }

    private class Fruit {
        int posX;
        int posY;

        Fruit(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Устанавливаем фиксированный размер игрового поля
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, FIELD_WIDTH, FIELD_HEIGHT); // Рисуем игровое поле

        // Рисуем игрока
        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, OBJECT_SIZE, OBJECT_SIZE); // Рисование игрока с фиксированным размером


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (direction) {
            case UP:
                lastDirection = LastDirection.UP;
                playerY -= 50;
                break;
            case RIGHT:
                lastDirection = LastDirection.RIGHT;
                playerX += 50;
                break;
            case DOWN:
                lastDirection = LastDirection.DOWN;
                playerY += 50;
                break;
            case LEFT:
                lastDirection = LastDirection.LEFT;
                playerX -= 50;
                break;
        }

        playerX = outOfBorder(playerX);
        playerY = outOfBorder(playerY);




        repaint(); // Перерисовка панели
    }

    private int outOfBorder(int playerC){
        if(playerC > FIELD_WIDTH - OBJECT_SIZE) playerC = 0;
        if(playerC < 0) playerC = FIELD_WIDTH - OBJECT_SIZE;
        return  playerC;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            switch (lastDirection) {
                case UP:
                case DOWN:
                case LEFT:
                    direction = Direction.LEFT;
                    break;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            switch (lastDirection) {
                case UP:
                case DOWN:
                case RIGHT:
                    direction = Direction.RIGHT;
                    break;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            switch (lastDirection) {
                case RIGHT:
                case UP:
                case LEFT:
                    direction = Direction.UP;
                    break;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            switch (lastDirection) {
                case RIGHT:
                case DOWN:
                case LEFT:
                    direction = Direction.DOWN;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeOutside game = new SnakeOutside();
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER); // Добавляем игровую панель в центр окна
        frame.setSize(816, 639); // Устанавливаем начальный размер окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        game.requestFocus(); // Запрос фокуса для панели игры
    }
}