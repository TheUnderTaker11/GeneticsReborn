package com.theundertaker11.geneticsreborn.items;

import java.util.List;

import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OrganicMatter extends ItemBase {

	public OrganicMatter(String name) {
		super(name);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		if(stack.getTagCompound()!=null)
		{
			tooltip.add("Cell type: "+ModUtils.getTagCompound(stack).getString("entityName"));
		}
		else tooltip.add("Cell type: Blank");
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
