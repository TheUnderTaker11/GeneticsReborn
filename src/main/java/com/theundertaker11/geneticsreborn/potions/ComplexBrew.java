package com.theundertaker11.geneticsreborn.potions;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class ComplexBrew implements IBrewingRecipe {
	private PotionType pot;
	private Item item;
	private String cellType;
	private ItemStack output;
	private EnumGenes inputGene;
	
	public ComplexBrew(PotionType pot, Item ingredient, String cellType, ItemStack output) {
		this.pot = pot;
		this.cellType = cellType;
		this.item = ingredient;
		this.output = output;
	}
	
	public ComplexBrew(PotionType pot, Item ingredient, String cellType, String gene, int outChance) {
		this.pot = pot;
		this.cellType = cellType;
		this.item = ingredient;
		this.output = new ItemStack(GRItems.Cell);
		NBTTagCompound tag = ModUtils.getTagCompound(this.output);
		if (pot == GRPotions.MUTATION_POTION) tag.setBoolean("mutation", true);
		tag.setString("forceGene", gene);
		tag.setInteger("chance", outChance);
		
	}

	public ComplexBrew(PotionType pot, Item ingredient, String cellType, EnumGenes outGene, int outChance) {
		this(pot, ingredient, cellType, outGene.toGeneName(), outChance);
	}
	
	
	public ComplexBrew(PotionType pot, Item dNAHelix, EnumGenes geneIn, EnumGenes geneOut, int chance) {
		this.pot = pot;
		this.inputGene = geneIn;
		this.item = dNAHelix;
		this.output = newPotion(geneOut);
		//chance is not used now, maybe later
	}
	
	private final ItemStack newPotion(EnumGenes gene) {
		ItemStack out = PotionUtils.addPotionToItemStack(new ItemStack(GRItems.ViralPotion), GRPotions.VIRAL_POTION);
		NBTTagCompound tag = ModUtils.getTagCompound(out);
		tag.setString("gene", gene.toGeneName());
		return out;
	}

	@Override
	public boolean isInput(ItemStack input) {
		PotionType type = PotionUtils.getPotionFromItem(input);		
		
		String match = null;
		if (input.getItem() == GRItems.Cell && input.hasTagCompound()) {
			match = input.getTagCompound().getString("entityName");
		} else if (input.getItem() == GRItems.DNAHelix && input.hasTagCompound()) {
			match = input.getTagCompound().getString("gene");
		} else if (type != PotionTypes.EMPTY && input.hasTagCompound()) {
			match = input.getTagCompound().getString("gene");
			if (match.isEmpty()) match = input.getTagCompound().getString("entityCodeName");
		}
		
		//this is for imprinting potions with cell/helix info
		if (type == pot) {
			if ((type == GRPotions.GROWTH_POTION) && (this.cellType == null) && (input.hasTagCompound() && !input.getTagCompound().hasKey("entityCodeName")))
				return true;
			if ((type == GRPotions.MUTATION_POTION) && (this.cellType == null) && (input.hasTagCompound() && !input.getTagCompound().hasKey("entityCodeName")))
				return true;
		}
		
		if (this.cellType != null && this.cellType.equals(match)) return true;
		if (this.cellType != null && this.cellType.equals("*") && match != null && !match.isEmpty()) return true;
		
		if (this.inputGene != null && this.inputGene.toGeneName().equals(match)) return true;
		
		return false;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return ingredient.getItem() == item;
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {		
		if (!isInput(input) || !isIngredient(ingredient)) return ItemStack.EMPTY;
		
		ItemStack result = output.copy();
		
		//this handles cell copying
		if (ingredient.hasTagCompound() && output.getItem() == GRItems.Cell) {
			result.setTagCompound(ingredient.getTagCompound());
			result.getTagCompound().removeTag("forceGene");
			result.getTagCompound().removeTag("chance");
			return result;
		}
		
		PotionType type = PotionUtils.getPotionFromItem(input);

		//this handles cell focus/mutation		
		if ((type == GRPotions.GROWTH_POTION || type == GRPotions.MUTATION_POTION) && output.getItem() == GRItems.Cell) {
			result.getTagCompound().setString("entityName", ModUtils.getTagCompound(input).getString("entityName"));
			result.getTagCompound().setString("entityCodeName", ModUtils.getTagCompound(input).getString("entityCodeName"));
		}
		
		//this transfers the cell info to the potion
		else if ((type == GRPotions.GROWTH_POTION || type == GRPotions.MUTATION_POTION) && ingredient.getItem() == GRItems.Cell && ingredient.hasTagCompound()) {
			ModUtils.getTagCompound(result).setString("entityName", ModUtils.getTagCompound(ingredient).getString("entityName"));
			ModUtils.getTagCompound(result).setString("entityCodeName", ModUtils.getTagCompound(ingredient).getString("entityCodeName"));
		}
		
		
		return result;
	}

}
