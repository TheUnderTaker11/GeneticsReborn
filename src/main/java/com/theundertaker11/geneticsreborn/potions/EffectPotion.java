package com.theundertaker11.geneticsreborn.potions;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class EffectPotion extends BasePotion {
	
	
	public EffectPotion(String name, boolean isBad, int color, int iconX, int iconY) {
		super(name, isBad, color);
		setIconIndex(iconX, iconY);
	}
	
	@Override
	public boolean hasStatusIcon() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/potion_effects.png"));
		return true;
	}
	
	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
		if (this == GRPotions.BLEED_EFFECT) {
			entityLivingBaseIn.attackEntityFrom(DamageSource.GENERIC, GeneticsReborn.clawsDamage * 8);
			return;
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		if (this == GRPotions.BLEED_EFFECT) {
			return duration % 160 == 20;
		}
		return false;
	}
	
}
