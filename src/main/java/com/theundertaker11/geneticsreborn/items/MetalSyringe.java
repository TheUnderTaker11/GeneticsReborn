package com.theundertaker11.geneticsreborn.items;

import java.util.List;

import javax.annotation.Nullable;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.Genes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.event.PlayerTickEvent;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MetalSyringe extends ItemBase {

	public MetalSyringe(String name) {
		super(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
	}

	@Override
    @SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Left click an entity to draw their blood");
		tooltip.add("Shift left click to insert the blood back");
		if (stack.getTagCompound() != null && stack.getItemDamage() == 1) {
			if (stack.getTagCompound().getBoolean("pure")) tooltip.add("This blood is purified");
			else tooltip.add("This blood is contaminated");
			tooltip.add("Blood of a " + stack.getTagCompound().getString("entname"));
		}
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (player.getEntityWorld().isRemote) return false;
		if (!GeneticsReborn.allowGivingEntityGenes) {
			player.sendMessage(new TextComponentString("That is disabled in the config, sorry"));
			return false;
		}
		NBTTagCompound tag = ModUtils.getTagCompound(stack);
		if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
			EntityLivingBase entityLiving = (EntityLivingBase) entity;
			if (ModUtils.getIGenes(entityLiving) != null && ModUtils.getIMaxHealth(entityLiving) != null) {
				if (!player.isSneaking() && stack.getItemDamage() == 0) {
					stack.setItemDamage(1);
					entityLiving.attackEntityFrom(DamageSource.GENERIC, 2);
					tag.setString("entname", entityLiving.getName());
					tag.setString("entCodeName", entityLiving.getClass().getSimpleName());
					tag.setBoolean("pure", false);
					Genes.setNBTStringsFromGenes(stack, entityLiving);
					return true;
				} else if (player.isSneaking() && stack.getTagCompound() != null && stack.getTagCompound().getBoolean("pure") && stack.getItemDamage() == 1) {
					if (entityLiving instanceof EntityWither ||
							entityLiving instanceof EntityCreeper ||
							!stack.getTagCompound().getString("entCodeName").equals(entityLiving.getClass().getSimpleName()))
						return false;

					stack.setItemDamage(0);
					stack.getTagCompound().removeTag("pure");
					entityLiving.attackEntityFrom(DamageSource.GENERIC, 2);
					IGenes genes = ModUtils.getIGenes(entityLiving);
					
					//need to make a list of genes in syringe
					IGenes syGenes = new Genes();
					for (int i = 0; i < Genes.TotalNumberOfGenes; i++) {
						String nbtname = "Null";
						if (tag.hasKey(Integer.toString(i))) {
							nbtname = tag.getString(Integer.toString(i));
							syGenes.addGene(Genes.getGeneFromString(nbtname));
						}
					}					
					for (int i = 0; i < Genes.TotalNumberOfGenes; i++) {
						String nbtname = "Null";
						if (tag.hasKey(Integer.toString(i))) {
							nbtname = tag.getString(Integer.toString(i));
							tag.removeTag(Integer.toString(i));
							EnumGenes gene = Genes.getGeneFromString(nbtname);
							if (gene != null && gene.canAddMutation(genes, syGenes) && !genes.hasGene(gene)) genes.addGene(gene);
							PlayerTickEvent.geneChanged(entityLiving, gene, true);
						}
						if (tag.hasKey(i + "anti")) {
							nbtname = tag.getString(i + "anti");
							tag.removeTag(i + "anti");
							EnumGenes gene = Genes.getGeneFromString(nbtname);
							if (gene != null && genes.hasGene(gene)) genes.removeGene(gene);
							PlayerTickEvent.geneChanged(entityLiving, gene, false);
						}
					}
					return true;
				}
			}
		}
		return true;
	}
}
