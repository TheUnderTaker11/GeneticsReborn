package com.theundertaker11.GeneticsReborn.blocks.cellanalyser;

import javax.annotation.Nullable;

import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.blocks.StorageBlockBase;
import com.theundertaker11.GeneticsReborn.gui.GuiHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CellAnalyser extends StorageBlockBase{

	public CellAnalyser(String name) {
		super(name);
	}
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
    {
		return new GRTileEntityCellAnalyser();
    }
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote) return true;
		TileEntity tEntity = worldIn.getTileEntity(pos);
		
		if(tEntity!=null&&tEntity instanceof GRTileEntityCellAnalyser)
		{
			playerIn.openGui(GeneticsReborn.instance, GuiHandler.CellAnalyserGuiID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
