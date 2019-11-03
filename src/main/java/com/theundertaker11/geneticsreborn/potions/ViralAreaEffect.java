package com.theundertaker11.geneticsreborn.potions;

import java.util.List;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ViralAreaEffect extends EntityAreaEffectCloud {
	private EnumGenes gene;

	public ViralAreaEffect(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public void setGene(EnumGenes gene) {
		this.gene = gene;
	}
	@Override
	public void onUpdate() {
		super.onUpdate();
		double radius = getRadius();
        AxisAlignedBB aabb = this.getEntityBoundingBox().grow(radius, radius * 0.667, radius);
        List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, aabb);

        for (EntityLivingBase entitylivingbase : list) {
            double dist = this.getDistanceSq(entitylivingbase);

            if (dist < radius * radius) {
            	IGenes genes = ModUtils.getIGenes(entitylivingbase);
            	genes.addGene(gene);
            }
        }		
	}

}
