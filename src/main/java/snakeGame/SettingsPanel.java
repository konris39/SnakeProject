package snakeGame;

import snakeGame.strategy.FastSpeedStrategy;
import snakeGame.strategy.MediumSpeedStrategy;
import snakeGame.strategy.SlowSpeedStrategy;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private static final int PANEL_WIDTH = 600;
    private static final int PANEL_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;

    private final GameFrame frame;
    private final JLabel infoLabel;

    public SettingsPanel(GameFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(null);
        setBackground(Color.BLACK);

        JLabel title = new JLabel("SETTINGS", SwingConstants.CENTER);
        title.setFont(new Font("Roboto Condensed", Font.BOLD, 36));
        title.setForeground(Color.GREEN);
        title.setBounds(0, 50, PANEL_WIDTH, 50);
        add(title);

        infoLabel = new JLabel("", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Roboto Condensed", Font.BOLD, 20));
        infoLabel.setForeground(Color.YELLOW);
        infoLabel.setBounds(0, 120, PANEL_WIDTH, 30);
        add(infoLabel);

        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = (PANEL_WIDTH - buttonWidth) / 2;
        int startY = 180;
        int gap = 60;

        JButton slowButton = createStyledButton("SLOW");
        slowButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        slowButton.addActionListener(e -> {
            frame.setGameDelay(new SlowSpeedStrategy().getDelay());
            showInfo("Zmieniono na SLOW");
        });
        add(slowButton);

        JButton mediumButton = createStyledButton("MEDIUM");
        mediumButton.setBounds(centerX, startY + gap, buttonWidth, buttonHeight);
        mediumButton.addActionListener(e -> {
            frame.setGameDelay(new MediumSpeedStrategy().getDelay());
            showInfo("Zmieniono na MEDIUM");
        });
        add(mediumButton);

        JButton fastButton = createStyledButton("FAST");
        fastButton.setBounds(centerX, startY + 2 * gap, buttonWidth, buttonHeight);
        fastButton.addActionListener(e -> {
            frame.setGameDelay(new FastSpeedStrategy().getDelay());
            showInfo("Zmieniono na FAST");
        });
        add(fastButton);

        JButton backButton = createStyledButton("BACK TO MENU");
        backButton.setBounds(centerX, startY + 3 * gap, buttonWidth, buttonHeight);
        backButton.addActionListener(e -> frame.showMenu());
        add(backButton);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(new Color(0, 255, 0));
        button.setFont(new Font("Roboto Condensed", Font.BOLD, 20));
        button.setFocusPainted(false);
        return button;
    }

    private void showInfo(String message) {
        infoLabel.setText(message);
        Timer hideTimer = new Timer(2000, e -> infoLabel.setText(""));
        hideTimer.setRepeats(false);
        hideTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < PANEL_WIDTH / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, PANEL_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, PANEL_WIDTH, i * UNIT_SIZE);
        }
    }
}
