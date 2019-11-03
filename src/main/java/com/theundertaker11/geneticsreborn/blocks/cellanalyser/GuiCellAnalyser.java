package com.theundertaker11.geneticsreborn.blocks.cellanalyser;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.blocks.GuiBase;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCellAnalyser extends GuiBase {

	public GuiCellAnalyser(InventoryPlayer invPlayer, GRTileEntityCellAnalyser tileInventory) {
		super(new ContainerCellAnalyser(invPlayer, tileInventory), tileInventory);
		texture = new ResourceLocation(Reference.MODID, "textures/gui/guicellanalyser.png");
	}
}
