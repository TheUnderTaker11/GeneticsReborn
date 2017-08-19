package com.theundertaker11.geneticsreborn.api.capability.genes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class GeneCapabilityProvider implements ICapabilitySerializable<NBTBase>
{
	@CapabilityInject(IGenes.class)
	public static final Capability<IGenes> GENES_CAPABILITY = null;

	private IGenes instance = GENES_CAPABILITY.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == GENES_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == GENES_CAPABILITY ? GENES_CAPABILITY.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT()
	{
		return GENES_CAPABILITY.getStorage().writeNBT(GENES_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		GENES_CAPABILITY.getStorage().readNBT(GENES_CAPABILITY, this.instance, null, nbt);
	}
}
