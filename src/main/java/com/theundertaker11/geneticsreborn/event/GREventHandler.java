package com.theundertaker11.geneticsreborn.event;

import java.util.ArrayList;
import java.util.List;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.CapabilityHandler;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.keybinds.KeybindHandler;
import com.theundertaker11.geneticsreborn.packets.GeneticsRebornPacketHandler;
import com.theundertaker11.geneticsreborn.packets.SendShootDragonBreath;
import com.theundertaker11.geneticsreborn.packets.SendTeleportPlayer;
import com.theundertaker11.geneticsreborn.util.PlayerCooldowns;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GREventHandler {

	public static int flightticktimer;
	public static int potionRefreshTimer;
	public static int worldTickTimer;

	public static List<PlayerCooldowns> cooldownList = new ArrayList<PlayerCooldowns>();

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
	}

	/**
	 * Just a counter, and keeps track of a thing or two. Its called twice for whatever reason so I have a boolean
	 * thats toggled each time its run.
	 */
	private boolean canRun = true;

	@SubscribeEvent
	public void GameTick(TickEvent.ServerTickEvent event) {
		if (canRun) {
			if (flightticktimer < 100) ++flightticktimer;
			if (potionRefreshTimer < 600) ++potionRefreshTimer;
			if (GeneticsReborn.allowGivingEntityGenes) worldTickTimer++;
			if (worldTickTimer > 39) {
				worldTickTimer = 0;
			}

			if (!cooldownList.isEmpty()) {
				for (int i = 0; i < cooldownList.size(); i++) {
					cooldownList.get(i).removeTick();
					if (cooldownList.get(i).isFinished()) {
						cooldownList.remove(i);
					}
				}
			}
		}
		canRun = (!canRun);
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
