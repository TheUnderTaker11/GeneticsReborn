package com.theundertaker11.geneticsreborn.items;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.potions.GRItemPotion;
import com.theundertaker11.geneticsreborn.potions.ViralSplashPotion;
import com.theundertaker11.geneticsreborn.render.IItemModelProvider;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class GRItems {

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
	public static Item ViralPotion;
	public static Item GRPotion;
	
	public static void init() {
		GlassSyringe = register(new GlassSyringe("glasssyringe"));
		MetalSyringe = register(new MetalSyringe("metalsyringe"));
		MetalScraper = register(new DamageableItemBase("metalscraper", 200, true));
		AdvancedScraper = register(new DamageableItemBase("advancedscraper", 50, true));
		Overclocker = register(new ItemBase("overclocker").setMaxStackSize(1));
		OrganicMatter = register(new OrganicMatter("organicmatter"));
		Cell = register(new OrganicMatter("cell"));
		DNAHelix = register(new DNAHelix("dnahelix"));
		AntiPlasmid = register(new Plasmid("antiplasmid"));
		Plasmid = register(new Plasmid("plasmid"));
		AntiField = register(new AntiField("antifield"));
		
		GRPotion = register(new GRItemPotion("grpotion"));
		GeneticsReborn.proxy.registerItemRenderer(GRPotion, 0, "grpotion");
		ViralPotion = register(new ViralSplashPotion("viralpotion"));
		GeneticsReborn.proxy.registerItemRenderer(ViralPotion, 0, "viralpotion");
		
		DragonHealthCrystal = register(new DamageableItemBase("dragonhealthcrystal", 1000, true));
		
		
	}

	private static <T extends Item> T register(T item) {
		ForgeRegistries.ITEMS.register(item);

		if (item instanceof IItemModelProvider) {
			((IItemModelProvider) item).registerItemModel(item);
		}
		return item;
	}
}
