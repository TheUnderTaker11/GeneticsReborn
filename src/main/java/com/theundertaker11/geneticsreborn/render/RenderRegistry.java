package com.theundertaker11.geneticsreborn.render;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.items.GRItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class RenderRegistry {

	public static void Render() {
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
