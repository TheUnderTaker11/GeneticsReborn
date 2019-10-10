package com.theundertaker11.geneticsreborn.util;

import java.util.List;

import javax.annotation.Nullable;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.MaxHealthCapabilityProvider;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		if ("GeneticsRebornBasicGene".equals(rawname)) return "Basic Gene";
		EnumGenes gene = EnumGenes.fromGeneName(rawname);
		if (gene == null) return "This is an error. Report to Genetics Reborn Github.";
		return gene.getDescrption();
	}
	
	public static boolean isGeneEnabled(String rawname)
	{
		
		if(rawname.equals("GeneticsRebornBasicGene")) return true;
		EnumGenes gene = EnumGenes.fromGeneName(rawname);
		return (gene == null) ? false : gene.isActive();
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
