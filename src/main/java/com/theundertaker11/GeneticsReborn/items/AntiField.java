package com.theundertaker11.geneticsreborn.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AntiField extends ItemBase{

	public AntiField(String name) {
		super(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack stack = playerIn.getHeldItemMainhand();
		if(stack.getItemDamage()==0) stack.setItemDamage(1);
		else stack.setItemDamage(0);
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return this.getDamage(stack)==1;
	}

}
