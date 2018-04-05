package com.theundertaker11.geneticsreborn.tile;

import com.theundertaker11.geneticsreborn.blocks.bloodpurifier.GRTileEntityBloodPurifier;
import com.theundertaker11.geneticsreborn.blocks.cellanalyser.GRTileEntityCellAnalyser;
import com.theundertaker11.geneticsreborn.blocks.cloningmachine.GRTileEntityCloningMachine;
import com.theundertaker11.geneticsreborn.blocks.coalgenerator.GRTileEntityCoalGenerator;
import com.theundertaker11.geneticsreborn.blocks.dnadecrypter.GRTileEntityDNADecrypter;
import com.theundertaker11.geneticsreborn.blocks.dnaextractor.GRTileEntityDNAExtractor;
import com.theundertaker11.geneticsreborn.blocks.plasmidinfuser.GRTileEntityPlasmidInfuser;
import com.theundertaker11.geneticsreborn.blocks.plasmidinjector.GRTileEntityPlasmidInjector;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GRTileEntity {

	public static void regTileEntitys() {
		GameRegistry.registerTileEntity(GRTileEntityCellAnalyser.class, "GRTileEntityCellAnalyser");
		GameRegistry.registerTileEntity(GRTileEntityDNAExtractor.class, "GRTileEntityDNAExtractor");
		GameRegistry.registerTileEntity(GRTileEntityDNADecrypter.class, "GRTileEntityDNADecrypter");
		GameRegistry.registerTileEntity(GRTileEntityPlasmidInfuser.class, "GRTileEntityPlasmidInfuser");
		GameRegistry.registerTileEntity(GRTileEntityBloodPurifier.class, "GRTileEntityBloodPurifier");
		GameRegistry.registerTileEntity(GRTileEntityPlasmidInjector.class, "GRTileEntityPlasmidInjector");
		GameRegistry.registerTileEntity(GRTileEntityCloningMachine.class, "GRTileEntityCloningMachine");
		GameRegistry.registerTileEntity(GRTileEntityCoalGenerator.class, "GRTileEntityCoalGenerator");
	}
}
