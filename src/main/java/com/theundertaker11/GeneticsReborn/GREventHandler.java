package com.theundertaker11.GeneticsReborn;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

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

	private static int flightticktimer;
	private static int EmeraldHeartTimer;
	private static int teleportTimer;
	private static int dragonsBreathTimer;
	private static List<String> PlayersWithFlight = new ArrayList<String>();
	private static List<String> PlayersOnEmeraldHeartCooldown = new ArrayList<String>();

	public static List<PlayerCooldowns> cooldownList = new ArrayList<PlayerCooldowns>();
	
	/**
	 * I use this to scrape entities, with basic or advanced scraper.
	 * Also at the bottom it is used to handle the wooly and milky genes
	 */
	@SubscribeEvent
	public void rightClickEntity(EntityInteract event)
	{
		if(event.getHand()==EnumHand.MAIN_HAND&&!event.getWorld().isRemote&&event.getEntityPlayer().getHeldItemMainhand()!=null)
		{
			EntityPlayer player = event.getEntityPlayer();
			Entity target = event.getTarget();
			World world = event.getWorld();
			//THE MOST SPECIAL SNOWFLAKE, DOESNT EVEN EXTEND ENTITYLIVINGBASE...
			if(target instanceof EntityDragonPart&&player.getHeldItemMainhand().getItem() instanceof DamageableItemBase) 
			{
				if(player.getHeldItemMainhand().getItem()==GRItems.MetalScraper||player.getHeldItemMainhand().getItem()==GRItems.AdvancedScraper)
				{
					ItemStack organicmatter = new ItemStack(GRItems.OrganicMatter, 1);
					target.attackEntityFrom(DamageSource.causePlayerDamage(player), 0.5F);
					ModUtils.getTagCompound(organicmatter).setString("entityName", "Ender Dragon");
					player.getHeldItemMainhand().damageItem(1, player);
					EntityItem entity = new EntityItem(player.getEntityWorld(), target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getZ(), organicmatter);
					player.getEntityWorld().spawnEntityInWorld(entity);
				}
			}
			//Glad thats over.
			
			if((target instanceof EntityLivingBase&&!(target instanceof EntityPlayer))&&player.getHeldItemMainhand().getItem() instanceof DamageableItemBase)
			{
				EntityLivingBase livingtarget = (EntityLivingBase)target;
				String name = livingtarget.getName();
				
				//Start special snowflakes.
				if(livingtarget instanceof EntitySkeleton)
				{
					EntitySkeleton skeleton = (EntitySkeleton)livingtarget;
					if(skeleton.func_189771_df()==SkeletonType.WITHER)
					{
						name = "Wither Skeleton";
					}
				}
				if(livingtarget instanceof EntityPigZombie)
				{
					name="Zombie Pigman";
				}
				
				//End special snowflakes.
				ModUtils.scrapeEntity(player, livingtarget, name);
			}
			
			//START WOOLY/MILK GENES
			if(target instanceof EntityPlayer)
			{
				EntityPlayer targetplayer = (EntityPlayer)target;
				if(ModUtils.getIGenes(targetplayer)!=null)
				{
					IGenes targetplayergenes = ModUtils.getIGenes(targetplayer);
					
					if(GeneticsReborn.enableWooly&&player.getHeldItemMainhand().getItem() instanceof ItemShears&&targetplayergenes.hasGene(EnumGenes.WOOLY))
					{
						ItemStack wool = new ItemStack(Blocks.WOOL, 1);
						player.getHeldItemMainhand().damageItem(1, player);
						EntityItem entitywool = new EntityItem(event.getWorld(), targetplayer.getPosition().getX(), targetplayer.getPosition().getY(), targetplayer.getPosition().getZ(), wool);
						world.spawnEntityInWorld(entitywool);
					}
				
					if(GeneticsReborn.enableMilky&&player.getHeldItemMainhand().getItem()==Items.BUCKET&&targetplayergenes.hasGene(EnumGenes.MILKY))
					{
						
						if((player.getHeldItemMainhand().stackSize-1)<=0) player.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.MILK_BUCKET));
						else 
						{
							if(!player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) player.dropItem(Items.MILK_BUCKET, 1);
							player.getHeldItemMainhand().stackSize=(player.getHeldItemMainhand().stackSize-1);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Just a counter, and keeps track of a thing or two. Its called twice for whatever stupid reason so I have a boolean
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
			if(GeneticsReborn.enableEmeraldHeart)
			{
				if(EmeraldHeartTimer<6010) ++EmeraldHeartTimer;
				if(EmeraldHeartTimer>6000)
				{
					EmeraldHeartTimer = 0;
					PlayersOnEmeraldHeartCooldown = new ArrayList<String>();
				}
			}
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
	 * This handles the enabling/disabling of flight and fire immunity based on player genes.
	 */
	@SubscribeEvent
	public void WorldTick(WorldTickEvent event)
	{
		//This will allow players to fly if they have the Flight gene.(Currently doesn't remove flight)
		if(GeneticsReborn.enableFlight&&flightticktimer>25&&event.world.provider.getDimension()==0&&!event.world.isRemote)	
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
	 * Handles night vision, jump boost, speed, resistance, and part of poison/wither proof(Other part in onDamage). 
	 * Water breathing is handled here to.
	 * @param event
	 */
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event)
	{
		if(!event.player.getEntityWorld().isRemote&&event.player!=null&&ModUtils.getIGenes(event.player)!=null)
		{
			EntityPlayer player = event.player;
			IGenes playergenes = ModUtils.getIGenes(player);
			//This keeps the players air up
			if(GeneticsReborn.enableWaterBreathing&&playergenes.hasGene(EnumGenes.WATER_BREATHING))
			{
				player.setAir(100);
			}
			//These all have to do with potion effects.
			if(GeneticsReborn.enableNightVision&&playergenes.hasGene(EnumGenes.NIGHT_VISION))
			{
				player.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.nightVision), 20, 0)));
			}
			if(GeneticsReborn.enableJumpBoost&&playergenes.hasGene(EnumGenes.JUMP_BOOST))
			{
				player.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.jumpBoost), 20, 1)));
			}
			if(GeneticsReborn.enableSpeed&&playergenes.hasGene(EnumGenes.SPEED))
			{
				player.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.moveSpeed), 20, 1)));
			}
			if(GeneticsReborn.enableResistance&&playergenes.hasGene(EnumGenes.RESISTANCE))
			{
				player.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.resistance), 20, 1)));
			}
			if(GeneticsReborn.enablePoisonProof&&playergenes.hasGene(EnumGenes.POISON_PROOF))
			{
				player.removePotionEffect(Potion.getPotionById(ModUtils.poison));
			}
			if(GeneticsReborn.enableWitherProof&&playergenes.hasGene(EnumGenes.WITHER_PROOF))
			{
				player.removePotionEffect(Potion.getPotionById(ModUtils.wither));
			}
		}
	}
	
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
			if(GeneticsReborn.enableEatGrass&&playergenes.hasGene(EnumGenes.EAT_GRASS))
			{
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()+1);
				event.getWorld().setBlockState(event.getPos(), Blocks.DIRT.getDefaultState());
			}else return;
		}
	}
	
	/**
	 *This handles fire immunity, dragon health, and Wither Proof.(And a small part of poison proof)
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
						event.setAmount(0);
						event.setCanceled(true);
					 }
				 }
				 if(GeneticsReborn.enableWitherProof&&playergenes.hasGene(EnumGenes.WITHER_PROOF))
				 {
					 if(event.getSource().equals(DamageSource.wither))
					 {
						event.setAmount(0);
						event.setCanceled(true);
					 }
				 }
				 if(GeneticsReborn.enablePoisonProof&&playergenes.hasGene(EnumGenes.POISON_PROOF))
				 {
					 if(event.getSource().equals(DamageSource.magic) && player.getActivePotionEffect(Potion.getPotionById(ModUtils.poison))!=null)
					 {
						player.removePotionEffect(Potion.getPotionById(ModUtils.poison));
						event.setAmount(0);
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
	
	@SubscribeEvent
	public void onPlayerDrops(PlayerDropsEvent event)
	{
		if(GeneticsReborn.enableSaveInventory&&ModUtils.getIGenes(event.getEntityPlayer())!=null)
		{
			IGenes genes = ModUtils.getIGenes(event.getEntityPlayer());
			if(genes.hasGene(EnumGenes.SAVE_INVENTORY))
			{
				for(int i=(event.getDrops().size()-1);i>=0;i--)
				{
					event.getEntityPlayer().inventory.setInventorySlotContents(i, event.getDrops().get(i).getEntityItem());
					event.getDrops().remove(i);
				}
			}
		}
	}
	/**
	 * This makes players keep genes and health on death(If enabled in config)
	 */
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event)
	{
		if(ModUtils.getIMaxHealth(event.getEntityPlayer())!=null)
		{
			final IMaxHealth oldMaxHealth = ModUtils.getIMaxHealth(event.getOriginal());
			final IMaxHealth newMaxHealth = ModUtils.getIMaxHealth(event.getEntityPlayer());

			if (newMaxHealth != null && oldMaxHealth != null)
			{
				newMaxHealth.setBonusMaxHealth(oldMaxHealth.getBonusMaxHealth());
			}
		}
		if(GeneticsReborn.enableSaveInventory&&event.isWasDeath())
		{
			event.getEntityPlayer().inventory.copyInventory(event.getOriginal().inventory);
		}
		if(GeneticsReborn.keepGenesOnDeath&&event.isWasDeath())
		{
			IGenes oldgenes = event.getOriginal().getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
			IGenes newgenes = event.getEntityPlayer().getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
			newgenes.setGeneList(oldgenes.getGeneList());
		}
	}
	
	/**
	 * This makes players drop slimeballs and emeralds on death.
	 * @param event
	 */
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(ModUtils.getIGenes(player)!=null)
			{
				IGenes playergenes = ModUtils.getIGenes(player);
				if(GeneticsReborn.enableEmeraldHeart&&playergenes.hasGene(EnumGenes.EMERALD_HEART))
				{
					if(!PlayersOnEmeraldHeartCooldown.contains(player.getUUID(player.getGameProfile()).toString()))
					{
						PlayersOnEmeraldHeartCooldown.add(player.getUUID(player.getGameProfile()).toString());
						EntityItem entity = new EntityItem(player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new ItemStack(Items.EMERALD));
						player.getEntityWorld().spawnEntityInWorld(entity);
					}
				}
				if(GeneticsReborn.enableSlimy&&playergenes.hasGene(EnumGenes.SLIMY))
				{
					EntityItem entity = new EntityItem(player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new ItemStack(Items.SLIME_BALL, 3));
					player.getEntityWorld().spawnEntityInWorld(entity);
				}
			}
		}
	}
	
	/**
	 * This handles the no fall gene(duh?)
	 */
	@SubscribeEvent
	public void onFall(LivingFallEvent event)
	{
		if(GeneticsReborn.enableNoFallDamage&&event.getDistance()>3&&event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(!player.capabilities.allowFlying&&ModUtils.getIGenes(player)!=null)
			{
				IGenes playergenes = ModUtils.getIGenes(player);
				if(playergenes.hasGene(EnumGenes.NO_FALL_DAMAGE))
				{
					event.setCanceled(true);
				}
			}
		}
	}
	
	/**
	 * This handles the wither-hit gene.
	 * @param event
	 */
	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event)
	{
		if(GeneticsReborn.enableWitherHit)
		{
			EntityPlayer player = event.getEntityPlayer();
			if(ModUtils.getIGenes(player)!=null&&event.getTarget() instanceof EntityLivingBase)
			{
				IGenes genes = ModUtils.getIGenes(player);
				EntityLivingBase entityliving = (EntityLivingBase)event.getTarget();
				if(genes.hasGene(EnumGenes.WITHER_HIT))
				{
					entityliving.addPotionEffect((new PotionEffect(Potion.getPotionById(ModUtils.wither), 100, 1)));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onRightClickItem(RightClickItem event)
	{
		if(GeneticsReborn.enableShootFireballs)
		{
			if(event.getItemStack()!=null&&event.getItemStack().getItem()==Items.BLAZE_ROD)
			{
				EntityPlayer player = event.getEntityPlayer();
				if(ModUtils.getIGenes(player)!=null)
				{
					IGenes genes = ModUtils.getIGenes(player);
					if(genes.hasGene(EnumGenes.SHOOT_FIREBALLS))
					{
						Vec3d v3 = player.getLook(1);
        	            EntitySmallFireball fireball = new EntitySmallFireball(player.getEntityWorld(), player.posX, player.posY + player.eyeHeight, player.posZ, v3.xCoord, v3.yCoord, v3.zCoord);
        	            fireball.shootingEntity = player;
        	            player.getEntityWorld().spawnEntityInWorld(fireball);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityCreature) attachCreeperTask(event, (EntityCreature)event.getEntity());
	}
	private boolean hasCreeperGene(@Nullable EntityPlayer player)
	{
		return ModUtils.getIGenes(player)!=null&&ModUtils.getIGenes(player).hasGene(EnumGenes.SCARE_CREEPERS);
	}
	public void attachCreeperTask(EntityJoinWorldEvent event, EntityCreature entity)
	{
		if(entity instanceof EntityCreeper)
		{
			entity.tasks.addTask(3, new EntityAIAvoidEntity<>(entity, EntityPlayer.class, this::hasCreeperGene, 6.0F, 1.0D, 1.2D));
		}
		for (Object a : entity.tasks.taskEntries.toArray())
		{
			EntityAIBase ai = ((EntityAITaskEntry) a).action;
			
		}
		 

		for (Object a : entity.targetTasks.taskEntries.toArray())
		{
			EntityAIBase ai = ((EntityAITaskEntry) a).action;
			if(entity instanceof EntityCreeper && ai instanceof EntityAINearestAttackableTarget)
			{
				entity.targetTasks.removeTask(ai);
				entity.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(entity, EntityPlayer.class, 10, false, false, (player)-> !hasCreeperGene(player)));
			}
			
		}
	}
}
