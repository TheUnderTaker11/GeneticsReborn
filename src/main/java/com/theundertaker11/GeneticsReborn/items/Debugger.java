package com.theundertaker11.GeneticsReborn.items;

import java.util.List;

import com.theundertaker11.GeneticsReborn.api.capability.genes.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.genes.IGenes;
import com.theundertaker11.GeneticsReborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class Debugger extends ItemBase{
	public Debugger(String name) {
		super(name);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		tooltip.add("Right click to add all genes, shift right click to remove all genes.");
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
		//Returns if nothing should happen.
		if(worldIn.isRemote||ModUtils.getIGenes(playerIn)==null||hand!=EnumHand.MAIN_HAND||ModUtils.getIMaxHealth(playerIn)==null) return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		NBTTagCompound tag = ModUtils.getTagCompound(stack);
		IGenes genes = ModUtils.getIGenes(playerIn);
		IMaxHealth maxhealth = ModUtils.getIMaxHealth(playerIn);
		if(!playerIn.isSneaking())
		{
			genes.addGene(EnumGenes.SCARE_CREEPERS);
			playerIn.addChatMessage(new TextComponentString("Added genes "));
		}
		else
		{
			genes.removeGene(EnumGenes.SCARE_CREEPERS);
			playerIn.addChatMessage(new TextComponentString("Removed genes "));
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
	
}
