package com.theundertaker11.geneticsreborn.blocks.coalgenerator;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.util.CustomEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class GRTileEntityCoalGenerator extends TileEntity implements ITickable {

    public CustomEnergyStorage storage = new CustomEnergyStorage(100000);
    public int generation = GeneticsReborn.CoalGeneratorBaseRF;
    public boolean isGenerating = false;
    public int currentItemBurnTime = 0;
    public int burnTime = 0;
    public boolean hasOverclocker = false;

    public ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        public void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            GRTileEntityCoalGenerator.this.markDirty();
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        isGenerating = nbt.getBoolean("isGenerating");
        currentItemBurnTime = nbt.getInteger("currentItemBurnTime");
        burnTime = nbt.getInteger("burnTime");
        hasOverclocker = nbt.getBoolean("hasOverclocker");
        generation = nbt.getInteger("generation");
        if (nbt.hasKey("items")) {
            inventory.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
        }

        storage.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("items", inventory.serializeNBT());
        nbt.setBoolean("isGenerating", isGenerating);
        nbt.setInteger("currentItemBurnTime", currentItemBurnTime);
        nbt.setInteger("burnTime", burnTime);
        nbt.setBoolean("hasOverclocker", hasOverclocker);
        nbt.setInteger("generation", generation);
        super.writeToNBT(nbt);
        return storage.writeToNBT(nbt);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }

        if (storage.getMaxEnergyStored() - storage.getEnergyStored() >= generation) {
            if (!isGenerating) {
                if (!inventory.getStackInSlot(0).isEmpty()) {
                    currentItemBurnTime = getItemBurnTime(inventory.getStackInSlot(0));
                    if (currentItemBurnTime > 0) {
                        if (inventory.getStackInSlot(0).getCount() > 1) {
                            inventory.getStackInSlot(0).shrink(1);
                        } else {
                            inventory.setStackInSlot(0, ItemStack.EMPTY);
                        }
                        isGenerating = true;
                        markDirty();
                    } else {
                        isGenerating = false;
                        markDirty();
                    }
                }
            } else {
                if (burnTime < currentItemBurnTime) {
                    burnTime++;
                    storage.receiveEnergy(generation, false);
                    markDirty();
                } else {
                    burnTime = 0;
                    isGenerating = false;
                    markDirty();
                }
            }
        }

        handleSendingEnergy();
    }

    public void addOverclocker() {
        if (!hasOverclocker) {
            hasOverclocker = true;
            generation = generation * 2;
            markDirty();
        }
    }

    public void removeOverclocker() {
        if (hasOverclocker) {
            hasOverclocker = false;
            generation = generation / 2;
            markDirty();
        }
    }


    public static int getItemBurnTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
            if (burnTime >= 0) return burnTime;
            Item item = stack.getItem();

            if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) {
                return 150;
            } else if (item == Item.getItemFromBlock(Blocks.WOOL)) {
                return 100;
            } else if (item == Item.getItemFromBlock(Blocks.CARPET)) {
                return 67;
            } else if (item == Item.getItemFromBlock(Blocks.LADDER)) {
                return 300;
            } else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) {
                return 100;
            } else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD) {
                return 300;
            } else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
                return 16000;
            } else if (item instanceof ItemTool && "WOOD".equals(((ItemTool) item).getToolMaterialName())) {
                return 200;
            } else if (item instanceof ItemSword && "WOOD".equals(((ItemSword) item).getToolMaterialName())) {
                return 200;
            } else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe) item).getMaterialName())) {
                return 200;
            } else if (item == Items.STICK) {
                return 100;
            } else if (item != Items.BOW && item != Items.FISHING_ROD) {
                if (item == Items.SIGN) {
                    return 200;
                } else if (item == Items.COAL) {
                    return 1600;
                } else if (item == Items.LAVA_BUCKET) {
                    return 20000;
                } else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL) {
                    if (item == Items.BLAZE_ROD) {
                        return 2400;
                    } else if (item instanceof ItemDoor && item != Items.IRON_DOOR) {
                        return 200;
                    } else {
                        return item instanceof ItemBoat ? 400 : 0;
                    }
                } else {
                    return 100;
                }
            } else {
                return 300;
            }
        }
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    public double percentage() {
        return (double) (this.burnTime / this.currentItemBurnTime) * 100;
    }

    public static boolean isEnergyTE(TileEntity te) {
        return (te != null && te.hasCapability(CapabilityEnergy.ENERGY, null));
    }

    private void handleSendingEnergy() {
        int energyStored = storage.getEnergyStored();

        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos pos = getPos().offset(facing);
            TileEntity te = world.getTileEntity(pos);
            if (isEnergyTE(te)) {
                EnumFacing opposite = facing.getOpposite();
                int rfToGive = 2000 <= energyStored ? 2000 : energyStored;
                int received;
                received = receiveEnergy(te, opposite, rfToGive);
                energyStored -= storage.extractEnergy(received, false);
                if (energyStored <= 0) {
                    break;
                }
            }
        }
    }

    public static int receiveEnergy(TileEntity tileEntity, EnumFacing from, int maxReceive) {
        if (tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, from)) {
            net.minecraftforge.energy.IEnergyStorage capability = tileEntity.getCapability(CapabilityEnergy.ENERGY, from);
            if (capability.canReceive()) {
                return capability.receiveEnergy(maxReceive, false);
            }
        }
        return 0;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }

        if (capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            IItemHandler handler = this.inventory;
            if (handler != null) {
                return (T) handler;
            }
        }

        if (capability == CapabilityEnergy.ENERGY) {
            return (T) this.storage;
        }
        return super.getCapability(capability, facing);
    }
}
