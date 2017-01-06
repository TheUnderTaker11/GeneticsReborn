package com.theundertaker11.GeneticsReborn.tile;

import javax.annotation.Nullable;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GRTileEntityBasicEnergyReceiver extends TileEntity implements IEnergyReceiver{
	private final static int SIZE = 1;
	protected int maxOverclockers = 10;
	public static final int capacity = 20000;
	protected int energy;
	protected int maxReceive;
	protected int overclockers;
	protected int ticksCooking;
	
	public GRTileEntityBasicEnergyReceiver()
	{
		this.maxReceive = 20000;
	}
	//If you overwrite either of these make sure to call the super
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
        super.writeToNBT(compound);
        compound.setTag("inputitem", itemStackHandler.serializeNBT());
        compound.setTag("outputitem", itemStackHandlerOutput.serializeNBT());
        compound.setInteger("Energy", this.energy);
        compound.setInteger("overclockers", this.overclockers);
        return compound;
        
    }
	/**
	 * 
	 * @return Inventory size as set in the class
	 */
	public static int getSIZE()
	{
		return SIZE;
	}
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (compound.hasKey("inputitem")){
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("inputitem"));
        }
		if (compound.hasKey("outputitem")){
            itemStackHandlerOutput.deserializeNBT((NBTTagCompound) compound.getTag("outputitem"));
        }
		this.energy = compound.getInteger("Energy");
		this.overclockers = compound.getInteger("overclockers");
		if (energy > capacity){
			energy = capacity;
		}
		if(overclockers>maxOverclockers){
			overclockers=maxOverclockers;
		}
	}

	@Override
	public boolean canConnectEnergy(EnumFacing facing) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate) {
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) {
			energy += energyReceived;
		}
		markDirty();
		return energyReceived;
	}

	@Override
	public int getEnergyStored(EnumFacing facing) {
		return this.energy;
	}

	@Override
	public int getMaxEnergyStored(EnumFacing facing) {
		return this.capacity;
	}
	
	public double fractionOfEnergyRemaining()
	{
		double frac = 0;
		frac = (this.energy/this.capacity);
		return frac;
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
	
	public int getOverclockerCount()
	{
		return this.overclockers;
	}
	
	public boolean isUseableByPlayer(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }
	
	private ItemStackHandler itemStackHandler = 
    		new ItemStackHandler(SIZE){
        @Override
        protected void onContentsChanged(int slot){
            markDirty();
        }
    };
    private ItemStackHandler itemStackHandlerOutput = 
    		new ItemStackHandler(SIZE){
        @Override
        protected void onContentsChanged(int slot){
            markDirty();
        }
    };
    
	@Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(facing==EnumFacing.DOWN) return (T)itemStackHandlerOutput;
            else return (T) itemStackHandler;
        }
        return super.getCapability(capability, facing);
    }
    
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
      NBTTagCompound updateTagDescribingTileEntityState = getUpdateTag();
      final int METADATA = 0;
      return new SPacketUpdateTileEntity(this.pos, METADATA, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound updateTagDescribingTileEntityState = pkt.getNbtCompound();
      handleUpdateTag(updateTagDescribingTileEntityState);
    }

    /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
       Warning - although our getUpdatePacket() uses this method, vanilla also calls it directly, so don't remove it.
     */
    @Override
    public NBTTagCompound getUpdateTag()
    {
  		NBTTagCompound nbtTagCompound = new NBTTagCompound();
  		writeToNBT(nbtTagCompound);
      return nbtTagCompound;
    }

    /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
     Warning - although our onDataPacket() uses this method, vanilla also calls it directly, so don't remove it.
   */
    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
      this.readFromNBT(tag);
    }
}