package com.theundertaker11.geneticsreborn.blocks.airdispersal;

import com.theundertaker11.geneticsreborn.blocks.BaseContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAirDispersal extends BaseContainer  {
	
	public ContainerAirDispersal(InventoryPlayer invPlayer, GRTileEntityAirDispersal tileInventory) {
		this.tileInventory = tileInventory;
		INPUT_SLOTS = 2;
		attachPlayerInventory(invPlayer);
		IItemHandler itemhandlerinput = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		this.addSlotToContainer(new ContainerAirDispersal.Potion(itemhandlerinput, 0, 55, 35));
		this.addSlotToContainer(new ContainerAirDispersal.Block(itemhandlerinput, 1, 8, 60));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileInventory.isUseableByPlayer(player);
	}
	
	@Override
	protected boolean canAcceptItem(Slot slot) {
		return slot.isItemValid(slot.getStack());
	}	
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (((GRTileEntityAirDispersal)tileInventory).isRunning())
			for (IContainerListener listener : this.listeners) {
				listener.sendWindowProperty(this, 5, ((GRTileEntityAirDispersal)tileInventory).timeLeft());
			}
	}

    static class Potion extends SlotItemHandler {
        public Potion(IItemHandler inv, int index, int x, int y) {
            super(inv, index, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return (stack.getItem() instanceof ItemSplashPotion);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }

    static class Block extends SlotItemHandler {
        public Block(IItemHandler inv, int index, int x, int y) {
            super(inv, index, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return (stack.getItem() instanceof ItemBlock);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
	

    static class LockItem extends SlotItemHandler {
        public LockItem(IItemHandler inv, int index, int x, int y) {
            super(inv, index, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return true;
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
        
        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
        	return false;
        }
               
    }

}
