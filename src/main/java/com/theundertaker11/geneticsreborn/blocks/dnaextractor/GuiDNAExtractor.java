package com.theundertaker11.geneticsreborn.blocks.dnaextractor;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.blocks.GuiBase;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDNAExtractor extends GuiBase {
	public GuiDNAExtractor(InventoryPlayer invPlayer, GRTileEntityDNAExtractor tileInventory) {
		super(new ContainerDNAExtractor(invPlayer, tileInventory), tileInventory);
		texture = new ResourceLocation(Reference.MODID, "textures/gui/guidnaextractor.png");
	}
}
