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
import net.minecraft.util.ActionResult;
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
			if(GeneticsReborn.playerGeneSharing)
			{
				stack = Genes.setNBTStringsFromPlayerGenes(stack, playerIn);	
			}
		}
		else if(stack.getItemDamage()==1){
			stack.setItemDamage(0);
			if(GeneticsReborn.playerGeneSharing)
			{
				IGenes genes = playerIn.getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
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
			
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		
    }
}
