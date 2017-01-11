package com.theundertaker11.GeneticsReborn.crafting;

import com.theundertaker11.GeneticsReborn.items.GRItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CraftingManager {
	public static void RegisterRecipes(){
		//Items
		IRecipe MetalScraper = new ShapedOreRecipe(new ItemStack(GRItems.MetalScraper), new Object[]{
				" yx",
				" xy",
				"x  ", 'x', "stickWood", 'y', "ingotIron"});
		GameRegistry.addRecipe(MetalScraper);
		
		IRecipe AdvScraper = new ShapedOreRecipe(new ItemStack(GRItems.AdvancedScraper), new Object[]{
				"  y",
				" x ",
				"x  ", 'x', GRItems.MetalScraper, 'y', "gemDiamond"});
		GameRegistry.addRecipe(AdvScraper);
		
		IRecipe GlassSyringe = new ShapedOreRecipe(new ItemStack(GRItems.GlassSyringe), new Object[]{
				" z ",
				"xax",
				"xyx", 'x', "blockGlass", 'a', Items.GLASS_BOTTLE, 'y', Items.ARROW, 'z', Blocks.PISTON});
		GameRegistry.addRecipe(GlassSyringe);
		
		IRecipe Overclocker = new ShapedOreRecipe(new ItemStack(GRItems.Overclocker), new Object[] {
				"zyz",
				"yxy",
				"zyz", 'x', Items.CLOCK, 'y', "gemLapis", 'z', GRItems.Cell});
		GameRegistry.addRecipe(Overclocker);
		
		IRecipe Plasmid = new ShapedOreRecipe(new ItemStack(GRItems.Plasmid), new Object[] {
				"zzz",
				"z z",
				"zzz", 'z', GRItems.DNAHelix});
		GameRegistry.addRecipe(Plasmid);
		
		GameRegistry.addRecipe(new AntiPlasmidCrafting());
	}
}
