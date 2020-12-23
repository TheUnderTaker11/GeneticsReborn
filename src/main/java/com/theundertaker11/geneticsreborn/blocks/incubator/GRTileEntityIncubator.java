package com.theundertaker11.geneticsreborn.blocks.incubator;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.blocks.ItemStackHandlerControl;
import com.theundertaker11.geneticsreborn.blocks.StorageBlockBase;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.potions.GRPotions;
import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GRTileEntityIncubator extends GRTileEntityBasicEnergyReceiver implements ITickable {
	private boolean advanced;
	private boolean lowTemp;
	private boolean brewComplete;
	
	public GRTileEntityIncubator() {
		super();
	}
	
	public GRTileEntityIncubator(String name) {
		this(name, false);		
	}
	
	public GRTileEntityIncubator(String name, boolean a) {
		super(name);
		advanced = a;
		lowTemp = false;
		brewComplete = false;
		NUMBER_OF_FIELDS = 4;
	}
	
	public boolean isAdvanced() {
		return advanced;
	}

	public boolean isBrewComplete() {
		return brewComplete;
	}

	private void setBrewComplete(boolean v) {
		this.brewComplete = v;
		
		if (world != null && !world.isRemote) 
			for (EnumFacing enumfacing : EnumFacing.values())
				world.neighborChanged(pos.offset(enumfacing), world.getBlockState(pos).getBlock(), pos);
		
	}
	
	public boolean isLowTemp() {
		return lowTemp;
	}

	public void setTemp(boolean low) {
		this.lowTemp = low;
		ticksCooking = 0;
	}

	@Override
	public void update() {
		if (world.isRemote) return;
		
		int rfpertick = ((lowTemp) ? GeneticsReborn.baseRfPerTickIncubatorLow : GeneticsReborn.baseRfPerTickIncubatorHigh + (this.overclockers * GeneticsReborn.OVERCLOCK_RF_COST));
		double totalTicks = (double) ((lowTemp) ? GeneticsReborn.baseTickIncubatorLow : GeneticsReborn.baseTickIncubatorHigh) - (this.overclockers * GeneticsReborn.OVERCLOCK_BONUS);
		if (canBrew()) {

			if (this.storage.getEnergyStored() > rfpertick) {
				this.storage.extractEnergy(rfpertick, false);
				//this is only for testing, if you add it DO NOT forget to remove it.
				//ticksCooking += 1000;
				ticksCooking += 1;
				markDirty();
			}
			if (ticksCooking < 0) ticksCooking = 0;

			if (ticksCooking >= totalTicks) {
				ticksCooking = 0;
				finishBrew();
			}
		} else ticksCooking = 0;
	}
	
	private void finishBrew() {
		setBrewComplete(true);		
		ItemStack ingredient = ingredientStackHandler.getStackInSlot(0);	
		ItemStack input = inputStackHandler.getStackInSlot(0);	
		PotionType pot = PotionUtils.getPotionFromItem(input);
		ItemStack fuel = fuelStackHandler.getStackInSlot(0);
		
		for (int i =0; i < guiStackHandler.getSlots(); i++) {
            ItemStack output = BrewingRecipeRegistry.getOutput(guiStackHandler.getStackInSlot(i), ingredient);
            if (!output.isEmpty()) {
            	//cell production for substrate
            	if (lowTemp && (pot == GRPotions.SUBSTRATE) && (output.getItem() == GRItems.Cell)) output.setCount(6);
            	
            	//flag lowtemp and overclockers for forced genes
            	if (lowTemp && output.hasTagCompound() && output.getTagCompound().hasKey("forceGene")) { 
            		output.getTagCompound().setBoolean("lowTemp", true); 
            		if ((overclockers > 0) && fuel.isEmpty()) 
            			output.getTagCompound().setInteger("overclocked", overclockers);
            	}
            	guiStackHandler.setStackInSlot(i, output);
            }
        }
		
		if (lowTemp && !fuel.isEmpty()) fuel.shrink(1);
		ingredient.shrink(1);		
		markDirty();
	}
	public boolean isBrewing() {
		return canBrew();
	}
	
	public boolean hasPower() {
		return storage.canExtract();
	}
	
	private boolean canBrew() {
		return hasPower() && !brewComplete && 
			   BrewingRecipeRegistry.hasOutput(inputStackHandler.getStackInSlot(0), ingredientStackHandler.getStackInSlot(0)) ||
			   BrewingRecipeRegistry.hasOutput(inputStackHandler.getStackInSlot(1), ingredientStackHandler.getStackInSlot(0)) || 
			   BrewingRecipeRegistry.hasOutput(inputStackHandler.getStackInSlot(2), ingredientStackHandler.getStackInSlot(0)) ;				
	}
	
	@Override
	public double getTotalTicks() {
		return (double) ((lowTemp) ? GeneticsReborn.baseTickIncubatorLow : GeneticsReborn.baseTickIncubatorHigh) - (this.overclockers * GeneticsReborn.OVERCLOCK_BONUS);		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("advanced", advanced);
		compound.setBoolean("lowTemp", lowTemp);
		compound.setBoolean("complete", brewComplete);
		compound.setTag("ingredients", ingredientStackHandler.serializeNBT());
		compound.setTag("fuel", fuelStackHandler.serializeNBT());
		compound.setTag("potions", guiStackHandler.serializeNBT());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		advanced = compound.getBoolean("advanced");
		lowTemp = compound.getBoolean("lowTemp");
		setBrewComplete(compound.getBoolean("complete"));
		ingredientStackHandler.deserializeNBT(compound.getCompoundTag("ingredients"));
		fuelStackHandler.deserializeNBT(compound.getCompoundTag("fuel"));
		guiStackHandler.deserializeNBT(compound.getCompoundTag("potions"));
	}

	//These are the actual slots, used by GUI, unlimited access
    private ItemStackHandler guiStackHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            if (brewComplete) {
            	boolean empty = true;
            	for (int i = 0; i < stacks.size(); i++) {
                    if (!stacks.get(i).isEmpty()) empty = false;
                }
            	if (empty) setBrewComplete(false);
            }
        }

    };
    
    //these are input slots, you cannot input when a brew is complete, this gives the 
    //output handler time to eject the output, if all 3 slots are empty, then input is allowed again
    private ItemStackHandler inputStackHandler = new ItemStackHandlerControl(guiStackHandler) {        
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
        	return ItemStack.EMPTY;
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        	if (brewComplete) return stack;
        	return super.insertItem(slot, stack, simulate);
        }
                
        @Override
        public int getSlotLimit(int slot) {
        	return 1;
        }
    };
    
    //these are the output slots, you cannot insert, but can only extract when the brew is complete.
    private ItemStackHandler outputStackHandler =  new ItemStackHandlerControl(guiStackHandler) {

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
       	  	if (!brewComplete) return ItemStack.EMPTY;
       	  	return super.extractItem(slot, amount, simulate);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        	return stack;
        }
    };
    
    //top slot, always resets brew
    private ItemStackHandler ingredientStackHandler = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            setBrewComplete(false);
        }
    };
    
    //fuel == chrous fruit to stop overclocker penalty
    private ItemStackHandler fuelStackHandler = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    
    @SuppressWarnings("unchecked")
	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    	if (capability == CapabilityEnergy.ENERGY) return (T) storage;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) return (T) guiStackHandler;
            if (facing == EnumFacing.UP) return (T) ingredientStackHandler;
            if (facing == EnumFacing.DOWN) return (T) outputStackHandler;
            
            //if the block is being destroyed, the block is already air, 
            //so we cannot get the facing, so N, S, E, W all return the fuelSlot,
            //use "null" to get the input slots
            IBlockState bs = world.getBlockState(getPos());
            if (bs.getBlock() == Blocks.AIR) return (T) fuelStackHandler;
            
            EnumFacing blockFacing = bs.getValue(StorageBlockBase.FACING);
            if (blockFacing == EnumFacing.NORTH || blockFacing == EnumFacing.SOUTH) {
            	if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) return (T) inputStackHandler;
            	if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) return (T) fuelStackHandler;
            } else {
            	if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) return (T) fuelStackHandler;
            	if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) return (T) inputStackHandler;            	
            }
        }

        return super.getCapability(capability, facing);
    }
	
	private static final byte TEMP_FIELD_ID = 3;
	private static final byte BREW_FIELD_ID = 4;

	public int getField(int id) {
		if (id == TEMP_FIELD_ID) return this.lowTemp ? 1 : 0;
		if (id == BREW_FIELD_ID) return this.brewComplete ? 1 : 0;
		return super.getField(id);
	}

	public void setField(int id, int value) {
		if (id == TEMP_FIELD_ID) setTemp(value == 1);
		else if (id == BREW_FIELD_ID) setBrewComplete(value == 1);
		else super.setField(id, value);		
	}    
}
