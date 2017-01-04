package com.theundertaker11.GeneticsReborn.api.capability;

import java.util.List;
/**
 * This interface is not for adding your own genes, this handles adding/removing existing genes.
 * If I make all genes be Strings it will take more manual coding but make it easier for other mods to add their own genes.
 * That being said, unless it's requested by a few people, I won't do it.
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
