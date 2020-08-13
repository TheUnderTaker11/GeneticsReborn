package com.theundertaker11.geneticsreborn.potions;

import java.util.Arrays;
import java.util.List;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.Genes;
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
		
		if (pot != null && pot != PotionTypes.EMPTY && type == pot) {
			String match = null;
			if (input.getItem() == GRItems.Cell && input.hasTagCompound()) {
				match = input.getTagCompound().getString("entityName");
			} else if (input.getItem() == GRItems.DNAHelix && input.hasTagCompound()) {
				match = input.getTagCompound().getString("gene");
			} else if (type != PotionTypes.EMPTY && input.hasTagCompound()) {
				match = input.getTagCompound().getString("gene");
				if (match.isEmpty()) match = input.getTagCompound().getString("entityCodeName");
			}

			if (type == GRPotions.SUBSTRATE) 
				return true;

			if (this.inputGene != null) {
				if ((type == GRPotions.VIRAL_POTION) && (!input.hasTagCompound() || (input.hasTagCompound() && !input.getTagCompound().hasKey("gene"))))
					return true;

				if (this.inputGene.toGeneName().equals(match)) return true;
				else return false;
			}
			
			//this is for imprinting potions with cell/helix info
			if (this.cellType == null) {
				if ((type == GRPotions.GROWTH_POTION) && (input.hasTagCompound() && !input.getTagCompound().hasKey("entityCodeName")))
					return true;
				if ((type == GRPotions.MUTATION_POTION) && (input.hasTagCompound() && !input.getTagCompound().hasKey("entityCodeName")))
					return true;
				if (type == PotionTypes.MUNDANE) 
					return true;
			} else {
				if (type == GRPotions.VIRAL_POTION) return true;
				if (this.cellType.equals(match)) return true;
				if (this.cellType.equals("*") && match != null && !match.isEmpty()) return true;				
			}
			
		}
		return false;
	}

	private static final EnumGenes[] REQUIRED_GENES = {
			EnumGenes.CURSED, EnumGenes.POISON_4, EnumGenes.WITHER, EnumGenes.WEAKNESS, 
			EnumGenes.BLINDNESS, EnumGenes.SLOWNESS_6, EnumGenes.NAUSEA, EnumGenes.HUNGER, EnumGenes.FLAME, 
			EnumGenes.MINING_WEAKNESS, EnumGenes.LEVITATION, EnumGenes.DEAD_CREEPERS, 
			EnumGenes.DEAD_UNDEAD, EnumGenes.DEAD_HOSTILE, EnumGenes.DEAD_OLD_AGE};
	@SuppressWarnings("unchecked")
	@Override
	public boolean isIngredient(ItemStack ingredient) {
		if (ingredient.getItem() == GRItems.GlassSyringe) {
			//special case - Black Death
			List<EnumGenes> required = Arrays.asList(REQUIRED_GENES);
			int found = 0;
			NBTTagCompound tag = ingredient.getTagCompound();
			for (int i = 0; i < Genes.TotalNumberOfGenes; i++) {
				String nbtname = "";
				if (tag.hasKey(Integer.toString(i))) 
					nbtname = tag.getString(Integer.toString(i));
				EnumGenes gene = EnumGenes.fromGeneName(nbtname);
				if (required.contains(gene)) found++;
			}
			return found == required.size();
		} else if (inputGene != null) {
			NBTTagCompound tag = ingredient.getTagCompound();
			return (tag != null && inputGene.toGeneName().equals(tag.getString("gene")));
		} else if (cellType != null) {
			if (ingredient.getItem() == GRItems.DNAHelix) {
				return ingredient.getTagCompound().getString("gene").equals(cellType);
			}
		} 
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
