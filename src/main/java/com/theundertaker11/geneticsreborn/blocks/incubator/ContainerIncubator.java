package com.theundertaker11.geneticsreborn.blocks.incubator;

import com.theundertaker11.geneticsreborn.blocks.BaseContainer;
import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerIncubator extends BaseContainer  {
	private boolean cachedTemp;
	
	public ContainerIncubator(InventoryPlayer invPlayer, GRTileEntityIncubator tileInventory) {
		this.tileInventory = tileInventory;
		INPUT_SLOTS = 5;

		attachPlayerInventory(invPlayer);


		IItemHandler itemhandlerfuel;
		EnumFacing blockFacing = tileInventory.getWorld().getBlockState(tileInventory.getPos()).getValue(StorageBlockBase.FACING);
		if (blockFacing == EnumFacing.NORTH || blockFacing == EnumFacing.SOUTH) {
			itemhandlerfuel = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		} else {
			itemhandlerfuel = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.WEST);
		}
		IItemHandler itemhandlerinput = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		IItemHandler itemhandleringredient = tileInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

		this.addSlotToContainer(new ContainerIncubator.Potion(itemhandlerinput, 0, 60, 49));
        this.addSlotToContainer(new ContainerIncubator.Potion(itemhandlerinput, 1, 83, 56));
        this.addSlotToContainer(new ContainerIncubator.Potion(itemhandlerinput, 2, 106, 49));
        this.addSlotToContainer(new ContainerIncubator.Ingredient(itemhandleringredient, 0, 83, 15));
        this.addSlotToContainer(new ContainerIncubator.Fuel(itemhandlerfuel, 0, 141, 32));
				
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileInventory.isUseableByPlayer(player);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		tileInventory.setField(id, data);
	}

	@Override
	protected boolean canAcceptItem(Slot slot) {
		//shouldn't be using this...
		return false;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack()){
			itemstack = slot.getStack();
			if(index < VANILLA_SLOT_COUNT) {
				//first loop tries the whole stack...
				for (int i=0; i<INPUT_SLOTS; i++) {
					Slot inputSlot = getSlot(i);
					if(inputSlot.isItemValid(itemstack)) {
						if (this.mergeItemStack(itemstack, 36+i, 37+i, false)) {
							inputSlot.onSlotChanged();
							return itemstack;
						}					
					}
				}
				//second loop tries a single item
				for (int i=0; i<INPUT_SLOTS; i++) {
					Slot inputSlot = getSlot(i);
					ItemStack newStack = itemstack.copy();
					newStack.setCount(1);
					if(inputSlot.isItemValid(newStack)) {
						if (this.mergeItemStack(newStack, 36+i, 37+i, false)) {
							inputSlot.onSlotChanged();
							itemstack.shrink(1);
							return itemstack;
						}					
					}
				}
				return ItemStack.EMPTY;
			} else if (!this.mergeItemStack(itemstack, 0, VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				
			}
		}
		return itemstack;
	}
	

    static class Fuel extends SlotItemHandler {
        public Fuel(IItemHandler iInventoryIn, int index, int xPosition, int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        public boolean isItemValid(ItemStack stack) {
        	return stack.getItem() == Items.CHORUS_FRUIT;
        }

        public int getSlotStackLimit() {
            return 64;
        }
    }

    static class Ingredient extends SlotItemHandler {
        public Ingredient(IItemHandler iInventoryIn, int index, int xPosition, int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        public boolean isItemValid(ItemStack stack) {
            return BrewingRecipeRegistry.isValidIngredient(stack);
        }

        public int getSlotStackLimit() {
            return 64;
        }
    }

    static class Potion extends SlotItemHandler {
        public Potion(IItemHandler inv, int index, int x, int y) {
            super(inv, index, x, y);
        }

    	@Override
        public boolean isItemValid(ItemStack stack) {
        	//copied from registry to bypass the count == 1 check
            for (IBrewingRecipe recipe : BrewingRecipeRegistry.getRecipes()) {
                if (recipe.isInput(stack))
                    return true;
            }
            return false;
        }

    	@Override
        public int getSlotStackLimit() {
            return 1;
        }
    	
    	@Override
    	public int getItemStackLimit(ItemStack stack) {
            return 1;
    	}

    	@Override
        public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
            PotionType potiontype = PotionUtils.getPotionFromItem(stack);

            if (thePlayer instanceof EntityPlayerMP) {
                net.minecraftforge.event.ForgeEventFactory.onPlayerBrewedPotion(thePlayer, stack);
                CriteriaTriggers.BREWED_POTION.trigger((EntityPlayerMP)thePlayer, potiontype);
            }

            super.onTake(thePlayer, stack);
            return stack;
        }

    }
    
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if (tileInventory == null) return;
		
		GRTileEntityIncubator te = (GRTileEntityIncubator)tileInventory;
		
		if (cachedTemp != te.isLowTemp()) {
			cachedTemp = te.isLowTemp();
			for (IContainerListener listener : this.listeners)
				listener.sendWindowProperty(this, 3, this.cachedTemp ? 1 : 0);
		}
	}    
	
}
