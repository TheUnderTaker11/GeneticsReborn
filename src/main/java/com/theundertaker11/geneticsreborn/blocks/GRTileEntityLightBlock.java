package com.theundertaker11.geneticsreborn.blocks;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.EntityLivingBase;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/*
 * This class is used for the Bioluminescence trait
 * Based on the Tutorial at: http://jabelarminecraft.blogspot.com/p/minecraft-modding-making-hand-held.html
 */
public class GRTileEntityLightBlock extends TileEntity implements ITickable {
    public EntityLivingBase entity; 
    private boolean dying;
    private static final int MAX_DEATH_TIMER = 4; 
    private int deathTimer; 

    public GRTileEntityLightBlock() {
    	super();
    	dying = false;
        deathTimer = MAX_DEATH_TIMER;
    }
    
    @Override
    public void update() {
    	boolean shouldDie = false;
    	
    	if (world.isRemote) return;
			
		if (entity == null || entity.isDead) {
            shouldDie = true;
        } else if (entity.isBurning()) {
        	shouldDie = false;
        } else if (needsLight(entity)) {
            double distanceSquared = getDistanceSq(entity.posX, entity.posY, entity.posZ);
            if (distanceSquared > 5.0D) shouldDie = true;    
		} else {
            shouldDie = true;
        }
		
		if (dying != shouldDie) {
			if (shouldDie) dying = true;
			else {
				dying = false;
				deathTimer = MAX_DEATH_TIMER;
			}
		}

		if (dying) {
			deathTimer--;
			if (deathTimer <= 0) {
                world.setBlockToAir(getPos());
                world.removeTileEntity(getPos());
			}
		}
			
    }
    
    public static final boolean needsLight(EntityLivingBase entity) {
        IGenes genes = ModUtils.getIGenes(entity);
        return GeneticsReborn.enableBiolumin && genes.hasGene(EnumGenes.BIOLUMIN);
    }

    @Override
    public void setPos(BlockPos pos) {
    	super.setPos(pos);
        BlockPos p = pos.toImmutable();
        entity = ModUtils.getNearestEntity(world, p, 2.0D);
    }
}
