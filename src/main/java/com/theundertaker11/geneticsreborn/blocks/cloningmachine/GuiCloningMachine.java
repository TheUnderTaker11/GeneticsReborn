package com.theundertaker11.geneticsreborn.blocks.cloningmachine;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.blocks.GuiBase;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCloningMachine extends GuiBase {

	public GuiCloningMachine(InventoryPlayer invPlayer, GRTileEntityCloningMachine tileInventory) {
		super(new ContainerCloningMachine(invPlayer, tileInventory), tileInventory);
		texture = new ResourceLocation(Reference.MODID, "textures/gui/guibasicmachine_bg.png");
	}
}
