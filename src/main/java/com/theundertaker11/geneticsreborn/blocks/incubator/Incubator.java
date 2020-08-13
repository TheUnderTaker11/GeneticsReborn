package com.theundertaker11.geneticsreborn.blocks.incubator;

import java.util.Random;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.proxy.GuiProxy;
import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class Incubator extends StorageBlockBase {
	private boolean advanced;
	
	public Incubator(String name, boolean advanced) {
		super(name);
		this.advanced = advanced;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new GRTileEntityIncubator(getRegistryName().getResourcePath(), advanced);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return overclockerOrGUI(worldIn, pos, playerIn, hand, GeneticsReborn.ocIncubator, GuiProxy.IncubatorGuiID);
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return getStrongPower(blockState, blockAccess, pos, side);
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		//side is the side of the block facing me...so a hopper under me reports "up"
		if (side == EnumFacing.UP) return 0;
		TileEntity te = blockAccess.getTileEntity(pos);
		if (te instanceof GRTileEntityIncubator) {
			return ((GRTileEntityIncubator) te).isBrewComplete() ? 15 : 0;
		}
		return 0;
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	
	private void dropItem(TileEntity te, ItemStack stack) {
		if (stack.isEmpty()) return;
		EntityItem entityinput = new EntityItem(te.getWorld(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), stack);
		te.getWorld().spawnEntity(entityinput);		
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile != null && tile instanceof GRTileEntityIncubator) {
			GRTileEntityIncubator tileInventory = (GRTileEntityIncubator)tile;
			IItemHandler itemhandlerfuel = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
			IItemHandler itemhandlerinput = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			//This is handled by the base class Issue #196
			//IItemHandler itemhandleringredient = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
			
			dropItem(tileInventory, itemhandlerfuel.getStackInSlot(0));
			dropItem(tileInventory, itemhandlerinput.getStackInSlot(0));
			dropItem(tileInventory, itemhandlerinput.getStackInSlot(1));
			dropItem(tileInventory, itemhandlerinput.getStackInSlot(2));
			//dropItem(tileInventory, itemhandleringredient.getStackInSlot(0));
		}
		for (EnumFacing enumfacing : EnumFacing.values())
            worldIn.neighborChanged(pos.offset(enumfacing), this, pos);
        
		super.breakBlock(worldIn, pos, state);
	}
	
    @SideOnly(Side.CLIENT)
	@Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    	GRTileEntityIncubator te = (GRTileEntityIncubator)worldIn.getTileEntity(pos);
    	if (te != null && te.isBrewing()) {
            double d0 = (double)((float)pos.getX() + 0.4F + rand.nextFloat() * 0.2F);
            double d1 = (double)((float)pos.getY() + 0.7F + rand.nextFloat() * 0.3F);
            double d2 = (double)((float)pos.getZ() + 0.4F + rand.nextFloat() * 0.2F);
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);    		
    	}
    }	
}
