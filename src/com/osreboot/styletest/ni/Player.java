package com.osreboot.styletest.ni;

import com.osreboot.ridhvl.HvlCoord;
import com.osreboot.ridhvl.input.HvlInputSeriesAction;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class Player {

	public static final float playerSize = 48f;
	public static final float accel = 256f;
	public static final float motionDecay = -0.5f, stationaryDecay = -1f;
	public static final float rotDecay = -2f;
	
	public static final float maxRot = 135f;
	
	public static final float maxVel = 2048f;
	
	private static float x, y;
	
	private static float theta;
	private static float rotVel;
	
	private static HvlCoord velocity;
	
	public static void reset() {
		x = 0;
		y = 0;
		velocity = new HvlCoord(0, 0);
		theta = 90;
	}

	public static void update(float delta) {

		float xIn = HvlInputSeriesAction.RIGHT.getCurrentOutput() - HvlInputSeriesAction.LEFT.getCurrentOutput();
		float yIn = HvlInputSeriesAction.DOWN.getCurrentOutput() - HvlInputSeriesAction.UP.getCurrentOutput();
		
		rotVel += xIn * delta;
		rotVel = Math.min(Math.max(rotVel, -maxVel), rotVel);
		rotVel *= Math.pow(Math.E, delta * rotDecay);
		theta += Math.min(maxRot, Math.max(-maxRot, rotVel * velocity.length())) * delta;
		
		HvlCoord motion = new HvlCoord((float)Math.cos(Math.toRadians(theta + 90)) * yIn, (float)Math.sin(Math.toRadians(theta + 90)) * yIn);
		motion.normalize();
		
		motion.mult(accel);
		motion.mult(delta);
	
		motion.x = Float.isNaN(motion.x) ? 0 : motion.x;
		motion.y = Float.isNaN(motion.y) ? 0 : motion.y;
		
		velocity.add(motion);

		velocity.x = Float.isNaN(velocity.x) ? 0 : velocity.x;
		velocity.y = Float.isNaN(velocity.y) ? 0 : velocity.y;
		
		float dec = yIn == 0 ? stationaryDecay : motionDecay;
		
		velocity.x *= (float)Math.pow(Math.E, delta * dec);
		velocity.y *= (float)Math.pow(Math.E, delta * dec);
		
		if (velocity.length() > maxVel)
		{
			velocity.normalize();
			velocity.mult(maxVel);
		}
		
		x += velocity.x * delta;
		y += velocity.y * delta;
	}

	public static void draw(float delta) {
		HvlPainter2D.hvlRotate(x, y, theta);
		HvlPainter2D.hvlDrawQuad(x - (playerSize / 2), y - (playerSize / 2), playerSize, playerSize, HvlTemplateInteg2D.getTexture(Main.playerIndex));
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
