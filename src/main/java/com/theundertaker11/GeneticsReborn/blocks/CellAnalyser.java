package com.theundertaker11.GeneticsReborn.blocks;

import com.theundertaker11.GeneticsReborn.tile.GRTileEntityCellAnalyser;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
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
}
