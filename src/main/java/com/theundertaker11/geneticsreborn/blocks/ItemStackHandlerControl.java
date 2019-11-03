package com.theundertaker11.geneticsreborn.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerControl extends ItemStackHandler {
	protected ItemStackHandler core;
	
	public ItemStackHandlerControl(ItemStackHandler handler) {
		this.core = handler;
	}

	@Override
	public void setSize(int size) {
		core.setSize(size);
		stacks = NonNullList.withSize(size, ItemStack.EMPTY);  //needed?
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		core.setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots() {
		return core.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return core.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return core.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return core.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		return core.getSlotLimit(slot);
	}
	
	
}
