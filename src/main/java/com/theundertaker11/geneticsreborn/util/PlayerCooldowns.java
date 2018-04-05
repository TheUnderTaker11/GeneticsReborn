package com.theundertaker11.geneticsreborn.util;

import net.minecraft.entity.EntityLivingBase;

/**
 * This class is used to add cooldowns. It doesn't handle any of it, just works
 * as an object handled by the EventHandler class.
 * <p>
 * Althought it says "Player" it works for any EntityLivingBase. Didn't feel like re-naming it.
 * @author TheUnderTaker11
 *
 */
public class PlayerCooldowns {

	private EntityLivingBase player;
	private int ticksLeft;
	private String Name;

	public PlayerCooldowns(EntityLivingBase entityLiving, String name, int ticks) {
		this.player = entityLiving;
		this.ticksLeft = ticks;
		this.Name = name;
	}

	public void removeTick() {
		this.ticksLeft--;
	}

	public void removeTick(int amount) {
		this.ticksLeft -= amount;
		if (ticksLeft < 0)
			ticksLeft = 0;
	}

	public boolean isFinished() {
		return (this.ticksLeft <= 0);
	}

	public String getIDString() {
		return this.player.getUniqueID().toString();
	}

	public String getPlayerName() {
		return this.player.getName();
	}

	public String getName() {
		return this.Name;
	}
}
