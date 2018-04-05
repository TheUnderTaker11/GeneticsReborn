package com.theundertaker11.geneticsreborn.api.capability.genes;

import java.util.List;

public interface IGenes {
	
	public void addGene(EnumGenes gene);
	
	public void removeGene(EnumGenes gene);
	
	public boolean hasGene(EnumGenes gene);
	
	public void removeAllGenes();
	
	public void addAllGenes();
	/**
	 * Gets the current number of genes the player has
	 * @return # of genes a player has.
	 */
	public int getGeneNumber();
	
	public List<EnumGenes> getGeneList();
	
	public void setGeneList(List<EnumGenes> list);
}
