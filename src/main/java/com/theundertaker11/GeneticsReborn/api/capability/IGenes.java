package com.theundertaker11.GeneticsReborn.api.capability;

import java.util.List;
/**
 * I figured using an Enum would be the simplest way to implement a genes system.
 * You get all the genes through the EnumGenes class.
 * @author TheUnderTaker11
 *
 */
public interface IGenes{
	
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
