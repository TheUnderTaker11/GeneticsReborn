package com.theundertaker11.geneticsreborn.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BaseContainer extends Container {
	protected final int HOTBAR_SLOT_COUNT = 9;
	protected final int PLAYER_INVENTORY_ROW_COUNT = 3;
	protected final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	protected final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	protected final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
	protected final int VANILLA_FIRST_SLOT_INDEX = 0;

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	protected void attachPlayerInventory(InventoryPlayer invPlayer) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new Slot(invPlayer, k, 8 + k * 18, 142));
		}		
	}
	
	protected boolean canAcceptItem(Slot slot) {
		return false;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(index < VANILLA_SLOT_COUNT){
				if(canAcceptItem(slot)){
					if (!this.mergeItemStack(slot.getStack(), 36, 37, false)) {
						this.mergeItemStack(slot.getStack(), 36, 37,false);
						return ItemStack.EMPTY;
					}
					else{
						return itemstack;
					}
				} else
					return ItemStack.EMPTY;
			} else if (!this.mergeItemStack(itemstack1, 0, VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
	
}
