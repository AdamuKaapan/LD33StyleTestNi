package com.osreboot.styletest.ni;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlTextureUtil;
import com.osreboot.ridhvl.painter.HvlCamera;
import com.osreboot.ridhvl.painter.HvlCamera.HvlCameraAlignment;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;
import com.osreboot.ridhvl.tile.HvlTile;
import com.osreboot.ridhvl.tile.collection.HvlSimpleTile;

public class Game {

	public static class Checkpoint {
		public int x, y;
		
		public Checkpoint(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	public static HvlLayeredTileMap map;

	public static String currentLevel;

	private static Map<Integer, List<Checkpoint>> checkpoints;
	
	private static int currentCheckpoint;
	
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
		checkpoints = new HashMap<>();
		for (int x = 0; x < map.getLayer(2).getMapWidth(); x++)
		{
			for (int y = 0; y < map.getLayer(2).getMapHeight(); y++)
			{
				HvlTile get = map.getLayer(2).getTile(x,  y);
				if (get == null) continue;
				
				HvlSimpleTile tile = (HvlSimpleTile) get;
				
				int waypointIndex = tile.getTile() - 240;
				
				if (waypointIndex < 0) continue;
				
				if (!checkpoints.containsKey(waypointIndex))
					checkpoints.put(waypointIndex, new LinkedList<Game.Checkpoint>());
				
				checkpoints.get(waypointIndex).add(new Game.Checkpoint(x, waypointIndex));
			}
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
		HvlPainter2D.hvlDrawQuad(-32, -32, 64, 64, 0, 0, 1, 1, HvlTextureUtil.getColoredRect(64, 64, Color.white));
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
