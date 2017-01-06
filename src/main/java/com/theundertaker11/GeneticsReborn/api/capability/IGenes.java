package com.theundertaker11.GeneticsReborn.api.capability;

import java.util.ArrayList;
import java.util.List;
/**
 * This interface is not optimized to add your own genes.
 * I could change the whole system to be able to add genes, but I don't see a point.
 * That being said, unless it's requested by a lot of people, I won't do it.
 * I figured using an Enum would be the simplest way to implement a genes system, but it makes it difficult for others to add genes.
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
