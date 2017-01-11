package com.theundertaker11.GeneticsReborn.api.capability.maxhealth;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.theundertaker11.GeneticsReborn.Reference;
import com.theundertaker11.GeneticsReborn.util.ModUtils;

import javax.annotation.Nullable;


public class MaxHealthCapabilityProvider implements ICapabilitySerializable<NBTBase>{

	@CapabilityInject(IMaxHealth.class)
	public static final Capability<IMaxHealth> MAX_HEALTH_CAPABILITY = null;

	private IMaxHealth instance;

	public MaxHealthCapabilityProvider(IMaxHealth Instance)
	{
		this.instance = Instance;
	}
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == MAX_HEALTH_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == MAX_HEALTH_CAPABILITY ? MAX_HEALTH_CAPABILITY.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT()
	{
		return MAX_HEALTH_CAPABILITY.getStorage().writeNBT(MAX_HEALTH_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		MAX_HEALTH_CAPABILITY.getStorage().readNBT(MAX_HEALTH_CAPABILITY, this.instance, null, nbt);
	}

}
