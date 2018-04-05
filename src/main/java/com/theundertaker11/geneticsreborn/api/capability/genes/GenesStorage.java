package com.theundertaker11.geneticsreborn.api.capability.genes;

import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class GenesStorage implements IStorage<IGenes> {

	@Override
	public NBTBase writeNBT(Capability<IGenes> capability, IGenes instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		List<EnumGenes> genelist = instance.getGeneList();
		for (int i = 0; i < instance.getGeneNumber(); i++) {
			String genename = genelist.get(i).toString();
			tag.setString(Integer.toString(i), "GeneticsReborn" + genename);
		}
		return tag;
	}


	@Override
	public void readNBT(Capability<IGenes> capability, IGenes instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		for (int i = 0; i < Genes.TotalNumberOfGenes; i++) {
			String nbtname = "Null";
			if (tag.hasKey(Integer.toString(i))) {
				nbtname = tag.getString(Integer.toString(i));
			}
			if (Genes.getGeneFromString(nbtname) != null) {
				instance.addGene(Genes.getGeneFromString(nbtname));
			}
		}
	}
}
