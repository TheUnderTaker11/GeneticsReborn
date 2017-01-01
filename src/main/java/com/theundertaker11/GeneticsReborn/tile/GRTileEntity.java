package com.theundertaker11.GeneticsReborn.tile;

import com.theundertaker11.GeneticsReborn.blocks.cellanalyser.GRTileEntityCellAnalyser;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GRTileEntity {
	public static void regTileEntitys()
	{
		GameRegistry.registerTileEntity(GRTileEntityCellAnalyser.class, "GRTileEntityCellAnalyser");
		
	}
}
