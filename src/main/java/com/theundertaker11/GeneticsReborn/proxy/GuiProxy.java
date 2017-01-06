package com.theundertaker11.GeneticsReborn.proxy;

import com.theundertaker11.GeneticsReborn.blocks.bloodpurifier.ContainerBloodPurifier;
import com.theundertaker11.GeneticsReborn.blocks.bloodpurifier.GRTileEntityBloodPurifier;
import com.theundertaker11.GeneticsReborn.blocks.bloodpurifier.GuiBloodPurifier;
import com.theundertaker11.GeneticsReborn.blocks.cellanalyser.ContainerCellAnalyser;
import com.theundertaker11.GeneticsReborn.blocks.cellanalyser.GRTileEntityCellAnalyser;
import com.theundertaker11.GeneticsReborn.blocks.cellanalyser.GuiCellAnalyser;
import com.theundertaker11.GeneticsReborn.blocks.dnadecrypter.ContainerDNADecrypter;
import com.theundertaker11.GeneticsReborn.blocks.dnadecrypter.GRTileEntityDNADecrypter;
import com.theundertaker11.GeneticsReborn.blocks.dnadecrypter.GuiDNADecrypter;
import com.theundertaker11.GeneticsReborn.blocks.dnaextractor.ContainerDNAExtractor;
import com.theundertaker11.GeneticsReborn.blocks.dnaextractor.GRTileEntityDNAExtractor;
import com.theundertaker11.GeneticsReborn.blocks.dnaextractor.GuiDNAExtractor;
import com.theundertaker11.GeneticsReborn.blocks.plasmidinfuser.ContainerPlasmidInfuser;
import com.theundertaker11.GeneticsReborn.blocks.plasmidinfuser.GRTileEntityPlasmidInfuser;
import com.theundertaker11.GeneticsReborn.blocks.plasmidinfuser.GuiPlasmidInfuser;

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
        //Add new if's for each gui
        return null;
    }
}
