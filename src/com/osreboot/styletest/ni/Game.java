package com.osreboot.styletest.ni;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.osreboot.ridhvl.HvlCoord;
import com.osreboot.ridhvl.painter.HvlCamera;
import com.osreboot.ridhvl.painter.HvlCamera.HvlCameraAlignment;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;
import com.osreboot.ridhvl.tile.HvlTile;
import com.osreboot.ridhvl.tile.collection.HvlSimpleTile;

public class Game {
	
	public static final String level1 = "TestLevel.map";
	public static ArrayList<String> levels = new ArrayList<>();

	public static class Checkpoint {
		public static float distance = 128f;
		
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
	private static int currentLap;
	
	private static float time;
	
	public static void reset() {
		Player.reset();
		currentCheckpoint = 0;
		currentLap = 0;
		time = 0.0f;
	}

	public static void initialize() {
		levels.add(level1);
		updateLevel();
	}

	public static void updateLevel(){
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
				
				checkpoints.get(waypointIndex).add(new Game.Checkpoint(x, y));
			}
		}
		
		reset();
	}
	
	public static void update(float delta) {
		Player.update(delta);
		
		List<Checkpoint> currentChecks = checkpoints.get(currentCheckpoint);
		
		for (Checkpoint c : currentChecks)
		{
			float worldX = c.x * map.getTileWidth(), worldY = c.y * map.getTileHeight();
			
			HvlCoord dist = new HvlCoord(Player.getX() - worldX, Player.getY() - worldY);
			
			if (dist.length() < Checkpoint.distance)
			{
				System.out.println("Checkpoint!");
				currentCheckpoint++;
				if (currentCheckpoint >= checkpoints.size())
				{
					currentCheckpoint = 0;
					currentLap++;
					System.out.println("LAP " + currentLap + "!");
				}
			}
		}

		HvlCamera.setAlignment(HvlCameraAlignment.CENTER);
		HvlCamera.setPosition(Player.getX(), Player.getY());
	}

	public static void draw(float delta) {
		map.draw(delta);
		Player.draw(delta);
	}

	public static String getCurrentLevel() {
		return currentLevel;
	}

	public static void setCurrentLevel(String currentLevel) {
		Game.currentLevel = currentLevel;
		updateLevel();
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
