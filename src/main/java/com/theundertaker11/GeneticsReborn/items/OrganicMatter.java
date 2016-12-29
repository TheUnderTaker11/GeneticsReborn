package com.theundertaker11.GeneticsReborn.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OrganicMatter extends ItemBase {

	public OrganicMatter(String name) {
		super(name);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		if(stack.getTagCompound()!=null)
		{
			tooltip.add("Cell type: "+stack.getTagCompound().getString("entityName"));
		}
    }

}
