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
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GRTileEntityBasicEnergyReceiver extends TileEntity {
    private final static int SIZE = 1;
    public CustomEnergyStorage storage = new CustomEnergyStorage(GeneticsReborn.maxEnergyStored, 20000, 0);
    protected int overclockers;
    protected int ticksCooking;

    public GRTileEntityBasicEnergyReceiver() {
        super();
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

    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private ItemStackHandler itemStackHandlerOutput =
            new ItemStackHandler(SIZE) {
                @Override
                protected void onContentsChanged(int slot) {
                    markDirty();
                }
            };

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {//||capability==CapabilityEnergy.ENERGY) {
            return true;
        } else if (capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

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
}