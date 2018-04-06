package com.theundertaker11.geneticsreborn.items;

import com.theundertaker11.geneticsreborn.render.IItemModelProvider;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GRItems {
	public static Item Debugger;

	public static Item AdvancedScraper;
	public static Item AntiField;
	public static Item AntiPlasmid;
	public static Item Cell;
	public static Item DNAHelix;
	public static Item DragonHealthCrystal;
	public static Item GlassSyringe;
	public static Item MetalScraper;
	public static Item MetalSyringe;
	public static Item OrganicMatter;
	public static Item Overclocker;
	public static Item Plasmid;
	
	
	public static void init()
	{
		Debugger = register(new Debugger("Debugger"));
		GlassSyringe = register(new GlassSyringe("GlassSyringe"));
		MetalSyringe = register(new MetalSyringe("MetalSyringe"));
		MetalScraper = register(new DamageableItemBase("MetalScraper", 200, true));
		AdvancedScraper = register(new DamageableItemBase("AdvancedScraper", 50, true));
		Overclocker = register(new ItemBase("Overclocker"));
		OrganicMatter = register(new OrganicMatter("OrganicMatter"));
		Cell = register(new OrganicMatter("Cell"));
		DNAHelix = register(new DNAHelix("DNAHelix"));
		AntiPlasmid = register(new Plasmid("AntiPlasmid"));
		Plasmid = register(new Plasmid("Plasmid"));
		AntiField = register(new AntiField("AntiField"));
		
		DragonHealthCrystal = register(new DamageableItemBase("DragonHealthCrystal", 1000, true));
	}
	
	private static <T extends Item> T register(T item) 
	{
		GameRegistry.register(item);
		
		if(item instanceof IItemModelProvider){
			((IItemModelProvider)item).registerItemModel(item);
		}
		return item;
	}
}
