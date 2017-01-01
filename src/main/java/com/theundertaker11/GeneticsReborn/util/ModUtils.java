package com.theundertaker11.GeneticsReborn.util;

import javax.annotation.Nullable;

import com.theundertaker11.GeneticsReborn.api.capability.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.IGenes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModUtils{
	//Potion effect ID's for easy use.
	public static int moveSpeed = 1;
	public static int moveSlowness = 2;
	public static int digSpeed = 3;
	public static int miningSlowDown = 4;
	public static int strength = 5;
	public static int jumpBoost = 8;
	public static int nausea = 9;
	public static int regeneration = 10;
	public static int resistance = 11;
	public static int fireResistance = 12;
	public static int waterBreathing = 13;
	public static int invisibility = 14;
	public static int blindness = 15;
	public static int nightVision = 16;
	public static int hunger = 17;
	public static int weakness = 18;
	public static int poison = 19;
	public static int wither = 20;
	/**
	 * This makes it so I don't have to check for null on every tag compound.
	 * Thanks to the Not Enough Wands mod for this code.
	 * https://github.com/romelo333/notenoughwands1.8.8/blob/1.10/src/main/java/romelo333/notenoughwands/varia/Tools.java
	 * @param stack
	 * @return
	 */
	public static NBTTagCompound getTagCompound(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null){
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        return tag;
    }
	/**
	 * Checks the player given has the item given, ignores NBT/Damage, only checks the actual item.
	 * @param player
	 * @param item
	 * @return
	 */
	public static boolean playerHasItem(EntityPlayer player, Item item) {
		for(int i=0;i<player.inventory.getSizeInventory();i++)
		{
			if(player.inventory.getStackInSlot(i)!=null&&player.inventory.getStackInSlot(i).getItem()==item) return true;
		}
		return false;
	}
	/**
	 * Gets the IGenes capability from the player.
	 * Can return a null.
	 * @param player
	 * @return
	 */
	@Nullable
	public static IGenes getIGenes(EntityPlayer player)
	{
		return player.getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
	}
}
