package com.osreboot.styletest.ni;

import com.osreboot.ridhvl.HvlCoord;

public class Player {

	private static float x, y;

	private static HvlCoord velocity;
	
	private static HvlCoord momentum;
	
	public static void reset() {
		velocity = new HvlCoord(0, 0);
		momentum = new HvlCoord(0, 0);
	}

	public static void update(float delta) {
		
	}

	public static void draw(float delta) {
		
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
