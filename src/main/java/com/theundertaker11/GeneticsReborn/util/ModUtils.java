package com.theundertaker11.GeneticsReborn.util;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

import com.theundertaker11.GeneticsReborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.genes.IGenes;
import com.theundertaker11.GeneticsReborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.GeneticsReborn.api.capability.maxhealth.MaxHealthCapabilityProvider;
import com.theundertaker11.GeneticsReborn.items.GRItems;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

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
		return genename;
	}
	
	/**
	 * I feed the entityName gotten from the original organic matter into this, along with a
	 * randomly generated number between 1 and 100 inclusively.
	 * @param entityName Name of entity given to original organic matter
	 * @param number random number between 1 and 100 inclusively
	 * @return Gene name for use in getGeneFromString
	 */
	public static String nameToGeneLogic(String entityName)
	{
		int number = ThreadLocalRandom.current().nextInt(1, 101);
		String name = "BasicGene";
		if(number>40) return "GeneticsRebornBasicGene";
		
		if(entityName.equals("Iron Golem"))
		{
			name="MORE_HEARTS";
		}
		if(entityName.equals("Cow"))
		{
			if(number<=20) name="EAT_GRASS";
			else name="MILKY";
		}
		if(entityName.equals("Chicken"))
		{
			name="NO_FALL_DAMAGE";
		}
		if(entityName.equals("Sheep"))
		{
			if(number<=20) name="EAT_GRASS";
			else name="WOOLY";
		}
		if(entityName.equals("Creeper"))
		{
			
		}
		if(entityName.equals("Skeleton"))
		{
			
		}
		if(entityName.equals("Spider"))
		{
			name="NIGHT_VISION";
		}
		if(entityName.equals("Zombie")||entityName.equals("entity.Zombie.name"))
		{
			name="RESISTANCE";
		}
		if(entityName.equals("Slime"))
		{
			name="SLIMY";
		}
		if(entityName.equals("Ghast"))
		{
			if(number<=20) name="SHOOT_FIREBALLS";
			else name="FLY";
		}
		if(entityName.equals("Enderman"))
		{
			if(number<=20) name="MORE_HEARTS";
			else name="TELEPORTER";
		}
		if(entityName.equals("Cave Spider"))
		{
			if(number<=20) name="POISON_PROOF";
			else name="NIGHT_VISION";
		}
		if(entityName.equals("Silverfish"))
		{
			
		}
		if(entityName.equals("Blaze"))
		{
			if(number<=20) name="SHOOT_FIREBALLS";
			else name="FIRE_PROOF";
		}
		if(entityName.equals("Magma Cube"))
		{
			name="FIRE_PROOF";
		}
		if(entityName.equals("Bat"))
		{
			if(number<=20) name="FLY";
			else name="NIGHT_VISION";
		}
		if(entityName.equals("Witch"))
		{
			name="POISON_PROOF";
		}
		if(entityName.equals("Endermite"))
		{
			name="SAVE_INVENTORY";
		}
		if(entityName.equals("Guardian"))
		{
			name="WATER_BREATHING";
		}
		if(entityName.equals("Shulker"))
		{
			name="RESISTANCE";
		}
		if(entityName.equals("Squid"))
		{
			name="WATER_BREATHING";
		}
		if(entityName.equals("Wolf"))
		{
			
		}
		if(entityName.equals("Mooshroom"))
		{
			
		}
		if(entityName.equals("Ocelot"))
		{
			if(number<=20) name="SPEED";
			else name="SCARE_CREEPERS";
		}
		if(entityName.equals("Donkey"))
		{
			
		}
		if(entityName.equals("Rabbit"))
		{
			if(number<=20) name="SPEED";
			else name="JUMP_BOOST";
			
		}
		if(entityName.equals("Polar Bear"))
		{
			
		}
		if(entityName.equals("Horse"))
		{
			name="JUMP_BOOST";
		}
		if(entityName.equals("Villager"))
		{
			name="EMERALD_HEART";
		}
		if(entityName.equals("Wither"))
		{
			name="WITHER_PROOF";
		}
		if(entityName.equals("Wither Skeleton"))
		{
			name="WITHER_HIT";
		}
		if(entityName.equals("Zombie Pigman"))
		{
			name="FIRE_PROOF";
		}
		if(entityName.equals("Ender Dragon"))
		{
			if(number<=20) name="DRAGONS_BREATH";
			else name="ENDER_DRAGON_HEALTH";
		}
		
		return ("GeneticsReborn"+name);
	}
}
