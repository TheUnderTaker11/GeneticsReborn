package com.theundertaker11.geneticsreborn.blocks.incubator;

import java.util.Random;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;
import com.theundertaker11.geneticsreborn.proxy.GuiProxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
