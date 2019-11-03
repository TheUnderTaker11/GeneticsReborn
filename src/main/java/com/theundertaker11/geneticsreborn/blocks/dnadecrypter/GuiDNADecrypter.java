package com.theundertaker11.geneticsreborn.blocks.dnadecrypter;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.blocks.GuiBase;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiDNADecrypter extends GuiBase {

	public GuiDNADecrypter(InventoryPlayer invPlayer, GRTileEntityDNADecrypter tileInventory) {
		super(new ContainerDNADecrypter(invPlayer, tileInventory), tileInventory);
		texture = new ResourceLocation(Reference.MODID, "textures/gui/guidnadecrypter.png");
	}
}
