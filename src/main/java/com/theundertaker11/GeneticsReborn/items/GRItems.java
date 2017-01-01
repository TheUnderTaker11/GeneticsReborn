package com.theundertaker11.GeneticsReborn.items;

import com.theundertaker11.GeneticsReborn.render.IItemModelProvider;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GRItems {
	public static Item Debugger;
	
	public static Item GlassSyringe;
	public static Item MetalScraper;
	public static Item advancedScraper;
	public static Item OrganicMatter;
	public static Item Cell;
	public static Item Plasmid;
	public static Item DragonHealthCrystal;
	
	public static void init()
	{
		Debugger = register(new Debugger("Debugger"));
		GlassSyringe = register(new GlassSyringe("GlassSyringe"));
		MetalScraper = register(new DamageableItemBase("MetalScraper", 200, true));
		advancedScraper = register(new DamageableItemBase("advancedScraper", 50, true));
		OrganicMatter = register(new OrganicMatter("OrganicMatter"));
		Plasmid = register(new Plasmid("Plasmid"));
		DragonHealthCrystal = register(new DamageableItemBase("DragonHealthCrystal", 100, true));//TODO MAKE IT 1000
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
