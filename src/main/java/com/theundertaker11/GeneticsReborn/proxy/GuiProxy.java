package com.theundertaker11.GeneticsReborn.proxy;

import com.theundertaker11.GeneticsReborn.blocks.cellanalyser.ContainerCellAnalyser;
import com.theundertaker11.GeneticsReborn.blocks.cellanalyser.GRTileEntityCellAnalyser;
import com.theundertaker11.GeneticsReborn.blocks.cellanalyser.GuiCellAnalyser;

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
        //Add new if's for each gui
        return null;
    }
}
