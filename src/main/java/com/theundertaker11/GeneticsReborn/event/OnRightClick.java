package com.theundertaker11.geneticsreborn.event;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/**
 * This handles any right click related events.
 */
public class OnRightClick{

	/**
	 * This handles the eat grass gene
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		if(player.getFoodStats().needFood()==true&&event.getEntityPlayer().getHeldItemMainhand()==null&&ModUtils.getIGenes(player)!=null&&event.getWorld().getBlockState(event.getPos()).getBlock()==Blocks.GRASS)
		{
			IGenes playergenes = ModUtils.getIGenes(player);
			if(GeneticsReborn.enableEatGrass&&playergenes.hasGene(EnumGenes.EAT_GRASS))
			{
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()+1);
				event.getWorld().setBlockState(event.getPos(), Blocks.DIRT.getDefaultState());
			}else return;
		}
	}
	
	/**
	 * This handles the shoot fireballs genes since you need to be holding a blaze rod.
	 * @param event
	 */
	@SubscribeEvent
	public void onRightClickItem(RightClickItem event)
	{
		if(GeneticsReborn.enableShootFireballs)
		{
			if(event.getItemStack()!=null&&event.getItemStack().getItem()==Items.BLAZE_ROD)
			{
				EntityPlayer player = event.getEntityPlayer();
				if(ModUtils.getIGenes(player)!=null)
				{
					IGenes genes = ModUtils.getIGenes(player);
					if(genes.hasGene(EnumGenes.SHOOT_FIREBALLS))
					{
						Vec3d v3 = player.getLook(1);
        	            EntitySmallFireball fireball = new EntitySmallFireball(player.getEntityWorld(), player.posX, player.posY + player.eyeHeight, player.posZ, v3.xCoord, v3.yCoord, v3.zCoord);
        	            fireball.shootingEntity = player;
        	            player.getEntityWorld().spawnEntityInWorld(fireball);
					}
				}
			}
		}
	}
}
