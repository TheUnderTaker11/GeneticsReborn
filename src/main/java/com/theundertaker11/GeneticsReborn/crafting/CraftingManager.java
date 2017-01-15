package com.theundertaker11.GeneticsReborn.crafting;

import com.theundertaker11.GeneticsReborn.blocks.GRBlocks;
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
		
		IRecipe MetalSyringe = new ShapedOreRecipe(new ItemStack(GRItems.MetalSyringe), new Object[]{
				"xzx",
				"xax",
				"xyx", 'x', "ingotIron", 'a', GRItems.GlassSyringe, 'y', Blocks.OBSIDIAN, 'z', "gemDiamond"});
		GameRegistry.addRecipe(MetalSyringe);
		
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
		
		IRecipe AntiPlasmid = new ShapedOreRecipe(new ItemStack(GRItems.AntiPlasmid), new Object[] {
				"zzz",
				"zxz",
				"zzz", 'z', GRItems.DNAHelix, 'x', Items.FERMENTED_SPIDER_EYE});
		GameRegistry.addRecipe(AntiPlasmid);
		
		IRecipe DragonHealthCrystal = new ShapedOreRecipe(new ItemStack(GRItems.DragonHealthCrystal), new Object[] {
				"xzx",
				"zxz",
				"xzx", 'z', Items.END_CRYSTAL, 'x', "gemDiamond"});
		GameRegistry.addRecipe(DragonHealthCrystal);
		
		GameRegistry.addRecipe(new AntiPlasmidCrafting());
//////////////////////Start blocks//////////////////////////////////////////////////////////////////////////////////////////
		
		IRecipe CellAnalyser = new ShapedOreRecipe(new ItemStack(GRBlocks.CellAnalyser), new Object[] {
				"zzz",
				"zxz",
				"zyz",'x',"paneGlass",'z', "ingotIron", 'y', "dustRedstone"});
		GameRegistry.addRecipe(CellAnalyser);
		
		IRecipe DNAExtractor = new ShapedOreRecipe(new ItemStack(GRBlocks.DNAExtractor), new Object[] {
				"zzz",
				"yxy",
				"zzz",'x',GRItems.Cell,'z', "ingotIron", 'y', Blocks.STICKY_PISTON});
		GameRegistry.addRecipe(DNAExtractor);
		
		IRecipe DNADecrypter = new ShapedOreRecipe(new ItemStack(GRBlocks.DNADecrypter), new Object[] {
				"zaz",
				"yxy",
				"zaz",'x',"ingotGold",'z', "ingotIron", 'y', GRItems.DNAHelix, 'a', "blockGlass"});
		GameRegistry.addRecipe(DNADecrypter);
		
		IRecipe PlasmidInfuser = new ShapedOreRecipe(new ItemStack(GRBlocks.PlasmidInfuser), new Object[] {
				"zzz",
				"yxy",
				"zaz",'x',Blocks.PISTON,'z', "ingotIron", 'y', GRItems.Plasmid, 'a', "gemDiamond"});
		GameRegistry.addRecipe(PlasmidInfuser);
		
		IRecipe PlasmidInjector = new ShapedOreRecipe(new ItemStack(GRBlocks.PlasmidInjector), new Object[] {
				"zyz",
				"yxy",
				"zyz",'x',Items.BUCKET,'z', "ingotIron", 'y', GRItems.GlassSyringe});
		GameRegistry.addRecipe(PlasmidInjector);
		
		IRecipe BloodPurifier = new ShapedOreRecipe(new ItemStack(GRBlocks.BloodPurifier), new Object[] {
				"zaz",
				"yxy",
				"zaz",'x',Items.BUCKET,'z', "ingotIron", 'y', GRItems.GlassSyringe, 'a', Blocks.WOOL});
		GameRegistry.addRecipe(BloodPurifier);
	}
}
