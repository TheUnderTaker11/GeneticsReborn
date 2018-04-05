package com.theundertaker11.geneticsreborn.blocks.bloodpurifier;

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

public class ContainerBloodPurifier extends Container {

	private GRTileEntityBloodPurifier tileInventory;

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

	public ContainerBloodPurifier(InventoryPlayer invPlayer, GRTileEntityBloodPurifier tileInventory) {
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
	public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex) {
		Slot slot = (Slot) this.inventorySlots.get(sourceSlotIndex);
		if (slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
		if (tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP) == null)
			return ItemStack.EMPTY;
		IItemHandler input = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

		ItemStack sourceStack = slot.getStack();
		ItemStack copyOfStack = sourceStack.copy();

		if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			if ((sourceStack.getItem() == GRItems.GlassSyringe || sourceStack.getItem() == GRItems.MetalSyringe) && sourceStack.getItemDamage() == 1) {
				if (input.insertItem(0, sourceStack, true).isEmpty()) {
					input.insertItem(0, sourceStack, false);
					player.inventory.setInventorySlotContents(sourceSlotIndex, ItemStack.EMPTY);
				} else if (input.insertItem(0, sourceStack, true).getCount() == sourceStack.getCount()) {
					return ItemStack.EMPTY;
				} else {
					player.inventory.setInventorySlotContents(sourceSlotIndex, input.insertItem(0, sourceStack, false));
				}
			} else return ItemStack.EMPTY;
		} else if (sourceSlotIndex == INPUT_SLOT_INDEX || sourceSlotIndex == OUTPUT_SLOT_INDEX) {
			if (!this.mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}
		} else return ItemStack.EMPTY;
		return copyOfStack;
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
				// Note that although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts
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
			return ((stack.getItem() == GRItems.GlassSyringe || stack.getItem() == GRItems.MetalSyringe) && stack.getItemDamage() == 1);
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
