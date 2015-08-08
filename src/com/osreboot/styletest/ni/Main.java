package com.osreboot.styletest.ni;

import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class Main extends HvlTemplateInteg2D {

	public static final int playerTextureIndex = 1;
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		super(60, 1280, 720, "LD33 Style Test - Ni", new HvlDisplayModeDefault());
	}

	@Override
	public void initialize(){
		
		MenuManager.initialize();
		
		getTextureLoader().loadResource("Player");
	}

	@Override
	public void update(float delta){
		MenuManager.update(delta);
	}

}
