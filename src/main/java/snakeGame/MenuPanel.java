package snakeGame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class MenuPanel extends JPanel {

    private static final int PANEL_WIDTH = 600;
    private static final int PANEL_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;

    private static final float MIN_FONT_SIZE = 50f;
    private static final float MAX_FONT_SIZE = 60f;
    private static final float FONT_SIZE_STEP = 0.22f;
    private float currentFontSize = MIN_FONT_SIZE;
    private boolean growing = true;

    private final JLabel titleLabel;
    private final Timer animationTimer;

    private final Integer[] topScores = new Integer[5];

    public MenuPanel(GameFrame frame) {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(null);
        setBackground(Color.BLACK);

        loadTopScores();

        titleLabel = new JLabel("Snake Game", SwingConstants.CENTER);
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("Roboto Condensed", Font.BOLD, (int) currentFontSize));
        titleLabel.setBounds(0, 50, PANEL_WIDTH, 60);
        add(titleLabel);

        animationTimer = new Timer(10, e -> animateTitle());
        animationTimer.start();

        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = (PANEL_WIDTH - buttonWidth) / 2;
        int startY = 180;
        int gap = 60;

        JButton playButton = createStyledButton("PLAY");
        playButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        playButton.addActionListener(e -> frame.startGame());
        add(playButton);

        JButton settingsButton = createStyledButton("SETTINGS");
        settingsButton.setBounds(centerX, startY + gap, buttonWidth, buttonHeight);
        settingsButton.addActionListener(e -> frame.showSettings());
        add(settingsButton);

        JButton exitButton = createStyledButton("EXIT");
        exitButton.setBounds(centerX, startY + 2 * gap, buttonWidth, buttonHeight);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(new Color(0, 255, 0));
        button.setFont(new Font("Roboto Condensed", Font.BOLD, 20));
        button.setFocusPainted(false);
        return button;
    }

    private void animateTitle() {
        if (growing) {
            currentFontSize += FONT_SIZE_STEP;
            if (currentFontSize >= MAX_FONT_SIZE) {
                growing = false;
            }
        } else {
            currentFontSize -= FONT_SIZE_STEP;
            if (currentFontSize <= MIN_FONT_SIZE) {
                growing = true;
            }
        }
        titleLabel.setFont(titleLabel.getFont().deriveFont(currentFontSize));
        repaint();
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


    @SuppressWarnings("unused")
    private void saveTopScores() {
        try (PrintWriter out = new PrintWriter("topScores.txt")) {
            for (int score : topScores) {
                out.println(score);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < PANEL_WIDTH / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, PANEL_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, PANEL_WIDTH, i * UNIT_SIZE);
        }

        drawTopScores(g);
    }

    private void drawTopScores(Graphics g) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("Roboto Condensed", Font.BOLD, 15));
        FontMetrics fm = g.getFontMetrics();

        String title = "Top 5 Scores:";
        int startY = PANEL_HEIGHT - 105;

        g.drawString(title, (PANEL_WIDTH - fm.stringWidth(title)) / 2, startY);

        int gap = 20;
        for (int i = 0; i < topScores.length; i++) {
            String tScore = "Top " + (i + 1) + ": " + topScores[i];
            g.drawString(
                    tScore,
                    (PANEL_WIDTH - fm.stringWidth(tScore)) / 2,
                    startY + gap * (i + 1)
            );
        }
    }
}
