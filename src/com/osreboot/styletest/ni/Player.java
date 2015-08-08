package com.osreboot.styletest.ni;

import com.osreboot.ridhvl.HvlCoord;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class Player {

	public static final float playerSize = 64f;
	
	private static float x, y;
	
	private static float theta;

	private static HvlCoord velocity;
	
	private static HvlCoord momentum;
	
	public static void reset() {
		velocity = new HvlCoord(0, 0);
		momentum = new HvlCoord(0, 0);
		theta = 0;
	}

	public static void update(float delta) {
		
	}

	public static void draw(float delta) {
		HvlPainter2D.hvlRotate(x, y, theta);
		HvlPainter2D.hvlDrawQuad(x - (playerSize / 2), y - (playerSize / 2), playerSize, playerSize, HvlTemplateInteg2D.getTexture(Main.playerTextureIndex));
		HvlPainter2D.hvlResetRotation();
	}
	
	public static float getX() {
		return x;
	}

	public static void setX(float x) {
		Player.x = x;
	}

	public static float getY() {
		return y;
	}

	public static void setY(float y) {
		Player.y = y;
	}
}
