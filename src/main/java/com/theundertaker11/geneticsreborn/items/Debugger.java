package com.theundertaker11.geneticsreborn.items;

import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.geneticsreborn.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Debugger extends ItemBase {
	public Debugger(String name) {
		super(name);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Right click to add all genes, shift right click to remove all genes.");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack stack = playerIn.getHeldItemMainhand();
		if (worldIn.isRemote || ModUtils.getIGenes(playerIn) == null || hand != EnumHand.MAIN_HAND || ModUtils.getIMaxHealth(playerIn) == null)
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		IGenes genes = ModUtils.getIGenes(playerIn);
		IMaxHealth maxhealth = ModUtils.getIMaxHealth(playerIn);
		if (!playerIn.isSneaking()) {
			genes.addAllGenes();
			maxhealth.setBonusMaxHealth(20);
			playerIn.sendMessage(new TextComponentString("Added genes"));
		} else {
			genes.removeAllGenes();
			maxhealth.setBonusMaxHealth(0);
			playerIn.sendMessage(new TextComponentString("Removed genes"));
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}
}
