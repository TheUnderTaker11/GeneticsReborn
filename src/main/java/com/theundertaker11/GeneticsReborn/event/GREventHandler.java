package com.theundertaker11.GeneticsReborn.event;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.api.capability.CapabilityHandler;
import com.theundertaker11.GeneticsReborn.api.capability.genes.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.genes.IGenes;
import com.theundertaker11.GeneticsReborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.GeneticsReborn.items.DamageableItemBase;
import com.theundertaker11.GeneticsReborn.items.GRItems;
import com.theundertaker11.GeneticsReborn.keybinds.KeybindHandler;
import com.theundertaker11.GeneticsReborn.packets.GeneticsRebornPacketHandler;
import com.theundertaker11.GeneticsReborn.packets.SendShootDragonBreath;
import com.theundertaker11.GeneticsReborn.packets.SendTeleportPlayer;
import com.theundertaker11.GeneticsReborn.util.ModUtils;
import com.theundertaker11.GeneticsReborn.util.PlayerCooldowns;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/**
 * I handle pretty much all my events/genes in this class. Its just one big mess of different things.
 * @author TheUnderTaker11
 *
 */
public class GREventHandler {

	public static int flightticktimer;
	public static int potionRefreshTimer;

	public static List<PlayerCooldowns> cooldownList = new ArrayList<PlayerCooldowns>();
	
	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		
		MinecraftForge.EVENT_BUS.register(new GREventHandler());
		MinecraftForge.EVENT_BUS.register(new RightClickEntityEvent());
		MinecraftForge.EVENT_BUS.register(new OnWorldTickEvent());
		MinecraftForge.EVENT_BUS.register(new OnRightClick());
		MinecraftForge.EVENT_BUS.register(new OnEntityHurt());
		MinecraftForge.EVENT_BUS.register(new PlayerDeathRelatedEvents());
		MinecraftForge.EVENT_BUS.register(new AIChangeEvents());
	}
	
	/**
	 * Just a counter, and keeps track of a thing or two. Its called twice for whatever reason so I have a boolean
	 * thats toggled each time its run.
	 * 
	 */
	private boolean canRun = true;
	@SubscribeEvent
	public void GameTick(TickEvent.ServerTickEvent event)
	{
		if(canRun)
		{
			if(flightticktimer<100) ++flightticktimer;
			if(potionRefreshTimer<600) ++potionRefreshTimer;
			//Counts down 1 on each cooldown in the list.
			for(int i=0; i<cooldownList.size();i++)
			{
				cooldownList.get(i).removeTick();
				if(cooldownList.get(i).isFinished())
				{
					cooldownList.remove(i);
				}
			}
		}
		canRun = (!canRun);
	}
	
	/**
	 * This handles the keybinds
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void clientPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(GeneticsReborn.enableTeleporter)
		{
			if(KeybindHandler.keybindTeleport.isPressed())
			{
				GeneticsRebornPacketHandler.INSTANCE.sendToServer(new SendTeleportPlayer());
			}
		}
		if(GeneticsReborn.enableDragonsBreath)
		{
			if(KeybindHandler.keybindDragonsBreath.isPressed())
			{
				GeneticsRebornPacketHandler.INSTANCE.sendToServer(new SendShootDragonBreath());
			}
		}
		
	}
}
