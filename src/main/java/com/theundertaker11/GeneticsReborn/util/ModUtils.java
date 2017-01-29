package com.theundertaker11.GeneticsReborn.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

import com.theundertaker11.GeneticsReborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.genes.IGenes;
import com.theundertaker11.GeneticsReborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.GeneticsReborn.api.capability.maxhealth.MaxHealthCapabilityProvider;
import com.theundertaker11.GeneticsReborn.items.GRItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ModUtils{
	//Potion effect ID's for easy use.
	public static final int moveSpeed = 1;
	public static final int moveSlowness = 2;
	public static final int digSpeed = 3;
	public static final int miningSlowDown = 4;
	public static final int strength = 5;
	public static final int jumpBoost = 8;
	public static final int nausea = 9;
	public static final int regeneration = 10;
	public static final int resistance = 11;
	public static final int fireResistance = 12;
	public static final int waterBreathing = 13;
	public static final int invisibility = 14;
	public static final int blindness = 15;
	public static final int nightVision = 16;
	public static final int hunger = 17;
	public static final int weakness = 18;
	public static final int poison = 19;
	public static final int wither = 20;
	
	public static final int ATTRIBUTE_MODIFIER_OPERATION_ADD = 0;
	
	/**
	 * This makes it so I don't have to check for null on every tag compound.
	 * Thanks to the Not Enough Wands mod for this code.
	 * https://github.com/romelo333/notenoughwands1.8.8/blob/1.10/src/main/java/romelo333/notenoughwands/varia/Tools.java
	 * @param stack
	 * @return
	 */
	public static NBTTagCompound getTagCompound(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null){
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        return tag;
    }
	
	/**
	 * Teleports a player, given the x, y, z, and dimension ID.
	 * Works cross-dimensionally(Hence needing dimension ID)
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 * @param dimension
	 */
	public static void TeleportPlayer(EntityPlayer player, double x, double y, double z, int dimension)
	{
		if(player.dimension==dimension)
		{
			player.setPositionAndUpdate(x, y, z);
		}
		else
		{
			player.changeDimension(dimension);
			player.setPositionAndUpdate(x, y, z);	
		}
	}
	
	/**
	 * Gets the IGenes capability from the player.
	 * Can return a null.
	 * @param entityliving
	 * @return
	 */
	@Nullable
	public static IGenes getIGenes(EntityLivingBase entityliving)
	{
		return entityliving.getCapability(GeneCapabilityProvider.GENES_CAPABILITY, null);
	}
	
	/**
	 * Gets the IMaxHealth of the given entity
	 */
	@Nullable
	public static IMaxHealth getIMaxHealth(EntityLivingBase entity)
	{
		return entity.getCapability(MaxHealthCapabilityProvider.MAX_HEALTH_CAPABILITY, null);
	}
	
	public static String getGeneNameForShow(String rawname)
	{
		String genename = "This is an error. Report to Genetics Reborn Github.";
		if(rawname.equals("GeneticsRebornBasicGene"))
		{
			genename="Basic Gene";
		}
		if(rawname.equals("GeneticsRebornDRAGONS_BREATH"))
		{
			genename="Dragon's Breath";
		}
		if(rawname.equals("GeneticsRebornEAT_GRASS"))
		{
			genename="Eat Grass";
		}
		if(rawname.equals("GeneticsRebornEMERALD_HEART"))
		{
			genename="Emerald Heart";
		}
		if(rawname.equals("GeneticsRebornENDER_DRAGON_HEALTH"))
		{
			genename="Ender Dragon Health";
		}
		if(rawname.equals("GeneticsRebornFIRE_PROOF"))
		{
			genename="Fire Proof";
		}
		if(rawname.equals("GeneticsRebornFLY"))
		{
			genename="Fly";
		}
		if(rawname.equals("GeneticsRebornJUMP_BOOST"))
		{
			genename="Jump Boost";
		}
		if(rawname.equals("GeneticsRebornMILKY"))
		{
			genename="Milky";
		}
		if(rawname.equals("GeneticsRebornMORE_HEARTS"))
		{
			genename="More Hearts";
		}
		if(rawname.equals("GeneticsRebornNIGHT_VISION"))
		{
			genename="Night Vision";
		}
		if(rawname.equals("GeneticsRebornNO_FALL_DAMAGE"))
		{
			genename="No Fall Damage";
		}
		if(rawname.equals("GeneticsRebornPOISON_PROOF"))
		{
			genename="Poison Immunity";
		}
		if(rawname.equals("GeneticsRebornRESISTANCE"))
		{
			genename="Resistance";
		}
		if(rawname.equals("GeneticsRebornSAVE_INVENTORY"))
		{
			genename="Keep Inventory";
		}
		if(rawname.equals("GeneticsRebornSCARE_CREEPERS"))
		{
			genename="Scare Creepers";
		}
		if(rawname.equals("GeneticsRebornSHOOT_FIREBALLS"))
		{
			genename="Shoot Fireballs";
		}
		if(rawname.equals("GeneticsRebornSLIMY"))
		{
			genename="Slimy";
		}
		if(rawname.equals("GeneticsRebornSPEED"))
		{
			genename="Speed";
		}
		if(rawname.equals("GeneticsRebornTELEPORTER"))
		{
			genename="Teleporter";
		}
		if(rawname.equals("GeneticsRebornWATER_BREATHING"))
		{
			genename="Water Breathing";
		}
		if(rawname.equals("GeneticsRebornWOOLY"))
		{
			genename="Wooly";
		}
		if(rawname.equals("GeneticsRebornWITHER_HIT"))
		{
			genename="Wither Hit";
		}
		if(rawname.equals("GeneticsRebornWITHER_PROOF"))
		{
			genename="Wither Proof";
		}
		if(rawname.equals("GeneticsRebornXP_MAGNET"))
		{
			genename="XP Attraction Field";
		}
		if(rawname.equals("GeneticsRebornITEM_MAGNET"))
		{
			genename="Item Attraction Field";
		}
		if(rawname.equals("GeneticsRebornEXPLOSIVE_EXIT"))
		{
			genename="Explosive Exit";
		}
		if(rawname.equals("GeneticsRebornSTRENGTH"))
		{
			genename="Strength";
		}
		return genename;
	}
	
	//Next two are from Joetato and for the magnet code.
	public static List<Entity> getEntitiesInRange(Class<? extends Entity> entityType, World world, double x, double y, double z, double radius) {
		return getEntitesInTange(entityType, world, x - radius, y - radius, z - radius, x + radius, y + radius,
				z + radius);
	}

	public static List<Entity> getEntitesInTange(Class<? extends Entity> entityType, World world, double x, double y, double z, double x2,
			double y2, double z2) {
		return world.getEntitiesWithinAABB(entityType, new AxisAlignedBB(x, y, z, x2, y2, z2));
	}
}
