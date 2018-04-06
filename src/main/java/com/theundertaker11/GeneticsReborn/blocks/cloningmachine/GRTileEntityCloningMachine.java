package com.theundertaker11.geneticsreborn.blocks.cloningmachine;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GRTileEntityCloningMachine extends GRTileEntityBasicEnergyReceiver implements ITickable{
	
	public static int TICKS_NEEDED = GeneticsReborn.baseTickCloningMachine;
	public static int baseRfPerTick = GeneticsReborn.baseRfPerTickCloningMachine;
	public GRTileEntityCloningMachine(){
		super();
	}
	
	@Override
	public void update()
	{
		int rfpertick = (baseRfPerTick+(this.overclockers*1300));
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
	
	public static ItemStack getSmeltingResultForItem(ItemStack stack)
	{
		if(stack!=null&&stack.getItem()==GRItems.OrganicMatter&&stack.getTagCompound()!=null)
		{
			if(!ModUtils.getTagCompound(stack).hasKey("mobTag")) return null;
			for(String entityClass : GeneticsReborn.CloningBlacklist)
			{
				if(entityClass.equals(ModUtils.getTagCompound(stack).getString("entityCodeName"))) return null;
			}
			ItemStack result = new ItemStack(GRItems.OrganicMatter);
			ModUtils.getTagCompound(result).setString("entityName", ModUtils.getTagCompound(stack).getString("entityName"));
			ModUtils.getTagCompound(result).setString("entityCodeName", ModUtils.getTagCompound(stack).getString("entityCodeName"));
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
		ItemStack result;
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
								if(performSmelt) spawnEntity(ModUtils.getTagCompound(inputSlotStack));
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
								if(performSmelt) spawnEntity(ModUtils.getTagCompound(inputSlotStack));
								return true;
							}
						}
				}
			}
		return false;
	}
	
	public void spawnEntity(NBTTagCompound tag)
	{
		if(this.getWorld().isRemote) return;
		NBTBase mobCompound = tag.getTag("mobTag");
        String type = tag.getString("type");
        EntityLivingBase entityLivingBase = createEntity(this.getWorld(), type);
        if (entityLivingBase != null) {
        	entityLivingBase.readEntityFromNBT((NBTTagCompound) mobCompound);
            entityLivingBase.setLocationAndAngles(pos.getX()+.5, pos.getY()+2.0, pos.getZ()+.5, 0, 0);
            
            this.getWorld().spawnEntityInWorld(entityLivingBase);
        }
	}

	public EntityLivingBase createEntity(World world, String type)
	{
		EntityLivingBase entityLivingBase;
        try {
            entityLivingBase = (EntityLivingBase) Class.forName(type).getConstructor(World.class).newInstance(world);
        } catch (Exception e) {
            entityLivingBase = null;
        }
        return entityLivingBase;
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
