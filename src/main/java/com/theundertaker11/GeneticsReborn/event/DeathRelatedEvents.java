package com.theundertaker11.geneticsreborn.event;

import java.util.ArrayList;
import java.util.List;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.geneticsreborn.util.ModUtils;
import com.theundertaker11.geneticsreborn.util.PlayerCooldowns;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DeathRelatedEvents {
	
	public static List<String> playersWithGunpowder = new ArrayList<String>();
	
	/**
	 * Checks for save inventory and puts all the drops back in their inventory if they have it.
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerDrops(PlayerDropsEvent event)
	{
		//Checks if the player has 5 gunpowder.
		if(GeneticsReborn.enableExplosiveExit&&ModUtils.getIGenes(event.getEntityPlayer())!=null)
		{
			IGenes genes = ModUtils.getIGenes(event.getEntityPlayer());
			if(genes.hasGene(EnumGenes.EXPLOSIVE_EXIT))
			{
				for(int i=(event.getDrops().size()-1);i>=0;i--)
				{
					if(event.getDrops().get(i).getItem().getItem()==Items.GUNPOWDER&&event.getDrops().get(i).getItem().getCount()>4)
					{
						playersWithGunpowder.add(event.getEntityPlayer().getName());
					}
				}
			}
		}
		//Puts all items back in the players inventory
		if(GeneticsReborn.enableSaveInventory&&ModUtils.getIGenes(event.getEntityPlayer())!=null)
		{
			IGenes genes = ModUtils.getIGenes(event.getEntityPlayer());
			if(genes.hasGene(EnumGenes.SAVE_INVENTORY))
			{
				for(int i=(event.getDrops().size()-1);i>=0;i--)
				{
					event.getEntityPlayer().inventory.setInventorySlotContents(i, event.getDrops().get(i).getItem());
					event.getDrops().remove(i);
				}
			}
		}
	}
	/**
	 * This makes players keep genes, health, and inventory on death(If enabled in config)
	 */
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event)
	{
		if(GeneticsReborn.enableSaveInventory&&event.isWasDeath())
		{
			event.getEntityPlayer().inventory.copyInventory(event.getOriginal().inventory);
		}
		if(GeneticsReborn.keepGenesOnDeath||!event.isWasDeath())
		{
			IGenes oldgenes = ModUtils.getIGenes(event.getOriginal());
			IGenes newgenes = ModUtils.getIGenes(event.getEntityPlayer());
			newgenes.setGeneList(oldgenes.getGeneList());
			if(ModUtils.getIMaxHealth(event.getEntityPlayer())!=null)
			{
				final IMaxHealth oldMaxHealth = ModUtils.getIMaxHealth(event.getOriginal());
				final IMaxHealth newMaxHealth = ModUtils.getIMaxHealth(event.getEntityPlayer());

				if (newMaxHealth != null && oldMaxHealth != null)
				{
					newMaxHealth.setBonusMaxHealth(oldMaxHealth.getBonusMaxHealth());
				}
			}
		}
	}
	
	/**
	 * This makes players drop slimeballs and emeralds on death.
	 * Also makes all entitylivingbases explode if they have needed gene.
	 * @param event
	 */
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event)
	{
		EntityLivingBase entityliving = event.getEntityLiving();
		
		if(!GeneticsReborn.allowGivingEntityGenes&&!(entityliving instanceof EntityPlayer)) return;
		
		if(entityliving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entityliving;
			if(ModUtils.getIGenes(player)!=null)
			{
				IGenes playergenes = ModUtils.getIGenes(player);
				if(GeneticsReborn.enableEmeraldHeart&&playergenes.hasGene(EnumGenes.EMERALD_HEART))
				{
					boolean allow = true;
	    			for(int i=0; i<GREventHandler.cooldownList.size();i++)
	    			{
	    				if("emerald".equals(GREventHandler.cooldownList.get(i).getName())&&player.getName().equals(GREventHandler.cooldownList.get(i).getPlayerName()))
	    				{
	    					allow = false;
	    					break;
	    				}
	    			}
	    			if(allow)
	    			{
	    				GREventHandler.cooldownList.add(new PlayerCooldowns(player, "emerald", 6000));
						EntityItem entity = new EntityItem(player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new ItemStack(Items.EMERALD));
						player.getEntityWorld().spawnEntity(entity);
					}
				}
				if(GeneticsReborn.enableSlimy&&playergenes.hasGene(EnumGenes.SLIMY))
				{
					EntityItem entity = new EntityItem(player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new ItemStack(Items.SLIME_BALL, 3));
					player.getEntityWorld().spawnEntity(entity);
				}
			}
		}
		
		if(GeneticsReborn.enableExplosiveExit&&entityliving!=null&&ModUtils.getIGenes(entityliving)!=null)
		{
			IGenes genes = ModUtils.getIGenes(entityliving);
			if(genes.hasGene(EnumGenes.EXPLOSIVE_EXIT))
			{
				boolean allow = true;
				if(entityliving instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)entityliving;
					if(!playersWithGunpowder.contains(player.getName()))
					{
						allow=false;
					}
					else{
						playersWithGunpowder.remove(player.getName());
					}
				}
				if(allow)
				{
					entityliving.getEntityWorld().createExplosion(entityliving, entityliving.getPosition().getX(), 
							entityliving.getPosition().getY(), entityliving.getPosition().getZ(), 2, true);
				}
			}
		}
	}
}
