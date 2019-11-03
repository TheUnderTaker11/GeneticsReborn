package com.theundertaker11.geneticsreborn.blocks.bloodpurifier;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.proxy.GuiProxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BloodPurifier extends StorageBlockBase {

	public BloodPurifier(String name) {
		super(name);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new GRTileEntityBloodPurifier(getRegistryName().getResourcePath());
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) return true;
		TileEntity tEntity = worldIn.getTileEntity(pos);

		if (tEntity != null && tEntity instanceof GRTileEntityBloodPurifier && hand == EnumHand.MAIN_HAND) {
			if (playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem() == GRItems.Overclocker) {
				GRTileEntityBloodPurifier tile = (GRTileEntityBloodPurifier) tEntity;
				tile.addOverclocker(playerIn, GeneticsReborn.ocBloodPurifier);
			} else
				playerIn.openGui(GeneticsReborn.instance, GuiProxy.BloodPurifierGuiID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
