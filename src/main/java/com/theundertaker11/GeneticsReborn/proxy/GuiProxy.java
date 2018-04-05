package com.theundertaker11.geneticsreborn.proxy;

import com.theundertaker11.geneticsreborn.blocks.bloodpurifier.ContainerBloodPurifier;
import com.theundertaker11.geneticsreborn.blocks.bloodpurifier.GRTileEntityBloodPurifier;
import com.theundertaker11.geneticsreborn.blocks.bloodpurifier.GuiBloodPurifier;
import com.theundertaker11.geneticsreborn.blocks.cellanalyser.ContainerCellAnalyser;
import com.theundertaker11.geneticsreborn.blocks.cellanalyser.GRTileEntityCellAnalyser;
import com.theundertaker11.geneticsreborn.blocks.cellanalyser.GuiCellAnalyser;
import com.theundertaker11.geneticsreborn.blocks.cloningmachine.ContainerCloningMachine;
import com.theundertaker11.geneticsreborn.blocks.cloningmachine.GRTileEntityCloningMachine;
import com.theundertaker11.geneticsreborn.blocks.cloningmachine.GuiCloningMachine;
import com.theundertaker11.geneticsreborn.blocks.dnadecrypter.ContainerDNADecrypter;
import com.theundertaker11.geneticsreborn.blocks.dnadecrypter.GRTileEntityDNADecrypter;
import com.theundertaker11.geneticsreborn.blocks.dnadecrypter.GuiDNADecrypter;
import com.theundertaker11.geneticsreborn.blocks.dnaextractor.ContainerDNAExtractor;
import com.theundertaker11.geneticsreborn.blocks.dnaextractor.GRTileEntityDNAExtractor;
import com.theundertaker11.geneticsreborn.blocks.dnaextractor.GuiDNAExtractor;
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

public class GuiProxy implements IGuiHandler{

	@Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof GRTileEntityCellAnalyser)
        {
            return new ContainerCellAnalyser(player.inventory, (GRTileEntityCellAnalyser) te);
        }
        if(te instanceof GRTileEntityDNAExtractor)
        {
        	return new ContainerDNAExtractor(player.inventory, (GRTileEntityDNAExtractor) te);
        }
        if(te instanceof GRTileEntityDNADecrypter)
        {
        	return new ContainerDNADecrypter(player.inventory, (GRTileEntityDNADecrypter) te);
        }
        if(te instanceof GRTileEntityPlasmidInfuser)
        {
        	return new ContainerPlasmidInfuser(player.inventory, (GRTileEntityPlasmidInfuser) te);
        }
        if(te instanceof GRTileEntityBloodPurifier)
        {
        	return new ContainerBloodPurifier(player.inventory, (GRTileEntityBloodPurifier) te);
        }
        if(te instanceof GRTileEntityPlasmidInjector)
        {
        	return new ContainerPlasmidInjector(player.inventory, (GRTileEntityPlasmidInjector) te);
        }
        if(te instanceof GRTileEntityCloningMachine)
        {
        	return new ContainerCloningMachine(player.inventory, (GRTileEntityCloningMachine) te);
        }
       //Add new if's for each gui
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof GRTileEntityCellAnalyser)
        {
            return new GuiCellAnalyser(player.inventory, (GRTileEntityCellAnalyser) te);
        }
        if (te instanceof GRTileEntityDNAExtractor)
        {
            return new GuiDNAExtractor(player.inventory, (GRTileEntityDNAExtractor) te);
        }
        if (te instanceof GRTileEntityDNADecrypter)
        {
            return new GuiDNADecrypter(player.inventory, (GRTileEntityDNADecrypter) te);
        }
        if (te instanceof GRTileEntityPlasmidInfuser)
        {
            return new GuiPlasmidInfuser(player.inventory, (GRTileEntityPlasmidInfuser) te);
        }
        if (te instanceof GRTileEntityBloodPurifier)
        {
            return new GuiBloodPurifier(player.inventory, (GRTileEntityBloodPurifier) te);
        }
        if (te instanceof GRTileEntityPlasmidInjector)
        {
            return new GuiPlasmidInjector(player.inventory, (GRTileEntityPlasmidInjector) te);
        }
        if (te instanceof GRTileEntityCloningMachine)
        {
            return new GuiCloningMachine(player.inventory, (GRTileEntityCloningMachine) te);
        }
        //Add new if's for each gui
        return null;
    }
}
