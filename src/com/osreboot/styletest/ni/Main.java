package com.osreboot.styletest.ni;

import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.HvlSongPlayer;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class Main extends HvlTemplateInteg2D {

	public static final int fontIndex = 0, playerIndex = 1, tilemapIndex = 2, fontSmallIndex = 3, buttonIndex = 4, buttonHoverIndex = 5, buttonPressedIndex = 6, backgroundIndex = 7, waypointIndex = 8,
			pointerIndex = 9;
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		super(60, 1280, 720, "LD33 Style Test - Ni", new HvlDisplayModeDefault());
	}

	@Override
	public void initialize(){
		getTextureLoader().loadResource("Font");
		getTextureLoader().loadResource("Player");
		getTextureLoader().loadResource("Tilesheet");
		getTextureLoader().loadResource("FontSmall");
		getTextureLoader().loadResource("ButtonIdle");
		getTextureLoader().loadResource("ButtonHover");
		getTextureLoader().loadResource("ButtonPressed");
		getTextureLoader().loadResource("MenuBackground");
		getTextureLoader().loadResource("Checkpoint");
		getTextureLoader().loadResource("Pointer");
		
		getSoundLoader().loadResource("Electric reign");
		getSoundLoader().loadResource("Slide Velocity");
		getSoundLoader().loadResource("Intro to electric regin");
		getSoundLoader().loadResource("GateHit2");
		
		Game.setCurrentLevel("TestLevel");
		Game.initialize();
		
		MenuManager.initialize();
		HvlSongPlayer.addSong(MenuManager.main, getSound(1));
		HvlSongPlayer.addMenuChild(MenuManager.main, MenuManager.levels);
		
		HvlSongPlayer.addSong(MenuManager.game, getSound(0));
	}

	@Override
	public void update(float delta){
		MenuManager.update(delta);
	}

}
