package monster.hebubai.cxk;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;

public class ShootGame extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 563;
	public static BufferedImage background;
	public static BufferedImage bigPlane;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage hero2;
	public static BufferedImage hero3;
	public static BufferedImage hero4;
	public static BufferedImage hero5;
	public static BufferedImage hero6;
	public static BufferedImage hero7;

	static {
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.jpg"));
			bigPlane = ImageIO.read(ShootGame.class.getResource("bigplane.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("ji.png"));
			bee = ImageIO.read(ShootGame.class.getResource("lsh.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("ball.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("1.png"));
			hero2 = ImageIO.read(ShootGame.class.getResource("2.png"));
			hero3 = ImageIO.read(ShootGame.class.getResource("3.png"));
			hero4 = ImageIO.read(ShootGame.class.getResource("4.png"));
			hero5 = ImageIO.read(ShootGame.class.getResource("5.png"));
			hero6 = ImageIO.read(ShootGame.class.getResource("6.png"));
			hero7 = ImageIO.read(ShootGame.class.getResource("7.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.jpg"));
			gameover = ImageIO.read(ShootGame.class.getResource("die.jpg"));
			start = ImageIO.read(ShootGame.class.getResource("start.jpg"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Hero hero = new Hero();
	private FlyingObject[] flyings = {};
	private Bullet[] bullets = {};

	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	private int state = START;

	public FlyingObject nextOne() {

		Random rand = new Random();
		int type = rand.nextInt(20);
		if (type < 5) {
			return new Bee();
		} else {
			return new Airplane();
		}

	}

	int flyIndex = 0;

	public void enterAction() {
		flyIndex++;
		if (flyIndex % 40 == 0) {
			FlyingObject obj = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;

		}
	}

	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			bang(b);
		}
	}

	int score = 0;

	public void bang(Bullet b) {
		int index = -1;
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (f.shootBy(b)) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			FlyingObject one = flyings[index];
			if (one instanceof Enemy) {
				Enemy e = (Enemy) one;
				score += e.getScore();

			}
			if (one instanceof Award) {
				Award a = (Award) one;
				int type = a.getType();
				switch (type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();

					break;

				}

			}
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length - 1];
			flyings[flyings.length - 1] = t;
			flyings = Arrays.copyOf(flyings, flyings.length - 1);

		}
	}

	public void stepAction() {
		hero.step();
		for (int i = 0; i < flyings.length; i++) {
			flyings[i].step();
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
	}

	int shootIndex = 0;

	public void shootAction() {
		shootIndex++;
		if (shootIndex % 30 == 0) {
			Bullet[] bs = hero.shoot();

			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);
		}

	}

	private void outOfBoundsAction() {
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds()) {
				flyingLives[index] = f;
				index++;

			}
		}
		flyings = Arrays.copyOf(flyingLives, index);

		index = 0;
		Bullet[] bulletsLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletsLives[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletsLives, index);
	}

	public void hitAction() {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (hero.hit(f)) {
				hero.subtractLife();
				hero.clearDoubleFire();
				FlyingObject t = flyings[i];
				flyings[i] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length - 1);
			}
		}

	}

	public void checkGameOverAction() {
		if (hero.getLife() <= 0) {
			state = GAME_OVER;

		}

	}

	public void action() {
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (state == RUNNING) {
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}

			}

			public void mouseClicked(MouseEvent e) {
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					score = 0;
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					break;

				}
			}

			public void mouseExited(MouseEvent e) {
				if (state == RUNNING) {
					state = PAUSE;
				}

			}

			public void mouseEntered(MouseEvent e) {
				if (state == PAUSE) {
					state = RUNNING;
				}

			}

		};

		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		Timer timer = new Timer();
		int interval = 10;
		timer.schedule(new TimerTask() {
			public void run() {
				if (state == RUNNING) {
					enterAction();
					stepAction();
					shootAction();
					outOfBoundsAction();
					bangAction();
					hitAction();
					checkGameOverAction();

				}
				repaint();
			}
		}, interval, interval);
	}

	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintHero(g);
		paintFlyingObjects(g);
		paintBullets(g);
		paintScoreAndLife(g);
		paintState(g);

	}

	public void paintHero(Graphics g) {
		g.drawImage(hero.image, hero.x, hero.y, null);
	}

	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.image, f.x, f.y, null);
		}
	}

	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.image, b.x, b.y, null);
		}

	}

	public void paintScoreAndLife(Graphics g) {
		g.setColor(new Color(0xFFFF11));
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		g.drawString("score:" + score, 10, 25);
		g.drawString("LIFE:" + hero.getLife(), 10, 60);
	}

	public void paintState(Graphics g) {
		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("蔡徐坤，快来打飞机！");
		ShootGame game = new ShootGame();
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.action();

	}

}
