package com.theundertaker11.geneticsreborn.blocks.plasmidinjector;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.Genes;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GRTileEntityPlasmidInjector extends GRTileEntityBasicEnergyReceiver implements ITickable {

	public GRTileEntityPlasmidInjector() {
		super();
	}
	
	public GRTileEntityPlasmidInjector(String name) {
		super(name);
	}

	@Override
	public void update() {
		int rfpertick = (GeneticsReborn.baseRfPerTickPlasmidInjector + (this.overclockers * GeneticsReborn.OVERCLOCK_RF_COST));
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

	/**
	 * Doesn't just check if the item is good, also checks if there is room in the output.
	 */
	private boolean canSmelt() {
		return smeltItem(false);
	}

	private void smeltItem() {
		smeltItem(true);
	}

	private void cancelGene(NBTTagCompound tag, String gene) {
		for (int i = 0; i < Genes.TotalNumberOfGenes; i++) {
			String key = Integer.toString(i);
			if (tag.hasKey(key)) {
				if (tag.getString(key).equals(gene)) { 
					tag.removeTag(key);
					return;
				}
			}
		}		
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


		if (inventory != null && !inventory.getStackInSlot(0).isEmpty() && inventoryoutput != null) {
			ItemStack item = inventory.getStackInSlot(0);
			result = inventoryoutput.getStackInSlot(0);
			if (!(result.getItem() == GRItems.GlassSyringe || result.getItem() == GRItems.MetalSyringe)) return false;

			if (item.getTagCompound() != null) {
				NBTTagCompound itemtag = ModUtils.getTagCompound(item);
				if (result.getTagCompound() == null || result.getItemDamage() == 0 || !(result.getTagCompound().getBoolean("pure"))) {
					return false;
				} else {
					NBTTagCompound resulttag = ModUtils.getTagCompound(result);
					String gene = itemtag.getString("gene");
					if (itemtag.getInteger("num") == itemtag.getInteger("numNeeded")) {
						if (item.getItem() == GRItems.AntiPlasmid) {
							for (int i = 0; i < Genes.TotalNumberOfGenes; i++) {
								if (resulttag.hasKey(i + "anti")) {
									if (resulttag.getString(i + "anti").equals(gene)) {
										return false;
									}
								} else {
									if (performSmelt) {
										resulttag.setString(i + "anti", gene);
										cancelGene(resulttag, gene);
										resulttag.setBoolean("pure", false);
										inventory.extractItem(0, item.getCount(), false);																				
										this.markDirty();
									}
									return true;
								}
							}
						} else if (item.getItem() == GRItems.Plasmid) {
							for (int i = 0; i < Genes.TotalNumberOfGenes; i++) {
								if (resulttag.hasKey(Integer.toString(i))) {
									if (resulttag.getString(Integer.toString(i)).equals(gene)) {
										return false;
									}
								} else {
									if (performSmelt) {
										resulttag.setString(Integer.toString(i), gene);										
										resulttag.setBoolean("pure", false);
										inventory.extractItem(0, item.getCount(), false);
										this.markDirty();
									}
									return true;
								}
							}
						}
					} else return false;
				}
			}
		}
		return false;
	}

	public double getTotalTicks() {
		return (double) (GeneticsReborn.baseTickPlasmidInjector - (this.overclockers * GeneticsReborn.OVERCLOCK_BONUS));
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
