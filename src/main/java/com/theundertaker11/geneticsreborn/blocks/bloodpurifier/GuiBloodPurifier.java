package com.theundertaker11.geneticsreborn.blocks.bloodpurifier;
import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.blocks.GuiBase;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiBloodPurifier extends GuiBase {

	public GuiBloodPurifier(InventoryPlayer invPlayer, GRTileEntityBloodPurifier tileInventory){
		super(new ContainerBloodPurifier(invPlayer, tileInventory), tileInventory);
		texture = new ResourceLocation(Reference.MODID, "textures/gui/guibasicmachine_bg.png");
	}


}
