package com.theundertaker11.geneticsreborn.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class InvisibleButton extends GuiButton {

	public InvisibleButton(int buttonId, int x, int y, int widthIn, int heightIn) {
		super(buttonId, x, y, widthIn, heightIn, "");
		visible = false;
	}
	
	@Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

}
