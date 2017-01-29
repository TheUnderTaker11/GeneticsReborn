package com.theundertaker11.GeneticsReborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StorageBlockBase extends BlockBase{
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public StorageBlockBase(String name, Material material, float hardness, float resistance) 
	{
		super(name, material, hardness, resistance);
		this.isBlockContainer=true;
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
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
	//This should  be overridden in every actual block code.
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
    {
		return null;
    }
	
	/////////BEGIN CODE TO MAKE IT FACE TOWARDS THE PLAYER WHEN PLACED/////////
	@Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
    }
	
	public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
        return EnumFacing.getFacingFromVector(
             (float) (entity.posX - clickedBlock.getX()),
             0,//(float) (entity.posY - clickedBlock.getY()),
             (float) (entity.posZ - clickedBlock.getZ()));
    }

	@Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
    /////////END CODE TO MAKE IT FACE TOWARDS THE PLAYER WHEN PLACED/////////
}
