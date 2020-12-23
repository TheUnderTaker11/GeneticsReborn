package com.theundertaker11.geneticsreborn.tile;

import javax.annotation.Nullable;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;
import com.theundertaker11.geneticsreborn.util.CustomEnergyStorage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GRTileEntityBasicEnergyReceiver extends TileEntity {
    private final static int SIZE = 1;
    protected CustomEnergyStorage storage = new CustomEnergyStorage(GeneticsReborn.maxEnergyStored, 20000);
    protected int overclockers;
    protected int ticksCooking;
    protected String name;

    public GRTileEntityBasicEnergyReceiver() {
    	
    }

    public GRTileEntityBasicEnergyReceiver(String name) {
        this.name = name;
    }
    
    public String getName() {
    	return name;
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inputitem", itemStackHandler.serializeNBT());
        compound.setTag("outputitem", itemStackHandlerOutput.serializeNBT());
        //compound.setInteger("Energy", this.storage.getEnergyStored());
        this.storage.writeToNBT(compound);
        compound.setInteger("overclockers", this.overclockers);
        return compound;
    }

    /**
     * @return Inventory size as set in the class
     */
    public static int getSIZE() {
        return SIZE;
    }

	public double getTotalTicks() {
		return 0;		
	}
    
	public double percComplete() {
		return (double) (this.ticksCooking / getTotalTicks());
	}
	
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("inputitem")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("inputitem"));
        }
        if (compound.hasKey("outputitem")) {
            itemStackHandlerOutput.deserializeNBT((NBTTagCompound) compound.getTag("outputitem"));
        }
        this.overclockers = compound.getInteger("overclockers");

        this.storage.readFromNBT(compound);
    }

    public double fractionOfEnergyRemaining() {
        return (this.storage.getEnergyStored() / this.storage.getEnergyStored());
    }

    /**
     * Adds an overclocker and removes 1 from the players inventory
     *
     * @param player
     */
    public void addOverclocker(EntityPlayer player, int maxOverclockers) {
        if (this.overclockers < maxOverclockers) {
            this.overclockers++;
            player.getHeldItem(EnumHand.MAIN_HAND).shrink(1);
        } else player.sendMessage(new TextComponentString("Max Overclockers is " + maxOverclockers));
    }

    public int getOverclockerCount() {
        return this.overclockers;
    }

    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    protected ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    protected ItemStackHandler itemStackHandlerOutput =
            new ItemStackHandler(SIZE) {
                @Override
                protected void onContentsChanged(int slot) {
                    markDirty();
                }
            };

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        if (capability == CapabilityEnergy.ENERGY) return true;
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.getWorld().getBlockState(this.pos).getBlock() instanceof StorageBlockBase) {
            EnumFacing rightSide = this.getWorld().getBlockState(this.pos).getValue(StorageBlockBase.FACING).rotateAround(Axis.Y).getOpposite();
            if (facing == EnumFacing.DOWN || facing == rightSide) return (T) itemStackHandlerOutput;
            else return (T) itemStackHandler;
        } else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) return (T) itemStackHandlerOutput;
            else return (T) itemStackHandler;
        } else if (capability == CapabilityEnergy.ENERGY) {
            return (T) storage;
        }

        return super.getCapability(capability, facing);
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound updateTagDescribingTileEntityState = getUpdateTag();
        final int METADATA = 0;
        return new SPacketUpdateTileEntity(this.pos, METADATA, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound updateTagDescribingTileEntityState = pkt.getNbtCompound();
        handleUpdateTag(updateTagDescribingTileEntityState);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }
    
	private static final byte TICKS_COOKING_FIELD_ID = 0;
	private static final byte ENERGY_STORED_FIELD_ID = 1;
	private static final byte OVERCLOCKERS_FIELD_ID = 2;

	protected byte NUMBER_OF_FIELDS = 3;

	public int getField(int id) {
		if (id == TICKS_COOKING_FIELD_ID) return ticksCooking;
		if (id == ENERGY_STORED_FIELD_ID) return this.storage.getEnergyStored();
		if (id == OVERCLOCKERS_FIELD_ID) return this.overclockers;
		
		if (id > NUMBER_OF_FIELDS) System.err.println("Invalid field ID in GRTileEntity.getField:" + id);
		return 0;
	}

	public void setField(int id, int value) {
		if (id == TICKS_COOKING_FIELD_ID) ticksCooking = (short) value;
		else if (id == ENERGY_STORED_FIELD_ID) this.storage.setEnergyStored((short) value);
		else if (id == OVERCLOCKERS_FIELD_ID) this.overclockers = (short) value;
		
    	if (id > NUMBER_OF_FIELDS) System.err.println("Invalid field ID in GRTileEntity.getField:" + id);
	}

	public int getFieldCount() {
		return NUMBER_OF_FIELDS;
	}

	public int getEnergyStored() {
		return storage.getEnergyStored();
	}

	public int getMaxEnergyStored() {
		return storage.getMaxEnergyStored();
	}    
}