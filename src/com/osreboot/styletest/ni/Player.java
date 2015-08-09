package com.osreboot.styletest.ni;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.*;

import com.osreboot.ridhvl.HvlCoord;
import com.osreboot.ridhvl.input.HvlInputSeriesAction;
import com.osreboot.ridhvl.painter.HvlAnimatedTextureUV;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;
import com.osreboot.ridhvl.tile.HvlTile;
import com.osreboot.ridhvl.tile.collection.HvlSimpleTile;

public class Player {

	public static final float playerSize = 64f;
	public static final float accel = 512;
	public static final float motionDecay = -0.5f, stationaryDecay = -1f;
	
	public static final float maxRot = 5000f;
	
	public static final float maxVel = 2048f;
	
	private static float x, y;
	
	private static float theta;
	private static float rotVel;
	
	private static HvlCoord velocity;
	
	private static HvlAnimatedTextureUV drawing;
	private static boolean allowInput = true;
	
	public static void reset() {
		drawing = new HvlAnimatedTextureUV(HvlTemplateInteg2D.getTexture(Main.playerIndex), 32, 8, 0.1f);
		drawing.setRunning(false);
		
		int startX = -1;
		int startY = -1;
		for (int x = 0; x < Game.map.getLayer(2).getMapWidth(); x++)
		{
			for (int y = 0; y < Game.map.getLayer(2).getMapHeight(); y++)
			{
				HvlTile t = Game.map.getLayer(2).getTile(x, y);
				if (t == null) continue;
				
				if (!(t instanceof HvlSimpleTile)) continue;
				
				HvlSimpleTile tile = (HvlSimpleTile) t;
				
				if (tile.getTile() == 239)
				{
					startX = x;
					startY = y;
					break;
				}
			}
		}
		
		x = startX * Game.map.getTileWidth() + (playerSize / 2);
		y = startY * Game.map.getTileHeight() + (playerSize / 2);
		velocity = new HvlCoord(0, 0);
		theta = 90;
		allowInput = true;
	}

	public static void update(float delta) {

		float xIn = allowInput ? HvlInputSeriesAction.RIGHT.getCurrentOutput() - HvlInputSeriesAction.LEFT.getCurrentOutput() : 0;
		float yIn = allowInput ? HvlInputSeriesAction.DOWN.getCurrentOutput() - HvlInputSeriesAction.UP.getCurrentOutput() : 0;
		
		rotVel += xIn * delta;
		rotVel = Math.min(Math.max(rotVel, -maxVel), rotVel);
		rotVel *= Math.pow(Math.E, delta * -2);
		theta += Math.min(maxRot, Math.max(-maxRot, rotVel * (velocity.length() < 16 ? 0 : (velocity.length() < 32 ? 128 : (velocity.length() < 64 ? 256 : 512))))) * delta;
		
		HvlCoord input = new HvlCoord((float)Math.cos(Math.toRadians(theta + 90)) * yIn, (float)Math.sin(Math.toRadians(theta + 90)) * yIn);
		input.normalize();
		
		input.mult(accel);
		input.mult(delta);
	
		input.x = Float.isNaN(input.x) ? 0 : input.x;
		input.y = Float.isNaN(input.y) ? 0 : input.y;
				
		float bounce = 0.15f;
		
		velocity.add(input);
		
		float adjX = x + (velocity.x * delta), adjY = y + (velocity.y * delta);
		float wallGrind = -1f;
		
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
		float adj = theta;
		
		int frame = (int) (adj / 90f) * 2;
		
		drawing.setCurrentFrame(frame);
		
		hvlRotate(x, y, (adj%90));
		HvlPainter2D.hvlDrawQuad(x - (playerSize / 2), y - (playerSize / 2), playerSize, playerSize, drawing);
		hvlResetRotation();
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

	public static boolean isAllowInput() {
		return allowInput;
	}

	public static void setAllowInput(boolean allowInputArg) {
		allowInput = allowInputArg;
	}
}
