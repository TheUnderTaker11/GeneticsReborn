package com.theundertaker11.geneticsreborn.blocks.plasmidinfuser;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.BaseContainer;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPlasmidInfuser extends BaseContainer {

	private GRTileEntityPlasmidInfuser tileInventory;

	private int cachedEnergyUsed;
	private int cachedEnergyStored;
	private int cachedOverclockers;
	private int cachedNum;
	private int cachedNumNeeded;

	private final int INPUT_SLOT_NUMBER = 0;
	private final int OUTPUT_SLOT_NUMBER = 0;

	public ContainerPlasmidInfuser(InventoryPlayer invPlayer, GRTileEntityPlasmidInfuser tileInventory) {
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
				if(itemstack1.getItem() == GRItems.DNAHelix) {
					if (itemstack1.getTagCompound() != null && ModUtils.getTagCompound(itemstack1).hasKey("gene") && 
					(!GeneticsReborn.hardMode || !"GeneticsRebornBasicGene".equals( ModUtils.getTagCompound(itemstack1).getString("gene")))){
						if (!this.mergeItemStack(slot.getStack(), 36, 37, false)) {
							this.mergeItemStack(slot.getStack(), 36, 37,false);
							return ItemStack.EMPTY;
						}
						else return itemstack;
					} else 
						return ItemStack.EMPTY;
				}
				if(slot.getStack().getItem() == GRItems.Plasmid){
					if (!this.mergeItemStack(slot.getStack(), 37, 38, false)) {
						this.mergeItemStack(slot.getStack(), 37, 38,false);
						return ItemStack.EMPTY;
					}
					else{
						return itemstack;
					}
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

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		boolean fieldHasChanged = false;
		boolean overclockersChanged = false;
		boolean numChanged = false;
		if (cachedEnergyUsed != tileInventory.getField(0) || cachedEnergyStored != tileInventory.getField(1)) {
			this.cachedEnergyUsed = tileInventory.getField(0);
			this.cachedEnergyStored = tileInventory.getField(1);
			fieldHasChanged = true;
		}
		if (cachedOverclockers != tileInventory.getField(2)) {
			this.cachedOverclockers = tileInventory.getField(2);
			overclockersChanged = true;
		}
		if (cachedNum != tileInventory.getField(3) || cachedNumNeeded != tileInventory.getField(4)) {
			this.cachedNum = tileInventory.getField(3);
			this.cachedNumNeeded = tileInventory.getField(4);
			numChanged = true;
		}

		for (IContainerListener listener : this.listeners) {
			if (fieldHasChanged) {
				listener.sendWindowProperty(this, 0, this.cachedEnergyUsed);
				listener.sendWindowProperty(this, 1, this.cachedEnergyStored);
			}
			if (overclockersChanged) {
				listener.sendWindowProperty(this, 2, this.cachedOverclockers);
			}
			if (numChanged) {
				listener.sendWindowProperty(this, 3, this.cachedNum);
				listener.sendWindowProperty(this, 4, this.cachedNumNeeded);
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
			return stack.getItem() == GRItems.DNAHelix &&
			(!GeneticsReborn.hardMode || !"GeneticsRebornBasicGene".equals(ModUtils.getTagCompound(stack).getString("gene")));
		}
	}

	public class SlotOutput extends SlotItemHandler {
		public SlotOutput(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return (stack.getItem() == GRItems.Plasmid);
		}
	}
}
