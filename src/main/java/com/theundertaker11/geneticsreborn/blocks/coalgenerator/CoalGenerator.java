package com.theundertaker11.geneticsreborn.blocks.coalgenerator;

import javax.annotation.Nullable;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;
import com.theundertaker11.geneticsreborn.gui.GuiHandler;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
/*
public class CoalGenerator extends StorageBlockBase{

	public CoalGenerator(String name) {
		super(name);
	}
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
    {
		return new GRTileEntityCoalGenerator();
    }
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote) return true;
		TileEntity tEntity = worldIn.getTileEntity(pos);
		ItemStack heldItem = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		if(tEntity!=null&&tEntity instanceof GRTileEntityCoalGenerator&&hand==EnumHand.MAIN_HAND)
		{
			GRTileEntityCoalGenerator tile = (GRTileEntityCoalGenerator)tEntity;
			IItemHandler inv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(!heldItem.isEmpty())
			{
				if(heldItem.getItem()==GRItems.Overclocker)
				{
					tile.addOverclocker(playerIn, GeneticsReborn.ocCoalGenerator);
				}
				else if(ModUtils.isValidFuel(heldItem))
				{
					
					if(!playerIn.isSneaking())
					{
						playerIn.setHeldItem(EnumHand.MAIN_HAND, inv.insertItem(0, heldItem, false));
					}
				}
			}
			else if(playerIn.isSneaking())
			{
				playerIn.setHeldItem(EnumHand.MAIN_HAND, inv.extractItem(0, inv.getStackInSlot(0).getCount(), false));
			}
			else{
				playerIn.sendMessage(new TextComponentString("Power Stored: "+tile.getEnergyStored(null)));
				playerIn.sendMessage(new TextComponentString("Max RF/t: "+tile.getRfPerTick()));
				playerIn.sendMessage(new TextComponentString("Ticks left on current fuel: "+tile.ticksleft));
			}
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        //All the null checks are just in case and probably not actually needed
        if (tile!=null&&tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)!=null&&tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)!=null
        	&&tile instanceof GRTileEntityCoalGenerator&&tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
        {
        	GRTileEntityCoalGenerator tileentity = (GRTileEntityCoalGenerator)tile;
        	IItemHandler input = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        	if(!input.getStackInSlot(0).isEmpty())
        	{
        		ItemStack inputstack = input.getStackInSlot(0);
        		EntityItem entityinput = new EntityItem(tileentity.getWorld(), tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), inputstack);
        		tileentity.getWorld().spawnEntity(entityinput);
        	}
        	
        	if(tileentity.overclockers>0)
        	{
            	ItemStack overclockers = new  ItemStack(GRItems.Overclocker, tileentity.overclockers);
            	EntityItem entityoverclockers = new EntityItem(tileentity.getWorld(), tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), overclockers);
        		tileentity.getWorld().spawnEntity(entityoverclockers);
        	}
        }
        super.breakBlock(worldIn, pos, state);
    }
}
*/