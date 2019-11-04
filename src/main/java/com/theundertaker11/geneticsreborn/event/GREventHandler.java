package com.theundertaker11.geneticsreborn.event;

import java.util.HashMap;
import java.util.Map;

import com.theundertaker11.geneticsreborn.api.capability.CapabilityHandler;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.keybinds.KeybindHandler;
import com.theundertaker11.geneticsreborn.packets.GeneticsRebornPacketHandler;
import com.theundertaker11.geneticsreborn.packets.SendShootDragonBreath;
import com.theundertaker11.geneticsreborn.packets.SendTeleportPlayer;
import com.theundertaker11.geneticsreborn.potions.GRPotions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GREventHandler {

	public static int flightticktimer;

	public static Map<String, Long> cooldownList = new HashMap<String, Long>();

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new GREventHandler());
		MinecraftForge.EVENT_BUS.register(new RightClickEntityEvent());
		MinecraftForge.EVENT_BUS.register(new OnWorldTickEvent());
		MinecraftForge.EVENT_BUS.register(new OnRightClick());
		MinecraftForge.EVENT_BUS.register(new OnEntityHurt());
		MinecraftForge.EVENT_BUS.register(new DeathRelatedEvents());
		MinecraftForge.EVENT_BUS.register(new AIChangeEvents());
		MinecraftForge.EVENT_BUS.register(new PlayerTickEvent());
		MinecraftForge.EVENT_BUS.register(new GRPotions());
	}

	@SubscribeEvent
	public void GameTick(TickEvent.WorldTickEvent event) {
		if (event.phase == Phase.END) {
			if (flightticktimer < 100) ++flightticktimer;
		}
	}
	
	//these two functions do different things...
	//use this one usually
	public static boolean isInCooldown(EntityLivingBase elb, String type, long now) {
		return !cooldownList.isEmpty() && !cooldownList.containsKey(getCooldownString(elb, type)) || 
		isCooldownExpired(elb, type, now, true);	
	}
	
	//use this one for seeing if the next item drop is ready..
	public static boolean isCooldownExpired(EntityLivingBase elb, String type, long now, boolean remove) {
		if (cooldownList.isEmpty()) return false;
		final String key = getCooldownString(elb, type);
		Long time = cooldownList.get(key);
		if (time == null) time = 0L;
		if (time < now) {
			if (remove) cooldownList.remove(key);
			return true;
		}
		return false;
	}
	
	public static void addCooldown(EntityLivingBase elb, String type, long now, int duration) {
		final String name = getCooldownString(elb, type);
		cooldownList.put(name, now+duration);
	}
	
	private static final String getCooldownString(EntityLivingBase elb, String type) {
		return elb.getUniqueID().toString() + "/" + type;
	}

	/**
	 * This handles the keybinds
	 *
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void clientPlayerTick(TickEvent.PlayerTickEvent event) {
		if (EnumGenes.TELEPORTER.isActive()) {
			if (KeybindHandler.keybindTeleport.isPressed()) {
				GeneticsRebornPacketHandler.INSTANCE.sendToServer(new SendTeleportPlayer());
				
			}
		}
		if (EnumGenes.DRAGONS_BREATH.isActive()) {
			if (KeybindHandler.keybindDragonsBreath.isPressed()) {
				GeneticsRebornPacketHandler.INSTANCE.sendToServer(new SendShootDragonBreath());
			}
		}
	}
}
