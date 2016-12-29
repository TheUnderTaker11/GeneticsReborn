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
import net.minecraft.util.EnumHand;
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
		if(event.getHand()==EnumHand.MAIN_HAND&&!event.getWorld().isRemote&&player.getHeldItemMainhand()!=null)
		{
			if(target instanceof EntityLivingBase)
			{
				EntityLivingBase livingtarget = (EntityLivingBase)target;
				if(player.getHeldItemMainhand().getItem()==GRItems.MetalScraper)
				{
					livingtarget.attackEntityFrom(DamageSource.causePlayerDamage(player), 0.25F);
					player.getHeldItemMainhand().damageItem(1, player);
				
					//Begin setting NBT to the item.
					ItemStack organicmatter = new ItemStack(GRItems.OrganicMatter, 1);
					ModUtils.getTagCompound(organicmatter).setString("entityName", livingtarget.getName());
					EntityItem entity = new EntityItem(event.getWorld(), livingtarget.getPosition().getX(), livingtarget.getPosition().getY(), livingtarget.getPosition().getZ(), organicmatter);
					event.getWorld().spawnEntityInWorld(entity);
				}
				if(player.getHeldItemMainhand().getItem()==GRItems.advancedScraper)
				{
					livingtarget.attackEntityFrom(DamageSource.causePlayerDamage(player), 1.0F);
					player.getHeldItemMainhand().damageItem(1, player);
					
					//Begin setting NBT to the item.
					ItemStack organicmatter = new ItemStack(GRItems.OrganicMatter, 1);
					NBTTagCompound entitytag = new NBTTagCompound();
					NBTTagCompound itemtag = ModUtils.getTagCompound(organicmatter);
					livingtarget.writeToNBT(entitytag);
					
					itemtag.setTag("mobTag", entitytag);
					itemtag.setString("type", livingtarget.getClass().getCanonicalName());
					itemtag.setString("entityName", livingtarget.getName());
					EntityItem entity = new EntityItem(event.getWorld(), livingtarget.getPosition().getX(), livingtarget.getPosition().getY(), livingtarget.getPosition().getZ(), organicmatter);
					event.getWorld().spawnEntityInWorld(entity);
				}
			}
		}
	}
}
