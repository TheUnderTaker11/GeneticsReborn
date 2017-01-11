package com.theundertaker11.GeneticsReborn.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerCooldowns {
	
	private EntityPlayer player;
	private int ticksLeft;
	private String Name;
	
	public PlayerCooldowns(EntityPlayer entityPlayer, String name, int ticks)
	{
		this.player=entityPlayer;
		this.ticksLeft=ticks;
		this.Name=name;
	}
	
	public void removeTick()
	{
		this.ticksLeft--;
	}
	
	public boolean isFinished()
	{
		return (this.ticksLeft<=0);
	}
	
	public String getPlayerName()
	{
		return this.player.getName();
	}
	
	public String getName()
	{
		return this.Name;
	}
}
