package com.theundertaker11.GeneticsReborn;

import java.util.ArrayList;
import java.util.List;

import com.theundertaker11.GeneticsReborn.api.capability.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.IGenes;
import com.theundertaker11.GeneticsReborn.items.GRItems;
import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class GREventHandler {

	private static int flightticktimer;
	private static List<String> PlayersWithFlight = new ArrayList<String>();
	
	/**
	 * I use this to scrape entities, with basic or advanced scraper.
	 * Also at the bottom it is used to handle the wooly and milky genes
	 */
	@SubscribeEvent
	public void rightClickEntity(EntityInteract event)
	{
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		World world = event.getWorld();
		if(event.getHand()==EnumHand.MAIN_HAND&&!world.isRemote&&player.getHeldItemMainhand()!=null)
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
				//Gives more info than other one for use in cloning, but makes items not stack.
				if(player.getHeldItemMainhand().getItem()==GRItems.AdvancedScraper)
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
			
			//START WOOLY/MILK GENES
			if(target instanceof EntityPlayer)
			{
				EntityPlayer targetplayer = (EntityPlayer)target;
				if(ModUtils.getIGenes(targetplayer)!=null)
				{
					IGenes targetplayergenes = ModUtils.getIGenes(targetplayer);
					
					if(player.getHeldItemMainhand().getItem() instanceof ItemShears&&targetplayergenes.hasGene(EnumGenes.WOOLY))
					{
						ItemStack wool = new ItemStack(Blocks.WOOL, 1);
						EntityItem entitywool = new EntityItem(event.getWorld(), targetplayer.getPosition().getX(), targetplayer.getPosition().getY(), targetplayer.getPosition().getZ(), wool);
						world.spawnEntityInWorld(entitywool);
					}
				
					if(player.getHeldItemMainhand().getItem()==Items.BUCKET&&targetplayergenes.hasGene(EnumGenes.MILKY))
					{
						if((player.getHeldItemMainhand().stackSize-1)<=0) player.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.MILK_BUCKET));
						else if(!player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) player.dropItem(Items.MILK_BUCKET, 1);
					}
				}
			}
		}
	}
	
	/**
	 * Just a counter
	 * 
	 */
	@SubscribeEvent
	public void GameTick(TickEvent event)
	{
		if(flightticktimer<1000) ++flightticktimer;
	}

	/**
	 * This handles the enabling/disabling of flight and fire immunity based on player genes.
	 */
	@SubscribeEvent
	public void WorldTick(WorldTickEvent event)
	{
		//This will allow players to fly if they have the Flight gene.(Currently doesn't remove flight)
		if(GeneticsReborn.enableFlight&&flightticktimer>150&&event.world.provider.getDimension()==0&&!event.world.isRemote)	
		{
			flightticktimer = 0;
			for(EntityPlayerMP player: event.world.getMinecraftServer().getPlayerList().getPlayerList())
			{
				if(ModUtils.getIGenes(player)!=null)
				{
					IGenes playergenes = ModUtils.getIGenes(player);
					String username = player.getName();
					//This if/else enables/disables flight
					if(playergenes.hasGene(EnumGenes.FLY))
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
	}
	
	/**
	 * This handles the eat grass gene.
	 */
	@SubscribeEvent
	public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		if(player.getFoodStats().needFood()==true&&event.getEntityPlayer().getHeldItemMainhand()==null&&ModUtils.getIGenes(player)!=null&&event.getWorld().getBlockState(event.getPos()).getBlock()==Blocks.GRASS)
		{
			IGenes playergenes = ModUtils.getIGenes(player);
			if(playergenes.hasGene(EnumGenes.EAT_GRASS))
			{
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()+1);
				event.getWorld().setBlockState(event.getPos(), Blocks.DIRT.getDefaultState());
			}else return;
		}
	}
	
	/**
	 *This handles fire immunity and the dragon health crystal protecting you.
	 *It checks the fire first so taking fire damage with that gene won't still take from health crystal duribility 
	 */
	@SubscribeEvent
	public void onHurt(LivingHurtEvent event)
	{
		 if(!event.getEntity().getEntityWorld().isRemote&&event.getEntity() instanceof EntityPlayer)
		 {
			 EntityPlayer player = (EntityPlayer)event.getEntity();
			 if(ModUtils.getIGenes(player)!=null)
			 {
				 IGenes playergenes = ModUtils.getIGenes(player);
				 
				 if(GeneticsReborn.enableFireProof&&playergenes.hasGene(EnumGenes.FIRE_PROOF))
				 {
					 if(event.getSource().equals(DamageSource.lava) || event.getSource().equals(DamageSource.inFire) || event.getSource().equals(DamageSource.onFire))
					 {
						event.setCanceled(true);
					 }
				 }
				 
				 if(GeneticsReborn.enableEnderDragonHealth&&playergenes.hasGene(EnumGenes.ENDER_DRAGON_HEALTH)&&ModUtils.playerHasItem(player, GRItems.DragonHealthCrystal))
				 {
					for(int i=0;i<player.inventory.getSizeInventory();i++)
					{
						if(player.inventory.getStackInSlot(i)!=null&&player.inventory.getStackInSlot(i).getItem()==GRItems.DragonHealthCrystal)
						{
							ItemStack stack = player.inventory.getStackInSlot(i);
							stack.damageItem((int)event.getAmount(), player);
							//For some reason unless I manually remove it the game will crash or glitch when the dur gets to 0.
							//So this below checks for that and makes sure to remove it.
							if(stack.stackSize<1) player.inventory.setInventorySlotContents(i, null);
							event.setAmount(0);
							event.setCanceled(true);
							break;
						}
					}
				 }
			 }
		 }
	}
	
	/**
	 * This makes players keep genes on death(If enabled in config)
	 */
	@SubscribeEvent
	 public void onPlayerClone(PlayerEvent.Clone event)
	 {
		 if(!event.isWasDeath()) return;
		 if(!GeneticsReborn.keepGenesOnDeath) return;
		 IGenes oldgenes = event.getOriginal().getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
		 IGenes newgenes = event.getEntityPlayer().getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
		 newgenes.setGeneList(oldgenes.getGeneList());
	 }
}
