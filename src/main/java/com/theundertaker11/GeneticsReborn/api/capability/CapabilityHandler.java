package com.theundertaker11.geneticsreborn.api.capability;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.geneticsreborn.api.capability.genes.Genes;
import com.theundertaker11.geneticsreborn.api.capability.genes.GenesStorage;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.MaxHealth;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.MaxHealthCapabilityProvider;
import com.theundertaker11.geneticsreborn.api.capability.maxhealth.MaxHealthStorage;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

public class CapabilityHandler {

	 public static final ResourceLocation GENES_CAPABILITY = new ResourceLocation(Reference.MODID, "genes");
	 public static final ResourceLocation MAXHEALTH_CAPABILITY = new ResourceLocation(Reference.MODID, "maxhealth");

	 public static void init()
	 {
		CapabilityManager.INSTANCE.register(IMaxHealth.class, new MaxHealthStorage(), MaxHealth.class);
		CapabilityManager.INSTANCE.register(IGenes.class, new GenesStorage(), Genes.class);
	 }
	 @SubscribeEvent
	 public void attachCapability(AttachCapabilitiesEvent<Entity> event)
	 {
		 if(event.getObject() instanceof EntityLivingBase&&event.getObject()!=null)
		 {
			 event.addCapability(GENES_CAPABILITY, new GeneCapabilityProvider());
			 
			 final MaxHealth maxHealth = new MaxHealth((EntityLivingBase) event.getObject());
			 event.addCapability(MAXHEALTH_CAPABILITY, new MaxHealthCapabilityProvider(maxHealth));
		 }
	 }
	 
	 @SubscribeEvent
	 public void onPlayerChangeDim(PlayerChangedDimensionEvent event)
	 {
		 final IMaxHealth maxHealth = ModUtils.getIMaxHealth(event.player);
		 if (maxHealth != null)
		 {
			 maxHealth.synchronise();
		 }
	 }
}
