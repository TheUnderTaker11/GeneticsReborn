package com.theundertaker11.geneticsreborn.blocks.cloningmachine;

import com.theundertaker11.geneticsreborn.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiCloningMachine extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/guibasicmachine_bg.png");
	private GRTileEntityCloningMachine tileEntity;

	public GuiCloningMachine(InventoryPlayer invPlayer, GRTileEntityCloningMachine tileInventory) {
		super(new ContainerCloningMachine(invPlayer, tileInventory));
		xSize = 176;
		ySize = 207;
		this.tileEntity = tileInventory;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		renderEnergy();
		double cookProgress = tileEntity.percComplete();
		drawTexturedModalRect(guiLeft + 85, guiTop + 38, 178, 61,
				(int) (cookProgress * 22), 16);

		if (this.isPointInRegion(9, 22, 14, 42, x, y)) {
			List<String> energy = new ArrayList<String>();
			energy.add(tileEntity.storage.getEnergyStored() + " / " + tileEntity.storage.getMaxEnergyStored() + "  FE");
			GuiUtils.drawHoveringText(energy, x, y, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		fontRenderer.drawString("Cloning Machine", LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());

		final int OVERCLOCKERCOUNT_XPOS = 36;
		final int OVERCLOCKERCOUNT_YPOS = 70;
		fontRenderer.drawString(("Overclockers: " + tileEntity.getOverclockerCount()), OVERCLOCKERCOUNT_XPOS, OVERCLOCKERCOUNT_YPOS, Color.darkGray.getRGB());

		List<String> hoveringText = new ArrayList<String>();

		if (isInRect(guiLeft + 85, guiTop + 38, 22, 16, mouseX, mouseY)) {
			hoveringText.add("Progress:");
			int cookPercentage = (int) (tileEntity.percComplete() * 100);
			hoveringText.add(cookPercentage + "%");
		}

		if (!hoveringText.isEmpty()) {
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
		}

	}

	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}

	public void renderEnergy() {
		if (tileEntity.storage.getEnergyStored() > 0) {
			int i = 40;
			int j = tileEntity.storage.getEnergyStored() * i / tileEntity.storage.getMaxEnergyStored();
			drawTexturedModalRect(guiLeft + 10, guiTop + 63 - j, 178, 44 - j, 12, j);
		}
	}
}
