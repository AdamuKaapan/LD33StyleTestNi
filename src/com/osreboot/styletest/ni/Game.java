package com.osreboot.styletest.ni;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;
import com.osreboot.ridhvl.tile.HvlTile;
import com.osreboot.ridhvl.tile.collection.HvlSimpleTile;
//github.com/AdamuKaapan/LD33StyleTestNi.git
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;
import com.osreboot.ridhvl.painter.HvlCamera;
import com.osreboot.ridhvl.painter.HvlCamera.HvlCameraAlignment;

public class Game {
	public static final int requiredLaps = 2;

	public static final String level1 = "TestLevel.map", level2 = "SlowAsMudBitch.map";
	public static ArrayList<String> levels = new ArrayList<>();

	public static class Checkpoint {
		public static float distance = 160f;

		public int x, y;

		public Checkpoint(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static HvlLayeredTileMap map;

	public static String currentLevel;

	private static Map<Integer, List<Checkpoint>> checkpoints;

	private static int currentCheckpoint;
	private static int currentLap;

	private static float circleAngle;

	private static float time, finished = 0;

	public static void reset() {
		Player.reset();
		currentCheckpoint = 0;
		currentLap = 0;
		time = 0.0f;
		circleAngle = 0.0f;
		time = -Main.introLength;
		finished = 0;
	}

	public static void initialize() {
		levels.clear();
		levels.add(level1);
		levels.add(level2);
		updateLevel();
	}

	public static void updateLevel() {
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
		for (int x = 0; x < map.getLayer(2).getMapWidth(); x++) {
			for (int y = 0; y < map.getLayer(2).getMapHeight(); y++) {
				HvlTile get = map.getLayer(2).getTile(x, y);
				if (get == null)
					continue;

				HvlSimpleTile tile = (HvlSimpleTile) get;

				int waypointIndex = tile.getTile() - 240;

				if (waypointIndex < 0)
					continue;

				if (!checkpoints.containsKey(waypointIndex))
					checkpoints.put(waypointIndex, new LinkedList<Game.Checkpoint>());

				checkpoints.get(waypointIndex).add(new Game.Checkpoint(x, y));

				Game.map.getLayer(2).setTile(x, y, null);
			}
		}

		reset();
	}

	public static void update(float delta) {
		if (HvlTemplateInteg2D.getSound(1).isPlaying()) HvlTemplateInteg2D.getSound(1).stop();
		if (time >= 0)
		{
			if (!HvlTemplateInteg2D.getSound(0).isPlaying())
				HvlTemplateInteg2D.getSound(0).playAsSoundEffect(1, 1, false);
		}
		else if (time >= -Main.introLength)
		{
			if (!HvlTemplateInteg2D.getSound(2).isPlaying())
				HvlTemplateInteg2D.getSound(2).playAsSoundEffect(1, 1, false);
		}
		
		circleAngle += 90.0f * delta;

		if (currentLap > requiredLaps)
			finished += delta;
		if (finished == 0)
			time += delta;
		else
			Player.setAllowInput(false);
		if (time > 0)
			Player.update(delta);

		if (finished > 5)
			HvlMenu.setCurrent(MenuManager.levels);

		List<Checkpoint> currentChecks = checkpoints.get(currentCheckpoint);

		for (Checkpoint c : currentChecks) {
			float worldX = c.x * map.getTileWidth(), worldY = c.y * map.getTileHeight();

			HvlCoord dist = new HvlCoord(Player.getX() - worldX, Player.getY() - worldY);

			if (dist.length() < Checkpoint.distance) {
				currentCheckpoint++;
				if (currentCheckpoint >= checkpoints.size()) {
					currentCheckpoint = 0;
					currentLap++;
				}
			}
		}

		HvlCamera.setAlignment(HvlCameraAlignment.CENTER);
		HvlCamera.setPosition(Player.getX(), Player.getY());
	}

	public static void draw(float delta) {
		map.draw(delta);

		List<Checkpoint> current = checkpoints.get(currentCheckpoint);
		int nextC = currentCheckpoint + 1;
		if (nextC >= checkpoints.size())
			nextC = 0;
		List<Checkpoint> next = checkpoints.get(nextC);

		float averagex = 0, averagey = 0;
		int count = 0;

		if (finished == 0) {
			for (Checkpoint c : current) {
				float x = c.x * map.getTileWidth();
				float y = c.y * map.getTileHeight();
				HvlPainter2D.hvlDrawQuad(x, y, map.getTileWidth(), map.getTileHeight(), 0, 0, 0.5f, 0.26f, HvlTemplateInteg2D.getTexture(Main.waypointIndex));

				HvlPainter2D.hvlRotate(x + (map.getTileWidth() / 2), y + (map.getTileHeight() / 2), circleAngle);
				HvlPainter2D.hvlDrawQuad(x + (map.getTileWidth() / 2) - Checkpoint.distance, y + (map.getTileHeight() / 2) - Checkpoint.distance,
						Checkpoint.distance * 2, Checkpoint.distance * 2, 0, 0.25f, 1f, 0.75f, HvlTemplateInteg2D.getTexture(Main.waypointIndex), new Color(1,
								1, 1, 0.2f));
				HvlPainter2D.hvlResetRotation();
				averagex += x;
				averagey += y;
				count++;
			}

			averagex /= count;
			averagey /= count;

			hvlRotate(Player.getX(), Player.getY(), (float) Math.toDegrees(Math.atan2(averagex - Player.getX(), Player.getY() - averagey)));
			hvlDrawQuad(Player.getX() - 64, Player.getY() - 192, 128, 128, HvlTemplateInteg2D.getTexture(Main.pointerIndex), new Color(1, 1, 1, 0.5f));
			hvlResetRotation();

			for (Checkpoint c : next) {

				float x = c.x * map.getTileWidth();
				float y = c.y * map.getTileHeight();
				HvlPainter2D.hvlDrawQuad(x, y, map.getTileWidth(), map.getTileHeight(), 0.5f, 0, 1, 0.26f, HvlTemplateInteg2D.getTexture(Main.waypointIndex),
						new Color(1, 1, 1, 0.5f));
			}
		}

		Player.draw(delta);

		if (time < 0 || finished != 0) {
			String timer = "" + Math.round(time * 10) / 10f;
			MenuManager.font.drawWord(timer, Player.getX() + -MenuManager.font.getLineWidth(timer), Player.getY() - 128, 2, Color.white);
		} else {
			String timer = "" + Math.round(time * 100) / 100f;
			MenuManager.fontSmall
					.drawWord(timer, Player.getX() - (Display.getWidth() / 2) + 64, Player.getY() - (Display.getHeight() / 2) + 64, 2, Color.white);
		}
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
