package com.theundertaker11.geneticsreborn.potions;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class GREntityPotion extends EntityPotion {
	double blastRange;
	EnumGenes gene;
	ItemStack potion;
	int delay = 80;

	public GREntityPotion(World worldIn) {
		 super(worldIn);
		 blastRange = GeneticsReborn.virusRange;
	}


	public GREntityPotion(World worldIn, double x, double y, double z, ItemStack potionDamageIn) {
		this(worldIn, x, y, z, GeneticsReborn.virusRange, potionDamageIn);		
	}

	public GREntityPotion(World worldIn, double x, double y, double z, double range, ItemStack potionDamageIn) {
		super(worldIn, x, y, z, potionDamageIn);
		this.setItem(potionDamageIn);
		this.potion = potionDamageIn;
        blastRange = range;
	}
	
    public GREntityPotion(World worldIn, EntityLivingBase throwerIn, ItemStack potionDamageIn) {
        super(worldIn, throwerIn, potionDamageIn);
        this.setItem(potionDamageIn);
        this.potion = potionDamageIn;
        blastRange = GeneticsReborn.virusRange;
    }    
    
    public void setBlastRange(float r) {
    	blastRange = r;
    }
    
    public void setGene(EnumGenes g) {
    	gene = g;
    }
    
    public void setDelay(int d) {
    	delay = d;
    }
	
    @Override
    public ItemStack getPotion() {
    	return potion;
    }    
    
	//concept taken from EntityPotion
    protected void onImpact(RayTraceResult result)  {
        if (!this.world.isRemote) {
            ItemStack itemstack = this.getPotion();
            PotionType potiontype = PotionUtils.getPotionFromItem(itemstack);
            List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);

            //standard handling
        	if (list.size() > 1 || (list.size() > 0 && !(list.get(0).getPotion() == GRPotions.VIRAL_EFFECT)))
        		super.onImpact(result);
            
        	//special handling for viral potions
            if (blastRange != 0.0) this.makeAreaOfEffectCloud(itemstack, potiontype);
            if (result.typeOfHit != RayTraceResult.Type.MISS) {
            	BlockPos pos = (result.typeOfHit == RayTraceResult.Type.BLOCK) ? result.getBlockPos() : 
            		result.entityHit.getPosition();
	            this.applySplash(pos, list);
	
	            int i = potiontype.hasInstantEffect() ? 2007 : 2002;
	            this.world.playEvent(i, new BlockPos(this), PotionUtils.getColor(itemstack));
            }
            this.setDead();
        }
    }	
	
    private void applySplash(BlockPos pos, List<PotionEffect> potionEffects) {
    	if (blastRange > 0.0) {
	        AxisAlignedBB aabb = this.getEntityBoundingBox().grow(blastRange, blastRange * 0.667, blastRange);
	        List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, aabb);
	
	        for (EntityLivingBase entitylivingbase : list) {
	            double dist = this.getDistanceSq(entitylivingbase);
	
	            if (dist < blastRange * blastRange) {
	            	IGenes genes = ModUtils.getIGenes(entitylivingbase);
	            	genes.addGene(gene);
	            }
	        }
    	} else {
    		//single target block
    		if (pos == null || pos == BlockPos.ORIGIN) return;
    		PotionEffect effect = potionEffects.get(0);
    		if (effect.getPotion() == GRPotions.GROWTH_EFFECT) applyGrowth(pos);
    		if (effect.getPotion() == GRPotions.MUTATION_EFFECT) applyMutation(pos);
    	}
    }
    
    private void applyMutation(BlockPos blockPos) {
    	Block cursedEarth = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extrautils2:cursedearth"));
    	if (cursedEarth != Blocks.AIR) this.world.setBlockState(blockPos, cursedEarth.getDefaultState());
	}

    private boolean makeGrow(BlockPos pos) {
    	IBlockState bs = this.world.getBlockState(pos);
    	Block b = bs.getBlock();
    	if (b instanceof IGrowable) {
    		IGrowable g = (IGrowable)b;
    		g.grow(this.world, ThreadLocalRandom.current(), pos, bs);
    		return true;
    	} if (b == Blocks.DIRT) {
    		this.world.setBlockState(pos, Blocks.GRASS.getDefaultState());
    		return true;
    	}    	
    	return false;
    }

	private void applyGrowth(BlockPos pos) {
		Vec3i start = new Vec3i(-4, 0, -4);
		pos.add(start);
		
		for (int x=-4; x<5; x++) {
			for (int z=-4; z<5; z++) {
				if (!makeGrow(pos.add(x, 0, z))) {
					if (!makeGrow(pos.add(x, 1, z))) {
						makeGrow(pos.add(x, -1, z));
					}
				}
			}
		}
	}


	//from EntityPotion
    private void makeAreaOfEffectCloud(ItemStack potionItem, PotionType potion)
    {
        EntityAreaEffectCloud cloud = new ViralAreaEffect(this.world, this.posX, this.posY, this.posZ);
        cloud.setOwner(this.getThrower());
        cloud.setRadius((float)blastRange);
        cloud.setDuration(1800);
        cloud.setRadiusOnUse(0);
        cloud.setWaitTime(delay);
        cloud.setPotion(potion);
        cloud.setRadiusPerTick(-cloud.getRadius() / (float)cloud.getDuration());
        ((ViralAreaEffect)cloud).setGene(gene);
        
        NBTTagCompound nbttagcompound = potionItem.getTagCompound();
        if (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99))
            cloud.setColor(nbttagcompound.getInteger("CustomPotionColor"));

        this.world.spawnEntity(cloud);
    }	

   
}
