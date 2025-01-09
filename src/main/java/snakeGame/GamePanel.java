package snakeGame;

import snakeGame.observer.Apple;
import snakeGame.observer.AppleObserver;
import snakeGame.state.GameOverState;
import snakeGame.state.GameState;
import snakeGame.state.PlayingState;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener, AppleObserver {
    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 600;
    public static final int UNIT_SIZE = 25;
    private final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    private int delay = 75;

    private final int[] x = new int[GAME_UNITS];
    private final int[] y = new int[GAME_UNITS];

    private int bodyParts = 6;
    private int applesEaten;
    private char direction = 'R';

    private int topScore = 0;
    private final Integer[] topScores = new Integer[5];

    private Timer timer;

    private Apple apple;

    private final Random random;

    private final GameFrame frame;

    private GameState currentState;

    private JButton restartButton;
    private JButton backToMenuButton;

    public GamePanel(GameFrame frame) {
        this.frame = frame;
        random = new Random();

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        setLayout(null);

        addKeyListener(new MyKeyAdapter());

        loadTopScores();
    }

    public void setGameDelay(int delay) {
        this.delay = delay;
        if (timer != null) {
            timer.setDelay(delay);
        }
    }

    public void startGame() {
        removeAll();
        revalidate();
        repaint();

        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        apple = new Apple(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE);
        apple.getAppleSubject().attach(this);

        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(delay, this);
        timer.start();

        currentState = new PlayingState(this);

        requestFocusInWindow();

        setVisible(true);
    }

    private void loadTopScores() {
        File file = new File("topScores.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < topScores.length; i++) {
            topScores[i] = 0;
        }

        try (Scanner scanner = new Scanner(file)) {
            int index = 0;
            while (scanner.hasNextInt() && index < topScores.length) {
                topScores[index++] = scanner.nextInt();
            }
            Arrays.sort(topScores, Collections.reverseOrder());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveTopScores() {
        try (PrintWriter out = new PrintWriter("topScores.txt")) {
            for (int score : topScores) {
                out.println(score);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateTopScores() {
        for (int i = 0; i < topScores.length; i++) {
            if (applesEaten > topScores[i]) {
                for (int j = topScores.length - 1; j > i; j--) {
                    topScores[j] = topScores[j - 1];
                }
                topScores[i] = applesEaten;
                break;
            }
        }
        Arrays.sort(topScores, Collections.reverseOrder());
        saveTopScores();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentState != null) {
            currentState.draw(g);
        }
    }

    public void drawGame(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        g.setColor(Color.red);
        g.fillOval(apple.getX(), apple.getY(), UNIT_SIZE, UNIT_SIZE);

        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }

        g.setColor(Color.red);
        g.setFont(new Font("Roboto Condensed", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String scoreText = "Score: " + applesEaten;
        g.drawString(scoreText, (SCREEN_WIDTH - metrics.stringWidth(scoreText)) / 2, g.getFont().getSize());

        drawSessionTopScore(g);
    }

    public void drawGameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Roboto Condensed", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        String scoreText = "Score: " + applesEaten;
        g.drawString(scoreText, (SCREEN_WIDTH - metrics1.stringWidth(scoreText)) / 2, g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Roboto Condensed", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        drawSessionTopScore(g);
    }

    private void drawSessionTopScore(Graphics g) {
        g.setFont(new Font("Roboto Condensed", Font.BOLD, 20));
        FontMetrics metricsTopScore = getFontMetrics(g.getFont());
        String topScoreStr = "(Session) Top Score: " + topScore;
        g.setColor(Color.GRAY);
        g.drawString(topScoreStr,
                (SCREEN_WIDTH - metricsTopScore.stringWidth(topScoreStr)) / 2,
                g.getFont().getSize() * 4);

        int startY = SCREEN_HEIGHT - 105;
        g.setColor(Color.GRAY);
        g.setFont(new Font("Roboto Condensed", Font.BOLD, 15));
        FontMetrics scoreboard = getFontMetrics(g.getFont());
        String topScoresTitle = "Top 5 Scores:";
        g.drawString(topScoresTitle, (SCREEN_WIDTH - scoreboard.stringWidth(topScoresTitle)) / 2, startY);

        int scoreGap = 20;
        for (int i = 0; i < topScores.length; i++) {
            String tScore = "Top " + (i + 1) + ": " + topScores[i];
            g.drawString(tScore,
                    (SCREEN_WIDTH - scoreboard.stringWidth(tScore)) / 2,
                    startY + scoreGap * (i + 1)
            );
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == apple.getX()) && (y[0] == apple.getY())) {
            apple.appleEaten();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                triggerGameOver();
                return;
            }
        }
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            triggerGameOver();
        }
    }

    private void triggerGameOver() {
        timer.stop();
        updateTopScores();
        currentState = new GameOverState(this);
        repaint();

        if (!isGameOverButtonsAdded()) {
            addGameOverButtons();
        }
    }

    private boolean isGameOverButtonsAdded() {
        return (restartButton != null && restartButton.getParent() == this)
                || (backToMenuButton != null && backToMenuButton.getParent() == this);
    }

    private void addGameOverButtons() {
        backToMenuButton = new JButton("BACK TO MENU");
        backToMenuButton.setBackground(new Color(50, 50, 50));
        backToMenuButton.setForeground(new Color(0, 255, 0));
        backToMenuButton.setFont(new Font("Roboto Condensed", Font.BOLD, 20));
        backToMenuButton.setFocusPainted(false);
        backToMenuButton.setBounds(
                (SCREEN_WIDTH - 200) / 2,
                (SCREEN_HEIGHT / 2) + 10,
                200, 40
        );
        backToMenuButton.addActionListener(e -> frame.showMenu());
        add(backToMenuButton);

        restartButton = new JButton("RETRY");
        restartButton.setBackground(new Color(50, 50, 50));
        restartButton.setForeground(new Color(0, 255, 0));
        restartButton.setFont(new Font("Roboto Condensed", Font.BOLD, 20));
        restartButton.setFocusPainted(false);
        restartButton.setBounds(
                (SCREEN_WIDTH - 120) / 2,
                (SCREEN_HEIGHT / 2) + 60,
                120, 40
        );
        restartButton.addActionListener(e -> startGame());
        add(restartButton);

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentState != null) {
            currentState.update();
        }
        repaint();
    }

    @Override
    public void onAppleEaten() {
        bodyParts++;
        applesEaten++;
        if (applesEaten > topScore) {
            topScore = applesEaten;
        }
        apple.newApplePosition();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
            }
        }
    }
}
