package com.theundertaker11.geneticsreborn.blocks.airdispersal;

import java.util.Random;

import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;
import com.theundertaker11.geneticsreborn.proxy.GuiProxy;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AirDispersal extends StorageBlockBase {
	public static final PropertyBool MASKED = PropertyBool.create("masked"); 
	
	
	public AirDispersal(String name) {
		super(name);		
		setDefaultState(blockState.getBaseState().withProperty(MASKED, false));
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new GRTileEntityAirDispersal(getRegistryName().getResourcePath());
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return overclockerOrGUI(worldIn, pos, playerIn, hand, 0, GuiProxy.AirDispersalGuiID);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		GRTileEntityAirDispersal te = (GRTileEntityAirDispersal)world.getTileEntity(pos);		
		if (te.isLocked()) te.throwPotion();
		super.breakBlock(world, pos, state);
	}
	
    @SideOnly(Side.CLIENT)
	@Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    	GRTileEntityAirDispersal te = (GRTileEntityAirDispersal)worldIn.getTileEntity(pos);
    	if (te.isRunning()) {
	        double d0 = (double)((float)pos.getX() + 0.4F + rand.nextFloat() * 0.2F);
	        double d1 = (double)((float)pos.getY() + 0.8F + rand.nextFloat() * 0.3F);
	        double d2 = (double)((float)pos.getZ() + 0.4F + rand.nextFloat() * 0.2F);
	        worldIn.spawnParticle(EnumParticleTypes.TOWN_AURA, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    	}    	
    }	

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return (state.getValue(MASKED) == true) ? EnumBlockRenderType.ENTITYBLOCK_ANIMATED : EnumBlockRenderType.MODEL;
    }    
    
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, MASKED);
	}	
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		GRTileEntityAirDispersal te = (GRTileEntityAirDispersal)worldIn.getTileEntity(pos);
		te.setLocked(true);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		GRTileEntityAirDispersal te = (GRTileEntityAirDispersal)worldIn.getTileEntity(pos);
		te.setLocked(true);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(world, pos, neighbor);
		GRTileEntityAirDispersal te = (GRTileEntityAirDispersal)world.getTileEntity(pos);
		te.setLocked(true);
	}
    
}
