package com.osreboot.styletest.ni;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlFontUtil;
import com.osreboot.ridhvl.menu.HvlComponentDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.HvlLabel;
import com.osreboot.ridhvl.menu.component.collection.HvlTextButton;
import com.osreboot.ridhvl.menu.component.collection.HvlTextureDrawable;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class MenuManager {
	
	public static HvlFontPainter2D font, fontSmall;
	
	public static HvlMenu main, game;
	
	public static void initialize(){
		font = new HvlFontPainter2D(HvlTemplateInteg2D.getTexture(Main.fontIndex), HvlFontUtil.SIMPLISTIC, 512, 512, 32, 32, 16);
		
		fontSmall = new HvlFontPainter2D(HvlTemplateInteg2D.getTexture(Main.fontSmallIndex), HvlFontUtil.SIMPLISTIC, 256, 256, 16, 16, 16);
		
		HvlComponentDefault.setDefault(new HvlLabel(font, "add text plz", Color.white, 2));
		HvlArrangerBox defaultArranger = new HvlArrangerBox(Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		defaultArranger.setAlign(0.5f);
		defaultArranger.setBorderU(16);
		defaultArranger.setBorderL(16);
		HvlComponentDefault.setDefault(defaultArranger);
		
		HvlTextButton defaultButton = new HvlTextButton(256, 64, new HvlTextureDrawable(Main.getTexture(Main.buttonIndex)), new HvlTextureDrawable(Main.getTexture(Main.buttonHoverIndex)), new HvlTextureDrawable(Main.getTexture(Main.buttonPressedIndex)), fontSmall, "no text d:", 2f, Color.white);
		defaultButton.setxAlign(0.5f);
		defaultButton.setyAlign(0.5f);
		HvlComponentDefault.setDefault(defaultButton);
		
		main = new HvlMenu();
		main.add(new HvlArrangerBox.Builder().build());
		main.getFirstChildOfType(HvlArrangerBox.class).add(new HvlLabel.Builder().setText("lost in chrome").build());
		main.getFirstChildOfType(HvlArrangerBox.class).add(new HvlTextButton.Builder().setText("play").build());
		
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
		
		HvlMenu.setCurrent(main);
	}
	
	public static void update(float delta){
		HvlMenu.updateMenus(delta);
	}
	

}
