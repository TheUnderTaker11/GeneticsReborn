package com.theundertaker11.GeneticsReborn;

import com.theundertaker11.GeneticsReborn.items.GRItems;
import com.theundertaker11.GeneticsReborn.items.OrganicMatter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GREventHandler {
	@SubscribeEvent
	public void rightClickEntity(EntityInteract event)
	{
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		World world = event.getWorld();
		if(player.getHeldItemMainhand()!=null&&player.getHeldItemMainhand().getItem()==GRItems.MetalScraper)
		{
			if(target instanceof EntityLiving)
			{
				EntityLiving livingtarget = (EntityLiving)target;
				livingtarget.attackEntityFrom(DamageSource.causePlayerDamage(player), 0.25F);
				player.getHeldItemMainhand().damageItem(1, player);
				//Begin setting NBT to the item.
				String name = livingtarget.getName();
				int entityID = livingtarget.getEntityId();
				ItemStack organicmatter = new ItemStack(GRItems.OrganicMatter);
				
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("entityName", name);
				tag.setInteger("entityID", entityID);
				organicmatter.setTagCompound(tag);
				EntityItem entity = new EntityItem(event.getWorld(), livingtarget.getPosition().getX(), livingtarget.getPosition().getY(), livingtarget.getPosition().getZ(), organicmatter);
				event.getWorld().spawnEntityInWorld(entity);
			}
		}
	}
}
