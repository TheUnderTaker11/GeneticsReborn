package com.theundertaker11.GeneticsReborn.items;

import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.render.IItemModelProvider;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item implements IItemModelProvider{
	protected String name;
	boolean hasEffct;
	public ItemBase(String name, boolean hasEffect){
		super();
		this.name= name;
		this.hasEffct=hasEffect;
		setUnlocalizedName(name);
		setCreativeTab(GeneticsReborn.GRtab);
		setRegistryName(name);
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
