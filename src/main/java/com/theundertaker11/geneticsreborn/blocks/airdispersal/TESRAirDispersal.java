package com.theundertaker11.geneticsreborn.blocks.airdispersal;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRAirDispersal extends TileEntitySpecialRenderer<GRTileEntityAirDispersal> {
	@Override
	public void render(GRTileEntityAirDispersal te, double x, double y, double z, float partialTicks, int destroyStage,	float alpha) {
		//renderTileEntityFast(te, x, y, z, partialTicks, destroyStage, );
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void renderTileEntityFast(GRTileEntityAirDispersal te, double x, double y, double z, float partialTicks,	int destroyStage, float partial, BufferBuilder buffer) {
		ItemStack mask = te.maskBlock();

		BlockPos pos = new BlockPos(x,y,z);
		BlockRendererDispatcher br = Minecraft.getMinecraft().getBlockRendererDispatcher();
		IBlockAccess world = MinecraftForgeClient.getRegionRenderCache(te.getWorld(), pos);
		if (mask != ItemStack.EMPTY) {
			IBlockState newstate = Block.getBlockFromItem(mask.getItem()).getStateFromMeta(mask.getMetadata());
			newstate.getActualState(world, pos);
	        IBakedModel model = br.getBlockModelShapes().getModelForState(newstate);
	        buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
	        br.getBlockModelRenderer().renderModel(world, model, newstate, pos, buffer, false);
		}
	}	
	
	
}
