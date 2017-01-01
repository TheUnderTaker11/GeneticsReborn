package com.theundertaker11.GeneticsReborn.crafting;

import com.theundertaker11.GeneticsReborn.items.GRItems;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CraftingManager {
	public static void RegisterRecipes(){
		//Items
		IRecipe MetalScraper = new ShapedOreRecipe(new ItemStack(GRItems.MetalScraper), new Object[] {"x  ", " xy", " yx", 'x', "stickWood", 'y', "ingotIron"});
		GameRegistry.addRecipe(MetalScraper);
		IRecipe advScraper = new ShapedOreRecipe(new ItemStack(GRItems.advancedScraper), new Object[] {"x  ", " x ", "  y", 'x', GRItems.MetalScraper, 'y', "gemDiamond"});
		GameRegistry.addRecipe(advScraper);
		
		
	}
}
