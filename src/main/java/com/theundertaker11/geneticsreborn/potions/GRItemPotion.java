package com.theundertaker11.geneticsreborn.potions;

import com.theundertaker11.geneticsreborn.GeneticsReborn;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class GRItemPotion extends ItemPotion {
	public GRItemPotion(String name) {
		setRegistryName(name);
		setUnlocalizedName(name);
		setMaxStackSize(64);
		setCreativeTab(GeneticsReborn.GRtab);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return false;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == GeneticsReborn.GRtab) {
			items.add(PotionUtils.addPotionToItemStack(new ItemStack(this), GRPotions.GROWTH_POTION));
			items.add(PotionUtils.addPotionToItemStack(new ItemStack(this), GRPotions.SUBSTRATE));
			items.add(PotionUtils.addPotionToItemStack(new ItemStack(this), GRPotions.MUTATION_POTION));
			items.add(PotionUtils.addPotionToItemStack(new ItemStack(this), GRPotions.VIRAL_POTION));
			items.add(PotionUtils.addPotionToItemStack(new ItemStack(this), GRPotions.CURE_POTION));
		}
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
        PotionType potion = PotionUtils.getPotionFromItem(itemstack);
        
        if (potion == GRPotions.CURE_POTION) return super.onItemRightClick(worldIn, playerIn, handIn);
        
        if (potion != GRPotions.GROWTH_POTION && potion != GRPotions.MUTATION_POTION) 
        	return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        	        
        ItemStack itemstack1 = playerIn.capabilities.isCreativeMode ? itemstack.copy() : itemstack.splitStack(1);
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
        	GREntityPotion ep = new GREntityPotion(worldIn, playerIn, itemstack1);
            ep.setBlastRange(0.0f);
            ep.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
            worldIn.spawnEntity(ep);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }	
	
}
