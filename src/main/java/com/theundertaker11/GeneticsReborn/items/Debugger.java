package com.theundertaker11.GeneticsReborn.items;

import com.theundertaker11.GeneticsReborn.api.capability.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.IGenes;
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
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
		//Returns if nothing should happen.
		if(worldIn.isRemote||playerIn.getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null)==null||hand!=EnumHand.MAIN_HAND) return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		NBTTagCompound tag = ModUtils.getTagCompound(stack);
		IGenes genes = playerIn.getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
		if(!playerIn.isSneaking())
		{
			genes.addGene(EnumGenes.EAT_GRASS);
			playerIn.addChatMessage(new TextComponentString("Added gene"));
		}
		else
		{
			genes.removeGene(EnumGenes.EAT_GRASS);
			playerIn.addChatMessage(new TextComponentString("Removed gene"));
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
	
}
