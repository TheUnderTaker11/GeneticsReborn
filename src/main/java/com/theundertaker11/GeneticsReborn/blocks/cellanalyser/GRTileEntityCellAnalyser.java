package com.theundertaker11.GeneticsReborn.blocks.cellanalyser;

import com.theundertaker11.GeneticsReborn.tile.GRTileEntityBasicEnergyReceiver;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GRTileEntityCellAnalyser extends GRTileEntityBasicEnergyReceiver implements ITickable{
	
	
	/**The number of ticks the current item has been cooking*/
	private static int energyUsed;
	public static final short ENERGY_NEEDED = 5000;
	private boolean cachedisActive = false;
	
	public GRTileEntityCellAnalyser(){
		super();
	}
	
	// - see if the fuel has run out, and if so turn the furnace "off" and slowly uncook the current item (if any)
	// - see if any of the items have finished smelting
	// It runs both on the server and the client.
	@Override
	public void update()
	{
		// If there is nothing to smelt or there is no room in the output, reset energyUsed and return
		if (canSmelt()) 
		{
			// If there is enough energy, keep cooking the item
			if (this.energy > 20)//TODO add overclocker ability
			{
				useEnergy();
				energyUsed +=20;//TODO add overclocker ability
			}
			// Just in case
			if (energyUsed < 0) energyUsed = 0;

			// If energyUsed has reached maxCookTime smelt the item and reset energyUsed
			if (energyUsed >= ENERGY_NEEDED) {
				smeltItem();
				energyUsed = 0;
			}
		}
		else energyUsed = 0;
		// when it turns on, force the block to re-render, otherwise the change in state will not be visible.
		// The block update (for renderer) is only required on client side
		if (cachedisActive != this.isActive)
		{
			cachedisActive = this.isActive;
			if (worldObj.isRemote)
			{
				IBlockState iblockstate = this.worldObj.getBlockState(this.getPos());
				final int FLAGS = 3;  // I'm not sure what these flags do, exactly.
				worldObj.notifyBlockUpdate(pos, iblockstate, iblockstate, FLAGS);
			}
		}
	}
		
	//TODO This will be used to do the % chance for getting certain genes, for testing it just returns diamond
	public static ItemStack getSmeltingResultForItem(ItemStack stack)
	{
			return new ItemStack(Items.DIAMOND); 
	}

	/**
	 * 	for each fuel slot: decreases the burn time, checks if burnTimeRemaining = 0 and tries to consume a new piece of fuel if one is available
	 *  return the number of fuel slots which are burning
	 */
	private void useEnergy()
	{
		this.energy -=20;//TODO add overclocker ability
		markDirty();
	}

	/**
	 * Check if any of the input items are smeltable and there is sufficient space in the output slots
	 * @return true if smelting is possible
	 */
	private boolean canSmelt() {return smeltItem(false);}

	/**
	 * Smelt an input item into an output slot, if possible
	 */
	private void smeltItem() {smeltItem(true);}
	
	/**
	 * checks that there is an item to be smelted in one of the input slots and that there is room for the result in the output slots
	 * If desired, performs the smelt
	 * @param performSmelt if true, perform the smelt.  if false, check whether smelting is possible, but don't change the inventory
	 * @return false if no items can be smelted, true otherwise
	 */
	private boolean smeltItem(boolean performSmelt)
	{
		ItemStack result = null;
		IItemHandler inventory = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		IItemHandler inventoryoutput = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		
		// Sees if the input slot is smeltable and if result fits into an output slot (stacking if possible)
			if (inventory != null&&inventory.getStackInSlot(0)!=null) 
			{
				result = getSmeltingResultForItem(inventory.getStackInSlot(0));
				if (result != null)
				{
						//Trys to insert into output slot
						ItemStack inputSlotStack = inventory.getStackInSlot(0);
						ItemStack outputSlotStack = inventoryoutput.getStackInSlot(0);
						if (outputSlotStack == null)
						{
							if(inventoryoutput.insertItem(0, result, !performSmelt)==null)
							{
								inventory.extractItem(0, 1, !performSmelt);
								markDirty();
								return true;
							}
						}else
						{
							if(inventoryoutput.insertItem(0, result, true)!=null)
							{
								return false;
							}
							else
							{
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

	public double percComplete()
	{
		System.out.println("EnergyUsed:"+this.energyUsed+" EnergyNeeded:"+this.ENERGY_NEEDED);
		System.out.println("The number it is spitting out is"+(double)(this.energyUsed/this.ENERGY_NEEDED));
		return (double)(this.energyUsed/this.ENERGY_NEEDED);
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
        super.writeToNBT(compound);
        return compound;
        
    }
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
	}
	
	private static final byte ENERGY_USED_FIELD_ID = 0;
	private static final byte ENERGY_STORED_FIELD_ID = 1;
	
	private static final byte NUMBER_OF_FIELDS = 2;

	//@Override
	public int getField(int id) {
		if (id == ENERGY_USED_FIELD_ID) return energyUsed;
		if (id == ENERGY_STORED_FIELD_ID) return this.getEnergyStored(null);
		System.err.println("Invalid field ID in TileInventorySmelting.getField:" + id);
		return 0;
	}

	//@Override
	public void setField(int id, int value)
	{
		if (id == ENERGY_USED_FIELD_ID) {
			energyUsed = (short)value;
		} else if (id == ENERGY_STORED_FIELD_ID){
			this.energy = (short)value;
		}else {
			System.err.println("Invalid field ID in TileInventorySmelting.setField:" + id);
		}
	}

	//@Override
	public int getFieldCount() {
		return NUMBER_OF_FIELDS;
	}
	
}
