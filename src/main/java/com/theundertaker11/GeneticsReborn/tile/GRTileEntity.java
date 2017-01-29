package com.theundertaker11.GeneticsReborn.tile;

import com.theundertaker11.GeneticsReborn.blocks.bloodpurifier.GRTileEntityBloodPurifier;
import com.theundertaker11.GeneticsReborn.blocks.cellanalyser.GRTileEntityCellAnalyser;
import com.theundertaker11.GeneticsReborn.blocks.cloningmachine.GRTileEntityCloningMachine;
import com.theundertaker11.GeneticsReborn.blocks.dnadecrypter.GRTileEntityDNADecrypter;
import com.theundertaker11.GeneticsReborn.blocks.dnaextractor.GRTileEntityDNAExtractor;
import com.theundertaker11.GeneticsReborn.blocks.plasmidinfuser.GRTileEntityPlasmidInfuser;
import com.theundertaker11.GeneticsReborn.blocks.plasmidinjector.GRTileEntityPlasmidInjector;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GRTileEntity {
	public static void regTileEntitys()
	{
		GameRegistry.registerTileEntity(GRTileEntityCellAnalyser.class, "GRTileEntityCellAnalyser");
		GameRegistry.registerTileEntity(GRTileEntityDNAExtractor.class, "GRTileEntityDNAExtractor");
		GameRegistry.registerTileEntity(GRTileEntityDNADecrypter.class, "GRTileEntityDNADecrypter");
		GameRegistry.registerTileEntity(GRTileEntityPlasmidInfuser.class, "GRTileEntityPlasmidInfuser");
		GameRegistry.registerTileEntity(GRTileEntityBloodPurifier.class, "GRTileEntityBloodPurifier");
		GameRegistry.registerTileEntity(GRTileEntityPlasmidInjector.class, "GRTileEntityPlasmidInjector");
		GameRegistry.registerTileEntity(GRTileEntityCloningMachine.class, "GRTileEntityCloningMachine");
	}
}
