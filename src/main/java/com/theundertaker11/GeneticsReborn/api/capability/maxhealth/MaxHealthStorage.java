package com.theundertaker11.GeneticsReborn.api.capability.maxhealth;

import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MaxHealthStorage implements IStorage<IMaxHealth>{
	
	@Override
 	public NBTBase writeNBT(Capability<IMaxHealth> capability, IMaxHealth instance, EnumFacing side)
 	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("BonusHealth", instance.getBonusMaxHealth());
		return tag;
 	}
	
	@Override
 	public void readNBT(Capability<IMaxHealth> capability, IMaxHealth instance, EnumFacing side, NBTBase nbt)
 	{
		if(capability==null) return;
 		NBTTagCompound tag = (NBTTagCompound)nbt;
 		instance.addBonusMaxHealth(tag.getFloat("BonusHealth"));
 	}

}
