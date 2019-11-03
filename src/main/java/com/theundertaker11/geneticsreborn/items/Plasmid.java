package com.theundertaker11.geneticsreborn.items;

import java.util.List;

import javax.annotation.Nullable;

import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Plasmid extends ItemBase {

	public Plasmid(String name) {
		super(name);
		this.setMaxStackSize(1);
	}

	@Override
    @SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("gene") && stack.getTagCompound().hasKey("numNeeded")) {
			String rawname = ModUtils.getTagCompound(stack).getString("gene");
			String genename = ModUtils.getGeneNameForShow(rawname);
			tooltip.add("Gene: " + genename);
			if (stack.getItem() == GRItems.Plasmid) {
				if (ModUtils.getTagCompound(stack).getInteger("num") == ModUtils.getTagCompound(stack).getInteger("numNeeded")) {
					tooltip.add("Complete!");
				} else
					tooltip.add(ModUtils.getTagCompound(stack).getInteger("num") + "/" + ModUtils.getTagCompound(stack).getInteger("numNeeded"));
			}
		} else tooltip.add("Gene not set.");
		if (stack.getItem() == GRItems.AntiPlasmid && stack.getTagCompound() == null) {
			tooltip.add("Craft with a completed Plasmid to convert it.");
		}
	}
}
