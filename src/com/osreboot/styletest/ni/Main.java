package com.osreboot.styletest.ni;

import org.newdawn.slick.openal.SoundStore;

import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class Main extends HvlTemplateInteg2D {

	public static final int fontIndex = 0, playerIndex = 1, tilemapIndex = 2, fontSmallIndex = 3, buttonIndex = 4, buttonHoverIndex = 5, buttonPressedIndex = 6, backgroundIndex = 7, waypointIndex = 8,
			pointerIndex = 9;
	
	public static final float introLength = 5.266310f;
	
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
		
		Game.setCurrentLevel("TestLevel.map");
		Game.initialize();
		
		MenuManager.initialize();
		
		getSoundLoader().loadResource("Electric reign");
		getSoundLoader().loadResource("Slide Velocity");
		getSoundLoader().loadResource("Intro to electric regin");
	}

	@Override
	public void update(float delta){
		SoundStore.get().poll(0);
		if(HvlMenu.getCurrent() != MenuManager.game){
			if(!getSound(1).isPlaying()) getSound(1).playAsSoundEffect(1, 1, false);
			if(getSound(0).isPlaying()) getSound(0).stop();
		}
		
		MenuManager.update(delta);
	}

}
