package com.theundertaker11.geneticsreborn.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class DeathRelatedEvents {

	public static List<String> playersWithGunpowder = new ArrayList<String>();

	/**
	 * Checks for save inventory and puts all the drops back in their inventory if they have it.
	 *
	 * @param event
	 */
	@SubscribeEvent
	public static void onPlayerDrops(PlayerDropsEvent event) {
		if (EnumGenes.EXPLOSIVE_EXIT.isActive() && ModUtils.getIGenes(event.getEntityPlayer()) != null) {
			IGenes genes = ModUtils.getIGenes(event.getEntityPlayer());
			if (genes.hasGene(EnumGenes.EXPLOSIVE_EXIT)) {
				for (int i = (event.getDrops().size() - 1); i >= 0; i--) {
					if (event.getDrops().get(i).getItem().getItem() == Items.GUNPOWDER && event.getDrops().get(i).getItem().getCount() > 4) {
						playersWithGunpowder.add(event.getEntityPlayer().getName());
					}
				}
			}
		}
		if (EnumGenes.SAVE_INVENTORY.isActive() && ModUtils.getIGenes(event.getEntityPlayer()) != null) {
			IGenes genes = ModUtils.getIGenes(event.getEntityPlayer());
			if (genes.hasGene(EnumGenes.SAVE_INVENTORY)) {
				for (int i = (event.getDrops().size() - 1); i >= 0; i--) {
					event.getEntityPlayer().inventory.setInventorySlotContents(i, event.getDrops().get(i).getItem());
					event.getDrops().remove(i);
				}
			}
		}
	}
	
    @SubscribeEvent
    public static void onPlayerLoggedIn (PlayerLoggedInEvent event) {
    	IGenes genes = ModUtils.getIGenes(event.player);
    	for (EnumGenes g : genes.getGeneList()) 
    		PlayerTickEvent.geneChanged(event.player, g, true);
    }

	/**
	 * This makes players keep genes, health, and inventory on death(If enabled in config)
	 */
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (EnumGenes.SAVE_INVENTORY.isActive() && event.isWasDeath()) {
			event.getEntityPlayer().inventory.copyInventory(event.getOriginal().inventory);
		}
		if (GeneticsReborn.keepGenesOnDeath || !event.isWasDeath()) {
			IGenes oldgenes = ModUtils.getIGenes(event.getOriginal());
			IGenes newgenes = ModUtils.getIGenes(event.getEntityPlayer());
			newgenes.setGeneList(oldgenes.getGeneList());
			newgenes.removeGene(EnumGenes.DEAD_ALL);
			newgenes.removeGene(EnumGenes.DEAD_CREEPERS);
			newgenes.removeGene(EnumGenes.DEAD_UNDEAD);
			newgenes.removeGene(EnumGenes.DEAD_OLD_AGE);
			
			if (ModUtils.getIMaxHealth(event.getEntityPlayer()) != null) {
				final IMaxHealth oldMaxHealth = ModUtils.getIMaxHealth(event.getOriginal());
				final IMaxHealth newMaxHealth = ModUtils.getIMaxHealth(event.getEntityPlayer());

				if (newMaxHealth != null && oldMaxHealth != null) {
					newMaxHealth.setBonusMaxHealth(oldMaxHealth.getBonusMaxHealth());
				}
			}
		}
		
		
	}

	/**
	 * Also makes all entitylivingbases explode if they have needed gene.
	 *
	 * @param event
	 */
	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {
		EntityLivingBase entityliving = event.getEntityLiving();

		if (!GeneticsReborn.allowGivingEntityGenes && !(entityliving instanceof EntityPlayer)) return;

		if (entityliving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityliving;
			if (ModUtils.getIGenes(player) != null) {
				IGenes playergenes = ModUtils.getIGenes(player);
				long now =  player.getEntityWorld().getWorldTime();
				if (EnumGenes.EMERALD_HEART.isActive() && playergenes.hasGene(EnumGenes.EMERALD_HEART)) {
					boolean allow = !GREventHandler.isInCooldown(player,  "emerald", now);
					if (allow) {						
						GREventHandler.addCooldown(player, "emerald", now, 6000);
						Item item = Items.EMERALD;
						int i = ThreadLocalRandom.current().nextInt(1000);
						if (i < 30) item = Items.NETHER_STAR;
						else if (i < 5) item = Items.END_CRYSTAL;								
						EntityItem entity = new EntityItem(player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new ItemStack(item));						
						player.getEntityWorld().spawnEntity(entity);
					}
				}
			}
		}

		if (EnumGenes.EXPLOSIVE_EXIT.isActive() && entityliving != null && ModUtils.getIGenes(entityliving) != null) {
			IGenes genes = ModUtils.getIGenes(entityliving);
			if (genes.hasGene(EnumGenes.EXPLOSIVE_EXIT)) {
				boolean allow = true;
				if (entityliving instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entityliving;
					if (!playersWithGunpowder.contains(player.getName())) {
						allow = false;
					} else {
						playersWithGunpowder.remove(player.getName());
					}
				}
				if (allow) {
					entityliving.getEntityWorld().createExplosion(entityliving, entityliving.getPosition().getX(),
							entityliving.getPosition().getY(), entityliving.getPosition().getZ(), 2, true);
				}
			}
		}
	}
}
