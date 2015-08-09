package com.osreboot.styletest.ni;

import com.osreboot.ridhvl.HvlCoord;
import com.osreboot.ridhvl.input.HvlInputSeriesAction;
import com.osreboot.ridhvl.painter.HvlAnimatedTextureUV;
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
	
	private static HvlAnimatedTextureUV drawing;
	
	public static void reset() {
		drawing = new HvlAnimatedTextureUV(HvlTemplateInteg2D.getTexture(Main.playerIndex), 32, 8, 0.1f);
		drawing.setRunning(false);
		x = 5 * Game.map.getTileWidth() + (playerSize / 2);
		y = 6 * Game.map.getTileHeight() + (playerSize / 2);
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
		
		HvlCoord input = new HvlCoord((float)Math.cos(Math.toRadians(theta + 90)) * yIn, (float)Math.sin(Math.toRadians(theta + 90)) * yIn);
		input.normalize();
		
		input.mult(accel);
		input.mult(delta);
	
		input.x = Float.isNaN(input.x) ? 0 : input.x;
		input.y = Float.isNaN(input.y) ? 0 : input.y;
				
		float bounce = 0.15f;
		
		velocity.add(input);
		
		float adjX = x + (velocity.x * delta), adjY = y + (velocity.y * delta);
		float wallGrind = -3f;
		
		if (Game.isTileInLocation(adjX, adjY + (playerSize / 2), 1))
		{
			velocity.y *= -bounce;
			velocity.x *= Math.pow(Math.E, wallGrind * delta);
		}
		if (Game.isTileInLocation(adjX, adjY - (playerSize / 2), 1))
		{
			velocity.y *= -bounce;
			velocity.x *= Math.pow(Math.E, wallGrind * delta);
		}
		if (Game.isTileInLocation(adjX + (playerSize / 2), adjY, 1))
		{
			velocity.x *= -bounce;
			velocity.y *= Math.pow(Math.E, wallGrind * delta);
		}
		if (Game.isTileInLocation(adjX - (playerSize / 2), adjY, 1))
		{
			velocity.x *= -bounce;
			velocity.y *= Math.pow(Math.E, wallGrind * delta);
		}
		
		float root2 = (float) Math.sqrt(2) * 0.5f;
		
		if (Game.isTileInLocation(adjX + (playerSize * 0.5f * root2), adjY + (playerSize * 0.5f * root2), 1))
		{
			velocity.x *= -bounce * root2;
			velocity.y *= -bounce * root2;
			velocity.x *= Math.pow(Math.E, wallGrind * delta);
			velocity.y *= Math.pow(Math.E, -0.2f * delta);
		}
		if (Game.isTileInLocation(adjX + (playerSize * 0.5f * root2), adjY - (playerSize * 0.5f * root2), 1))
		{
			velocity.x *= -bounce * root2;
			velocity.y *= -bounce * root2;
			velocity.x *= Math.pow(Math.E, wallGrind * delta);
			velocity.y *= Math.pow(Math.E, -0.2f * delta);
		}
		if (Game.isTileInLocation(adjX - (playerSize * 0.5f * root2), adjY + (playerSize * 0.5f * root2), 1))
		{
			velocity.x *= -bounce * root2;
			velocity.y *= -bounce * root2;
			velocity.x *= Math.pow(Math.E, wallGrind * delta);
			velocity.y *= Math.pow(Math.E, -0.2f * delta);
		}
		if (Game.isTileInLocation(adjX - (playerSize * 0.5f * root2), adjY - (playerSize * 0.5f * root2), 1))
		{
			velocity.x *= -bounce * root2;
			velocity.y *= -bounce * root2;
			velocity.x *= Math.pow(Math.E, wallGrind * delta);
			velocity.y *= Math.pow(Math.E, -0.2f * delta);
		}

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
		float adj = theta - 22.5f;
		
		int frame = (int) (adj / 45.0f) + 1;
		
		drawing.setCurrentFrame(frame);
		
		HvlPainter2D.hvlDrawQuad(x - (playerSize / 2), y - (playerSize / 2), playerSize, playerSize, drawing);
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
