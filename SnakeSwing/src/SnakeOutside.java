import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeOutside extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int playerX = 3;
    private int playerY = 4;
    private int spawnTime = 5;
    private int score;
    private final int FIELD_WIDTH = 800;
    private final int FIELD_HEIGHT = 600;
    private final int OBJECT_SIZE = 50;
    private int[][] positions;
    private Direction lastDirection = Direction.RIGHT;
    private Direction direction = Direction.RIGHT;
    private ArrayList<Snake> tail = new ArrayList<>();
    private ArrayList<Fruit> fruits = new ArrayList<>();


    public SnakeOutside() {
        tail.add(new Snake(2, 4));
        tail.add(new Snake(1, 4));
        tail.add(new Snake(0, 4));
        timer = new Timer(1000, this);
        timer.start();
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
    }

    private void gameoverWindow(){
        timer.stop();
        JDialog gameOverDialog = new JDialog();
        gameOverDialog.setTitle("GAME OVER!");
        gameOverDialog.setModal(true);
        gameOverDialog.setSize(300, 150);
        gameOverDialog.setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        gameOverDialog.getContentPane().add(panel);
        gameOverDialog.setVisible(true);

    }

    private enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    public static class Snake {
        int posX;
        int posY;

        Snake(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }
    }

    private static class Fruit {
        int posX;
        int posY;
        int lifeTime;
        String color;

        Fruit(int posX, int posY, String color) {
            this.posX = posX;
            this.posY = posY;
            this.lifeTime = 15;
            this.color = color;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, FIELD_WIDTH, FIELD_HEIGHT);

        //

        for(Fruit fruit: fruits) {
            if(fruit.color.equals("RED")) g.setColor(Color.RED);
            else if(fruit.color.equals("BLACK")) g.setColor(Color.BLACK);
            else g.setColor(Color.YELLOW);
            g.fillRect(fruit.posX * OBJECT_SIZE, fruit.posY * OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE);
        }

        //
        g.setColor(Color.GREEN);
        g.fillRect(playerX * OBJECT_SIZE, playerY * OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE);
        g.setColor(Color.BLACK);
        g.fillRect((playerX * OBJECT_SIZE) + 10, (playerY * OBJECT_SIZE) + 10, 10, 10);
        g.fillRect((playerX * OBJECT_SIZE) + 30, (playerY * OBJECT_SIZE) + 10, 10, 10);

        //
        g.setColor(Color.GREEN);
        for(Snake snake: tail) g.fillRect(snake.posX * OBJECT_SIZE, snake.posY * OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE);
        g.setColor(Color.BLACK);
        g.fillRect((tail.getLast().posX * OBJECT_SIZE) + 20, (tail.getLast().posY * OBJECT_SIZE) + 20, 10, 10);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean[][] positions = new boolean[16][12];
        int prevX = playerX;
        int prevY = playerY;
        for(Snake snake: tail){
            int tmpX = snake.posX;
            int tmpY = snake.posY;
            snake.posX = prevX;
            snake.posY = prevY;
            positions[snake.posX][snake.posY] = true;
            prevX = tmpX;
            prevY = tmpY;
        }
        switch (direction) {
            case UP:
                lastDirection = Direction.UP;
                playerY--;
                break;
            case RIGHT:
                lastDirection = Direction.RIGHT;
                playerX++;
                break;
            case DOWN:
                lastDirection = Direction.DOWN;
                playerY++;
                break;
            case LEFT:
                lastDirection = Direction.LEFT;
                playerX--;
                break;
        }
        playerX = outOfBorder(playerX, 15);
        playerY = outOfBorder(playerY, 11);
        if(positions[playerX][playerY]) gameoverWindow();
        int index = 0;
        while(index < fruits.size()){
            Fruit fruit = fruits.get(index);
            if(playerX == fruit.posX && playerY == fruit.posY){
                switch (fruit.color) {
                    case "BLACK":
                        score -= 50;
                        break;
                    case "RED":
                        score += 15;
                        tail.add(new Snake(tail.getLast().posX, tail.getLast().posY));
                        break;
                    case "YELLOW":
                        score += 100;
                        tail.add(new Snake(tail.getLast().posX, tail.getLast().posY));
                        break;
                }
                fruits.remove(index);
            } else {
                fruit.lifeTime--;
                if (fruit.lifeTime == 0) {
                    fruits.remove(index);
                } else {
                    index++;
                    positions[fruit.posX][fruit.posY] = true;
                }
            }
        }
        positions[playerX][playerY] = true;

        spawnTime--;
        if(spawnTime == 0){
            Random random = new Random();
            spawnTime = random.nextInt(8) + 3;
            int colorChance = random.nextInt(101);
            String color;
            if(colorChance < 10) color = "YELLOW";
            else if(colorChance > 70) color = "BLACK";
            else color = "RED";
            ArrayList<int[]> falseIndices = new ArrayList<>();
            for (int i = 0; i < positions.length; i++) {
                for (int j = 0; j < positions[i].length; j++) {
                    if (!positions[i][j]) {
                        falseIndices.add(new int[]{i, j});
                    }
                }
            }
            int position = random.nextInt(falseIndices.size());
            fruits.add(new Fruit(falseIndices.get(position)[0], falseIndices.get(position)[1], color));
        }
        repaint();
    }

    private int outOfBorder(int playerC, int width){
        if(playerC > width) playerC = 0;
        else if(playerC < 0) playerC = width;
        return  playerC;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && lastDirection != Direction.RIGHT) {
            direction = Direction.LEFT;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && lastDirection != Direction.LEFT) {
            direction = Direction.RIGHT;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && lastDirection != Direction.DOWN) {
            direction = Direction.UP;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && lastDirection != Direction.UP) {
            direction = Direction.DOWN;
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
        frame.add(game, BorderLayout.CENTER);
        frame.setSize(816, 639);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        game.requestFocus();
    }
}