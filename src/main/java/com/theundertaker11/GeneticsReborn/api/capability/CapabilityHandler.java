package com.theundertaker11.GeneticsReborn.api.capability;

import com.theundertaker11.GeneticsReborn.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {

	 public static final ResourceLocation GENES_CAPABILITY = new ResourceLocation(Reference.MODID, "genes");

	 @SubscribeEvent
	 public void attachCapability(AttachCapabilitiesEvent.Entity event)
	 {
		 if(!(event.getEntity() instanceof EntityPlayer)) return;
		 event.addCapability(GENES_CAPABILITY, new GeneCapabilityProvider());
	 }
}
