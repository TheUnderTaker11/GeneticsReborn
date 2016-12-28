package com.theundertaker11.GeneticsReborn.tile;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class GRTileEntity {
	public static void regTileEntitys()
	{
		GameRegistry.registerTileEntity(GRTileEntityCellAnalyser.class, "KSTileEntityHealingBlock");
	}
}
