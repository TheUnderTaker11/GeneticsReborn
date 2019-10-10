package com.theundertaker11.geneticsreborn.event;

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

public class OnRightClick {

	@SubscribeEvent
	public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player.getFoodStats().needFood() == true && event.getEntityPlayer().getHeldItemMainhand().isEmpty() && ModUtils.getIGenes(player) != null && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.GRASS) {
			IGenes playergenes = ModUtils.getIGenes(player);
			if (EnumGenes.EAT_GRASS.isActive() && playergenes.hasGene(EnumGenes.EAT_GRASS)) {
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() + 1);
				event.getWorld().setBlockState(event.getPos(), Blocks.DIRT.getDefaultState());
			} else return;
		}
	}

	/**
	 * This handles the shoot fireballs genes since you need to be holding a blaze rod.
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onRightClickItem(RightClickItem event) {
		if (EnumGenes.SHOOT_FIREBALLS.isActive()) {
			if (event.getItemStack().getItem() == Items.BLAZE_ROD) {
				EntityPlayer player = event.getEntityPlayer();
				if (ModUtils.getIGenes(player) != null) {
					IGenes genes = ModUtils.getIGenes(player);
					if (genes.hasGene(EnumGenes.SHOOT_FIREBALLS)) {
						Vec3d v3 = player.getLook(1);
						EntitySmallFireball fireball = new EntitySmallFireball(player.getEntityWorld(), player.posX, player.posY + player.eyeHeight, player.posZ, v3.x, v3.y, v3.z);
						fireball.shootingEntity = player;
						player.getEntityWorld().spawnEntity(fireball);
					}
				}
			}
		}
	}
}
