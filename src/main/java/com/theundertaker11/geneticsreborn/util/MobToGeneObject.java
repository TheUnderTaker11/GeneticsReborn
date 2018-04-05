package com.theundertaker11.geneticsreborn.util;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;

public class MobToGeneObject {

	public final String MobCodeName;
	public final EnumGenes Gene1;
	public final EnumGenes Gene2;
	public final EnumGenes Gene3;
	private int ValidGenes;

	/**
	 * This is just an object I use to store a few things for registering mobs
	 *
	 * @param gene1
	 * @param gene2
	 * @param gene3
	 */
	public MobToGeneObject(String mobCodeName, EnumGenes gene1, EnumGenes gene2, EnumGenes gene3) {
		MobCodeName = mobCodeName;
		Gene1 = gene1;
		Gene2 = gene2;
		Gene3 = gene3;
		if (Gene1 != null) ValidGenes++;
		if (Gene2 != null) ValidGenes++;
		if (Gene3 != null) ValidGenes++;
	}

	public MobToGeneObject(String mobCodeName, EnumGenes gene1, EnumGenes gene2) {
		this(mobCodeName, gene1, gene2, null);
	}

	public MobToGeneObject(String mobCodeName, EnumGenes gene1) {
		this(mobCodeName, gene1, null, null);
	}

	public int getValidGenesNum() {
		return this.ValidGenes;
	}
}
