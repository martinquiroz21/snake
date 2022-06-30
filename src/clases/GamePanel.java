package clases;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 50;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 3;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	

	public GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(new Color(18, 18, 18));
		this.setFocusable(true);
		this.addKeyListener(new AdaptadorDeTeclado());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {

		if (running) {
			// Lineas en horizontal y vertical
			/*
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_HEIGHT, i * UNIT_SIZE);
			}
			*/
			
			// Color de la manzana
			g.setColor(new Color(214, 15, 15));
			
			// Forma geom�trica de la manzana (ovalo)
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {

				if (i == 0) {
					// Color de la cabeza
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					// Color del cuerpo
					g.setColor(new Color(45, 180, 0));
					
					// Hace que se pinte de colores aleatorios(en caso de querer usarlo "descomentar")
					//g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					
					// Forma del snake (cuerpo)
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			// Color del puntaje
			g.setColor(new Color(235, 209, 12));
			
			// Fuente de puntaje
			g.setFont(new Font("SansSerif", Font.BOLD, 35));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString(""+applesEaten, (SCREEN_WIDTH - metrics.stringWidth(""+applesEaten)) - 30, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
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

		default:
			break;
		}
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}

	public void checkCollisions() {
		// Comprueba si la cabeza choca con el cuerpo
		for (int i = bodyParts; i > 0; i--) {

			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// Comprueba si la cabeza toca el borde izquierdo
		if (x[0] < 0) {
			running = false;
		}
		// Comprueba si la cabeza toca el borde derecho
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		// Comprueba si la cabeza toca el borde superior
		if (y[0] < 0) {
			running = false;
		}
		// Comprueba si la cabeza toca el borde inferior
		if (y[0] > SCREEN_WIDTH) {
			running = false;
		}

		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		// Puntaje
		g.setColor(new Color(235, 209, 12));
		g.setFont(new Font("SansSerif", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Puntaje: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Puntaje: "+applesEaten))/ 2, g.getFont().getSize());
		// Texto "Game Over"
		g.setColor(new Color(191, 21, 21));
		g.setFont(new Font("Candara", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/ 2, SCREEN_HEIGHT / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class AdaptadorDeTeclado extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;

			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;

			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;

			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;

			default:
				break;
			}
		}
	}

}
