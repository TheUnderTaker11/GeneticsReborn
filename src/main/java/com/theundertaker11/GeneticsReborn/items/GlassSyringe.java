package com.theundertaker11.GeneticsReborn.items;

import java.util.List;

import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.api.capability.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.Genes;
import com.theundertaker11.GeneticsReborn.api.capability.IGenes;
import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class GlassSyringe extends ItemBase {
	private static String cachedName;
	public GlassSyringe(String name){
		super(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
		this.cachedName = this.getUnlocalizedName();
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		tooltip.add("Right click to draw blood, shift right click to inject blood");
		if(stack.getTagCompound()!=null&&stack.getItemDamage()==1)
		{
			if(stack.getTagCompound().getBoolean("pure")) tooltip.add("This blood is purified");
			else tooltip.add("This blood is contaminated");
		}
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
		//Returns if nothing should happen.
		if(worldIn.isRemote||playerIn.getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null)==null||hand!=EnumHand.MAIN_HAND) return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		NBTTagCompound tag = ModUtils.getTagCompound(stack);
		if(!playerIn.isSneaking()&&stack.getItemDamage()==0) 
		{
			stack.setItemDamage(1);
			playerIn.attackEntityFrom(DamageSource.generic, 2);
			tag.setBoolean("pure", false);
			tag.setString("owner", playerIn.getName());
			Genes.setNBTStringsFromPlayerGenes(stack, playerIn);
		}
		Boolean configallows=true;
		if(!GeneticsReborn.playerGeneSharing)
		{
			configallows = tag.getString("owner").equals(playerIn.getName());
		}
		if(configallows&&stack.getItemDamage()==1&&tag.getBoolean("pure"))
		{
			stack.setItemDamage(0);
			playerIn.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.blindness), 60, 1)));
			playerIn.attackEntityFrom(DamageSource.generic, 1);
			IGenes genes = playerIn.getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
			tag.removeTag("pure");
			for(int i=0;i<Genes.TotalNumberOfGenes;i++)
		 	{
		 		String nbtname = "Null";
		 		if(tag.hasKey(i+""))
		 		{
		 			nbtname = tag.getString(i+"");
		 		}
		 		if(Genes.getGeneFromString(nbtname)!=null)	
		 		{
		 			genes.addGene(Genes.getGeneFromString(nbtname));
		 		}
		 	}	
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		
    }
}
