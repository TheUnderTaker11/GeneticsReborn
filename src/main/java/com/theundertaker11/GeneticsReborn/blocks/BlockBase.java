package com.theundertaker11.GeneticsReborn.blocks;

import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.render.IItemModelProvider;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockBase extends Block implements IItemModelProvider{
	protected String Name;
	
	public BlockBase(String name, Material material, float hardness, float resistance) {
        super(material);
        this.Name=name;
        setUnlocalizedName(name);
        setCreativeTab(GeneticsReborn.GRtab);
        setHardness(hardness);
        setResistance(resistance);
        setHarvestLevel("axe", 0);
        setRegistryName(name);
    }

    public BlockBase(String name) {
        this(name, Material.ROCK, 0.5f, 0.5f);
    }

	@Override
	public void registerItemModel(Item itemBlock) {
		GeneticsReborn.proxy.registerItemRenderer(itemBlock, 0, Name);
	}
}
