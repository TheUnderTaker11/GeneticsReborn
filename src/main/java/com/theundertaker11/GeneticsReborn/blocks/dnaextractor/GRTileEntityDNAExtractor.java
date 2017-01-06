package com.theundertaker11.GeneticsReborn.blocks.dnaextractor;

import com.theundertaker11.GeneticsReborn.items.GRItems;
import com.theundertaker11.GeneticsReborn.tile.GRTileEntityBasicEnergyReceiver;
import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GRTileEntityDNAExtractor extends GRTileEntityBasicEnergyReceiver implements ITickable{
	
	public static final short TICKS_NEEDED = 200;
	
	public GRTileEntityDNAExtractor(){
		super();
	}
	
	@Override
	public void update()
	{
		int rfpertick = (20+(this.overclockers*40));//TODO change here based on power
		if (canSmelt()) 
		{
			if (this.energy > rfpertick)
			{
				this.energy -= rfpertick;
				ticksCooking++;
				markDirty();
			}
			// Just in case
			if (ticksCooking < 0) ticksCooking = 0;

			if (ticksCooking >= (TICKS_NEEDED-(this.overclockers*39))){//TODO change here based on power
				smeltItem();
				ticksCooking = 0;
			}
		}
		else ticksCooking = 0;
	}
	//TODO change here
	public static ItemStack getSmeltingResultForItem(ItemStack stack)
	{
		if(stack!=null&&stack.getItem()==GRItems.Cell&&stack.getTagCompound()!=null)
		{
			ItemStack result = new ItemStack(GRItems.DNAHelix);
			ModUtils.getTagCompound(result).setString("entityName", ModUtils.getTagCompound(stack).getString("entityName"));
			return result;
		}
		return null;
	}

	/**
	 * Doesn't just check if the item is good, also checks if there is room in the output.
	 */
	private boolean canSmelt() {return smeltItem(false);}

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
		return (double)((double)this.ticksCooking/(double)(TICKS_NEEDED-(this.overclockers*39)));//TODO change here based on power
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
	
	private static final byte TICKS_COOKING_FIELD_ID = 0;
	private static final byte ENERGY_STORED_FIELD_ID = 1;
	private static final byte OVERCLOCKERS_FIELD_ID = 2;
	
	private static final byte NUMBER_OF_FIELDS = 3;

	public int getField(int id) {
		if (id == TICKS_COOKING_FIELD_ID) return ticksCooking;
		if (id == ENERGY_STORED_FIELD_ID) return this.getEnergyStored(null);
		if(id==OVERCLOCKERS_FIELD_ID) return this.overclockers;
		System.err.println("Invalid field ID in GRTileEntity.getField:" + id);
		return 0;
	}

	public void setField(int id, int value)
	{
		if (id == TICKS_COOKING_FIELD_ID) {
			ticksCooking = (short)value;
		} else if (id == ENERGY_STORED_FIELD_ID){
			this.energy = (short)value;
		}else if(id==OVERCLOCKERS_FIELD_ID){
			this.overclockers = (short)value;
		}else {
			System.err.println("Invalid field ID in GRTileEntity.setField:" + id);
		}
	}

	public int getFieldCount() {
		return NUMBER_OF_FIELDS;
	}
	
}