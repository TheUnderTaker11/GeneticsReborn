package com.theundertaker11.geneticsreborn.proxy;

import com.theundertaker11.geneticsreborn.blocks.airdispersal.ContainerAirDispersal;
import com.theundertaker11.geneticsreborn.blocks.airdispersal.GRTileEntityAirDispersal;
import com.theundertaker11.geneticsreborn.blocks.airdispersal.GuiAirDispersal;
import com.theundertaker11.geneticsreborn.blocks.bloodpurifier.ContainerBloodPurifier;
import com.theundertaker11.geneticsreborn.blocks.bloodpurifier.GRTileEntityBloodPurifier;
import com.theundertaker11.geneticsreborn.blocks.bloodpurifier.GuiBloodPurifier;
import com.theundertaker11.geneticsreborn.blocks.cellanalyser.ContainerCellAnalyser;
import com.theundertaker11.geneticsreborn.blocks.cellanalyser.GRTileEntityCellAnalyser;
import com.theundertaker11.geneticsreborn.blocks.cellanalyser.GuiCellAnalyser;
import com.theundertaker11.geneticsreborn.blocks.cloningmachine.ContainerCloningMachine;
import com.theundertaker11.geneticsreborn.blocks.cloningmachine.GRTileEntityCloningMachine;
import com.theundertaker11.geneticsreborn.blocks.cloningmachine.GuiCloningMachine;
import com.theundertaker11.geneticsreborn.blocks.coalgenerator.ContainerCoalGenerator;
import com.theundertaker11.geneticsreborn.blocks.coalgenerator.GRTileEntityCoalGenerator;
import com.theundertaker11.geneticsreborn.blocks.coalgenerator.GuiCoalGenerator;
import com.theundertaker11.geneticsreborn.blocks.dnadecrypter.ContainerDNADecrypter;
import com.theundertaker11.geneticsreborn.blocks.dnadecrypter.GRTileEntityDNADecrypter;
import com.theundertaker11.geneticsreborn.blocks.dnadecrypter.GuiDNADecrypter;
import com.theundertaker11.geneticsreborn.blocks.dnaextractor.ContainerDNAExtractor;
import com.theundertaker11.geneticsreborn.blocks.dnaextractor.GRTileEntityDNAExtractor;
import com.theundertaker11.geneticsreborn.blocks.dnaextractor.GuiDNAExtractor;
import com.theundertaker11.geneticsreborn.blocks.incubator.ContainerIncubator;
import com.theundertaker11.geneticsreborn.blocks.incubator.GRTileEntityIncubator;
import com.theundertaker11.geneticsreborn.blocks.incubator.GuiIncubator;
import com.theundertaker11.geneticsreborn.blocks.plasmidinfuser.ContainerPlasmidInfuser;
import com.theundertaker11.geneticsreborn.blocks.plasmidinfuser.GRTileEntityPlasmidInfuser;
import com.theundertaker11.geneticsreborn.blocks.plasmidinfuser.GuiPlasmidInfuser;
import com.theundertaker11.geneticsreborn.blocks.plasmidinjector.ContainerPlasmidInjector;
import com.theundertaker11.geneticsreborn.blocks.plasmidinjector.GRTileEntityPlasmidInjector;
import com.theundertaker11.geneticsreborn.blocks.plasmidinjector.GuiPlasmidInjector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {
	public static final int CellAnalyserGuiID = 1;
	public static final int DNAExtractorGuiID = 2;
	public static final int DNADecrypterGuiID = 3;
	public static final int PlasmidInfuserGuiID = 4;
	public static final int BloodPurifierGuiID = 5;
	public static final int PlasmidInjectorGuiID = 6;
	public static final int CloningMachineGuiID = 7;
	public static final int CoalGeneratorGuiID = 8;
	public static final int IncubatorGuiID = 9;
	public static final int AirDispersalGuiID = 10;
	
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        
        switch (ID) {
        case CellAnalyserGuiID:
        	return new ContainerCellAnalyser(player.inventory, (GRTileEntityCellAnalyser) te);
        case DNAExtractorGuiID:
        	return new ContainerDNAExtractor(player.inventory, (GRTileEntityDNAExtractor) te);
        case DNADecrypterGuiID:
        	return new ContainerDNADecrypter(player.inventory, (GRTileEntityDNADecrypter) te);
        case PlasmidInfuserGuiID:
        	return new ContainerPlasmidInfuser(player.inventory, (GRTileEntityPlasmidInfuser) te);
        case BloodPurifierGuiID:
        	return new ContainerBloodPurifier(player.inventory, (GRTileEntityBloodPurifier) te);
        case PlasmidInjectorGuiID:
        	return new ContainerPlasmidInjector(player.inventory, (GRTileEntityPlasmidInjector) te);
        case CloningMachineGuiID:
        	return new ContainerCloningMachine(player.inventory, (GRTileEntityCloningMachine) te);
        case CoalGeneratorGuiID:
        	return new ContainerCoalGenerator(player.inventory, (GRTileEntityCoalGenerator) te);
        case IncubatorGuiID:
        	return new ContainerIncubator(player.inventory, (GRTileEntityIncubator) te);
        case AirDispersalGuiID:
        	return new ContainerAirDispersal(player.inventory, (GRTileEntityAirDispersal) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        
        switch (ID) {
        case CellAnalyserGuiID:
        	return new GuiCellAnalyser(player.inventory, (GRTileEntityCellAnalyser) te);
        case DNAExtractorGuiID:
        	return new GuiDNAExtractor(player.inventory, (GRTileEntityDNAExtractor) te);
        case DNADecrypterGuiID:
        	return new GuiDNADecrypter(player.inventory, (GRTileEntityDNADecrypter) te);
        case PlasmidInfuserGuiID:
        	return new GuiPlasmidInfuser(player.inventory, (GRTileEntityPlasmidInfuser) te);
        case BloodPurifierGuiID:
        	return new GuiBloodPurifier(player.inventory, (GRTileEntityBloodPurifier) te);
        case PlasmidInjectorGuiID:
        	return new GuiPlasmidInjector(player.inventory, (GRTileEntityPlasmidInjector) te);
        case CloningMachineGuiID:
        	return new GuiCloningMachine(player.inventory, (GRTileEntityCloningMachine) te);
        case CoalGeneratorGuiID:
        	return new GuiCoalGenerator(new ContainerCoalGenerator(player.inventory, (GRTileEntityCoalGenerator) te), (GRTileEntityCoalGenerator) te);
        case IncubatorGuiID:
        	return new GuiIncubator(player.inventory, (GRTileEntityIncubator) te);
        case AirDispersalGuiID:
        	return new GuiAirDispersal(player.inventory, (GRTileEntityAirDispersal) te);
        }
        
        return null;
    }
}
