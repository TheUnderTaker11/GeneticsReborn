package com.theundertaker11.GeneticsReborn.api.capability;

import com.theundertaker11.GeneticsReborn.Reference;
import com.theundertaker11.GeneticsReborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.maxhealth.IMaxHealth;
import com.theundertaker11.GeneticsReborn.api.capability.maxhealth.MaxHealth;
import com.theundertaker11.GeneticsReborn.api.capability.maxhealth.MaxHealthCapabilityProvider;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {

	 public static final ResourceLocation GENES_CAPABILITY = new ResourceLocation(Reference.MODID, "genes");
	 public static final ResourceLocation MAXHEALTH_CAPABILITY = new ResourceLocation(Reference.MODID, "maxhealth");

	 @SubscribeEvent
	 public void attachCapability(AttachCapabilitiesEvent<Entity> event)
	 {
		 if((event.getObject() instanceof EntityPlayer))
		 {
			 event.addCapability(GENES_CAPABILITY, new GeneCapabilityProvider());
		 }
		 if(event.getObject() instanceof EntityLivingBase&&event.getObject()!=null)
		 {
			 final MaxHealth maxHealth = new MaxHealth((EntityLivingBase) event.getObject());
			 event.addCapability(MAXHEALTH_CAPABILITY, new MaxHealthCapabilityProvider(maxHealth));
		 }
	 }
}
