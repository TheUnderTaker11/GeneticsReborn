package com.theundertaker11.GeneticsReborn;

import com.theundertaker11.GeneticsReborn.items.GRItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GREventHandler {
	@SubscribeEvent
	public void rightClickEntity(EntityInteract event)
	{
		//EntityItem entity = new EntityItem(worldObj, xCoord, yCoord, zCoord, itemStack);
		//worldObj.spawnEntityInWorld(entity);
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		World world = event.getWorld();
		if(player.getHeldItemMainhand()!=null&&player.getHeldItemMainhand().getItem()==GRItems.MetalScraper)
		{
			if(target instanceof EntityLiving)
			{
				
			}
		}
	}
}
