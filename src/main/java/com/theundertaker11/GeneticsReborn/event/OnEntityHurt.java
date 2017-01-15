package com.theundertaker11.GeneticsReborn.event;

import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.api.capability.genes.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.genes.IGenes;
import com.theundertaker11.GeneticsReborn.items.GRItems;
import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class OnEntityHurt {

	/**
	 * THIS IS CALLED BEFORE ARMOR IS FACTORED IN.
	 *This handles fire immunity, dragon health, and Wither Proof.(And a small part of poison proof)
	 *It checks the fire first so taking fire damage with that gene won't still take from health crystal durability 
	 */
	@SubscribeEvent
	public void onRawHurt(LivingAttackEvent event)
	{
		 if(!event.getEntity().getEntityWorld().isRemote)
		 {
			 EntityLivingBase entityliving = event.getEntityLiving();
			 if(ModUtils.getIGenes(entityliving)!=null)
			 {
				 IGenes genes = ModUtils.getIGenes(entityliving);
				 
				 if(GeneticsReborn.enableFireProof&&genes.hasGene(EnumGenes.FIRE_PROOF))
				 {
					 if(event.getSource().equals(DamageSource.lava) || event.getSource().equals(DamageSource.inFire) || event.getSource().equals(DamageSource.onFire))
					 {
						entityliving.extinguish();
						event.setCanceled(true);
					 }
				 }
				 if(GeneticsReborn.enableWitherProof&&genes.hasGene(EnumGenes.WITHER_PROOF))
				 {
					 System.out.println("It knows the entity has the gene");
					 if(event.getSource().equals(DamageSource.wither))
					 {
						entityliving.removePotionEffect(Potion.getPotionById(ModUtils.wither));
						event.setCanceled(true);
					 }
				 }
				 if(GeneticsReborn.enablePoisonProof&&genes.hasGene(EnumGenes.POISON_PROOF))
				 {
					 if(event.getSource().equals(DamageSource.magic) && entityliving.getActivePotionEffect(Potion.getPotionById(ModUtils.poison))!=null)
					 {
						entityliving.removePotionEffect(Potion.getPotionById(ModUtils.poison));
						event.setCanceled(true);
					 }
				 }
				 
				 
			 }
		 }
		 if(GeneticsReborn.enableWitherHit&&event.getSource().getEntity() instanceof EntityLivingBase)
		 {
			EntityLivingBase entityattacker = (EntityLivingBase)event.getSource().getEntity();
			if(ModUtils.getIGenes(entityattacker)!=null)
			{
				IGenes genes = ModUtils.getIGenes(entityattacker);
				EntityLivingBase entitytarget = event.getEntityLiving();
				if(genes.hasGene(EnumGenes.WITHER_HIT))
				{
					entitytarget.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.wither), 100, 1)));
				}
			}
		 }
	}
	
	/**
	 * Called after armor is factored in.
	 * @param event
	 */
	@SubscribeEvent
	public void onHurt(LivingHurtEvent event)
	{
		if(!event.getEntity().getEntityWorld().isRemote&&event.getEntity() instanceof EntityPlayer&&ModUtils.getIGenes(event.getEntityLiving())!=null)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			IGenes genes = ModUtils.getIGenes(player);
			if(GeneticsReborn.enableEnderDragonHealth&&genes.hasGene(EnumGenes.ENDER_DRAGON_HEALTH))
			{
				for(int i=0;i<player.inventory.getSizeInventory();i++)
				{
					if(player.inventory.getStackInSlot(i)!=null&&player.inventory.getStackInSlot(i).getItem()==GRItems.DragonHealthCrystal)
					{
						ItemStack stack = player.inventory.getStackInSlot(i);
						stack.damageItem((int)event.getAmount(), player);
						player.playSound(SoundEvents.BLOCK_ANVIL_LAND, 1.0F, 0.4F);
						//For some reason unless I manually remove it the game will crash or glitch when the dur gets to 0.
						//So this below checks for that and makes sure to remove it.
						if(stack.stackSize<1) player.inventory.setInventorySlotContents(i, null);
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
	public void onFall(LivingAttackEvent event)
	{
		if(GeneticsReborn.enableNoFallDamage&&event.getSource().equals(DamageSource.fall))
		{
			EntityLivingBase entityliving = event.getEntityLiving();
			if(ModUtils.getIGenes(entityliving)!=null)
			{
				IGenes genes = ModUtils.getIGenes(entityliving);
				if(genes.hasGene(EnumGenes.NO_FALL_DAMAGE))
				{
					event.setCanceled(true);
				}
			}
		}
	}
}
