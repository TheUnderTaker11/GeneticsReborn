package com.theundertaker11.geneticsreborn.blocks.bloodpurifier;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;
import com.theundertaker11.geneticsreborn.util.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GRTileEntityBloodPurifier extends GRTileEntityBasicEnergyReceiver implements ITickable {

	public static int TICKS_NEEDED = GeneticsReborn.baseTickBloodPurifier;
	public static int baseRfPerTick = GeneticsReborn.baseRfPerTickBloodPurifier;

	public GRTileEntityBloodPurifier() {
		super();
	}

	@Override
	public void update() {
		int rfpertick = (baseRfPerTick + (this.overclockers * 85));
		if (canSmelt()) {

			if (this.storage.getEnergyStored() > rfpertick) {
				this.storage.extractEnergy(rfpertick, false);
				ticksCooking++;
				markDirty();
			}
			if (ticksCooking < 0) ticksCooking = 0;

			if (ticksCooking >= (TICKS_NEEDED - (this.overclockers * 39))) {
				smeltItem();
				ticksCooking = 0;
			}
		} else ticksCooking = 0;
	}

	public static ItemStack getSmeltingResultForItem(ItemStack stack) {
		if ((stack.getItem() == GRItems.GlassSyringe || stack.getItem() == GRItems.MetalSyringe) && stack.getTagCompound() != null && stack.getItemDamage() == 1) {
			ItemStack result;
			if (stack.getItem() == GRItems.GlassSyringe) result = new ItemStack(GRItems.GlassSyringe, 1, 1);
			else result = new ItemStack(GRItems.MetalSyringe, 1, 1);
			NBTTagCompound tag = stack.getTagCompound().copy();
			result.setTagCompound(tag);
			ModUtils.getTagCompound(result).setBoolean("pure", true);
			return result;
		}
		return ItemStack.EMPTY;
	}

	/**
	 * Doesn't just check if the item is good, also checks if there is room in the output.
	 */
	private boolean canSmelt() {
		return smeltItem(false);
	}

	private void smeltItem() {
		smeltItem(true);
	}

	/**
	 * checks that there is an item to be smelted in one of the input slots and that there is room for the result in the output slots
	 * If desired, performs the smelt
	 *
	 * @param performSmelt if true, perform the smelt.  if false, check whether smelting is possible, but don't change the inventory
	 * @return false if no items can be smelted, true otherwise
	 */
	private boolean smeltItem(boolean performSmelt) {
		ItemStack result;
		IItemHandler inventory = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		IItemHandler inventoryoutput = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

		if (inventory != null && !inventory.getStackInSlot(0).isEmpty()) {
			result = getSmeltingResultForItem(inventory.getStackInSlot(0));
			if (!result.isEmpty()) {
				ItemStack outputSlotStack = inventoryoutput.getStackInSlot(0);
				if (outputSlotStack.isEmpty()) {
					if (inventoryoutput.insertItem(0, result, !performSmelt).isEmpty()) {
						inventory.extractItem(0, 1, !performSmelt);
						markDirty();
						return true;
					}
				} else {
					if (!inventoryoutput.insertItem(0, result, true).isEmpty()) {
						return false;
					} else {
						inventoryoutput.insertItem(0, result, !performSmelt);
						inventory.extractItem(0, 1, !performSmelt);
						markDirty();
						return true;
					}
				}
			}
		}
		return false;
	}

	public double percComplete() {
		return (double) ((double) this.ticksCooking / (double) (TICKS_NEEDED - (this.overclockers * 39)));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
	}

	private static final byte TICKS_COOKING_FIELD_ID = 0;
	private static final byte ENERGY_STORED_FIELD_ID = 1;
	private static final byte OVERCLOCKERS_FIELD_ID = 2;

	private static final byte NUMBER_OF_FIELDS = 3;

	public int getField(int id) {
		if (id == TICKS_COOKING_FIELD_ID) return ticksCooking;
		if (id == ENERGY_STORED_FIELD_ID) return this.storage.getEnergyStored();
		if (id == OVERCLOCKERS_FIELD_ID) return this.overclockers;
		System.err.println("Invalid field ID in GRTileEntity.getField:" + id);
		return 0;
	}

	public void setField(int id, int value) {
		if (id == TICKS_COOKING_FIELD_ID) {
			ticksCooking = (short) value;
		} else if (id == ENERGY_STORED_FIELD_ID) {
			this.storage.setEnergyStored((short) value);
		} else if (id == OVERCLOCKERS_FIELD_ID) {
			this.overclockers = (short) value;
		} else {
			System.err.println("Invalid field ID in GRTileEntity.setField:" + id);
		}
	}

	public int getFieldCount() {
		return NUMBER_OF_FIELDS;
	}
}
