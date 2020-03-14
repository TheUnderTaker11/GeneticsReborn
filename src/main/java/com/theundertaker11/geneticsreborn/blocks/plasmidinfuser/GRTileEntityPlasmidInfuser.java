package com.theundertaker11.geneticsreborn.blocks.plasmidinfuser;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GRTileEntityPlasmidInfuser extends GRTileEntityBasicEnergyReceiver implements ITickable {

	public int num;
	public int numNeeded;
	private int timer;

	public GRTileEntityPlasmidInfuser() {
		super();
		NUMBER_OF_FIELDS = 5;
	}
	
	public GRTileEntityPlasmidInfuser(String name) {
		super(name);
		NUMBER_OF_FIELDS = 5;
	}

	@Override
	public void update() {
		this.timer++;
		if (this.timer > 10 && this.getWorld().getTileEntity(this.getPos()) != null) {
			this.timer = 0;
			IItemHandler output = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
			if (output.getStackInSlot(0).getItem() == GRItems.Plasmid) {
				ItemStack stack = output.getStackInSlot(0);
				if (stack.getTagCompound() != null) {
					this.num = ModUtils.getTagCompound(stack).getInteger("num");
					this.numNeeded = ModUtils.getTagCompound(stack).getInteger("numNeeded");
				}
			} else {
				this.num = 0;
				this.numNeeded = 0;
			}
		}
		int rfpertick = (GeneticsReborn.baseRfPerTickPlasmidInfuser + (this.overclockers * GeneticsReborn.OVERCLOCK_RF_COST));
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

		if (inventory != null && !inventory.getStackInSlot(0).isEmpty() && !inventoryoutput.getStackInSlot(0).isEmpty()) {
			ItemStack item = inventory.getStackInSlot(0);
			result = inventoryoutput.getStackInSlot(0);
			if (item.getTagCompound() != null) {
				NBTTagCompound itemtag = ModUtils.getTagCompound(item);
				if (result.getTagCompound() == null) {
					String geneName = itemtag.getString("gene");
					if ("GeneticsRebornBasicGene".equals(geneName) || "GeneticsRebornMutatedGene".equals(geneName)) return false;
					EnumGenes gene = EnumGenes.fromGeneName(geneName);
					NBTTagCompound resulttag = ModUtils.getTagCompound(result);
					resulttag.setString("gene", geneName);
					resulttag.setInteger("num", 0);					
					resulttag.setInteger("numNeeded", EnumGenes.getNumberNeeded(gene));
					if (gene.isMutation()) resulttag.setBoolean("mutation", true);	 
				} else {
					NBTTagCompound resulttag = ModUtils.getTagCompound(result);
					if (resulttag.getInteger("num") == resulttag.getInteger("numNeeded")) return false;
					if (("GeneticsRebornBasicGene".equals(itemtag.getString("gene")) && !resulttag.hasKey("mutation")) ||
						"GeneticsRebornMutatedGene".equals(itemtag.getString("gene"))) {
						if (performSmelt) {
							resulttag.setInteger("num", resulttag.getInteger("num") + 1);
							inventory.extractItem(0, 1, false);
							this.markDirty();
						}
						return true;
					} else if (itemtag.getString("gene").equals(resulttag.getString("gene"))) {
						if (performSmelt) {
							resulttag.setInteger("num", resulttag.getInteger("num") + 2);
							if (resulttag.getInteger("num") > resulttag.getInteger("numNeeded")) {
								resulttag.setInteger("num", resulttag.getInteger("numNeeded"));
							}
							inventory.extractItem(0, 1, false);
							this.markDirty();
						}
						return true;
					} else return false;
				}
			}
		}
		return false;
	}

	public double getTotalTicks() {
		return (double) (GeneticsReborn.baseTickPlasmidInfuser - (this.overclockers * GeneticsReborn.OVERCLOCK_BONUS));
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

	private static final byte NUM_FIELD_ID = 3;
	private static final byte NUMNEEDED_FIELD_ID = 4;

	public int getField(int id) {
		if (id == NUM_FIELD_ID) return this.num;
		if (id == NUMNEEDED_FIELD_ID) return this.numNeeded;
		return super.getField(id);
	}

	public void setField(int id, int value) {
		if (id == NUM_FIELD_ID) {
			this.num = (short) value;
		} else if (id == NUMNEEDED_FIELD_ID) {
			this.numNeeded = (short) value;
		} else super.setField(id, value);		

	}

}
