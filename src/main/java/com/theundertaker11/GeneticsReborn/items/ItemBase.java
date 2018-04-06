package com.theundertaker11.geneticsreborn.items;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.render.IItemModelProvider;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item implements IItemModelProvider{
	protected String name;
	boolean hasEffct;
	public ItemBase(String name, boolean hasEffect){
		super();
		setRegistryName(name);
		this.name= name;
		this.hasEffct=hasEffect;
		setUnlocalizedName(name);
		setCreativeTab(GeneticsReborn.GRtab);
	}
	public ItemBase(String name){
		this(name, false);
	}
	@Override
	public void registerItemModel(Item item) {
		GeneticsReborn.proxy.registerItemRenderer(this, 0, name);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return this.hasEffct;
	}
}
