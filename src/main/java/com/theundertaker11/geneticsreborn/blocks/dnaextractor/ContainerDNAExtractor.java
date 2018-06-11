package com.theundertaker11.geneticsreborn.blocks.dnaextractor;

import com.theundertaker11.geneticsreborn.items.GRItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
/**
 * I copy any of my other machines from this class, the todo's are just a reminder to myself
 * where I need to change things
 * @author TheUnderTaker11
 *
 */
public class ContainerDNAExtractor extends Container {

	private GRTileEntityDNAExtractor tileInventory;

	private int cachedEnergyUsed;
	private int cachedEnergyStored;
	private int cachedOverclockers;

	private final int HOTBAR_SLOT_COUNT = 9;
	private final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	public final int INPUT_SLOTS_COUNT = 1;
	public final int OUTPUT_SLOTS_COUNT = 1;

	private final int VANILLA_FIRST_SLOT_INDEX = 0;
	private final int INPUT_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private final int OUTPUT_SLOT_INDEX = INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT;

	private final int INPUT_SLOT_NUMBER = 0;
	private final int OUTPUT_SLOT_NUMBER = 0;

	public ContainerDNAExtractor(InventoryPlayer invPlayer, GRTileEntityDNAExtractor tileInventory) {//TODO change last param here
		this.tileInventory = tileInventory;
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new Slot(invPlayer, k, 8 + k * 18, 142));
		}

		IItemHandler itemhandlerinput = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		IItemHandler itemhandleroutput = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		final int INPUT_SLOTS_XPOS = 63;
		final int INPUT_SLOTS_YPOS = 36;
		addSlotToContainer(new SlotSmeltableInput(itemhandlerinput, INPUT_SLOT_NUMBER, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS));


		final int OUTPUT_SLOTS_XPOS = 110;
		final int OUTPUT_SLOTS_YPOS = 36;
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
			if(index <= 35){
				if(slot.getStack().getItem()== GRItems.Cell){
					if (!this.mergeItemStack(slot.getStack(), 36, 37, false)) {
						this.mergeItemStack(slot.getStack(), 36, 37,false);
						return ItemStack.EMPTY;
					}
					else{
						return itemstack;
					}
				}
			} else if (!this.mergeItemStack(itemstack1, 0, 37, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}


	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		boolean fieldHasChanged = false;
		boolean overclockersChanged = false;
		if (cachedEnergyUsed != tileInventory.getField(0) || cachedEnergyStored != tileInventory.getField(1)) {
			this.cachedEnergyUsed = tileInventory.getField(0);
			this.cachedEnergyStored = tileInventory.getField(1);
			fieldHasChanged = true;
		}
		if (cachedOverclockers != tileInventory.getField(2)) {
			this.cachedOverclockers = tileInventory.getField(2);
			overclockersChanged = true;
		}

		for (IContainerListener listener : this.listeners) {
			if (fieldHasChanged) {
				listener.sendWindowProperty(this, 0, this.cachedEnergyUsed);
				listener.sendWindowProperty(this, 1, this.cachedEnergyStored);
			}
			if (overclockersChanged) {
				listener.sendWindowProperty(this, 2, this.cachedOverclockers);
			}

		}
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
			return (stack.getItem() == GRItems.Cell);//TODO change here
		}
	}

	public class SlotOutput extends SlotItemHandler {
		public SlotOutput(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
	}
}
