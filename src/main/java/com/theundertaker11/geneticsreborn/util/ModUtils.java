package com.theundertaker11.geneticsreborn.util;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.MaxHealthCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ModUtils{

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
	 * Checks if the stack is a vanilla fuel
	 */
	public static boolean isValidFuel(ItemStack stack)
	{
		if(TileEntityFurnace.getItemBurnTime(stack)>0)
			return true;
		else return false;
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
		if(rawname.equals("GeneticsRebornINFINITY"))
		{
			genename="Infinity";
		}
		if(rawname.equals("GeneticsRebornSTEP_ASSIST"))
		{
			genename="Step Assist";
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
		if(rawname.equals("GeneticsRebornPHOTOSYNTHESIS"))
		{
			genename="Photosynthesis";
		}
		if(rawname.equals("GeneticsRebornBIOLUMIN"))
		{
			genename="Bioluminescence";
		}
		if(rawname.equals("GeneticsRebornRESPAWN"))
		{
			genename="Respawn";
		}
		if(rawname.equals("GeneticsRebornCYBERNETIC"))
		{
			genename="Cybernetic";
		}
		return genename;
	}
	
	/**
	 * I hate myself for this. I set it up so that it is super hard to check if each is enabled or not.
	 * <p> This will return if the gene is enabled in config or not.
	 * @param rawname EnumGene.toString() prefixed with GeneticsReborn
	 * @return
	 */
	public static boolean isGeneEnabled(String rawname)
	{
		if(rawname.equals("GeneticsRebornBasicGene"))
		{
			return true;
		}
		if(rawname.equals("GeneticsRebornDRAGONS_BREATH"))
		{
			return GeneticsReborn.enableDragonsBreath;
		}
		if(rawname.equals("GeneticsRebornINFINITY"))
		{
			return GeneticsReborn.enableInfinity;
		}
		if(rawname.equals("GeneticsRebornSTEP_ASSIST"))
		{
			return GeneticsReborn.enableStepAssist;
		}
		if(rawname.equals("GeneticsRebornEAT_GRASS"))
		{
			return GeneticsReborn.enableEatGrass;
		}
		if(rawname.equals("GeneticsRebornEMERALD_HEART"))
		{
			return GeneticsReborn.enableEmeraldHeart;
		}
		if(rawname.equals("GeneticsRebornENDER_DRAGON_HEALTH"))
		{
			return GeneticsReborn.enableEnderDragonHealth;
		}
		if(rawname.equals("GeneticsRebornFIRE_PROOF"))
		{
			return GeneticsReborn.enableFireProof;
		}
		if(rawname.equals("GeneticsRebornFLY"))
		{
			return GeneticsReborn.enableFlight;
		}
		if(rawname.equals("GeneticsRebornJUMP_BOOST"))
		{
			return GeneticsReborn.enableJumpBoost;
		}
		if(rawname.equals("GeneticsRebornMILKY"))
		{
			return GeneticsReborn.enableMilky;
		}
		if(rawname.equals("GeneticsRebornMORE_HEARTS"))
		{
			return GeneticsReborn.enableMoreHearts;
		}
		if(rawname.equals("GeneticsRebornNIGHT_VISION"))
		{
			return GeneticsReborn.enableNightVision;
		}
		if(rawname.equals("GeneticsRebornNO_FALL_DAMAGE"))
		{
			return GeneticsReborn.enableNoFallDamage;
		}
		if(rawname.equals("GeneticsRebornPOISON_PROOF"))
		{
			return GeneticsReborn.enablePoisonProof;
		}
		if(rawname.equals("GeneticsRebornRESISTANCE"))
		{
			return GeneticsReborn.enableResistance;
		}
		if(rawname.equals("GeneticsRebornSAVE_INVENTORY"))
		{
			return GeneticsReborn.enableSaveInventory;
		}
		if(rawname.equals("GeneticsRebornSCARE_CREEPERS"))
		{
			return GeneticsReborn.enableScareCreepers;
		}
		if(rawname.equals("GeneticsRebornSHOOT_FIREBALLS"))
		{
			return GeneticsReborn.enableShootFireballs;
		}
		if(rawname.equals("GeneticsRebornSLIMY"))
		{
			return GeneticsReborn.enableSlimy;
		}
		if(rawname.equals("GeneticsRebornSPEED"))
		{
			return GeneticsReborn.enableSpeed;
		}
		if(rawname.equals("GeneticsRebornTELEPORTER"))
		{
			return GeneticsReborn.enableTeleporter;
		}
		if(rawname.equals("GeneticsRebornWATER_BREATHING"))
		{
			return GeneticsReborn.enableTeleporter;
		}
		if(rawname.equals("GeneticsRebornWOOLY"))
		{
			return GeneticsReborn.enableWooly;
		}
		if(rawname.equals("GeneticsRebornWITHER_HIT"))
		{
			return GeneticsReborn.enableWitherHit;
		}
		if(rawname.equals("GeneticsRebornWITHER_PROOF"))
		{
			return GeneticsReborn.enableWitherProof;
		}
		if(rawname.equals("GeneticsRebornXP_MAGNET"))
		{
			return GeneticsReborn.enableXPMagnet;
		}
		if(rawname.equals("GeneticsRebornITEM_MAGNET"))
		{
			return GeneticsReborn.enableItemMagnet;
		}
		if(rawname.equals("GeneticsRebornEXPLOSIVE_EXIT"))
		{
			return GeneticsReborn.enableExplosiveExit;
		}
		if(rawname.equals("GeneticsRebornSTRENGTH"))
		{
			return GeneticsReborn.enableStrength;
		}
		if(rawname.equals("GeneticsRebornPHOTOSYNTHESIS"))
		{
			return GeneticsReborn.enablePhotosynthesis;
		}
		if(rawname.equals("GeneticsRebornBIOLUMIN"))
		{
			return GeneticsReborn.enableBiolumin;
		}
		if(rawname.equals("GeneticsRebornRESPAWN"))
		{
			return GeneticsReborn.enableRespawn;
		}
		if(rawname.equals("GeneticsRebornCYBERNETIC"))
		{
			return GeneticsReborn.enableCybernetics;
		}
		return false;
	}
	
	public static List<Entity> getEntitiesInRange(Class<? extends Entity> entityType, World world, double x, double y, double z, double radius) {
		return getEntitesInTange(entityType, world, x - radius, y - radius, z - radius, x + radius, y + radius,
				z + radius);
	}

	public static List<Entity> getEntitesInTange(Class<? extends Entity> entityType, World world, double x, double y, double z, double x2,
			double y2, double z2) {
		return world.getEntitiesWithinAABB(entityType, new AxisAlignedBB(x, y, z, x2, y2, z2));
	}
	
    private static double getDistanceSq(BlockPos pos1, BlockPos pos2)  {
        return ((pos1.getX() - pos2.getX()) * (pos1.getX() - pos2.getX())
                + (pos1.getY() - pos2.getY()) * (pos1.getY() - pos2.getY())
                + (pos1.getZ() - pos2.getZ()) * (pos1.getZ() - pos2.getZ()));
    }
    
	public static EntityLivingBase getNearestEntity(World w, BlockPos pos, double reach) {
        if (reach <= 0.0D) return null;
        if (w == null || pos == null) return null;

        EntityLivingBase closest = null;
        double distanceSq = reach * reach;
        AxisAlignedBB aabb = new AxisAlignedBB(
        		pos.getX() - reach, pos.getY() - reach, pos.getZ() - reach,
        		pos.getX() + reach,	pos.getY() + reach,	pos.getZ() + reach);
        List<EntityLivingBase> listEntitiesInRange = w.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
        for (EntityLivingBase next : listEntitiesInRange) {
            if (getDistanceSq(next.getPosition(), pos) < distanceSq) {
                closest = next;
            }
        }

        return closest;		
	}
}
