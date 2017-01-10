package com.theundertaker11.GeneticsReborn.items;

import com.theundertaker11.GeneticsReborn.api.capability.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.IGenes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DamageableItemBase extends ItemBase {

	public DamageableItemBase(String name, int maxdamage, boolean full3d) {
		super(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxdamage);
		this.bFull3D=full3d;
	}	
}
