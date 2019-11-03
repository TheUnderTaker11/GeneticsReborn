package com.theundertaker11.geneticsreborn.blocks.incubator;

import java.io.IOException;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.blocks.GuiBase;
import com.theundertaker11.geneticsreborn.packets.GeneticsRebornPacketHandler;
import com.theundertaker11.geneticsreborn.packets.GuiMessage;
import com.theundertaker11.geneticsreborn.render.InvisibleButton;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiIncubator extends GuiBase {
	private GRTileEntityIncubator tileEntity;
	private int bubblePos = 0;

	public GuiIncubator(InventoryPlayer invPlayer, GRTileEntityIncubator tileInventory){
		super(new ContainerIncubator(invPlayer, tileInventory), tileInventory);
		this.tileEntity = tileInventory;
		texture = new ResourceLocation(Reference.MODID, "textures/gui/incubator.png");
		hasProgressArrow = false;
		ENERGY_OFFSET_Y = -8;
		ENERGY_OFFSET_X = 15;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new InvisibleButton(1, guiLeft+44, guiTop+14, 14, 33));
		this.buttonList.add(new InvisibleButton(2, guiLeft+64, guiTop+42, 18, 4));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		super.drawGuiContainerBackgroundLayer(partialTicks, x, y);
		
		if (tileEntity.hasPower()) {
			int offset = tileEntity.isLowTemp() ? 5 : 0;
			drawTexturedModalRect(guiLeft + 64, guiTop + 42, 177, 76 + offset, 18, 4);
		
			int speed = tileEntity.isLowTemp() ? 30 : 5;
			int b = tileEntity.isBrewing() ? 29 - MathHelper.floor((bubblePos / speed) % 29) : 29;
			drawTexturedModalRect(
					guiLeft + 67, guiTop + 12 + b, 
					186, 47 + b, 
					12, 29 - b);
			bubblePos++;
		}
				
		int b = MathHelper.floor(tileEntity.percComplete() * 29);
		drawTexturedModalRect(
				guiLeft + 101, guiTop + 14, 
				177, 47, 
				9, b);
		
	}
	
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id < 3) {
			GeneticsRebornPacketHandler.INSTANCE.sendToServer(new GuiMessage(tileEntity, 3, tileEntity.isLowTemp() ? 0 : 1));
		}
	}
}
