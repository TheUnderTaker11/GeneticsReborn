package com.theundertaker11.geneticsreborn.crafting;

import com.theundertaker11.geneticsreborn.blocks.GRBlocks;
import com.theundertaker11.geneticsreborn.items.GRItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CraftingManager {

	public static void RegisterRecipes() {
		GameRegistry.addShapedRecipe(GRBlocks.CoalGen.getRegistryName(), null, new ItemStack(GRBlocks.CoalGen), new Object[]{
				"zzz",
				"xax",
				"xyx", 'x', "ingotIron", 'a', Blocks.FURNACE, 'y', "dustRedstone", 'z', Blocks.GLASS});

		GameRegistry.addShapedRecipe(GRItems.MetalScraper.getRegistryName(), null, new ItemStack(GRItems.MetalScraper), new Object[]{
				" yx",
				" xy",
				"x  ", 'x', "stickWood", 'y', "ingotIron"});

		GameRegistry.addShapedRecipe(GRItems.AdvancedScraper.getRegistryName(), null, new ItemStack(GRItems.AdvancedScraper), new Object[]{
				"  y",
				" x ",
				"x  ", 'x', GRItems.MetalScraper, 'y', "gemDiamond"});

		GameRegistry.addShapedRecipe(GRItems.GlassSyringe.getRegistryName(), null, new ItemStack(GRItems.GlassSyringe), new Object[]{
				" z ",
				"xax",
				"xyx", 'x', "blockGlass", 'a', Items.GLASS_BOTTLE, 'y', Items.ARROW, 'z', Blocks.PISTON});

		GameRegistry.addShapedRecipe(GRItems.MetalSyringe.getRegistryName(), null, new ItemStack(GRItems.MetalSyringe), new Object[]{
				"xzx",
				"xax",
				"xyx", 'x', "ingotIron", 'a', GRItems.GlassSyringe, 'y', Blocks.OBSIDIAN, 'z', "gemDiamond"});

		GameRegistry.addShapedRecipe(GRItems.Overclocker.getRegistryName(), null, new ItemStack(GRItems.Overclocker), new Object[]{
				"zyz",
				"yxy",
				"zyz", 'x', Items.CLOCK, 'y', "gemLapis", 'z', GRItems.Cell});

		GameRegistry.addShapedRecipe(GRItems.Plasmid.getRegistryName(), null, new ItemStack(GRItems.Plasmid), new Object[]{
				"zzz",
				"z z",
				"zzz", 'z', GRItems.DNAHelix});

		GameRegistry.addShapedRecipe(GRItems.AntiPlasmid.getRegistryName(), null, new ItemStack(GRItems.AntiPlasmid), new Object[]{
				"zzz",
				"zxz",
				"zzz", 'z', GRItems.DNAHelix, 'x', Items.FERMENTED_SPIDER_EYE});

		GameRegistry.addShapedRecipe(GRItems.DragonHealthCrystal.getRegistryName(), null, new ItemStack(GRItems.DragonHealthCrystal), new Object[]{
				"xzx",
				"zxz",
				"xzx", 'z', Items.END_CRYSTAL, 'x', "gemDiamond"});

		GameRegistry.addShapedRecipe(GRItems.AntiField.getRegistryName(), null, new ItemStack(GRItems.AntiField), new Object[]{
				"xzx",
				"zyz",
				"xzx", 'y', Items.FERMENTED_SPIDER_EYE, 'x', Items.ENDER_PEARL, 'z', "blockGlass"});

		ForgeRegistries.RECIPES.register(new AntiPlasmidCrafting());

		GameRegistry.addShapedRecipe(GRBlocks.CellAnalyser.getRegistryName(), null, new ItemStack(GRBlocks.CellAnalyser), new Object[]{
				"zzz",
				"zxz",
				"zyz", 'x', "paneGlass", 'z', "ingotIron", 'y', "dustRedstone"});

		GameRegistry.addShapedRecipe(GRBlocks.DNAExtractor.getRegistryName(), null, new ItemStack(GRBlocks.DNAExtractor), new Object[]{
				"zzz",
				"yxy",
				"zzz", 'x', GRItems.Cell, 'z', "ingotIron", 'y', Blocks.STICKY_PISTON});

		GameRegistry.addShapedRecipe(GRBlocks.DNADecrypter.getRegistryName(), null, new ItemStack(GRBlocks.DNADecrypter), new Object[]{
				"zaz",
				"yxy",
				"zaz", 'x', "ingotGold", 'z', "ingotIron", 'y', GRItems.DNAHelix, 'a', "blockGlass"});

		GameRegistry.addShapedRecipe(GRBlocks.PlasmidInfuser.getRegistryName(), null, new ItemStack(GRBlocks.PlasmidInfuser), new Object[]{
				"zzz",
				"yxy",
				"zaz", 'x', Blocks.PISTON, 'z', "ingotIron", 'y', GRItems.Plasmid, 'a', "gemDiamond"});

		GameRegistry.addShapedRecipe(GRBlocks.PlasmidInjector.getRegistryName(), null, new ItemStack(GRBlocks.PlasmidInjector), new Object[]{
				"zyz",
				"yxy",
				"zyz", 'x', Items.BUCKET, 'z', "ingotIron", 'y', GRItems.GlassSyringe});

		GameRegistry.addShapedRecipe(GRBlocks.BloodPurifier.getRegistryName(), null, new ItemStack(GRBlocks.BloodPurifier), new Object[]{
				"zaz",
				"yxy",
				"zaz", 'x', Items.BUCKET, 'z', "ingotIron", 'y', GRItems.GlassSyringe, 'a', Blocks.WOOL});

		GameRegistry.addShapedRecipe(GRBlocks.CloningMachine.getRegistryName(), null, new ItemStack(GRBlocks.CloningMachine), new Object[]{
				"yyy",
				"zxz",
				"yyy", 'x', Items.NETHER_STAR, 'z', "gemDiamond", 'y', Blocks.GOLD_BLOCK});

		GameRegistry.addShapedRecipe(GRBlocks.AntiFieldBlock.getRegistryName(), null, new ItemStack(GRBlocks.AntiFieldBlock), new Object[]{
				"yyy",
				"yxy",
				"yyy", 'x', Items.ENDER_PEARL, 'y', "ingotIron"});
	}
}
