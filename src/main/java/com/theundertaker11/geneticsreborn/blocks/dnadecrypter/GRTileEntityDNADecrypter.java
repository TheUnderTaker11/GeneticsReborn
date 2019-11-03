package com.theundertaker11.geneticsreborn.blocks.dnadecrypter;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.MobToGeneRegistry;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GRTileEntityDNADecrypter extends GRTileEntityBasicEnergyReceiver implements ITickable {
	public GRTileEntityDNADecrypter() {
		super();
	}
	
	public GRTileEntityDNADecrypter(String name) {
		super(name);
	}

	@Override
	public void update() {
		int rfpertick = (GeneticsReborn.baseRfPerTickDNADecrypter + (this.overclockers * GeneticsReborn.OVERCLOCK_RF_COST));
		if (canSmelt()) {
			if (this.storage.getEnergyStored() > rfpertick) {
				this.storage.extractEnergy(rfpertick, false);
				ticksCooking++;
				markDirty();
			}
			if (ticksCooking < 0) ticksCooking = 0;

			if (ticksCooking >= getTotalTicks()) {
				smeltItem();
				ticksCooking = 0;
			}
		} else ticksCooking = 0;
	}

	public static ItemStack getSmeltingResultForItem(ItemStack stack, boolean simulate) {
		if (stack.getItem() == GRItems.DNAHelix && stack.getTagCompound() != null) {
			if (!ModUtils.getTagCompound(stack).hasKey("gene")) {
				ItemStack result = new ItemStack(GRItems.DNAHelix);
				if (simulate) return result;

				String entityName = ModUtils.getTagCompound(stack).getString("entityName");
				String entityCodeName = ModUtils.getTagCompound(stack).getString("entityCodeName");

				ModUtils.getTagCompound(result).setString("entityName", entityName);
				ModUtils.getTagCompound(result).setString("entityCodeName", entityCodeName);
				ModUtils.getTagCompound(result).setString("gene", MobToGeneRegistry.getGene(stack, entityCodeName));
				return result;
			}
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
		IItemHandler inventory = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		IItemHandler inventoryoutput = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

		if (inventory != null && !inventory.getStackInSlot(0).isEmpty()) {
			result = getSmeltingResultForItem(inventory.getStackInSlot(0), !performSmelt);
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

	public double getTotalTicks() {
		return (GeneticsReborn.baseTickDNADecrypter - (this.overclockers * GeneticsReborn.OVERCLOCK_BONUS));
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


}
