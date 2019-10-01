package com.theundertaker11.geneticsreborn.event;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.items.DamageableItemBase;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.util.ModUtils;

//TODO add back if ars is updated. import am2.bosses.AM2Boss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This class scrapes entities, inserts genes into entities, and handles the wooly/milky gene
 */
public class RightClickEntityEvent {


	@SubscribeEvent
	public void rightClickEntity(EntityInteract event) {
		if (event.getHand() == EnumHand.MAIN_HAND && !event.getWorld().isRemote && event.getEntityPlayer().getHeldItemMainhand() != null) {
			EntityPlayer player = event.getEntityPlayer();
			Entity target = event.getTarget();
			if (target instanceof MultiPartEntityPart && player.getHeldItemMainhand().getItem() instanceof DamageableItemBase) {
				if (player.getHeldItemMainhand().getItem() == GRItems.MetalScraper || player.getHeldItemMainhand().getItem() == GRItems.AdvancedScraper) {
					ItemStack organicmatter = new ItemStack(GRItems.OrganicMatter, 1);
					target.attackEntityFrom(DamageSource.causePlayerDamage(player), 0.5F);
					ModUtils.getTagCompound(organicmatter).setString("entityName", "Ender Dragon");
					ModUtils.getTagCompound(organicmatter).setString("entityCodeName", "Ender Dragon");
					player.getHeldItemMainhand().damageItem(1, player);
					EntityItem entity = new EntityItem(player.getEntityWorld(), target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getZ(), organicmatter);
					player.getEntityWorld().spawnEntity(entity);
				}
			}

			if ((target instanceof EntityLivingBase && !(target instanceof EntityPlayer)) && player.getHeldItemMainhand().getItem() instanceof DamageableItemBase) {
				EntityLivingBase livingtarget = (EntityLivingBase) target;
				scrapeEntity(player, livingtarget);
			}

			tryWoolyAndMilky(player, target);
		}
	}

	@Optional.Method(modid = "arsmagica2")
	public static boolean isArsBoss(Entity entity) {
		EntityDragon part = (EntityDragon) entity;
		//TODO add back if ars is updated
		//if(part.entityDragonObj instanceof AM2Boss)
		//	return true;
		return false;
	}

	@Optional.Method(modid = "arsmagica2")
	public static void setArsMobTag(ItemStack stack, Entity entity) {
		EntityDragon part = (EntityDragon) entity;
		String rawname = part.getEntityBoundingBox().toString();
		String name = rawname.substring(0, rawname.indexOf('['));
		ModUtils.getTagCompound(stack).setString("entityName", name);
		ModUtils.getTagCompound(stack).setString("entityCodeName", name);
	}

	/**
	 * Scrapes the EntityLivingBase given and drops organic matter with the name given.
	 * This accounts for metal and advanced scraper.
	 *
	 * @param player       Needed to damage scraper
	 * @param livingtarget Target entity
	 */
	public static void scrapeEntity(EntityPlayer player, EntityLivingBase livingtarget) {
		ItemStack organicmatter = new ItemStack(GRItems.OrganicMatter, 1);
		String simplename = livingtarget.getClass().getSimpleName();
		String name = livingtarget.getName();

		if (livingtarget instanceof EntityWitherSkeleton) {
			simplename = "Wither Skeleton";
			name = "Wither Skeleton";
		}
		if (livingtarget instanceof EntityPigZombie) {
			name = "Zombie Pigman";
		}

		if (player.getHeldItemMainhand().getItem() == GRItems.MetalScraper) {
			livingtarget.attackEntityFrom(DamageSource.causePlayerDamage(player), 0.5F);
			ModUtils.getTagCompound(organicmatter).setString("entityName", name);
			ModUtils.getTagCompound(organicmatter).setString("entityCodeName", simplename);
			player.getHeldItemMainhand().damageItem(1, player);
		}

		if (player.getHeldItemMainhand().getItem() == GRItems.AdvancedScraper) {
			livingtarget.attackEntityFrom(DamageSource.causePlayerDamage(player), 1.0F);
			NBTTagCompound entitytag = new NBTTagCompound();
			livingtarget.writeToNBT(entitytag);

			NBTTagCompound itemtag = ModUtils.getTagCompound(organicmatter);

			itemtag.setTag("mobTag", entitytag);
			itemtag.setString("type", livingtarget.getClass().getCanonicalName());
			itemtag.setString("entityName", name);
			itemtag.setString("entityCodeName", simplename);
			player.getHeldItemMainhand().damageItem(1, player);
		}
		EntityItem entity = new EntityItem(player.getEntityWorld(), livingtarget.getPosition().getX(), livingtarget.getPosition().getY(), livingtarget.getPosition().getZ(), organicmatter);
		player.getEntityWorld().spawnEntity(entity);
	}

	/**
	 * Checks if the entity has Wooly or Milky then tries to do the actions of the wooly/milky
	 * gene given the player right clicking and a target Entity.
	 *
	 * @param player Player right clicking
	 * @param target Target to check if it has wooly/milky.
	 */
	public static void tryWoolyAndMilky(EntityPlayer player, Entity target) {
		if (target instanceof EntityLivingBase && !(target instanceof EntityCow)) {
			EntityLivingBase targetentity = (EntityLivingBase) target;
			if (!GeneticsReborn.allowGivingEntityGenes && !(targetentity instanceof EntityPlayer)) return;
			World world = player.getEntityWorld();
			if (ModUtils.getIGenes(targetentity) != null) {
				IGenes targetentitygenes = ModUtils.getIGenes(targetentity);

				if (GeneticsReborn.enableWooly && player.getHeldItemMainhand().getItem() instanceof ItemShears && targetentitygenes.hasGene(EnumGenes.WOOLY)) {
					ItemStack wool = new ItemStack(Blocks.WOOL, 1);
					player.getHeldItemMainhand().damageItem(1, player);
					EntityItem entitywool = new EntityItem(world, targetentity.getPosition().getX(), targetentity.getPosition().getY(), targetentity.getPosition().getZ(), wool);
					world.spawnEntity(entitywool);
				}

				if (GeneticsReborn.enableMilky && player.getHeldItemMainhand().getItem() == Items.BUCKET && targetentitygenes.hasGene(EnumGenes.MILKY)) {

					if ((player.getHeldItemMainhand().getCount() - 1) <= 0)
						player.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.MILK_BUCKET));
					else {
						if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET)))
							player.dropItem(Items.MILK_BUCKET, 1);
						player.getHeldItemMainhand().shrink(1);
					}
				}
			}
		}
	}
}
