package monster.hebubai.cxk;

import java.util.Random;

public class Bee extends FlyingObject implements Award {
	private int xspeed = 1;
	private int yspeed = 2;
	private int awardType;

	public Bee() {
		image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);
		y = -this.height;
		awardType = rand.nextInt(2);
	}

	public int getType() {

		return awardType;
	}

	public void step() {
		x += xspeed;
		y += yspeed;
		if (x >= ShootGame.WIDTH - this.width) {
			xspeed = -1;
		}
		if (x <= 0) {
			xspeed = 1;
		}
	}

	public boolean outOfBounds() {
		return this.y >= ShootGame.HEIGHT;
	}
}
