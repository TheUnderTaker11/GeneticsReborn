package com.theundertaker11.geneticsreborn.blocks.bloodpurifier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.theundertaker11.geneticsreborn.Reference;


@SideOnly(Side.CLIENT)
public class GuiBloodPurifier extends GuiContainer {

	// This is the resource location for the background image
	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/guibasicmachine_bg.png");

	private GRTileEntityBloodPurifier tileEntity;

	public GuiBloodPurifier(InventoryPlayer invPlayer, GRTileEntityBloodPurifier tileInventory){
		super(new ContainerBloodPurifier(invPlayer, tileInventory));

		// Set the width and height of the gui
		xSize = 176;
		ySize = 207;

		this.tileEntity = tileInventory;
	}

	// some [x,y] coordinates of graphical elements
	final int COOK_BAR_XPOS = 49;
	final int COOK_BAR_YPOS = 60;
	final int COOK_BAR_ICON_U = 0;   // texture position of white arrow icon
	final int COOK_BAR_ICON_V = 207;
	final int COOK_BAR_WIDTH = 80;
	final int COOK_BAR_HEIGHT = 17;


	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		// Bind the image texture
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		// get cook progress as a double between 0 and 1
		double cookProgress = tileEntity.percComplete();
		// draw the cook progress bar
		drawTexturedModalRect(guiLeft + COOK_BAR_XPOS, guiTop + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V,
						              (int)(cookProgress * COOK_BAR_WIDTH), COOK_BAR_HEIGHT);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		fontRenderer.drawString("Blood Purifier", LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
		
		final int POWER_XPOS = 35;
		final int POWER_YPOS = 40;
		fontRenderer.drawString(("Power: "+tileEntity.storage.getEnergyStored()+"/"+tileEntity.storage.getMaxEnergyStored()+" RF"), POWER_XPOS, POWER_YPOS, Color.darkGray.getRGB());
		
		final int OVERCLOCKERCOUNT_XPOS = 36;
		final int OVERCLOCKERCOUNT_YPOS = 87;
		fontRenderer.drawString(("Overclockers: "+tileEntity.getOverclockerCount()), OVERCLOCKERCOUNT_XPOS, OVERCLOCKERCOUNT_YPOS, Color.darkGray.getRGB());
		
		List<String> hoveringText = new ArrayList<String>();

		// If the mouse is over the progress bar add the progress bar hovering text
		if (isInRect(guiLeft + COOK_BAR_XPOS, guiTop + COOK_BAR_YPOS, COOK_BAR_WIDTH, COOK_BAR_HEIGHT, mouseX, mouseY))
		{
			hoveringText.add("Progress:");
			int cookPercentage =(int)(tileEntity.percComplete() * 100);
			hoveringText.add(cookPercentage + "%");
		}

		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
		}

	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}
}
