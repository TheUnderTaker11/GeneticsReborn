package com.theundertaker11.GeneticsReborn.event;

import java.util.Iterator;
import java.util.List;

import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.api.capability.genes.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.genes.IGenes;
import com.theundertaker11.GeneticsReborn.blocks.GRBlocks;
import com.theundertaker11.GeneticsReborn.items.GRItems;
import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerTickEvent {
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		IGenes genes = ModUtils.getIGenes(event.player);
		World world = event.player.getEntityWorld();
		if(event.player==null||genes==null||event.player.getEntityWorld().isRemote) return;
		boolean allow = !player.inventory.hasItemStack(new ItemStack(GRItems.AntiField));
		if(GeneticsReborn.enableItemMagnet&&genes.hasGene(EnumGenes.ITEM_MAGNET)&&!player.isSneaking()&&allow)
		{
			// items
			Iterator iterator = ModUtils.getEntitiesInRange(EntityItem.class, world, player.posX, player.posY,
					player.posZ, 6.5).iterator();
			while (iterator.hasNext()) {
				EntityItem itemToGet = (EntityItem) iterator.next();
				
				if(!itemToGet.getTags().contains("geneticsrebornLOL")&&shouldPickupItem(event.player.getEntityWorld(), itemToGet.getPosition()))
				{
					EntityItemPickupEvent pickupEvent = new EntityItemPickupEvent(player, itemToGet);
					MinecraftForge.EVENT_BUS.post(pickupEvent);
					ItemStack itemStackToGet = itemToGet.getEntityItem();
					int stackSize = itemStackToGet.stackSize;

					if (pickupEvent.getResult() == Result.ALLOW || stackSize <= 0 || player.inventory.addItemStackToInventory(itemStackToGet))
					{
						player.onItemPickup(itemToGet, stackSize);
						world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.AMBIENT,
							0.15F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
					}
				}else if(!itemToGet.getTags().contains("geneticsrebornLOL")) itemToGet.addTag("geneticsrebornLOL");
			}
		}
		
		if(GeneticsReborn.enableXPMagnet&&genes.hasGene(EnumGenes.XP_MAGNET)&&!player.isSneaking()&&allow)
		{
			Iterator iterator = ModUtils.getEntitiesInRange(EntityXPOrb.class, world, player.posX, player.posY, player.posZ,
					6.5).iterator();
			while (iterator.hasNext()) {
				EntityXPOrb xpToGet = (EntityXPOrb) iterator.next();

				if (xpToGet.isDead || xpToGet.isInvisible()) {
					continue;
				}
				player.xpCooldown = 0;
				xpToGet.delayBeforeCanPickup=0;
				xpToGet.setPosition(player.posX,player.posY,player.posZ);
				PlayerPickupXpEvent xpEvent = new PlayerPickupXpEvent(player, xpToGet);
				MinecraftForge.EVENT_BUS.post(xpEvent);
				if(xpEvent.getResult()==Result.ALLOW){
					xpToGet.onCollideWithPlayer(player);
				}
				
			}
		}
		
	}
	
	public boolean shouldPickupItem(World world, BlockPos pos)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		for(int i=-5;i<=5;i++)
		{
			BlockPos newpos = new BlockPos(x,y+i,z);
			if(world.getBlockState(newpos).getBlock()==GRBlocks.AntiFieldBlock)
			{
				return false;
			}
		}
		return true;
	}
}
