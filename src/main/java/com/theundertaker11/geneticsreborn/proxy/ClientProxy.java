package com.theundertaker11.geneticsreborn.proxy;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.blocks.airdispersal.GRTileEntityAirDispersal;
import com.theundertaker11.geneticsreborn.blocks.airdispersal.TESRAirDispersal;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.potions.GREntityPotion;
import com.theundertaker11.geneticsreborn.render.RenderRegistry;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Reference.MODID + ":" + id, "inventory"));
	}
	
	@Override
	public void initPotionRender() {
		ItemColors itemcolors = Minecraft.getMinecraft().getItemColors();
		itemcolors.registerItemColorHandler(new IItemColor() {
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                return tintIndex > 0 ? -1 : PotionUtils.getColor(stack);
            }
        }, GRItems.GRPotion, GRItems.ViralPotion);				
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ResourceLocation GlassSyringeTextures = new ResourceLocation(Reference.MODID + ":" + "glasssyringefull");
		ResourceLocation MetalSyringeTextures = new ResourceLocation(Reference.MODID + ":" + "metalsyringefull");

		ModelBakery.registerItemVariants(GRItems.GlassSyringe, GlassSyringeTextures);
		ModelBakery.registerItemVariants(GRItems.MetalSyringe, MetalSyringeTextures);

		RenderRegistry.Render();

		TileEntityRendererDispatcher.instance.renderers.put(GRTileEntityAirDispersal.class, new TESRAirDispersal());
		
        RenderingRegistry.registerEntityRenderingHandler(GREntityPotion.class, manager -> new RenderSnowball<>(manager, GRItems.ViralPotion, Minecraft.getMinecraft().getRenderItem()));		
	}
	
	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
}
