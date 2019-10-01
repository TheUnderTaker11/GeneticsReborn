package com.theundertaker11.geneticsreborn.util;

import java.util.concurrent.ThreadLocalRandom;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;

public class MobToGeneObject {

	public final String MobCodeName;
	public final EnumGenes[] genes;

	/**
	 * This is just an object I use to store a few things for registering mobs
	 *
	 * @param gene1
	 * @param gene2
	 * @param gene3
	 */
	public MobToGeneObject(String mobCodeName, EnumGenes[] gene) {
		MobCodeName = mobCodeName;
		this.genes = gene;
	}

	public MobToGeneObject(String mobCodeName, EnumGenes gene) {
		MobCodeName = mobCodeName;
		this.genes = new EnumGenes[] {gene};
	}

	public int getValidGenesNum() {
		return this.genes.length;
	}
	
	public String getRandomGene() {
		 return genes[ThreadLocalRandom.current().nextInt(getValidGenesNum())].toString();		
	}
}
