package com.theundertaker11.geneticsreborn.api.capability.genes;

import com.theundertaker11.geneticsreborn.GeneticsReborn;

public enum EnumGenes {	
	//mutations, must be first
	HASTE_2("Haste II", null, true),
	EFFICIENCY_4("Efficiency IV", null, true), 
	REGENERATION_4("Regeneration IV", null, true), 
	SPEED_4("Speed IV", null, true), 
	SPEED_2("Speed II", SPEED_4, true), 
	RESISTANCE_2("Resistance II", null, true), 
	STRENGTH_2("Strength II", null, true), 
	MEATY_2("Meaty II", null, true), 
	MORE_HEARTS_2("More Hearts II", null, true), 
	INVISIBLE("Invisbility", null, true), 
	FLY("Flight", null, true), 
	LUCK("Luck", null, true), 
	SCARE_ZOMBIES("Scare Zombies", null, true),
	SCARE_SPIDERS("Scare Spiders", null, true),
	THORNS("Thorns", null, true),
	CLAWS_2("Claws II", null, true),
	
	//standard list
	DRAGONS_BREATH("Dragon's Breath"),
	EAT_GRASS("Eat Grass"),
	EMERALD_HEART("Emerald Heart"),
	ENDER_DRAGON_HEALTH("Ender Dragon Health"),
	EXPLOSIVE_EXIT("Explosive Exit"),
	FIRE_PROOF("Fire Proof"),
	ITEM_MAGNET("Item Magnet"),
	JUMP_BOOST("Jump Boost", FLY),
	MILKY("Milky"),
	MORE_HEARTS("More Hearts", MORE_HEARTS_2),
	NIGHT_VISION("Night Vision"),
	NO_FALL_DAMAGE("No Fall Damage"),
	PHOTOSYNTHESIS("Photosynthesis", THORNS),
	POISON_PROOF("Poison Immunity"),
	RESISTANCE("Resistance", RESISTANCE_2),
	SAVE_INVENTORY("Save Inventory"),
	SCARE_CREEPERS("Scare Creepers", SCARE_ZOMBIES),
	SCARE_SKELETONS("Scare Skeletons", SCARE_SPIDERS),
	SHOOT_FIREBALLS("Shoot Fireballs"),
	SLIMY("Slimy Death"), 
	SPEED("Speed", SPEED_2),
	STRENGTH("Strength", STRENGTH_2),
	TELEPORTER("Teleport", FLY),
	WATER_BREATHING("Water Breathing"),
	WOOLY("Wooly"),
	WITHER_HIT("Wither Hit"),
	WITHER_PROOF("Wither Proof"),
	XP_MAGNET("XP Magmet"),
	STEP_ASSIST("Step Assit"),
	INFINITY("Infinity"),
	BIOLUMIN("Bioluminescence"),
	CYBERNETIC("Cybernetic"),
	LAY_EGG("Lay Eggs"), 
	MEATY("Meaty", MEATY_2), 
	NO_HUNGER("No Hunger"), 
	CLAWS("Claws", CLAWS_2), 
	HASTE("Haste", HASTE_2),
	EFFICIENCY("Efficiency", EFFICIENCY_4),
	CLIMB_WALLS("Climb Walls"),
	MOB_SIGHT("Mob Sight"),
	REGENERATION("Regeneration", REGENERATION_4),
	
	//negative effects
	POISON("Poison II"),
	POISON_4("Poison IV"),
	WITHER("Wither II"),
	WEAKNESS("Weakness"),
	BLINDNESS("Blindness"),
	SLOWNESS("Slowness"),
	SLOWNESS_4("Slowness IV"),
	SLOWNESS_6("Slowness VI"),
	NAUSEA("Nausea"),
	HUNGER("Hunger"),
	FLAME("Flambe"),
	CURSED("Cursed"),
	LEVITATION("Levitation"),
	MINING_WEAKNESS("Mining Weakness"),
	DEAD_CREEPERS("Green Death"),
	DEAD_UNDEAD("Un-Death"),
	DEAD_OLD_AGE("Gray Death"),
	DEAD_HOSTILE("White Death"),
	DEAD_ALL("Black Death"),
	REALLY_DEAD_ALL("Void Death")
	; 
	
	static {
		FLY.mutateTo = FLY;
		INVISIBLE.mutateTo = INVISIBLE;
		LUCK.mutateTo = LUCK;
	}
	
	private EnumGenes(String d) {
		this(d, null, false);
	}
	
	private EnumGenes(String d, EnumGenes m) {
		this(d, m, false);
	}

	private EnumGenes(String d, EnumGenes m, boolean isMutation) {
		desc = d;
		mutateTo = m;
		mutation = isMutation;
	}
	
	public boolean isNegative() {
		switch(this) {
		case POISON:
		case POISON_4:
		case WITHER:
		case WEAKNESS:
		case BLINDNESS:
		case SLOWNESS:
		case SLOWNESS_4:
		case SLOWNESS_6:
		case NAUSEA:
		case HUNGER:
		case FLAME:
		case CURSED:
		case LEVITATION:
		case MINING_WEAKNESS:
		case DEAD_CREEPERS:
		case DEAD_UNDEAD:
		case DEAD_HOSTILE:
		case DEAD_OLD_AGE:
		case DEAD_ALL:
		case REALLY_DEAD_ALL:
			return true;
		default:
			return false;
		}
	}

	private String desc;
	private EnumGenes mutateTo;
	private boolean active = true;
	private boolean mutation;

	public final String getDescription() {
		return desc;	
	}
	
	public final boolean isMutation() {
		return mutation;	
	}
	
	public final EnumGenes getMutation() {
		if (mutateTo == null) return this;
		return mutateTo;
	}
	
	public final String toGeneName() {
		return "GeneticsReborn" + this.name();	
	}
	
	public static final EnumGenes fromGeneName(String n) {
		try {
			if (n.startsWith("GeneticsReborn")) return EnumGenes.valueOf(n.substring(14).toUpperCase());
			return EnumGenes.valueOf(n.toUpperCase());			
		} catch (IllegalArgumentException ignore) { }
		return null;
	}
	
	public final void setActive(boolean b) {
		this.active = b;
	}
	
	public final boolean isActive() {
		return this.active;
	}
	
	public final boolean canAddMutation(IGenes genes, IGenes syringeGenes) {
		switch (this) {
		case HASTE_2: return genes.hasGene(HASTE) || syringeGenes.hasGene(HASTE);
		case EFFICIENCY_4: return genes.hasGene(EFFICIENCY) || syringeGenes.hasGene(EFFICIENCY);
		case REGENERATION_4: return genes.hasGene(REGENERATION) || syringeGenes.hasGene(REGENERATION);
		case SPEED_4 : return genes.hasGene(SPEED_2) || syringeGenes.hasGene(SPEED_2);
		case SPEED_2 : return genes.hasGene(SPEED) || syringeGenes.hasGene(SPEED);
		case RESISTANCE_2: return genes.hasGene(RESISTANCE) || syringeGenes.hasGene(RESISTANCE);
		case STRENGTH_2 : return genes.hasGene(STRENGTH) || syringeGenes.hasGene(STRENGTH);
		case MEATY_2 : return genes.hasGene(MEATY) || syringeGenes.hasGene(MEATY);
		case MORE_HEARTS_2: return genes.hasGene(MORE_HEARTS) || syringeGenes.hasGene(MORE_HEARTS);
		case INVISIBLE : return true;
		case FLY : return genes.hasGene(JUMP_BOOST) || genes.hasGene(TELEPORTER) || syringeGenes.hasGene(JUMP_BOOST) || syringeGenes.hasGene(TELEPORTER);
		case LUCK : return true;
		case SCARE_ZOMBIES: return genes.hasGene(SCARE_CREEPERS) || syringeGenes.hasGene(SCARE_CREEPERS);
		case SCARE_SPIDERS: return genes.hasGene(SCARE_SKELETONS) || syringeGenes.hasGene(SCARE_SKELETONS);
		case THORNS: return genes.hasGene(PHOTOSYNTHESIS) || syringeGenes.hasGene(PHOTOSYNTHESIS);
		case CLAWS_2: return genes.hasGene(CLAWS) || syringeGenes.hasGene(CLAWS);
		default: return true;
		}
	}

	public static final int getNumberNeeded(EnumGenes gene) {
		if (gene.isMutation()) return 50;
		
		if (!GeneticsReborn.hardMode) 
			return 24;
		else 
			switch (gene) {
			case STEP_ASSIST:
			case JUMP_BOOST:
				return 10;
			case MILKY:
			case WOOLY:
			case MEATY:
			case LAY_EGG:
			case THORNS:
				return 12;
			case EAT_GRASS:
			case NIGHT_VISION:
			case MOB_SIGHT:
			case WATER_BREATHING:
			case BIOLUMIN:
				return 16;
			case DRAGONS_BREATH:
			case SCARE_CREEPERS:
			case SCARE_SKELETONS:
			case WITHER_HIT:
			case SPEED: 
			case CLAWS:
			case STRENGTH:
			case EXPLOSIVE_EXIT:
				return 20;
			case FIRE_PROOF:
			case POISON_PROOF:
			case SHOOT_FIREBALLS:
			case TELEPORTER:
				return 24;
			case EMERALD_HEART:
			case NO_FALL_DAMAGE:
			case NO_HUNGER:
			case RESISTANCE:
			case XP_MAGNET:
			case ITEM_MAGNET:
			case INFINITY:
			case CYBERNETIC:
				return 30;
			case WITHER_PROOF:
			case MORE_HEARTS:
			case CLIMB_WALLS:
			case SAVE_INVENTORY:
			case PHOTOSYNTHESIS:
				return 40;
			case REGENERATION:
			case ENDER_DRAGON_HEALTH:
			case SLIMY:
				return 60;
			default: 
				return 22;			
			}		
	}	
}