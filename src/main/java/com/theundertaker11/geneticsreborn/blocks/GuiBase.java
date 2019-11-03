package com.theundertaker11.geneticsreborn.blocks;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiBase extends GuiContainer {
	protected GRTileEntityBasicEnergyReceiver te;
	protected ResourceLocation texture = null;
	protected int LABEL_XPOS = 8;
	protected int LABEL_YPOS = 5;
	protected int OVERCLOCKERCOUNT_XPOS = 8;
	protected int OVERCLOCKERCOUNT_YPOS = 74;
	protected int ENERGY_OFFSET_X = 0;
	protected int ENERGY_OFFSET_Y = 0;
	protected boolean hasProgressArrow = true;
	protected boolean useDefaultText = true;
	
	public GuiBase(Container c, GRTileEntityBasicEnergyReceiver te) {		
		super(c);
		this.te = te;
		xSize = 176;
		ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		renderEnergy();
		if (hasProgressArrow) {
			double cookProgress = te.percComplete();
			drawTexturedModalRect(guiLeft + 84, guiTop + 37, 178, 61,
					(int)(cookProgress * 22), 16);
		}

	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = (new TextComponentTranslation("tile."+te.getName()+".name")).getFormattedText();
		if (useDefaultText) {
			fontRenderer.drawString(name, LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
			fontRenderer.drawString(("Overclockers: "+te.getOverclockerCount()), OVERCLOCKERCOUNT_XPOS, OVERCLOCKERCOUNT_YPOS, Color.darkGray.getRGB());
		}

		if (hasProgressArrow) {
			List<String> hoveringText = new ArrayList<String>();
		
			if (isPointInRegion(guiLeft + 85, guiTop + 38, 22, 16, mouseX, mouseY)) {
				hoveringText.add("Progress:");
				int cookPercentage =(int)(te.percComplete() * 100);
				hoveringText.add(cookPercentage + "%");
			}
	
			if (!hoveringText.isEmpty())
				drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
		}

		if (this.isPointInRegion(9+ENERGY_OFFSET_X, 22+ENERGY_OFFSET_Y, 14, 42, mouseX, mouseY)) {
			List<String> energy = new ArrayList<String>();
			energy.add(te.getEnergyStored() + " / " + te.getMaxEnergyStored() + "  FE");
			GuiUtils.drawHoveringText(energy, mouseX - guiLeft, mouseY - guiTop, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}	

	public void renderEnergy() {
		renderEnergy(0, 0);
	}

	public void renderEnergy(int offsetX, int offsetY) {
		if (te.getEnergyStored() > 0) {
			int i = 40;
			int j = te.getEnergyStored() * i / te.getMaxEnergyStored();
			drawTexturedModalRect(guiLeft + ENERGY_OFFSET_X + 10, guiTop + ENERGY_OFFSET_Y + 63 - j, 178, 44 - j, 12, j);
		}
	}

}
