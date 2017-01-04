package com.theundertaker11.GeneticsReborn.blocks.cellanalyser;

import com.theundertaker11.GeneticsReborn.items.GRItems;
import com.theundertaker11.GeneticsReborn.tile.GRTileEntityBasicEnergyReceiver;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

	/**
	 * ContainerSmelting is used to link the client side gui to the server side inventory and it is where
	 * you add the slots holding items. It is also used to send server side data such as progress bars to the client
	 * for use in guis
	 */
	public class ContainerCellAnalyser extends Container {

		// Stores the tile entity instance for later use
		private GRTileEntityCellAnalyser tileInventory;

		// These store cache values, used by the server to only update the client side tile entity when values have changed
		private int cachedEnergyUsed;
		private int cachedEnergyStored;
		private int cachedOverclockers;

		// must assign a slot index to each of the slots used by the GUI.
		// For this container, we can see the furnace fuel, input, and output slots as well as the player inventory slots and the hotbar.
		// Each time we add a Slot to the container using addSlotToContainer(), it automatically increases the slotIndex, which means
		//  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
		//  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
		//  36 - 39 = fuel slots (tileEntity 0 - 3)
		//  40 - 44 = input slots (tileEntity 4 - 8)
		//  45 - 49 = output slots (tileEntity 9 - 13)

		private final int HOTBAR_SLOT_COUNT = 9;
		private final int PLAYER_INVENTORY_ROW_COUNT = 3;
		private final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
		private final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
		private final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

		//public final int FUEL_SLOTS_COUNT = 4;
		public final int INPUT_SLOTS_COUNT = 1;
		public final int OUTPUT_SLOTS_COUNT = 1;
		public final int TOTAL_SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

		// slot index is the unique index for all slots in this container i.e. 0 - 35 for invPlayer then 36 - 49 for tileInventory
		private final int VANILLA_FIRST_SLOT_INDEX = 0;
		//private final int FIRST_FUEL_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
		private final int INPUT_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
		private final int OUTPUT_SLOT_INDEX = INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT;

		// slot number is the slot number within each component; i.e. invPlayer slots 0 - 35, and tileInventory slots 0 - 14
		//private final int FIRST_FUEL_SLOT_NUMBER = 0;
		private final int INPUT_SLOT_NUMBER = 0;
		private final int OUTPUT_SLOT_NUMBER = 0;

		public ContainerCellAnalyser(InventoryPlayer invPlayer, GRTileEntityCellAnalyser tileInventory){
			this.tileInventory = tileInventory;

			final int SLOT_X_SPACING = 18;
			final int SLOT_Y_SPACING = 18;
			final int HOTBAR_XPOS = 8;
			final int HOTBAR_YPOS = 183;
			// Add the players hotbar to the gui - the [xpos, ypos] location of each item
			for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
				int slotNumber = x;
				addSlotToContainer(new Slot(invPlayer, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
			}

			final int PLAYER_INVENTORY_XPOS = 8;
			final int PLAYER_INVENTORY_YPOS = 125;
			// Add the rest of the players inventory to the gui
			for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
				for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
					int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
					int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
					int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
					addSlotToContainer(new Slot(invPlayer, slotNumber,  xpos, ypos));
				}
			}
			/*
			final int FUEL_SLOTS_XPOS = 53;
			final int FUEL_SLOTS_YPOS = 96;
			// Add the tile fuel slots
			for (int x = 0; x < FUEL_SLOTS_COUNT; x++) {
				int slotNumber = x + FIRST_FUEL_SLOT_NUMBER;
				addSlotToContainer(new SlotFuel(tileInventory, slotNumber, FUEL_SLOTS_XPOS + SLOT_X_SPACING * x, FUEL_SLOTS_YPOS));
			}
			*/
			IItemHandler itemhandlerinput = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			IItemHandler itemhandleroutput = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
			final int INPUT_SLOTS_XPOS = 26;
			final int INPUT_SLOTS_YPOS = 24;
			// Add the tile input slots
			for (int y = 0; y < INPUT_SLOTS_COUNT; y++) {
				int slotNumber = y + INPUT_SLOT_NUMBER;
				addSlotToContainer(new SlotSmeltableInput(itemhandlerinput, slotNumber, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS+ SLOT_Y_SPACING * y));
			}

			final int OUTPUT_SLOTS_XPOS = 134;
			final int OUTPUT_SLOTS_YPOS = 24;
			// Add the tile output slots
			for (int y = 0; y < OUTPUT_SLOTS_COUNT; y++) {
				int slotNumber = y + OUTPUT_SLOT_NUMBER;
				addSlotToContainer(new SlotOutput(itemhandleroutput, slotNumber, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS + SLOT_Y_SPACING * y));
			}
		}

		// Checks each tick to make sure the player is still able to access the inventory and if not closes the gui
		@Override
		public boolean canInteractWith(EntityPlayer player)
		{
			return tileInventory.isUseableByPlayer(player);
		}

		//Always make sure it returns null if nothing should happen, and after stuff has happened make it return a copy
		//of the stack
		@Override
		public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
		{
			ItemStack itemstack = null;
	        Slot slot = (Slot)this.inventorySlots.get(sourceSlotIndex);
	        if(slot == null || !slot.getHasStack()) return null;
	        if(tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)==null) return null;
	        IItemHandler input = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
	        
	        ItemStack sourceStack = slot.getStack();
	        ItemStack copyOfStack = sourceStack.copy();

	        if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT)
	        {
	        	if(sourceStack.getItem()==GRItems.OrganicMatter)
	        	{
	        		if (input.insertItem(0, sourceStack, true)!=null){
						return null;
					}
	        		else
	        		{
	        			input.insertItem(0, sourceStack, false);
	        			player.inventory.setInventorySlotContents(sourceSlotIndex, null);
	        		}
	        	}
	        	else return null;
	        }
	        else if(sourceSlotIndex==INPUT_SLOT_INDEX||sourceSlotIndex==OUTPUT_SLOT_INDEX)
	        {
	        	if (player.inventory.addItemStackToInventory(sourceStack)){
	        		player.inventory.setInventorySlotContents(sourceSlotIndex, null);
				}
        		else
        		{
        			return null;
        		}
	        }else return null;
	        return copyOfStack;
		}	
		

		/* Client Synchronization */
		@Override
		public void detectAndSendChanges() {
			super.detectAndSendChanges();

			boolean fieldHasChanged = false;
			if (cachedEnergyUsed != tileInventory.getField(0)||cachedEnergyStored!=tileInventory.getField(1)||cachedOverclockers!=tileInventory.getField(2))
			{
				this.cachedEnergyUsed = tileInventory.getField(0);
				this.cachedEnergyStored = tileInventory.getField(1);
				this.cachedOverclockers = tileInventory.getField(2);
				fieldHasChanged = true;
			}

		// go through the list of listeners (players using this container) and update them if necessary
	    for (IContainerListener listener : this.listeners) {
				if (fieldHasChanged)
				{
					// Note that although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts
					listener.sendProgressBarUpdate(this, 0, this.cachedEnergyUsed);
					listener.sendProgressBarUpdate(this, 1, this.cachedEnergyStored);
					listener.sendProgressBarUpdate(this, 2, this.cachedOverclockers);
				}
				
			}
		}

		// Called when a progress bar update is received from the server. The two values (id and data) are the same two
		// values given to sendProgressBarUpdate.  In this case we are using fields so we just pass them to the tileEntity.
		@SideOnly(Side.CLIENT)
		@Override
		public void updateProgressBar(int id, int data)
		{
			tileInventory.setField(id, data);
		}

		// SlotSmeltableInput is a slot for input items
		public class SlotSmeltableInput extends SlotItemHandler {
			public SlotSmeltableInput(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
				super(inventoryIn, index, xPosition, yPosition);
			}

			// if this function returns false, the player won't be able to insert the given item into this slot
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem()==GRItems.OrganicMatter);
			}
		}

		// SlotOutput is a slot that will not accept any items
		public class SlotOutput extends SlotItemHandler {
			public SlotOutput(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
				super(inventoryIn, index, xPosition, yPosition);
			}

			// if this function returns false, the player won't be able to insert the given item into this slot
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		}
	}
