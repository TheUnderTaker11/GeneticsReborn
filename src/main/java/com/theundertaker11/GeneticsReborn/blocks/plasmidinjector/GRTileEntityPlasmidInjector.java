package com.theundertaker11.geneticsreborn.blocks.plasmidinjector;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.Genes;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GRTileEntityPlasmidInjector extends GRTileEntityBasicEnergyReceiver implements ITickable{
	
	public static int TICKS_NEEDED = GeneticsReborn.baseTickPlasmidInjector;
	public static int baseRfPerTick = GeneticsReborn.baseRfPerTickPlasmidInjector;
	public GRTileEntityPlasmidInjector(){
		super();
	}
	
	@Override
	public void update()
	{
		int rfpertick = (baseRfPerTick+(this.overclockers*85));
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

			if (ticksCooking >= (TICKS_NEEDED-(this.overclockers*39))){
				smeltItem();
				ticksCooking = 0;
			}
		}
		else ticksCooking = 0;
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
		ItemStack result;
		IItemHandler inventory = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		IItemHandler inventoryoutput = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		
		
			if (inventory != null&&inventory.getStackInSlot(0)!=null&&inventoryoutput!=null&&inventoryoutput.getStackInSlot(0)!=null) 
			{
				ItemStack item = inventory.getStackInSlot(0);
				result = inventoryoutput.getStackInSlot(0);
				if(!(result.getItem()==GRItems.GlassSyringe||result.getItem()==GRItems.MetalSyringe)) return false;
				
				if (item.getTagCompound()!=null)
				{
					NBTTagCompound itemtag = ModUtils.getTagCompound(item);
					if(result.getTagCompound()==null||result.getItemDamage()==0||!(result.getTagCompound().getBoolean("pure")))
					{
						return false;
					}
					else
					{
						NBTTagCompound resulttag = ModUtils.getTagCompound(result);
						if(itemtag.getInteger("num")==itemtag.getInteger("numNeeded"))
						{
							if(item.getItem()==GRItems.AntiPlasmid)
							{
								for(int i=0;i<Genes.TotalNumberOfGenes;i++)
								 {
								 	if(resulttag.hasKey(i+"anti"))
								 	{
								 		if(resulttag.getString(i+"anti").equals(itemtag.getString("gene")))
								 		{
								 			return false;
								 		}
								 	}
								 	else
								 	{
								 		if(performSmelt)
										{
								 			resulttag.setString(i+"anti", itemtag.getString("gene"));
								 			resulttag.setBoolean("pure", false);
								 			inventory.extractItem(0, item.stackSize, false);
								 			this.markDirty();
										}
								 		return true;
								 	}
								 }
							}
							else if(item.getItem()==GRItems.Plasmid)
							{
								for(int i=0;i<Genes.TotalNumberOfGenes;i++)
								 {
								 	if(resulttag.hasKey(Integer.toString(i)))
								 	{
								 		if(resulttag.getString(Integer.toString(i)).equals(itemtag.getString("gene")))
								 		{
								 			return false;
								 		}
								 	}
								 	else
								 	{
								 		if(performSmelt)
										{
								 			resulttag.setString(Integer.toString(i), itemtag.getString("gene"));
								 			resulttag.setBoolean("pure", false);
								 			inventory.extractItem(0, item.stackSize, false);
								 			this.markDirty();
										}
								 		return true;
								 	}
								 }
							}
						}
						else return false;
					}
				}
			}
		return false;
	}

	public double percComplete()
	{
		return (double)((double)this.ticksCooking/(double)(TICKS_NEEDED-(this.overclockers*39)));
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
