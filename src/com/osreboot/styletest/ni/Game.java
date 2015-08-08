package com.osreboot.styletest.ni;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.osreboot.ridhvl.template.HvlTemplateInteg2D;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;

public class Game {

	public static HvlLayeredTileMap map;

	public static String currentLevel;

	public static void reset() {
		Player.reset();
	}

	public static void initialize() {
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader("res/" + currentLevel));
//			StringBuilder sb = new StringBuilder();
//			String current;
//			while ((current = reader.readLine()) != null) {
//				sb.append(current);
//				sb.append(System.lineSeparator());
//			}
//			Game.map = HvlLayeredTileMap.load(sb.toString(), HvlTemplateInteg2D.getTexture(2), 0, 0, 128, 128);
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		reset();
	}

	public static void update(float delta) {
		Player.update(delta);
	}
	
	public static void draw(float delta) {
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
}
