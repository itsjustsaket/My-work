import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class FlappyBirdGame extends JPanel implements ActionListener, KeyListener {

    Timer timer;
    Random rand = new Random();

    // Game states
    int state = 0; // 0 = START, 1 = PLAYING, 2 = GAME OVER

    // Bird
    int birdY = 250;
    int velocity = 0;

    // Pipe
    int pipeX = 400;
    int gapY = 150;

    // Score
    int score = 0;
    int highScore = 0;

    public FlappyBirdGame() {
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(20, this);
        timer.start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");

        FlappyBirdGame game = new FlappyBirdGame();
        frame.add(game);

        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        // Fix keyboard focus
        SwingUtilities.invokeLater(() -> game.requestFocusInWindow());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (state == 0) {
            drawStart(g);
            return;
        }

        drawGame(g);

        if (state == 2) {
            drawGameOver(g);
        }
    }

    void drawStart(Graphics g) {
        g.setColor(Color.black);

        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("FLAPPY BIRD", 70, 200);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Press SPACE to Start", 80, 260);

        g.drawString("High Score: " + highScore, 110, 320);
    }

    void drawGame(Graphics g) {
        // Bird
        g.setColor(Color.red);
        g.fillOval(100, birdY, 30, 30);

        // Pipes
        g.setColor(Color.green);
        g.fillRect(pipeX, 0, 50, gapY);
        g.fillRect(pipeX, gapY + 120, 50, getHeight());

        // Ground
        g.setColor(Color.orange);
        g.fillRect(0, 500, getWidth(), 100);

        // Score
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("High: " + highScore, 260, 30);
    }

    void drawGameOver(Graphics g) {
        g.setColor(Color.black);

        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("GAME OVER", 90, 250);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Press SPACE to Restart", 70, 300);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (state != 1) {
            repaint();
            return;
        }

        // Gravity
        velocity += 1;
        birdY += velocity;

        // Move pipe
        pipeX -= 5;

        // Reset pipe
        if (pipeX < -50) {
            pipeX = getWidth();
            gapY = 50 + rand.nextInt(250);
            score++;
        }

        // Collision
        Rectangle bird = new Rectangle(100, birdY, 30, 30);
        Rectangle topPipe = new Rectangle(pipeX, 0, 50, gapY);
        Rectangle bottomPipe = new Rectangle(pipeX, gapY + 120, 50, getHeight());

        if (bird.intersects(topPipe) || bird.intersects(bottomPipe)
                || birdY > 470 || birdY < 0) {

            state = 2;

            if (score > highScore) {
                highScore = score;
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {

            if (state == 0) {
                state = 1;
            }
            else if (state == 1) {
                velocity = -10;
            }
            else if (state == 2) {
                resetGame();
            }
        }
    }

    void resetGame() {
        birdY = 250;
        velocity = 0;
        pipeX = getWidth();
        gapY = 150;
        score = 0;
        state = 1;
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}