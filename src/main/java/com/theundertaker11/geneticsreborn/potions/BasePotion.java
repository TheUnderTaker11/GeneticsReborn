package com.theundertaker11.geneticsreborn.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BasePotion extends Potion {

	protected BasePotion(String name, boolean isBad, int color) 	{
		super(isBad, color);
		setRegistryName(name);
		setPotionName("effect." + name);
	}
	
	@Override
	public boolean hasStatusIcon() {
		return false;
	}
	
	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		super.renderInventoryEffect(x, y, effect, mc);
	}	

}
