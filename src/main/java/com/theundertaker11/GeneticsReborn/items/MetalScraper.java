package com.theundertaker11.GeneticsReborn.items;

import net.minecraft.item.Item;

public class MetalScraper extends ItemBase {

	public MetalScraper(String name, int maxdamage) {
		super(name);
		this.maxStackSize=1;
		this.setMaxDamage(maxdamage);
		this.bFull3D=true;
	}
	
	
}
