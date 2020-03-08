package com.theundertaker11.geneticsreborn.event;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.collect.HashMultimap;
import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.geneticsreborn.blocks.GRBlocks;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.packets.ClientGeneChange;
import com.theundertaker11.geneticsreborn.packets.GeneticsRebornPacketHandler;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class PlayerTickEvent {
 
    @SubscribeEvent
    public void infinityFix(ArrowNockEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		IGenes genes = ModUtils.getIGenes(player);
		if (EnumGenes.INFINITY.isActive() && genes.hasGene(EnumGenes.INFINITY)) {
            event.getEntityPlayer().setActiveHand(event.getHand());
            event.setAction(new ActionResult<>(EnumActionResult.SUCCESS, event.getBow()));
		}
    }
    
    @SubscribeEvent
    public void onArrowFire(ArrowLooseEvent event) {
    	if (event.hasAmmo()) return;
    	
    	//no arrows on person, so create one, this allows you to use special arrows if you equip them, 
    	//but they do not gain "infinite" status
        World world = event.getEntityPlayer().getEntityWorld();
        ItemStack stack = event.getBow();
        EntityPlayer player = event.getEntityPlayer();
        
    	int charge = event.getCharge();
        float velocity = (float) charge / 20.0F;
        velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;

        if ((double) velocity < 0.1D)
            return;

        if (velocity > 1.0F)
            velocity = 1.0F;

            ItemStack arrowStack = new ItemStack(Items.ARROW);
            ItemArrow itemarrow = (ItemArrow) ((stack.getItem() instanceof ItemArrow ? arrowStack.getItem() : Items.ARROW));
            EntityArrow entityarrow = itemarrow.createArrow(world, arrowStack, player);
            entityarrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);

            /*
            float velocityModifier = 0.6f * velocity;

            entityarrow.motionX += (event.getWorld().rand.nextDouble() - 0.5) * velocityModifier;
            entityarrow.motionY += (event.getWorld().rand.nextDouble() - 0.5) * velocityModifier;
            entityarrow.motionZ += (event.getWorld().rand.nextDouble() - 0.5) * velocityModifier;
			*/
            
            if (velocity == 1.0F)
                entityarrow.setIsCritical(true);

            int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

            if (powerLevel > 0)
                entityarrow.setDamage(entityarrow.getDamage() + (double) powerLevel * 0.5D + 0.5D);

            int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

            if (punchLevel > 0)
                entityarrow.setKnockbackStrength(punchLevel);

            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                entityarrow.setFire(100);

            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

            world.spawnEntity(entityarrow);    	
    }
    
    @SubscribeEvent 
    public void onDigSpeed(PlayerEvent.BreakSpeed event) {
		EntityPlayer player = event.getEntityPlayer();
		IGenes genes = ModUtils.getIGenes(player);
		if (EnumGenes.EFFICIENCY.isActive()) {
			if (genes.hasGene(EnumGenes.EFFICIENCY_4))
				event.setNewSpeed(event.getOriginalSpeed() + (float) 5 * 0.05F);
			else if (genes.hasGene(EnumGenes.EFFICIENCY))
				event.setNewSpeed(event.getOriginalSpeed() + (float) 3 * 0.05F);
		}    	
    }
   
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != Phase.START) return;
		
		EntityPlayer player = event.player;
		IGenes genes = ModUtils.getIGenes(player);
		World world = event.player.getEntityWorld();
		if (player == null || genes == null) return;

		if (world.isRemote) {
			checkClimbing(player, world, genes);
		} else {
			if (player.ticksExisted % 100 == 1) {
				checkCybernetic(player, world, genes);
				checkHunger(player, world, genes);					
				tryPhotosynthesis(player, world, genes);							
			}
			tryItemMagnet(player, world, genes);
			tryXPMagnet(player, world, genes);
		}
	}
	
	private static final UUID uuidCybernetic = UUID.fromString("14b60de8-825e-45e1-99c0-51685676f69b");
	private static final HashMultimap<String, AttributeModifier> cyberneticModifierMap;
	
	public static void setCyberToleranceUpgrade(float f) {
		cyberneticModifierMap.clear();
		cyberneticModifierMap.put("cyberware.tolerance", new AttributeModifier(uuidCybernetic, "Cybernetic gene", f, 0));
	}
	    
	static {
		cyberneticModifierMap = HashMultimap.create();
	}
	
	private static void checkCybernetic(EntityPlayer player, World world, IGenes genes) {
		if (!EnumGenes.CYBERNETIC.isActive()) return;
		
		if (genes.hasGene(EnumGenes.CYBERNETIC)) 
		    player.getAttributeMap().applyAttributeModifiers(cyberneticModifierMap);
		else
		    player.getAttributeMap().removeAttributeModifiers(cyberneticModifierMap);
			
	}

	private static void checkClimbing(EntityPlayer player, World world, IGenes genes) {
		if (EnumGenes.CLIMB_WALLS.isActive() && ClientGeneChange.climbingPlayers.contains(player.getUniqueID())) {
			if (player.collidedHorizontally) player.motionY = 0.2D;
		}
	}

	/**
	 * Checks all the requirements and sets the player step height.
	 *
	 * @param player
	 * @param world
	 * @param genes
	 */
	private static void changeStepAssist(EntityPlayer player, World world, boolean add) {
		if (!EnumGenes.STEP_ASSIST.isActive()) return;
		float step_height = (add) ? 1.1f : 0.6f;
		if (player.stepHeight != step_height) {
			player.stepHeight = step_height;
			GeneticsRebornPacketHandler.INSTANCE.sendTo(new ClientGeneChange(1, step_height), (EntityPlayerMP) player);						
		}
	}
	
	private static void checkHunger(EntityPlayer player, World world, IGenes genes) {
		if (EnumGenes.NO_HUNGER.isActive() && genes.hasGene(EnumGenes.NO_HUNGER))
			if (player.getFoodStats().getFoodLevel() < 5) player.getFoodStats().setFoodLevel(5);
	}
	
	private static void checkMoreHearts(EntityLivingBase player, World world) {
		if (!EnumGenes.MORE_HEARTS.isActive()) return;
		IGenes genes = ModUtils.getIGenes(player);
		IMaxHealth hearts = ModUtils.getIMaxHealth(player);
		if (hearts == null) return;
		
		int newHearts = 0;
		if (genes.hasGene(EnumGenes.MORE_HEARTS)) newHearts += 20;
		if (genes.hasGene(EnumGenes.MORE_HEARTS_2)) newHearts += 20 * GeneticsReborn.mutationAmp;
		
		hearts.setBonusMaxHealth(newHearts);
	}
	
	
	/**
	 * Checks all the requirements and tries to do the item magnet gene(Suck in all items from an area).
	 *
	 * @param player
	 * @param world
	 * @param genes
	 */
	private static void tryItemMagnet(EntityPlayer player, World world, IGenes genes) {
		if (EnumGenes.ITEM_MAGNET.isActive() && genes.hasGene(EnumGenes.ITEM_MAGNET) && !player.inventory.hasItemStack(new ItemStack(GRItems.AntiField))
				&& !player.isSneaking()) {
			Iterator<Entity> iterator = ModUtils.getEntitiesInRange(EntityItem.class, world, player.posX, player.posY,
					player.posZ, 6.5).iterator();
			while (iterator.hasNext()) {
				EntityItem itemToGet = (EntityItem) iterator.next();

				if (!itemToGet.getTags().contains("geneticsrebornLOL") && shouldPickupItem(world, itemToGet.getPosition())) {
					EntityItemPickupEvent pickupEvent = new EntityItemPickupEvent(player, itemToGet);
					MinecraftForge.EVENT_BUS.post(pickupEvent);
					ItemStack itemStackToGet = itemToGet.getItem();
					int stackSize = itemStackToGet.getCount();

					if (pickupEvent.getResult() == Result.ALLOW || stackSize <= 0 || !player.inventory.addItemStackToInventory(itemStackToGet)) {
						player.onItemPickup(itemToGet, stackSize);
						world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.AMBIENT,
								0.15F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
					}
				} else if (!itemToGet.getTags().contains("geneticsrebornLOL")) itemToGet.addTag("geneticsrebornLOL");
			}
		}
	}

	/**
	 * Checks all the requirements and tries to do the xp magnet gene(Suck in all xp from an area).
	 *
	 * @param player
	 * @param world
	 * @param genes
	 */
	private static void tryXPMagnet(EntityPlayer player, World world, IGenes genes) {
		if (EnumGenes.XP_MAGNET.isActive() && genes.hasGene(EnumGenes.XP_MAGNET) && !player.inventory.hasItemStack(new ItemStack(GRItems.AntiField))
				&& !player.isSneaking()) {
			Iterator<Entity> iterator = ModUtils.getEntitiesInRange(EntityXPOrb.class, world, player.posX, player.posY, player.posZ,
					6.5).iterator();
			while (iterator.hasNext()) {
				EntityXPOrb xpToGet = (EntityXPOrb) iterator.next();

				if (xpToGet.isDead || xpToGet.isInvisible()) {
					continue;
				}
				player.xpCooldown = 0;
				xpToGet.delayBeforeCanPickup = 0;
				xpToGet.setPosition(player.posX, player.posY, player.posZ);
				PlayerPickupXpEvent xpEvent = new PlayerPickupXpEvent(player, xpToGet);
				MinecraftForge.EVENT_BUS.post(xpEvent);
				if (xpEvent.getResult() == Result.ALLOW) {
					xpToGet.onCollideWithPlayer(player);
				}

			}
		}
	}

	/**
	 * Checks all conditions and tries to give the player hunger when no blocks are above them, and it is day.
	 *
	 * @param player
	 * @param world
	 * @param genes
	 */
	private static void tryPhotosynthesis(EntityPlayer player, World world, IGenes genes) {
		if (EnumGenes.PHOTOSYNTHESIS.isActive() && genes.hasGene(EnumGenes.PHOTOSYNTHESIS)) {
			if (world.isDaytime() && world.getHeight(player.getPosition()).getY() < (player.getPosition().getY() + 1)) {
				if (ThreadLocalRandom.current().nextInt(100) < 5) 
					player.getFoodStats().addStats(1, 0.5F);
			}
		}
	}

	public static boolean shouldPickupItem(World world, BlockPos pos) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		for (int i = -5; i <= 5; i++) {
			BlockPos newpos = new BlockPos(x, y + i, z);
			if (world.getBlockState(newpos).getBlock() == GRBlocks.AntiFieldBlock) {
				return false;
			}
		}
		return true;
	}

	
	public static void geneChanged(EntityLivingBase entity, EnumGenes gene, boolean added) {
		World w = entity.world;
		
		if (gene == EnumGenes.MORE_HEARTS) checkMoreHearts(entity, w);
		if (gene == EnumGenes.MORE_HEARTS_2) checkMoreHearts(entity, w);
		if (entity instanceof EntityPlayer) {
			if (gene == EnumGenes.STEP_ASSIST) changeStepAssist((EntityPlayer)entity, w, added);
			if (gene == EnumGenes.CLIMB_WALLS) changeClimbWalls((EntityPlayer)entity, w, added);
		}
	}

	private static void changeClimbWalls(EntityPlayer entity, World w, boolean added) {
		GeneticsRebornPacketHandler.INSTANCE.sendTo(new ClientGeneChange(2, (added) ? 1 : 0), (EntityPlayerMP) entity);								
	}
}
