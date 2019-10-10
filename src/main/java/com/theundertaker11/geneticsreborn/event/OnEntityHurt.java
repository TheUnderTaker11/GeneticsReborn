package com.theundertaker11.geneticsreborn.event;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class OnEntityHurt {

	/**
	 * THIS IS CALLED BEFORE ARMOR IS FACTORED IN.
	 * This handles fire immunity, dragon health, and Wither Proof.(And a small part of poison proof)
	 * It checks the fire first so taking fire damage with that gene won't still take from health crystal durability
	 */
	@SubscribeEvent
	public void onRawHurt(LivingAttackEvent event) {
		if (!event.getEntity().getEntityWorld().isRemote) {
			if (EnumGenes.WITHER_HIT.isActive() && event.getSource().getTrueSource() instanceof EntityLivingBase) {
				EntityLivingBase entityattacker = (EntityLivingBase) event.getSource().getTrueSource();
				if (ModUtils.getIGenes(entityattacker) != null) {
					IGenes genes = ModUtils.getIGenes(entityattacker);
					EntityLivingBase entitytarget = event.getEntityLiving();
					if (genes.hasGene(EnumGenes.WITHER_HIT)) {
						entitytarget.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.wither), 100, 1)));
					}
				}
			}

			EntityLivingBase entityliving = event.getEntityLiving();
			if (!GeneticsReborn.allowGivingEntityGenes && !(entityliving instanceof EntityPlayer)) return;

			if (ModUtils.getIGenes(entityliving) != null) {
				IGenes genes = ModUtils.getIGenes(entityliving);

				if (EnumGenes.FIRE_PROOF.isActive() && genes.hasGene(EnumGenes.FIRE_PROOF)) {
					if (event.getSource().equals(DamageSource.LAVA) || event.getSource().equals(DamageSource.IN_FIRE)
							|| event.getSource().equals(DamageSource.ON_FIRE)) {
						entityliving.extinguish();
						event.setCanceled(true);
					}
				}
				if (EnumGenes.WITHER_PROOF.isActive() && genes.hasGene(EnumGenes.WITHER_PROOF)) {
					if (event.getSource().equals(DamageSource.WITHER)) {
						event.setCanceled(true);
					}
				}
				if (EnumGenes.POISON_PROOF.isActive() && genes.hasGene(EnumGenes.POISON_PROOF)) {
					if (event.getSource().equals(DamageSource.MAGIC) && entityliving.getActivePotionEffect(Potion.getPotionById(ModUtils.poison)) != null) {
						event.setCanceled(true);
					}
				}


			}
		}
	}

	/**
	 * Called after armor is factored in.
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onHurt(LivingHurtEvent event) {
		if (!event.getEntity().getEntityWorld().isRemote && event.getEntity() instanceof EntityPlayer && ModUtils.getIGenes(event.getEntityLiving()) != null) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IGenes genes = ModUtils.getIGenes(player);
			if (EnumGenes.ENDER_DRAGON_HEALTH.isActive() && genes.hasGene(EnumGenes.ENDER_DRAGON_HEALTH)) {
				for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
					if (player.inventory.getStackInSlot(i).getItem() == GRItems.DragonHealthCrystal) {
						ItemStack stack = player.inventory.getStackInSlot(i);
						stack.damageItem((int) event.getAmount(), player);
						player.playSound(SoundEvents.BLOCK_ANVIL_LAND, 1.0F, 0.4F);
						//For some reason unless I manually remove it the game will crash or glitch when the dur gets to 0.
						//So this below checks for that and makes sure to remove it.
						if (stack.getCount() < 1) player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
						event.setCanceled(true);
						break;
					}
				}
			}
		}
	}

	/**
	 * This handles the no fall gene. Is livingattack since NoFall doesn't always fire
	 */
	@SubscribeEvent
	public void onFall(LivingAttackEvent event) {
		if (EnumGenes.NO_FALL_DAMAGE.isActive() && event.getSource().equals(DamageSource.FALL)) {
			EntityLivingBase entityliving = event.getEntityLiving();
			if (!GeneticsReborn.allowGivingEntityGenes && !(entityliving instanceof EntityPlayer)) return;

			if (ModUtils.getIGenes(entityliving) != null) {
				IGenes genes = ModUtils.getIGenes(entityliving);
				if (genes.hasGene(EnumGenes.NO_FALL_DAMAGE)) {
					event.setCanceled(true);
				}
			}
		}
	}
}
