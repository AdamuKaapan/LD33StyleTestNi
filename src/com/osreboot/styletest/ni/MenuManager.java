package com.osreboot.styletest.ni;

import com.osreboot.ridhvl.HvlFontUtil;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class MenuManager {
	
	public static HvlFontPainter2D font;
	
	HvlMenu main;
	
	public static void initialize(){
		font = new HvlFontPainter2D(HvlTemplateInteg2D.getTexture(0), HvlFontUtil.SIMPLISTIC, 512, 512, 32, 32, 16);
		
		//HvlComponentDefault.setDefault(new HvlText);
	}
	
	public static void update(float delta){
		
	}
	

}
