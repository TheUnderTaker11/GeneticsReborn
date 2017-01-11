package com.theundertaker11.GeneticsReborn.api.capability.genes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Genes implements IGenes{

	public static final int TotalNumberOfGenes = EnumGenes.values().length;
	private List<EnumGenes> EnumGenesList = new ArrayList<EnumGenes>();
	
	@Override
	public void addGene(EnumGenes gene)
	{
		if(!EnumGenesList.contains(gene)) EnumGenesList.add(gene);
	}

	@Override
	public void removeGene(EnumGenes gene)
	{
		if(EnumGenesList.contains(gene))EnumGenesList.remove(gene);
	}
	
	@Override
	public boolean hasGene(EnumGenes gene)
	{
		if(EnumGenesList.contains(gene)) return true;
		else return false;
	}
	@Override
	public List<EnumGenes> getGeneList()
	{
		return this.EnumGenesList;
	}
	
	@Override
	public void setGeneList(List<EnumGenes> list)
	{
		this.EnumGenesList = list;
	}

	@Override
	public void removeAllGenes()
	{
		this.EnumGenesList = new ArrayList<EnumGenes>();
	}

	@Override
	public void addAllGenes()
	{
		this.EnumGenesList = new ArrayList<EnumGenes>(Arrays.asList(EnumGenes.values()));
	}
	
	public int getGeneNumber()
	{
		return this.EnumGenesList.size();
	}
	///////BELOW THIS LINE ARE THINGS THAT WOULD GO IN MY MODUTILS, BUT I WANT TO KEEP IT ALL IN THE API SECTION//////////////
	/**
	 * Given a string it tells if it matches to the gene, used to read the NBT I write to the player.
	 * Format is EnumGene to string with "GeneticsReborn" as the prefix.
	 * CAN BE NULL
	 * @param nbtstring "GeneticsReborn"+EnumGene.toString
	 * @return EnumGenes gene
	 */
	public static EnumGenes getGeneFromString(String nbtstring)
	{
		if(nbtstring.equals("Null")) return null;
		ArrayList<EnumGenes> allGenes = new ArrayList<EnumGenes>(Arrays.asList(EnumGenes.values()));
		for(int i=0;i<allGenes.size();i++)
		{
			String genename = allGenes.get(i).toString();
			if(nbtstring.equals("GeneticsReborn"+genename))
			{
				return allGenes.get(i);
			}
		}
		return null;
	}
	/**
	 * Give it a stack and a player and it will set the NBTStrings of all the players genes to
	 * an itemstack and return that.
	 * @param stack
	 * @param player
	 * @return
	 */
	public static ItemStack setNBTStringsFromPlayerGenes(ItemStack stack, EntityPlayer player)
	{
		NBTTagCompound tag = ModUtils.getTagCompound(stack);
		IGenes genes = player.getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
		List<EnumGenes> genelist = genes.getGeneList();
		for(int i=0;i<genes.getGeneNumber();i++)
		{
			String genename = genelist.get(i).toString();
			tag.setString(i+"", "GeneticsReborn"+genename);
		}
		return stack;
	}
}
