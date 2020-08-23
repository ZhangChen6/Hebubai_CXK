package monster.hebubai.cxk;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Hero extends FlyingObject {
	private int doubleFire;;
	private int life;
	private BufferedImage[] images;
	private int index;

	public Hero() {
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		new Random();
		x = 150;
		y = 400;
		doubleFire = 0;
		life = 3;
		images = new BufferedImage[] { ShootGame.hero0, ShootGame.hero1, ShootGame.hero2, ShootGame.hero3,
				ShootGame.hero4, ShootGame.hero5, ShootGame.hero6, ShootGame.hero7 };
		index = 0;
	}

	@Override
	public void step() {

		image = images[index++ / 15 % images.length];

	}

	public Bullet[] shoot() {
		int xStep = this.width / 4;
		int yStep = 20;
		if (doubleFire > 0) {
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x + 1 * xStep, this.y - yStep);
			bs[1] = new Bullet(this.x + 3 * xStep, this.y - yStep);
			doubleFire -= 2;
			return bs;
		} else {
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x + 2 * xStep, this.y - yStep);
			return bs;
		}

	}

	public void moveTo(int x, int y) {
		this.x = x - width / 2;
		this.y = y - height / 2;
	}

	public boolean outOfBounds() {

		return false;
	}

	public void addLife() {
		life++;
	}

	public int getLife() {
		return life;

	}

	public void subtractLife() {
		life--;
	}

	public void addDoubleFire() {
		doubleFire += 40;
	}

	public void clearDoubleFire() {
		doubleFire = 0;
	}

	public boolean hit(FlyingObject other) {
		int x1 = other.x - this.width / 2;
		int x2 = other.x + other.width + this.width / 2;
		int y1 = other.y - this.height / 2;
		int y2 = other.y + other.height + this.height / 2;
		int x = this.x + this.width / 2;
		int y = this.y + this.height / 2;
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}
}