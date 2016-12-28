package com.theundertaker11.GeneticsReborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class StorageBlockBase extends BlockBase{
	
	public StorageBlockBase(String name, Material material, float hardness, float resistance) 
	{
		super(name, material, hardness, resistance);
		this.isBlockContainer=true;
	}
	public StorageBlockBase(String name)
	{
		this(name, Material.IRON, 0.5f, 0.5f);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
    {
		//This should never actually be called since it should be overridden in every actual block code.
		return null;
    }
}
