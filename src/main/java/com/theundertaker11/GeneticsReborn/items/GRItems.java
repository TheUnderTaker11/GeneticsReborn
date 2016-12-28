package com.theundertaker11.GeneticsReborn.items;

import com.theundertaker11.GeneticsReborn.render.IItemModelProvider;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GRItems {
	public static Item GlassSyringe;
	
	public static void init()
	{
		GlassSyringe = register(new GlassSyringe("GlassSyringe"));
	}
	
	private static <T extends Item> T register(T item) 
	{
		GameRegistry.register(item);
		
		if(item instanceof IItemModelProvider) {
			((IItemModelProvider)item).registerItemModel(item);
		}
		return item;
	}
}
