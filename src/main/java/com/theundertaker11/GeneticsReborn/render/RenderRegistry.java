package com.theundertaker11.geneticsreborn.render;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.items.GRItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;

public class RenderRegistry {
	
	//This is for if an item or block doesn't extend My item/block base, or need special renders for metadata
	public static void Render(){
		//If you register a new texture for the same item you have to make another thing in ClientProxy!
		regWithMetaAndName(GRItems.GlassSyringe, 1, "glasssyringefull");
		regWithMetaAndName(GRItems.MetalSyringe, 1, "metalsyringefull");
		
		regWithMeta(GRItems.AntiField, 1);
	}

	public static void reg(Item item) {
		ModelResourceLocation res = new ModelResourceLocation(item.getRegistryName().toString(), "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, res);
	}

	public static void regWithMeta(Item item, int meta) {
		ModelResourceLocation res = new ModelResourceLocation(Reference.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, res);
	}
	
	public static void regWithMetaAndName(Item item, int meta, String name) {
		ModelResourceLocation res = new ModelResourceLocation(Reference.MODID + ":" + name, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, res);
	}
}
