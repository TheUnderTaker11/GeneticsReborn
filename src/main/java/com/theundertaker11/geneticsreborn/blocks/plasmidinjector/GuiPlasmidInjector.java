package com.theundertaker11.geneticsreborn.blocks.plasmidinjector;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.blocks.GuiBase;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPlasmidInjector extends GuiBase {

	public GuiPlasmidInjector(InventoryPlayer invPlayer, GRTileEntityPlasmidInjector tileInventory) {
		super(new ContainerPlasmidInjector(invPlayer, tileInventory), tileInventory);
		texture = new ResourceLocation(Reference.MODID, "textures/gui/guiplasmidinjector.png");
	}
}
