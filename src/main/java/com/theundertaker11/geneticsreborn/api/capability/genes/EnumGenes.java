package com.theundertaker11.geneticsreborn.api.capability.genes;

public enum EnumGenes {
	DRAGONS_BREATH("Dragon's Breath"),
	EAT_GRASS("Eat Grass"),
	EMERALD_HEART("Emerald Heart"),
	ENDER_DRAGON_HEALTH("Ender Dragon Health"),
	EXPLOSIVE_EXIT("Explosive Exit"),
	FIRE_PROOF("Fire Proof"),
	FLY("Flight"),
	ITEM_MAGNET("Item Magnet"),
	JUMP_BOOST("Jump Boost"),
	MILKY("Milky"),
	MORE_HEARTS("More Hearts"),
	NIGHT_VISION("Night Vision"),
	NO_FALL_DAMAGE("No Fall Damage"),
	PHOTOSYNTHESIS("Photosynthesis"),
	POISON_PROOF("Poison Immunity"),
	RESISTANCE("Resistance"),
	SAVE_INVENTORY("Save Inventory"),
	SCARE_CREEPERS("Scare Creepers"),
	SHOOT_FIREBALLS("Shoot Fireballs"),
	SLIMY("Slimy"),
	SPEED("Speed"),
	STRENGTH("Strength"),
	TELEPORTER("Teleport"),
	WATER_BREATHING("Water Breathing"),
	WOOLY("Wooly"),
	WITHER_HIT("Wither Hit"),
	WITHER_PROOF("Wither Proof"),
	XP_MAGNET("XP Magmet"),
	STEP_ASSIST("Step Assit"),
	INFINITY("Infinity"),
	BIOLUMIN("Bioluminescence"),
	RESPAWN("Cheat Death"),
	CYBERNETIC("Cybernetic"),
	LAY_EGG("Lay Eggs");
	
	private EnumGenes(String d) {
		desc = d;
	}
	private String desc;
	private boolean active;

	public final String getDescrption() {
		return desc;	
	}
	
	public final String toGeneName() {
		return "GeneticsReborn" + this.name();	
	}
	
	public static final EnumGenes fromGeneName(String n) {
		if (n.startsWith("GeneticsReborn")) return EnumGenes.valueOf(n.substring(14).toUpperCase());
		return null;
	}
	
	public final void setActive(boolean b) {
		this.active = b;
	}
	
	public final boolean isActive() {
		return this.active;
	}
}