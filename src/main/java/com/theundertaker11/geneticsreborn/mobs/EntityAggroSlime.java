package com.theundertaker11.geneticsreborn.mobs;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityAggroSlime extends EntitySlime {
	EntityLivingBase living;
	
	public EntityAggroSlime(World worldIn, EntityLivingBase e) {
		super(worldIn);
		living = e;
	}
	
	public EntityAggroSlime(World worldIn) {
		this(worldIn, null);
	}
	
	@Override
	public boolean getCanSpawnHere() {
        //IBlockState iblockstate = this.world.getBlockState((new BlockPos(this)).down());
        //return iblockstate.canEntitySpawn(this);
		return true;
	}

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    	this.setSlimeSize(4, true);
    	if (living != null) this.setHealth(living.getMaxHealth());
    	return livingdata;
    }
    
	@Override
	public void setAttackTarget(EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) super.setAttackTarget(null);
		else super.setAttackTarget(entity);
	}
    
	//pull aggro
    @Override
    public void onUpdate() {
    	super.onUpdate();
    	
    	List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(posX-16, posY-8, posZ-16, posX+16, posY+8, posZ+16));
    	for(EntityMob mob : mobs) 
    		if(mob.getAttackTarget() == null || mob.getAttackingEntity() == living) {
    			mob.setRevengeTarget(this);
    			mob.setAttackTarget(this);
    		}    	
    }

}
