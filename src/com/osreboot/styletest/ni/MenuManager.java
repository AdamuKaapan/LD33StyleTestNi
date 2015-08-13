package com.osreboot.styletest.ni;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlFontUtil;
import com.osreboot.ridhvl.action.HvlAction1;
import com.osreboot.ridhvl.menu.HvlButtonMenuLink;
import com.osreboot.ridhvl.menu.HvlComponentDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.HvlButton;
import com.osreboot.ridhvl.menu.component.HvlComponentDrawable;
import com.osreboot.ridhvl.menu.component.HvlDrawableComponent;
import com.osreboot.ridhvl.menu.component.HvlLabel;
import com.osreboot.ridhvl.menu.component.HvlSpacer;
import com.osreboot.ridhvl.menu.component.collection.HvlLabeledButton;
import com.osreboot.ridhvl.menu.component.collection.HvlTextureDrawable;
import com.osreboot.ridhvl.painter.HvlCamera;
import com.osreboot.ridhvl.painter.HvlCamera.HvlCameraAlignment;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;
import com.osreboot.ridhvl.tile.HvlLayeredTileMap;

public class MenuManager {
	
	public static HvlFontPainter2D font, fontSmall;
	
	public static HvlMenu main, levels, game;
	
	private static HvlLayeredTileMap displayMap;
	
	public static void initialize(){
		font = new HvlFontPainter2D(HvlTemplateInteg2D.getTexture(Main.fontIndex), HvlFontUtil.SIMPLISTIC, 512, 512, 32, 32, 16);
		
		fontSmall = new HvlFontPainter2D(HvlTemplateInteg2D.getTexture(Main.fontSmallIndex), HvlFontUtil.SIMPLISTIC, 256, 256, 16, 16, 16);
		
		HvlComponentDefault.setDefault(new HvlLabel(font, "add text plz", Color.white, 2));
		HvlArrangerBox defaultArranger = new HvlArrangerBox(Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		defaultArranger.setAlign(0.5f);
		defaultArranger.setBorderU(16);
		defaultArranger.setBorderL(16);
		HvlComponentDefault.setDefault(defaultArranger);
		
		HvlLabeledButton defaultButton = new HvlLabeledButton(256, 64, new HvlTextureDrawable(Main.getTexture(Main.buttonIndex)), new HvlTextureDrawable(Main.getTexture(Main.buttonHoverIndex)), new HvlTextureDrawable(Main.getTexture(Main.buttonPressedIndex)), fontSmall, "no text d:", 2f, Color.white);
		defaultButton.setxAlign(0.5f);
		defaultButton.setyAlign(0.5f);
		HvlComponentDefault.setDefault(defaultButton);
		
		main = new HvlMenu(){
			@Override
			public void draw(float delta){
				doDefaultMenuUpdate(delta);
				super.draw(delta);
			}
		};
		levels = new HvlMenu(){
			@Override
			public void draw(float delta){
				doDefaultMenuUpdate(delta);
				super.draw(delta);
			}
		};
		game = new HvlMenu(){
			@Override
			public void update(float delta){
				Game.update(delta);
			}
			@Override
			public void draw(float delta){
				Game.draw(delta);
			}
		};
		
		main.add(new HvlArrangerBox.Builder().build());
		main.getFirstChildOfType(HvlArrangerBox.class).add(new HvlLabel.Builder().setText("lost in chrome").build());
		main.getFirstChildOfType(HvlArrangerBox.class).add(new HvlLabeledButton.Builder().setText("play").setClickedCommand(new HvlButtonMenuLink(levels)).build());
		main.getFirstChildOfType(HvlArrangerBox.class).add(new HvlLabeledButton.Builder().setText("quit").setClickedCommand(new HvlAction1<HvlButton>(){
			@Override
			public void run(HvlButton arg0Arg){
				System.exit(0);
			}
		}).build());
		
		levels.add(new HvlArrangerBox.Builder().build());
		levels.getFirstChildOfType(HvlArrangerBox.class).add(new HvlLabel.Builder().setText("levels").build());
		levels.add(new HvlLabeledButton.Builder().setText("previous").setClickedCommand(new HvlAction1<HvlButton>(){
			@Override
			public void run(HvlButton arg0Arg) {
				if(Game.levels.indexOf(Game.getCurrentLevel()) <= 0) Game.setCurrentLevel(Game.levels.get(Game.levels.size() - 1));
				else Game.setCurrentLevel(Game.levels.get(Game.levels.indexOf(Game.getCurrentLevel()) - 1));
				updateMinimap();
			}
		}).setX((Display.getWidth()/2) - 256 - 256 - 32).setY((Display.getHeight()/2) - 32).build());
		levels.add(new HvlLabeledButton.Builder().setText("next").setClickedCommand(new HvlAction1<HvlButton>(){
			@Override
			public void run(HvlButton arg0Arg) {
				if(Game.levels.indexOf(Game.getCurrentLevel()) + 1 >= Game.levels.size()) Game.setCurrentLevel(Game.levels.get(0));
				else Game.setCurrentLevel(Game.levels.get(Game.levels.indexOf(Game.getCurrentLevel()) + 1));
				updateMinimap();
			}
		}).setX((Display.getWidth()/2) + 256 + 32).setY((Display.getHeight()/2) - 32).build());
		levels.add(new HvlDrawableComponent(512, 512, new HvlComponentDrawable(){
			@Override
			public void draw(float x, float y, float w, float h, float delta){
				if (displayMap == null)
					updateMinimap();
				displayMap.setX((Display.getWidth()/2) - 256);
				displayMap.setY((Display.getHeight()/2) - 256);
				displayMap.draw(delta);
//				Game.map.setTileWidth(512f/Game.map.getLayer(0).getMapWidth());
//				Game.map.setTileHeight(512f/Game.map.getLayer(0).getMapHeight());
				//hvlDrawQuad((Display.getWidth()/2) - 256, (Display.getHeight()/2) - 256, 512, 512, Color.white);
			}
		}));
		levels.getFirstChildOfType(HvlArrangerBox.class).add(new HvlSpacer(0, 512 + 16));
		levels.getFirstChildOfType(HvlArrangerBox.class).add(new HvlLabeledButton.Builder().setText("play").setClickedCommand(new HvlAction1<HvlButton>(){
			@Override
			public void run(HvlButton arg0Arg){
				Game.initialize();
				HvlMenu.setCurrent(game);
			}
		}).build());
		levels.add(new HvlLabeledButton.Builder().setText("back").setX(16).setY(16).setClickedCommand(new HvlButtonMenuLink(main)).build());
		
		HvlMenu.setCurrent(main);
	}
	
	public static void update(float delta){
		HvlMenu.updateMenus(delta);
	}
	
	private static void doDefaultMenuUpdate(float delta){
		HvlCamera.setAlignment(HvlCameraAlignment.TOPLEFT);
		HvlCamera.setPosition(0, 0);
		hvlDrawQuad((Display.getWidth()/2) - 640, (Display.getHeight()/2) - 360, 1280, 720, HvlTemplateInteg2D.getTexture(Main.backgroundIndex));
	}
	
	private static void updateMinimap() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("res/" + Game.getCurrentLevel()));
			StringBuilder sb = new StringBuilder();
			String current;
			while ((current = reader.readLine()) != null) {
				sb.append(current);
				sb.append(System.lineSeparator());
			}
			displayMap = HvlLayeredTileMap.load(sb.toString(), HvlTemplateInteg2D.getTexture(Main.tilemapIndex), 0, 0, 64, 64);
			displayMap.setTileWidth(512f/displayMap.getLayer(0).getMapWidth());
			displayMap.setTileHeight(512f/displayMap.getLayer(0).getMapHeight());
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
