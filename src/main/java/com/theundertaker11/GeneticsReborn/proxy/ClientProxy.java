package com.theundertaker11.GeneticsReborn.proxy;

import com.theundertaker11.GeneticsReborn.Reference;
import com.theundertaker11.GeneticsReborn.render.RenderRegistry;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemRenderer(Item item, int meta, String id) 
	{
		 ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Reference.MODID + ":" + id, "inventory"));
	}
	@Override
	public void registerRenders()
	{
		RenderRegistry.Render();
	}

}
