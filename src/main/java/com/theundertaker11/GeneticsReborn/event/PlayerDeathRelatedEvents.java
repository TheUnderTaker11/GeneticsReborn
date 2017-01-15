package com.theundertaker11.GeneticsReborn.event;

import java.util.ArrayList;
import java.util.List;

import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.api.capability.genes.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.genes.IGenes;
import com.theundertaker11.GeneticsReborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.GeneticsReborn.util.ModUtils;
import com.theundertaker11.GeneticsReborn.util.PlayerCooldowns;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerDeathRelatedEvents {

	
	/**
	 * Checks for save inventory and puts all the drops back in their inventory if they have it.
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerDrops(PlayerDropsEvent event)
	{
		if(GeneticsReborn.enableSaveInventory&&ModUtils.getIGenes(event.getEntityPlayer())!=null)
		{
			IGenes genes = ModUtils.getIGenes(event.getEntityPlayer());
			if(genes.hasGene(EnumGenes.SAVE_INVENTORY))
			{
				for(int i=(event.getDrops().size()-1);i>=0;i--)
				{
					event.getEntityPlayer().inventory.setInventorySlotContents(i, event.getDrops().get(i).getEntityItem());
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
		if(ModUtils.getIMaxHealth(event.getEntityPlayer())!=null)
		{
			final IMaxHealth oldMaxHealth = ModUtils.getIMaxHealth(event.getOriginal());
			final IMaxHealth newMaxHealth = ModUtils.getIMaxHealth(event.getEntityPlayer());

			if (newMaxHealth != null && oldMaxHealth != null)
			{
				newMaxHealth.setBonusMaxHealth(oldMaxHealth.getBonusMaxHealth());
			}
		}
		if(GeneticsReborn.enableSaveInventory&&event.isWasDeath())
		{
			event.getEntityPlayer().inventory.copyInventory(event.getOriginal().inventory);
		}
		if(GeneticsReborn.keepGenesOnDeath&&event.isWasDeath())
		{
			IGenes oldgenes = event.getOriginal().getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
			IGenes newgenes = event.getEntityPlayer().getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
			newgenes.setGeneList(oldgenes.getGeneList());
		}
	}
	
	/**
	 * This makes players drop slimeballs and emeralds on death.
	 * @param event
	 */
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(ModUtils.getIGenes(player)!=null)
			{
				IGenes playergenes = ModUtils.getIGenes(player);
				if(GeneticsReborn.enableEmeraldHeart&&playergenes.hasGene(EnumGenes.EMERALD_HEART))
				{
					boolean allow = true;
	    			for(int i=0; i<GREventHandler.cooldownList.size();i++)
	    			{
	    				if(GREventHandler.cooldownList.get(i).getName().equals("emerald")&&player.getName().equals(GREventHandler.cooldownList.get(i).getPlayerName()))
	    				{
	    					allow = false;
	    					break;
	    				}
	    			}
	    			if(allow)
	    			{
	    				GREventHandler.cooldownList.add(new PlayerCooldowns(player, "emerald", 6000));
						EntityItem entity = new EntityItem(player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new ItemStack(Items.EMERALD));
						player.getEntityWorld().spawnEntityInWorld(entity);
					}
				}
				if(GeneticsReborn.enableSlimy&&playergenes.hasGene(EnumGenes.SLIMY))
				{
					EntityItem entity = new EntityItem(player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new ItemStack(Items.SLIME_BALL, 3));
					player.getEntityWorld().spawnEntityInWorld(entity);
				}
			}
		}
	}
}
