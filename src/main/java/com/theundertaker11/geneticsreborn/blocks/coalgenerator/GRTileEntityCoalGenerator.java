package com.theundertaker11.geneticsreborn.blocks.coalgenerator;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class GRTileEntityCoalGenerator extends TileEntity implements IEnergyProvider, ITickable{

	public static int maxExtract = GeneticsReborn.CoalGeneratorMaxExtract;
	public static int capacity = GeneticsReborn.maxEnergyStored;
	public int ticksleft;
	private boolean active;
	private int energy;
	public short overclockers;
	
	public GRTileEntityCoalGenerator(){}
	
	@Override
	public void update()
	{
		if(this.getWorld().isRemote) 
			return;
		
		IItemHandler inv = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ItemStack stack = inv.getStackInSlot(0);
		if(!active&&stack!=null&&ModUtils.isValidFuel(stack))
		{
			this.active = true;
			this.ticksleft = TileEntityFurnace.getItemBurnTime(stack)/2;
			inv.extractItem(0, 1, false);
		}
		if(active&&capacity-energy>=getRfPerTick())
		{
			ticksleft--;
			this.energy+=getRfPerTick();
			
			if(this.energy>this.capacity)
				this.energy = this.capacity;
			if(ticksleft<1)
			{
				this.ticksleft = 0;
				this.active=false;
			}
		}
		BlockPos p = this.getPos();
		if(this.energy>0)
		{
			this.transferEnergy(p.up(), EnumFacing.DOWN);
			this.transferEnergy(p.down(), EnumFacing.UP);
			this.transferEnergy(p.east(), EnumFacing.WEST);
			this.transferEnergy(p.west(), EnumFacing.EAST);
			this.transferEnergy(p.north(), EnumFacing.SOUTH);
			this.transferEnergy(p.south(), EnumFacing.NORTH);
		}
	}
	
	public void transferEnergy(BlockPos target, EnumFacing targetFace)
	{
		IBlockState targetState = this.getWorld().getBlockState(target);
		if(targetState instanceof cofh.api.energy.IEnergyHandler)
		{
			int maxAvailable = this.extractEnergy(null, this.maxExtract, true);
			int energyTransferred = ((cofh.api.energy.IEnergyHandler)targetState).receiveEnergy(targetFace, maxAvailable, false); 
			this.extractEnergy(null, energyTransferred, false);
		}
		if(targetState instanceof IEnergyStorage)
		{
			int maxAvailable = this.extractEnergy(null, this.maxExtract, true);
			int energyTransferred = ((IEnergyStorage)targetState).receiveEnergy(maxAvailable, false); 
			this.extractEnergy(null, energyTransferred, false);
		}
	}
	public int getRfPerTick()
	{
		return (GeneticsReborn.CoalGeneratorBaseRF+(GeneticsReborn.CoalGeneratorBaseRF*overclockers));
	}
	@Override
	public boolean canConnectEnergy(EnumFacing facing) {
		return true;
	}

	@Override
	public int extractEnergy(EnumFacing facing, int maxExtract, boolean simulate) {
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
		if (!simulate) {
			energy -= energyExtracted;
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored(EnumFacing facing) {
		return energy;
	}

	@Override
	public int getMaxEnergyStored(EnumFacing facing) {
		return capacity;
	}
	
	/**
	 * Adds an overclocker and removes 1 from the players inventory
	 * @param player
	 */
	public void addOverclocker(EntityPlayer player, int maxOverclockers)
	{
		if(this.overclockers<maxOverclockers)
		{
			this.overclockers++;
			player.getHeldItem(EnumHand.MAIN_HAND).stackSize -= 1;
			if(player.getHeldItem(EnumHand.MAIN_HAND).stackSize<1) player.setHeldItem(EnumHand.MAIN_HAND, null);
		}
		else player.addChatMessage(new TextComponentString("Max Overclockers is "+maxOverclockers));
	}

	private ItemStackHandler itemStackHandler = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot){
            markDirty();
        }
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            if (stack == null || stack.stackSize == 0)
                return null;
            if(!ModUtils.isValidFuel(stack))
            	return stack.copy();
            
            validateSlotIndex(slot);

            ItemStack existing = this.stacks[slot];

            int limit = getStackLimit(slot, stack);

            if (existing != null)
            {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                    return stack;

                limit -= existing.stackSize;
            }

            if (limit <= 0)
                return stack;

            boolean reachedLimit = stack.stackSize > limit;

            if (!simulate)
            {
                if (existing == null)
                {
                    this.stacks[slot] = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
                }
                else
                {
                    existing.stackSize += reachedLimit ? limit : stack.stackSize;
                }
                onContentsChanged(slot);
            }

            return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) : null;
        }
    };
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        	return (T)itemStackHandler;

        return super.getCapability(capability, facing);
    }
    @Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
	    return (oldState.getBlock() != newState.getBlock());
	}
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
        super.writeToNBT(compound);
        compound.setTag("inputitem", itemStackHandler.serializeNBT());
        compound.setInteger("ticksleft", ticksleft);
        compound.setInteger("Energy", this.energy);
        compound.setShort("overclockers", this.overclockers);
        return compound;
    }
    @Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (compound.hasKey("inputitem")){
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("inputitem"));
        }
		this.ticksleft = compound.getInteger("ticksleft");
		this.energy = compound.getInteger("Energy");
		this.overclockers = compound.getShort("overclockers");
		if (energy > capacity){
			energy = capacity;
		}
	}
}
