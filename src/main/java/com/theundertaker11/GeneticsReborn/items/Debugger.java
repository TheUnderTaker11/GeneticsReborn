package com.theundertaker11.geneticsreborn.items;

import java.util.List;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
		ItemStack stack = playerIn.getHeldItemMainhand();
		//Returns if nothing should happen.
		if(worldIn.isRemote||ModUtils.getIGenes(playerIn)==null||hand!=EnumHand.MAIN_HAND||ModUtils.getIMaxHealth(playerIn)==null) return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		//NBTTagCompound tag = ModUtils.getTagCompound(stack);
		IGenes genes = ModUtils.getIGenes(playerIn);
		IMaxHealth maxhealth = ModUtils.getIMaxHealth(playerIn);
		if(!playerIn.isSneaking())
		{
			genes.addAllGenes();
			maxhealth.setBonusMaxHealth(20);
			playerIn.sendMessage(new TextComponentString("Added genes"));
		}
		else
		{
			genes.removeAllGenes();
			maxhealth.setBonusMaxHealth(0);
			playerIn.sendMessage(new TextComponentString("Removed genes"));
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
	/*
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		if(!player.getEntityWorld().isRemote&&entity instanceof EntityLivingBase)
		{
			EntityLivingBase entityLiving = (EntityLivingBase)entity;
			if(ModUtils.getIGenes(entityLiving)!=null)
			{
				IGenes genes = ModUtils.getIGenes(entityLiving);
				
				if(!player.isSneaking())
				{
					genes.addGene(EnumGenes.EXPLOSIVE_EXIT);
					player.addChatMessage(new TextComponentString("Added genes"));
				}
				else
				{
					genes.removeAllGenes();
					player.addChatMessage(new TextComponentString("Removed genes"));
				}
			}
		}
		return true;
    }*/
}
