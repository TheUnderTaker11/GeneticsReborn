package com.theundertaker11.geneticsreborn.api.capability.maxhealth;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

import com.theundertaker11.geneticsreborn.util.ModUtils;

import java.util.Collections;
import java.util.UUID;

/**
 * Default implementation of {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class MaxHealth implements IMaxHealth {
	/**
	 * The ID of the {@link AttributeModifier}.
	 */
	protected static final UUID MODIFIER_ID = UUID.fromString("9a847987-fa89-4b4c-8bd7-6796789711c9");

	/**
	 * The name of the {@link AttributeModifier}.
	 */
	protected static final String MODIFIER_NAME = "Bonus Max Health";

	/**
	 * The minimum max health a player can have.
	 */
	protected static final float MIN_AMOUNT = 2.0f;

	/**
	 * The entity this is attached to.
	 */
	private final EntityLivingBase entity;

	/**
	 * The bonus max health.
	 */
	private float bonusMaxHealth;

	/**
	 * The dummy max health attribute. Used to avoid setting the entity's actual attribute to an invalid value.
	 */
	private final IAttributeInstance dummyMaxHealthAttribute = new AttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);


	public MaxHealth(@Nullable EntityLivingBase entity) {
		this.entity = entity;
	}

	public MaxHealth() {
		this(null);
	}

	/**
	 * Get the bonus max health.
	 *
	 * @return The bonus max health
	 */
	@Override
	public final float getBonusMaxHealth() {
		return bonusMaxHealth;
	}

	/**
	 * Set the bonus max health.
	 *
	 * @param bonusMaxHealth The bonus max health
	 */
	@Override
	public final void setBonusMaxHealth(float bonusMaxHealth) {
		this.bonusMaxHealth = bonusMaxHealth;

		onBonusMaxHealthChanged();
	}

	/**
	 * Add an amount to the current bonus max health.
	 *
	 * @param healthToAdd The amount of health to add
	 */
	@Override
	public final void addBonusMaxHealth(float healthToAdd) {
		setBonusMaxHealth(getBonusMaxHealth() + healthToAdd);
	}

	/**
	 * Synchronise the entity's max health to watching clients.
	 */
	@Override
	public void synchronise() {
		if (entity != null && !entity.getEntityWorld().isRemote) {
			final IAttributeInstance entityMaxHealthAttribute = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
			final SPacketEntityProperties packet = new SPacketEntityProperties(entity.getEntityId(), Collections.singleton(entityMaxHealthAttribute));
			((WorldServer) entity.getEntityWorld()).getEntityTracker().sendToTrackingAndSelf(entity, packet);
		}
	}

	/**
	 * Create the {@link AttributeModifier}.
	 *
	 * @return The AttributeModifier
	 */
	protected AttributeModifier createModifier() {
		return new AttributeModifier(MODIFIER_ID, MODIFIER_NAME, getBonusMaxHealth(), ModUtils.ATTRIBUTE_MODIFIER_OPERATION_ADD);
	}

	/**
	 * Called when the bonus max health changes to re-apply the {@link AttributeModifier}.
	 */
	protected void onBonusMaxHealthChanged() {
		if (entity == null) return;

		final IAttributeInstance entityMaxHealthAttribute = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

		dummyMaxHealthAttribute.getModifiers().forEach(dummyMaxHealthAttribute::removeModifier);

		dummyMaxHealthAttribute.setBaseValue(entityMaxHealthAttribute.getBaseValue());
		entityMaxHealthAttribute.getModifiers().stream().filter(modifier -> !modifier.getID().equals(MODIFIER_ID)).forEach(dummyMaxHealthAttribute::applyModifier);

		AttributeModifier modifier = createModifier();
		dummyMaxHealthAttribute.applyModifier(modifier);

		while (dummyMaxHealthAttribute.getAttributeValue() < MIN_AMOUNT) {
			dummyMaxHealthAttribute.removeModifier(modifier);
			bonusMaxHealth += 0.5f;
			modifier = createModifier();
			dummyMaxHealthAttribute.applyModifier(modifier);
		}

		final float newAmount = getBonusMaxHealth();
		final float oldAmount;

		final AttributeModifier oldModifier = entityMaxHealthAttribute.getModifier(MODIFIER_ID);
		if (oldModifier != null) {
			entityMaxHealthAttribute.removeModifier(oldModifier);

			oldAmount = (float) oldModifier.getAmount();

		} else {
			oldAmount = 0.0f;
		}

		entityMaxHealthAttribute.applyModifier(modifier);

		final float amountToHeal = newAmount - oldAmount;
		if (amountToHeal > 0) {
			entity.heal(amountToHeal);
		}
	}
}
