package com.theundertaker11.geneticsreborn.blocks.cellanalyser;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;
import com.theundertaker11.geneticsreborn.gui.GuiHandler;
import com.theundertaker11.geneticsreborn.items.GRItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CellAnalyser extends StorageBlockBase {

	public CellAnalyser(String name) {
		super(name);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new GRTileEntityCellAnalyser();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) return true;
		TileEntity tEntity = worldIn.getTileEntity(pos);

		if (tEntity != null && tEntity instanceof GRTileEntityCellAnalyser && hand == EnumHand.MAIN_HAND) {
			if (playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem() == GRItems.Overclocker) {
				GRTileEntityCellAnalyser tile = (GRTileEntityCellAnalyser) tEntity;
				tile.addOverclocker(playerIn, GeneticsReborn.ocCellAnalyser);
			} else
				playerIn.openGui(GeneticsReborn.instance, GuiHandler.CellAnalyserGuiID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
