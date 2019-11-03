package com.theundertaker11.geneticsreborn.items;

import java.util.List;

import javax.annotation.Nullable;

import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DNAHelix extends ItemBase {

	public DNAHelix(String name) {
		super(name);
	}

	@Override
    @SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("entityName")) {
			tooltip.add("Gene of a " + ModUtils.getTagCompound(stack).getString("entityName"));
			if (ModUtils.getTagCompound(stack).hasKey("gene")) {
				String rawname = ModUtils.getTagCompound(stack).getString("gene");
				String genename = ModUtils.getGeneNameForShow(rawname);
				tooltip.add("Gene type: " + genename);
			} else tooltip.add("Gene type: Unknown");
		}
	}
}
