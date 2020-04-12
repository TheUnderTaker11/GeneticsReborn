package com.theundertaker11.geneticsreborn.potions;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ViralSplashPotion extends ItemSplashPotion {
	public ViralSplashPotion(String name) {
		setRegistryName(name);
		setUnlocalizedName(name);
		setMaxStackSize(64);
	}
	
	public static EnumGenes getGene(ItemStack stack) {
		NBTTagCompound tag = ModUtils.getTagCompound(stack);
		return EnumGenes.fromGeneName(tag.getString("gene"));
	}
	
	@Override
    public String getItemStackDisplayName(ItemStack stack) {
		EnumGenes gene = getGene(stack);
		if (gene == null) return super.getItemStackDisplayName(stack);		
		return (new TextComponentTranslation("potion.viral.name")).getFormattedText() + " " + gene.getDescription();
    }

	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
		EnumGenes gene = getGene(itemstack);
		if (gene == null && itemstack.getItem() != GRItems.ViralPotion) return super.onItemRightClick(worldIn, playerIn, handIn);
		
        ItemStack itemstack1 = playerIn.capabilities.isCreativeMode ? itemstack.copy() : itemstack.splitStack(1);
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            EntityPotion potion = new GREntityPotion(worldIn, playerIn, itemstack1);
            ((GREntityPotion)potion).setGene(gene);
            potion.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
            worldIn.spawnEntity(potion);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return false;
    }    
}
