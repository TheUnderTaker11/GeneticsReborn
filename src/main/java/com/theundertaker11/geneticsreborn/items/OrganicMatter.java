package com.theundertaker11.geneticsreborn.items;

import java.util.List;

import javax.annotation.Nullable;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OrganicMatter extends ItemBase {

	public OrganicMatter(String name) {
		super(name);
	}

	@Override
    @SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			tooltip.add("Cell type: " + tag.getString("entityName"));
			if (tag.hasKey("forceGene")) {
				if (tag.hasKey("mutation")) { 
					EnumGenes gene = EnumGenes.fromGeneName(tag.getString("forceGene"));
					if (gene != null) tooltip.add(TextFormatting.DARK_RED +  "Mutation: " + gene.getDescription() + TextFormatting.RESET);
					else if (tag.getString("forceGene").equals("GeneticsRebornMutatedGene")) tooltip.add(TextFormatting.DARK_RED +  "Mutation: Basic Gene" + TextFormatting.RESET);
				} else
					tooltip.add("Gene Focus: " + EnumGenes.fromGeneName(tag.getString("forceGene")).getDescription());				
			}
		} else tooltip.add("Cell type: Blank");
		
	}
	
	/* Code for use later to spawn the type of mob the organic matter was caught from

	  NBTBase mobCompound = tagCompound.getTag("mob");
                String type = tagCompound.getString("type");
                EntityLivingBase entityLivingBase = createEntity(player, world, type);
                if (entityLivingBase == null) {
             		Do some kind of fail code.
                }
                entityLivingBase.readEntityFromNBT((NBTTagCompound) mobCompound);
                entityLivingBase.setLocationAndAngles(x, y, z, 0, 0);
                
                world.spawnEntityInWorld(entityLivingBase);
	 */
}
