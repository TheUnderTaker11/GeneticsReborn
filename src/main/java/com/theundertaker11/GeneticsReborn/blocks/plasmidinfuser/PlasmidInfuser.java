package com.theundertaker11.geneticsreborn.blocks.plasmidinfuser;

import javax.annotation.Nullable;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;
import com.theundertaker11.geneticsreborn.gui.GuiHandler;
import com.theundertaker11.geneticsreborn.items.GRItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class PlasmidInfuser extends StorageBlockBase{
	
	public PlasmidInfuser(String name){
		super(name);
	}
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
    {
		return new GRTileEntityPlasmidInfuser();
    }
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote) return true;
		TileEntity tEntity = worldIn.getTileEntity(pos);
		
		if(tEntity!=null&&tEntity instanceof GRTileEntityPlasmidInfuser&&hand==EnumHand.MAIN_HAND)
		{
			if(playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem()==GRItems.Overclocker)
			{
				GRTileEntityPlasmidInfuser tile = (GRTileEntityPlasmidInfuser)tEntity;
				tile.addOverclocker(playerIn, GeneticsReborn.ocPlasmidInfuser);
			}
			else playerIn.openGui(GeneticsReborn.instance, GuiHandler.PlasmidInfuserGuiID, worldIn, pos.getX(), pos.getY(), pos.getZ()); 
		}
		return true;
	}
}