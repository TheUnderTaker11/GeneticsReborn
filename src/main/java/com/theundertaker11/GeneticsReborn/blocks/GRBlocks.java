package com.theundertaker11.GeneticsReborn.blocks;

import com.theundertaker11.GeneticsReborn.blocks.cellanalyser.CellAnalyser;
import com.theundertaker11.GeneticsReborn.render.IItemModelProvider;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GRBlocks {
	public static Block CellAnalyser;
	
	public static void init() 
	{
		CellAnalyser = register(new CellAnalyser("CellAnalyser"));
	}
	private static <T extends Block> T register (T block, ItemBlock itemBlock)
	{
		 GameRegistry.register(block);
		 if(itemBlock != null)
		 {
		 GameRegistry.register(itemBlock);
		 }
		 
		 if(block instanceof IItemModelProvider)
		 {
		 ((IItemModelProvider)block).registerItemModel(itemBlock);
		 }
		 
		 return block;
	}
		 
	private static <T extends Block> T register(T block)
	{
		 ItemBlock itemBlock = new ItemBlock(block);
		 itemBlock.setRegistryName(block.getRegistryName());
		 return register(block, itemBlock);
	}
}
