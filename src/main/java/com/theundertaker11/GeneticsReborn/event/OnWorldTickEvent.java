package com.theundertaker11.GeneticsReborn.event;

import java.util.ArrayList;
import java.util.List;

import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.api.capability.genes.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.genes.IGenes;
import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
/**
 * This handles the enabling/disabling of flight based on player genes.
 */
public class OnWorldTickEvent {
	
	private static List<String> PlayersWithFlight = new ArrayList<String>();
	
	@SubscribeEvent
	public void WorldTick(WorldTickEvent event)
	{
		//This will allow players to fly if they have the Flight gene.(Currently doesn't remove flight)
		if(GeneticsReborn.enableFlight&&GREventHandler.flightticktimer>40&&event.world.provider.getDimension()==0&&!event.world.isRemote)	
		{
			GREventHandler.flightticktimer = 0;
			for(EntityPlayerMP player: event.world.getMinecraftServer().getPlayerList().getPlayerList())
			{
				if(!player.isCreative()&&ModUtils.getIGenes(player)!=null)
				{
					IGenes genes = ModUtils.getIGenes(player);
					String username = player.getName();
					//This if/else enables/disables flight
					if(genes.hasGene(EnumGenes.FLY))
					{
						player.capabilities.allowFlying = true;
						player.sendPlayerAbilities();
						if(!PlayersWithFlight.contains(username)) PlayersWithFlight.add(username);
					}
					else if(PlayersWithFlight.contains(username))
					{
						PlayersWithFlight.remove(username);
						player.capabilities.allowFlying = false;
						player.capabilities.isFlying = false;
						player.sendPlayerAbilities();
					}
				}
			}
		}
		if(GREventHandler.potionRefreshTimer>30&&event.world.provider.getDimension()==0&&!event.world.isRemote)
		{
			GREventHandler.potionRefreshTimer = 0;
			for (int i = 0; i < event.world.loadedEntityList.size(); ++i)
			{
				if(event.world.loadedEntityList.get(i) instanceof EntityLivingBase)
				{
					EntityLivingBase entity = (EntityLivingBase)event.world.loadedEntityList.get(i);
					if(ModUtils.getIGenes(entity)!=null)
					{
						IGenes genes = ModUtils.getIGenes(entity);
						if(GeneticsReborn.enableWaterBreathing&&genes.hasGene(EnumGenes.WATER_BREATHING))
						{
							entity.setAir(300);
						}
						//These below all have to do with potion effects.
						if(GeneticsReborn.enableNightVision&&entity instanceof EntityPlayer&&genes.hasGene(EnumGenes.NIGHT_VISION))
						{
							entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.nightVision), 320, 0, false, false)));
						}
						if(GeneticsReborn.enableJumpBoost&&genes.hasGene(EnumGenes.JUMP_BOOST))
						{
							entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.jumpBoost), 50, 1, false, false)));
						}
						if(GeneticsReborn.enableSpeed&&genes.hasGene(EnumGenes.SPEED))
						{
							entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.moveSpeed), 50, 1, false, false)));
						}
						if(GeneticsReborn.enableResistance&&genes.hasGene(EnumGenes.RESISTANCE))
						{
							entity.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.resistance), 50, 1, false, false)));
						}
					}
				}
			}
		}
	}
}
