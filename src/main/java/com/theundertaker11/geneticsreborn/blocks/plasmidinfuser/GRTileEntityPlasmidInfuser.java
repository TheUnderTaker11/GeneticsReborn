package com.theundertaker11.geneticsreborn.blocks.plasmidinfuser;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
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

public class GRTileEntityPlasmidInfuser extends GRTileEntityBasicEnergyReceiver implements ITickable {

	public int num;
	public int numNeeded;
	private int timer;

	public GRTileEntityPlasmidInfuser() {
		super();
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
		int rfpertick = (GeneticsReborn.baseRfPerTickPlasmidInfuser + (this.overclockers * 85));
		if (canSmelt()) {
			if (this.storage.getEnergyStored() > rfpertick) {
				this.storage.extractEnergy(rfpertick, false);
				ticksCooking++;
				markDirty();
			}
			if (ticksCooking < 0) ticksCooking = 0;

			if (ticksCooking >= (GeneticsReborn.baseTickPlasmidInfuser - (this.overclockers * 39))) {
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
					if ("GeneticsRebornBasicGene".equals(item.getTagCompound().getString("gene"))) return false;

					NBTTagCompound resulttag = ModUtils.getTagCompound(result);
					String gene = ModUtils.getTagCompound(item).getString("gene");
					resulttag.setString("gene", gene);
					resulttag.setInteger("num", 0);
					resulttag.setInteger("numNeeded", numNeededLogic(gene));
				} else {
					NBTTagCompound resulttag = ModUtils.getTagCompound(result);
					if (resulttag.getInteger("num") == resulttag.getInteger("numNeeded")) return false;
					if ("GeneticsRebornBasicGene".equals(itemtag.getString("gene"))) {
						if (performSmelt) {
							resulttag.setInteger("num", resulttag.getInteger("num") + 1);
							inventory.extractItem(0, item.getCount(), false);
							this.markDirty();
						}
						return true;
					} else if (itemtag.getString("gene").equals(resulttag.getString("gene"))) {
						if (performSmelt) {
							resulttag.setInteger("num", resulttag.getInteger("num") + 2);
							if (resulttag.getInteger("num") > resulttag.getInteger("numNeeded")) {
								resulttag.setInteger("num", resulttag.getInteger("numNeeded"));
							}
							inventory.extractItem(0, item.getCount(), false);
							this.markDirty();
						}
						return true;
					} else return false;
				}
			}
		}
		return false;
	}

	private static int numNeededLogic(String gene) {
		EnumGenes enumGene = Genes.getGeneFromString(gene);
		if (enumGene == EnumGenes.DRAGONS_BREATH || enumGene == EnumGenes.FIRE_PROOF || enumGene == EnumGenes.SCARE_CREEPERS || enumGene == EnumGenes.WITHER_HIT
				|| enumGene == EnumGenes.SPEED || enumGene == EnumGenes.EXPLOSIVE_EXIT) {
			return 20;
		}
		if (enumGene == EnumGenes.EAT_GRASS || enumGene == EnumGenes.MILKY || enumGene == EnumGenes.SLIMY) {
			return 12;
		}
		if (enumGene == EnumGenes.EMERALD_HEART || enumGene == EnumGenes.RESISTANCE || enumGene == EnumGenes.XP_MAGNET
				|| enumGene == EnumGenes.ITEM_MAGNET || enumGene == EnumGenes.STRENGTH) {
			return 30;
		}
		if (enumGene == EnumGenes.ENDER_DRAGON_HEALTH) {
			return 60;
		}
		if (enumGene == EnumGenes.FLY || enumGene == EnumGenes.WITHER_PROOF || enumGene == EnumGenes.MORE_HEARTS || enumGene == EnumGenes.SAVE_INVENTORY || enumGene == EnumGenes.PHOTOSYNTHESIS) {
			return 40;
		}
		if (enumGene == EnumGenes.JUMP_BOOST || enumGene == EnumGenes.NIGHT_VISION || enumGene == EnumGenes.WOOLY || enumGene == EnumGenes.WATER_BREATHING || enumGene == EnumGenes.SHOOT_FIREBALLS) {
			return 16;
		}
		if (enumGene == EnumGenes.NO_FALL_DAMAGE) {
			return 10;
		}
		if (enumGene == EnumGenes.POISON_PROOF || enumGene == EnumGenes.TELEPORTER) {
			return 25;
		}

		return 50;
	}

	public double percComplete() {
		return (double) ((double) this.ticksCooking / (double) (GeneticsReborn.baseTickPlasmidInfuser - (this.overclockers * 39)));
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
	private static final byte NUM_FIELD_ID = 3;
	private static final byte NUMNEEDED_FIELD_ID = 4;

	private static final byte NUMBER_OF_FIELDS = 5;

	public int getField(int id) {
		if (id == TICKS_COOKING_FIELD_ID) return ticksCooking;
		if (id == ENERGY_STORED_FIELD_ID) return this.storage.getEnergyStored();
		if (id == OVERCLOCKERS_FIELD_ID) return this.overclockers;
		if (id == NUM_FIELD_ID) return this.num;
		if (id == NUMNEEDED_FIELD_ID) return this.numNeeded;
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
		} else if (id == NUM_FIELD_ID) {
			this.num = (short) value;
		} else if (id == NUMNEEDED_FIELD_ID) {
			this.numNeeded = (short) value;
		} else {
			System.err.println("Invalid field ID in GRTileEntity.setField:" + id);
		}
	}

	public int getFieldCount() {
		return NUMBER_OF_FIELDS;
	}
}
