package com.theundertaker11.geneticsreborn.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.blocks.GRBlocks;
import com.theundertaker11.geneticsreborn.blocks.GRTileEntityLightBlock;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * This handles the enabling/disabling of flight based on player genes.
 */
public class OnWorldTickEvent {

	private static List<String> PlayersWithFlight = new ArrayList<String>();

	@SubscribeEvent
	public void WorldTick(WorldTickEvent event) {
		if (event.phase != Phase.START || event.world.isRemote) return;
		checkFlight(event);
		
		World w = event.world;
		for (Entity ent : ImmutableList.copyOf(w.loadedEntityList)) {
			if (ent instanceof EntityLivingBase) {
				EntityLivingBase e = (EntityLivingBase)ent;
				IGenes genes = ModUtils.getIGenes(e);
				if (genes.getGeneNumber() > 0  && !e.isDead) worldTickGeneLogic(genes, e, w);
				
				//BIOLUMIN gene check
				if (!EnumGenes.BIOLUMIN.isActive()) continue;
				if (!e.isDead && GRTileEntityLightBlock.needsLight(e)) {
				      BlockPos loc = new BlockPos(
				              MathHelper.floor(e.posX), 
				              MathHelper.floor(e.posY - 0.2D - e.getYOffset()), 
				              MathHelper.floor(e.posZ))
				    		  .up();
				      Block block = w.getBlockState(loc).getBlock();
				
				      if (block == GRBlocks.lightBlock)
				    	  continue;
				      else if (block == Blocks.AIR)
				          placeLightBlock(e, loc);
				      else {
				    	  loc = loc.up();
				          block = w.getBlockState(loc).getBlock();
				          
				          if (block == Blocks.AIR)
				              placeLightBlock(e, loc);
				     }
				}
				
			}
		}			
		
	}
	

	private void checkFlight(WorldTickEvent event) {
		if ((EnumGenes.FLY.isActive() || !GeneticsReborn.allowGivingEntityGenes) && GREventHandler.flightticktimer > 30 && !event.world.isRemote) {
			GREventHandler.flightticktimer = 0;
			for (EntityPlayerMP player : event.world.getMinecraftServer().getPlayerList().getPlayers()) {
				IGenes genes = ModUtils.getIGenes(player);
				if (player != null && genes != null) {
					if (!player.isCreative()) {
						String username = player.getName();
						if (genes.hasGene(EnumGenes.FLY)) {
							boolean shouldFly = true;
							for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
								ItemStack stack = player.inventory.getStackInSlot(i);
								if (stack.getItem() == GRItems.AntiField && stack.getItemDamage() == 1) {
									shouldFly = false;
									if (player.capabilities.isFlying) player.capabilities.isFlying = false;
									break;
								}
							}
							player.capabilities.allowFlying = shouldFly;
							player.sendPlayerAbilities();
							if (!PlayersWithFlight.contains(username)) PlayersWithFlight.add(username);
						} else if (PlayersWithFlight.contains(username)) {
							PlayersWithFlight.remove(username);
							player.capabilities.allowFlying = false;
							player.capabilities.isFlying = false;
							player.sendPlayerAbilities();
						}
					}
				}
			}
		}		
	}


	private static void placeLightBlock(EntityLivingBase entity, BlockPos pos) {
		entity.world.setBlockState(pos, GRBlocks.lightBlock.getDefaultState());
		TileEntity te = entity.world.getTileEntity(pos);
		if (te instanceof GRTileEntityLightBlock) {
			  GRTileEntityLightBlock teLight = (GRTileEntityLightBlock) te;
		      teLight.entity = entity;
		}
	}
	
	
	private static void kill(EntityLivingBase e) {
		e.attackEntityFrom(GeneticsReborn.VIRUS_DAMAGE, Float.MAX_VALUE);
		e.setHealth(0);
		e.setDead();
	}	
	
	private static void worldTickGeneLogic(IGenes genes, EntityLivingBase entity, World world) {
		if ((world.getWorldTime() % 40 != 0) || genes == null) return;
		
		long now = world.getWorldTime();
		PotionEffect pe= null;
		int luck = 0;

		for (EnumGenes gene : genes.getGeneList()) {
			if (!gene.isActive()) continue;
			switch (gene) {
				case WATER_BREATHING: 
					entity.setAir(300);
					break;
				case NIGHT_VISION: 
					if (entity instanceof EntityPlayer) entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.nightVision), 250, 0, false, false)));
					break;
				case JUMP_BOOST: 
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.jumpBoost), 50, 1, false, false)));
					break;
				case BLINDNESS:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.blindness), 50, 1, false, false)));
					break;
				case CURSED:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.badLuck), 50, 1, false, false)));
					break;
				case DEAD_ALL:
					entity.attackEntityFrom(GeneticsReborn.VIRUS_DAMAGE, entity.getHealth() / 2);
					if (entity.getHealth() < 1.1) kill(entity);
					break;
				case REALLY_DEAD_ALL:
					kill(entity);
					break;
				case DEAD_CREEPERS:
					if (entity instanceof EntityCreeper) kill(entity);
					break;
				case DEAD_HOSTILE:
					if (entity instanceof EntityMob) kill(entity);
					break;
				case DEAD_OLD_AGE:
					if (entity instanceof EntityAgeable) kill(entity);
					break;
				case DEAD_UNDEAD:
					if (entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) kill(entity);
					break;
				case FLAME:
					entity.setFire(120);
					break;
				case MOB_SIGHT:
					List<Entity> entities = world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(128, 128, 128), Predicates.instanceOf(EntityLivingBase.class));
					for (Entity e : entities)
						((EntityLivingBase) e).addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.glowing), 110, 0, false, false)));
					break;
				case HASTE:
					if (!genes.hasGene(EnumGenes.HASTE_2)) entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.haste), 50, 0, false, false)));
					break;
				case HASTE_2:
					 entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.haste), 50, 1+GeneticsReborn.mutationAmp, false, false)));
					break;
				case HUNGER:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.hunger), 50, 1, false, false)));
					break;
				case INVISIBLE:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.invisibility), 50, 0, false, false)));
					break;
				case LAY_EGG:
					pe = entity.getActivePotionEffect(Potion.getPotionById(ModUtils.luck));
					luck = (pe == null) ? 0 : pe.getAmplifier() + 1;
					if (genes.hasGene(EnumGenes.LAY_EGG) && GREventHandler.isCooldownExpired(entity, "egg", now, true)) {
						entity.dropItem(Items.EGG, 1+luck);				
					}
					if (genes.hasGene(EnumGenes.LAY_EGG)) GREventHandler.addCooldown(entity, "egg", now, 6000 + ThreadLocalRandom.current().nextInt(6000) - luck * 1200);
					break;
				case LEVITATION:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.levetation), 50, 1+GeneticsReborn.mutationAmp, false, false)));
					break;
				case LUCK:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.luck), 50, 1+GeneticsReborn.mutationAmp, false, false)));
					break;
				case MEATY_2:
					pe = entity.getActivePotionEffect(Potion.getPotionById(ModUtils.luck));
					luck = (pe == null) ? 0 : pe.getAmplifier() + 1;
					if (genes.hasGene(EnumGenes.MEATY_2) && GREventHandler.isCooldownExpired(entity, "meat", now, true)) {
						entity.dropItem(Items.COOKED_PORKCHOP, 1+luck);				
					}
					if (genes.hasGene(EnumGenes.MEATY_2)) GREventHandler.addCooldown(entity, "meat", now, 6000 + ThreadLocalRandom.current().nextInt(6000) - luck * 1200);
					break;
				case MINING_WEAKNESS:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.miningFatigure), 50, 1+GeneticsReborn.mutationAmp, false, false)));
					break;
				case NAUSEA:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.nausea), 50, 1, false, false)));
					break;
				case POISON:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.poison), 50, 1, false, false)));
					break;
				case POISON_4:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.poison), 50, 1+GeneticsReborn.mutationAmp, false, false)));
					break;
				case REGENERATION:
					if (!genes.hasGene(EnumGenes.REGENERATION_4)) entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.regeneration), 110, 1, false, false)));
					break;
				case REGENERATION_4:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.regeneration), 50, 3+GeneticsReborn.mutationAmp, false, false)));
					break;
				case RESISTANCE:
					if (!genes.hasGene(EnumGenes.RESISTANCE_2)) entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.resistance), 110, 0, false, false)));
					break;
				case RESISTANCE_2:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.resistance), 50, 1+GeneticsReborn.mutationAmp, false, false)));
					break;
				case SLOWNESS:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.moveSlowness), 50, 1, false, false)));
					break;
				case SLOWNESS_4:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.moveSlowness), 50, 3+GeneticsReborn.mutationAmp, false, false)));
					break;
				case SLOWNESS_6:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.moveSlowness), 50, 5+GeneticsReborn.mutationAmp, false, false)));
					break;
				case SPEED:
					if (!genes.hasGene(EnumGenes.SPEED_4) && !genes.hasGene(EnumGenes.SPEED_2)) entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.moveSpeed), 110, 0, false, false)));
					break;
				case SPEED_2:
					if (!genes.hasGene(EnumGenes.SPEED_4)) entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.moveSpeed), 110, 1+GeneticsReborn.mutationAmp, false, false)));
					break;
				case SPEED_4:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.moveSpeed), 50, GeneticsReborn.mutationAmp*2, false, false)));
					break;
				case STRENGTH:
					if (!genes.hasGene(EnumGenes.STRENGTH_2)) entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.strength), 110, 0, false, false)));
					break;
				case STRENGTH_2:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.strength), 50, 1+GeneticsReborn.mutationAmp, false, false)));
					break;
				case WEAKNESS:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.weakness), 50, 1, false, false)));
					break;
				case WITHER:
					entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.wither), 50, 1+GeneticsReborn.mutationAmp, false, false)));
					break;
				default:
					break;
					
			}
		}
		
	}
}
