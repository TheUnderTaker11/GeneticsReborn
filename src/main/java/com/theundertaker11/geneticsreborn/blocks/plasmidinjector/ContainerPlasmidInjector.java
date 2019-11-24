package com.theundertaker11.geneticsreborn.blocks.plasmidinjector;

import com.theundertaker11.geneticsreborn.blocks.BaseContainer;
import com.theundertaker11.geneticsreborn.items.GRItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPlasmidInjector extends BaseContainer {

	private final int INPUT_SLOT_NUMBER = 0;
	private final int OUTPUT_SLOT_NUMBER = 0;

	public ContainerPlasmidInjector(InventoryPlayer invPlayer, GRTileEntityPlasmidInjector tileInventory) {
		this.tileInventory = tileInventory;

		attachPlayerInventory(invPlayer);

		IItemHandler itemhandlerinput = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		IItemHandler itemhandleroutput = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		int INPUT_SLOTS_XPOS = 63;
		int INPUT_SLOTS_YPOS = 36;
		addSlotToContainer(new SlotSmeltableInput(itemhandlerinput, INPUT_SLOT_NUMBER, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS));


		int OUTPUT_SLOTS_XPOS = 110;
		int OUTPUT_SLOTS_YPOS = 36;
		addSlotToContainer(new SlotOutput(itemhandleroutput, OUTPUT_SLOT_NUMBER, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileInventory.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack()){ //Checks that slot is valid and has items in it.
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(index < VANILLA_SLOT_COUNT){
				if(slot.getStack().getItem()== GRItems.Plasmid || slot.getStack().getItem() == GRItems.AntiPlasmid){
					if (itemstack1.getTagCompound() != null && itemstack1.getTagCompound().getInteger("num") == itemstack1.getTagCompound().getInteger("numNeeded")) {
						if (!this.mergeItemStack(slot.getStack(), 36, 37, false)) {
							this.mergeItemStack(slot.getStack(), 36, 37, false);
							return ItemStack.EMPTY;
						} else {
							return itemstack;
						}
					}
					else return ItemStack.EMPTY;
				}
				if(slot.getStack().getItem() == GRItems.GlassSyringe) {
					if (itemstack1.getTagCompound().getInteger("pure") == 1) {
						if (!this.mergeItemStack(slot.getStack(), 37, 38, false)) {
							this.mergeItemStack(slot.getStack(), 37, 38, false);
							return ItemStack.EMPTY;
						} else {
							return itemstack;
						}
					}
					else return ItemStack.EMPTY;
				}
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


	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		tileInventory.setField(id, data);
	}

	public class SlotSmeltableInput extends SlotItemHandler {
		public SlotSmeltableInput(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			if (stack.getItem() == GRItems.Plasmid || stack.getItem() == GRItems.AntiPlasmid) {
				if (stack.getTagCompound() != null && stack.getTagCompound().getInteger("num") == stack.getTagCompound().getInteger("numNeeded")) {
					return true;
				}
			}
			return false;
		}
	}
	
	public class SlotOutput extends SlotItemHandler {
		public SlotOutput(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return (stack.getItem() == GRItems.GlassSyringe) && (stack.getTagCompound() != null) && (stack.getTagCompound().getInteger("pure") == 1);
		}
	}	
}
