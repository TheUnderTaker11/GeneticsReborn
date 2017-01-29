package com.theundertaker11.GeneticsReborn.items;

import java.util.List;

import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DNAHelix extends ItemBase{

	public DNAHelix(String name) {
		super(name);
		this.maxStackSize=1;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		if(stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("entityName"))
		{
			tooltip.add("Gene of a "+ModUtils.getTagCompound(stack).getString("entityName"));
			if(ModUtils.getTagCompound(stack).hasKey("gene"))
			{
				String rawname = ModUtils.getTagCompound(stack).getString("gene");
				String genename = ModUtils.getGeneNameForShow(rawname);
				tooltip.add("Gene type: "+genename);
			}
			else tooltip.add("Gene type: Unknown");
		}
    }
}
