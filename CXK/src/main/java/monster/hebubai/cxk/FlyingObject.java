package monster.hebubai.cxk;

import java.awt.image.BufferedImage;

public abstract class FlyingObject {
	protected BufferedImage image;
	protected int width;
	protected int height;
	protected int x;
	protected int y;

	public abstract void step();

	public abstract boolean outOfBounds();

	public boolean shootBy(Bullet bullet) {
		int x1 = this.x;
		int x2 = this.x + this.width;
		int y1 = this.y;
		int y2 = this.y + this.height;
		int x = bullet.x;
		int y = bullet.y;
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;

	}

}
