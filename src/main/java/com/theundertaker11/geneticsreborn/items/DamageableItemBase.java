package com.theundertaker11.geneticsreborn.items;

public class DamageableItemBase extends ItemBase {

	public DamageableItemBase(String name, int maxdamage, boolean full3d) {
		super(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxdamage);
		this.bFull3D = full3d;
	}
}
