package com.theundertaker11.GeneticsReborn.items;

import java.util.List;

import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Plasmid extends ItemBase {

	public Plasmid(String name) {
		super(name);
		this.setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		if(stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("gene")&&stack.getTagCompound().hasKey("numNeeded"))
		{
			String rawname = ModUtils.getTagCompound(stack).getString("gene");
			String genename = ModUtils.getGeneNameForShow(rawname);
			tooltip.add("Gene: "+genename);
			if(stack.getItem()==GRItems.Plasmid)
			{
				if(ModUtils.getTagCompound(stack).getInteger("num")==ModUtils.getTagCompound(stack).getInteger("numNeeded"))
				{
					tooltip.add("Complete!");
				}
				else tooltip.add(ModUtils.getTagCompound(stack).getInteger("num")+"/"+ModUtils.getTagCompound(stack).getInteger("numNeeded"));
			}
		}
		else tooltip.add("Gene not set.");
		if(stack.getItem()==GRItems.AntiPlasmid&&stack.getTagCompound()==null)
		{
			tooltip.add("Craft with a completed Plasmid to convert it.");
		}
    }

}
