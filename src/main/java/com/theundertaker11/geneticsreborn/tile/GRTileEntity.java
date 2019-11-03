package com.theundertaker11.geneticsreborn.tile;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.GRTileEntityLightBlock;
import com.theundertaker11.geneticsreborn.blocks.airdispersal.GRTileEntityAirDispersal;
import com.theundertaker11.geneticsreborn.blocks.bloodpurifier.GRTileEntityBloodPurifier;
import com.theundertaker11.geneticsreborn.blocks.cellanalyser.GRTileEntityCellAnalyser;
import com.theundertaker11.geneticsreborn.blocks.cloningmachine.GRTileEntityCloningMachine;
import com.theundertaker11.geneticsreborn.blocks.coalgenerator.GRTileEntityCoalGenerator;
import com.theundertaker11.geneticsreborn.blocks.dnadecrypter.GRTileEntityDNADecrypter;
import com.theundertaker11.geneticsreborn.blocks.dnaextractor.GRTileEntityDNAExtractor;
import com.theundertaker11.geneticsreborn.blocks.incubator.GRTileEntityIncubator;
import com.theundertaker11.geneticsreborn.blocks.plasmidinfuser.GRTileEntityPlasmidInfuser;
import com.theundertaker11.geneticsreborn.blocks.plasmidinjector.GRTileEntityPlasmidInjector;
import com.theundertaker11.geneticsreborn.potions.GREntityPotion;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
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
		GameRegistry.registerTileEntity(GRTileEntityLightBlock.class, "GRTileEntityLightBlock");
		GameRegistry.registerTileEntity(GRTileEntityIncubator.class, "GRTileEntityIncubator");
		GameRegistry.registerTileEntity(GRTileEntityAirDispersal.class, "GRTileEntityAirDispersal");
		
		EntityRegistry.registerModEntity(new ResourceLocation("viralpotion"), GREntityPotion.class, "viralpotion", 1, GeneticsReborn.instance, 80, 10, true);

	}
}
