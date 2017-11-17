package com.theundertaker11.geneticsreborn.event;

import javax.annotation.Nullable;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AIChangeEvents {

	//The ones below this attach new AI to the creepers
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event)
	{
				if(event.getEntity() instanceof EntityCreature) attachCreeperTask(event, (EntityCreature)event.getEntity());
	}
	private boolean hasCreeperGene(@Nullable EntityPlayer player)
	{
		return ModUtils.getIGenes(player)!=null&&ModUtils.getIGenes(player).hasGene(EnumGenes.SCARE_CREEPERS);
	}
	public void attachCreeperTask(EntityJoinWorldEvent event, EntityCreature entity)
	{
		if(entity instanceof EntityCreeper)
		{
			entity.tasks.addTask(3, new EntityAIAvoidEntity<>(entity, EntityPlayer.class, this::hasCreeperGene, 6.0F, 1.0D, 1.2D));
		}
		/*for (Object a : entity.tasks.taskEntries.toArray())
		{
			EntityAIBase ai = ((EntityAITaskEntry) a).action;
		} */

		for (Object a : entity.targetTasks.taskEntries.toArray())
		{
			EntityAIBase ai = ((EntityAITaskEntry) a).action;
			if(entity instanceof EntityCreeper && ai instanceof EntityAINearestAttackableTarget)
			{
				entity.targetTasks.removeTask(ai);
				entity.targetTasks.addTask(0, new EntityAINearestAttackableTarget<>(entity, EntityPlayer.class, 10, false, false, (player)-> !hasCreeperGene(player)));
			}	
		}
	}
}
