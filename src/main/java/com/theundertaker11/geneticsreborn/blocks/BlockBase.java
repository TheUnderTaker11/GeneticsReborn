package com.theundertaker11.geneticsreborn.blocks;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.render.IItemModelProvider;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockBase extends Block implements IItemModelProvider {
    protected String Name;

    public BlockBase(String name, Material material, float hardness, float resistance) {
        super(material);
        setRegistryName(name);
        this.Name = name;
        setUnlocalizedName(name);
        setCreativeTab(GeneticsReborn.GRtab);
        setHardness(hardness);
        setResistance(resistance);
        setHarvestLevel("pickaxe", 0);
    }

    public BlockBase(String name) {
        this(name, Material.IRON, 0.5f, 0.5f);
    }

    @Override
    public void registerItemModel(Item itemBlock) {
        GeneticsReborn.proxy.registerItemRenderer(itemBlock, 0, Name);
    }
}
