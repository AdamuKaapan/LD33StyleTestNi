package com.osreboot.styletest.ni;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.painter.HvlCamera;
import com.osreboot.ridhvl.painter.HvlCamera.HvlCameraAlignment;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;

public class Game {

	public static HvlLayeredTileMap map;

	public static String currentLevel;

	public static void reset() {
		Player.reset();
	}

	public static void initialize() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("res/" + currentLevel));
			StringBuilder sb = new StringBuilder();
			String current;
			while ((current = reader.readLine()) != null) {
				sb.append(current);
				sb.append(System.lineSeparator());
			}
			Game.map = HvlLayeredTileMap.load(sb.toString(), HvlTemplateInteg2D.getTexture(Main.tilemapIndex), 0, 0, 64, 64);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		reset();
	}

	public static void update(float delta) {
		Player.update(delta);

		HvlCamera.setAlignment(HvlCameraAlignment.CENTER);
		HvlCamera.setPosition(Player.getX(), Player.getY());
	}

	public static void draw(float delta) {
		map.draw(delta);
		HvlPainter2D.hvlDrawQuad(-32, -32, 64, 64, Color.magenta);
		Player.draw(delta);
	}

	public static String getCurrentLevel() {
		return currentLevel;
	}

	public static void setCurrentLevel(String currentLevel) {
		Game.currentLevel = currentLevel;
	}

	public static HvlLayeredTileMap getMap() {
		return map;
	}

	public static boolean isTileInLocation(float x, float y, int... layers) {
		int tileX = (int) (x / map.getTileWidth());
		int tileY = (int) (y / map.getTileHeight());

		for (int layer : layers) {
			if (tileX < 0 || tileY < 0 || tileX >= map.getLayer(layer).getMapWidth() || tileY >= map.getLayer(layer).getMapHeight())
				continue;

			if (map.getLayer(layer).getTile(tileX, tileY) != null)
				return true;
		}

		return false;
	}
}
