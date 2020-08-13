package com.theundertaker11.geneticsreborn.potions;

import java.lang.reflect.Method;
import java.util.UUID;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GRPotions {

	public static final Potion SUBSTRATE_EFFECT = new BasePotion("geneticsreborn:substrate", false, 0x17661e);
	public static final Potion GROWTH_EFFECT = new BasePotion("geneticsreborn:growth", false, 0x95eb34);
	public static final Potion MUTATION_EFFECT = new BasePotion("geneticsreborn:mutation", false, 0x5c0d30);
	public static final Potion VIRAL_EFFECT = new BasePotion("geneticsreborn:viral", false, 0xd18e1b);
	public static final Potion BLEED_EFFECT = new EffectPotion("geneticsreborn:bleed", true, 0x5c0d30, 0, 0);
	public static final Potion CURE_EFFECT = new EffectPotion("geneticsreborn:cure", true, 0xa83283, 1, 0);
	
	public static final PotionType SUBSTRATE = new PotionType("geneticsreborn:substrate", new PotionEffect[] {new PotionEffect(SUBSTRATE_EFFECT, 0)}).setRegistryName("geneticsreborn:substrate");
	public static final PotionType GROWTH_POTION = new PotionType("geneticsreborn:growth", new PotionEffect[] {new PotionEffect(GROWTH_EFFECT, 0)}).setRegistryName("geneticsreborn:growth");
	public static final PotionType MUTATION_POTION = new PotionType("geneticsreborn:mutation", new PotionEffect[] {new PotionEffect(MUTATION_EFFECT, 0)}).setRegistryName("geneticsreborn:mutation");
	public static final PotionType VIRAL_POTION = new PotionType("geneticsreborn:viral", new PotionEffect[] {new PotionEffect(VIRAL_EFFECT, 0)}).setRegistryName("geneticsreborn:viral");
	public static final PotionType CURE_POTION = new PotionType("geneticsreborn:cure", new PotionEffect[] {new PotionEffect(CURE_EFFECT, 1200)}).setRegistryName("geneticsreborn:cure");

	public static void init() {
		ForgeRegistries.POTIONS.register(SUBSTRATE_EFFECT);
		ForgeRegistries.POTIONS.register(GROWTH_EFFECT);
		ForgeRegistries.POTIONS.register(MUTATION_EFFECT);
		ForgeRegistries.POTIONS.register(VIRAL_EFFECT);
		ForgeRegistries.POTIONS.register(BLEED_EFFECT);
		ForgeRegistries.POTIONS.register(CURE_EFFECT);

		ForgeRegistries.POTION_TYPES.register(SUBSTRATE);
		ForgeRegistries.POTION_TYPES.register(GROWTH_POTION);
		ForgeRegistries.POTION_TYPES.register(MUTATION_POTION);
		ForgeRegistries.POTION_TYPES.register(VIRAL_POTION);
		ForgeRegistries.POTION_TYPES.register(CURE_POTION);
		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(PotionTypes.MUNDANE, GRItems.OrganicMatter, null,  
				PotionUtils.addPotionToItemStack(new ItemStack(GRItems.GRPotion), SUBSTRATE)));
		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(SUBSTRATE, GRItems.DNAHelix, "GeneticsRebornBasicGene",  
				PotionUtils.addPotionToItemStack(new ItemStack(GRItems.GRPotion), GROWTH_POTION)));

		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.EMERALD_HEART.toGeneName(),  
				PotionUtils.addPotionToItemStack(new ItemStack(GRItems.GRPotion), CURE_POTION)));

		BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(GRItems.GRPotion), CURE_POTION), 
				new ItemStack(Items.GUNPOWDER, 1),
				PotionUtils.addPotionToItemStack(new ItemStack(GRItems.ViralPotion), CURE_POTION));
		
		//create more cells
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(SUBSTRATE, GRItems.Cell, null,  
				new ItemStack(GRItems.Cell, 1)));
		
		//any cell + growth = growth potion of that cell
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, GRItems.Cell, null,  
				PotionUtils.addPotionToItemStack(new ItemStack(GRItems.GRPotion), GROWTH_POTION)));
		
		//gene focus brews
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.GLOWSTONE_DUST, "EntityBlaze", EnumGenes.BIOLUMIN, 50));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.GLOWSTONE_DUST, "EntityMagmaCube", EnumGenes.BIOLUMIN, 50));		

		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.EMERALD, "EntityVillager", EnumGenes.EMERALD_HEART, 75));		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.EMERALD, "EntityShulker", EnumGenes.SAVE_INVENTORY, 50));		

		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.REDSTONE, "EntityRabbit", EnumGenes.SPEED, 75));		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.APPLE, "EntityIronGolem", EnumGenes.MORE_HEARTS, 75));		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.GOLDEN_APPLE, "EntityVillager", EnumGenes.REGENERATION, 75));		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.EGG, "EntityChicken", EnumGenes.LAY_EGG, 75));		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.PORKCHOP, "EntityPig", EnumGenes.MEATY, 75));		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.ENDER_PEARL, "EntityEnderman", EnumGenes.TELEPORTER, 75));		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.IRON_INGOT, "EntityEnderman", EnumGenes.ITEM_MAGNET, 75));		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.GOLDEN_APPLE, "EntityEnderman", EnumGenes.MORE_HEARTS, 75));		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.GLOWSTONE_DUST, "EntitySlime", EnumGenes.PHOTOSYNTHESIS, 50));		
		
		//create mutation potion
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(GROWTH_POTION, Items.FERMENTED_SPIDER_EYE, null,  
				PotionUtils.addPotionToItemStack(new ItemStack(GRItems.GRPotion), MUTATION_POTION)));
		
		//any cell + mutation = mutation potion of that cell
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, GRItems.Cell, null,  
				PotionUtils.addPotionToItemStack(new ItemStack(GRItems.GRPotion), MUTATION_POTION)));
		
		//create mutated genes
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.SUGAR, "*",  "GeneticsRebornMutatedGene", 75));
		
		//mutation brews
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.FEATHER, "EntityBat",  EnumGenes.FLY, 75));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.FEATHER, "EntityParrot",  EnumGenes.FLY, 75));

		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.EMERALD, "EntityPolarBear",  EnumGenes.STRENGTH, 50));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.EMERALD, "EntityLlama",  EnumGenes.STRENGTH, 50));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.EMERALD, "EntityRabbit",  EnumGenes.LUCK, 50));

		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.DIAMOND, "EntityShulker",  EnumGenes.RESISTANCE, 50));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.DIAMOND, "EntityZombie",  EnumGenes.RESISTANCE, 50));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.DIAMOND, "EntityPolarBear",  EnumGenes.CLAWS, 50));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.DIAMOND, "EntityLlama",  EnumGenes.CLAWS, 50));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.DIAMOND, "EntityWolf",  EnumGenes.CLAWS, 50));
		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.REDSTONE, "EntityRabbit",  EnumGenes.SPEED_2, 50));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.REDSTONE, "EntityOcelot",  EnumGenes.SPEED_4, 50));
		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.IRON_INGOT, "EntityRabbit",  EnumGenes.HASTE, 50));

		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.REDSTONE, "EntitySilverfish",  EnumGenes.EFFICIENCY, 50));

		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.SPIDER_EYE, "EntityZombie",  EnumGenes.SCARE_CREEPERS, 75));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.SPIDER_EYE, "EntitySpider",  EnumGenes.SCARE_SKELETONS, 75));

		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.REDSTONE, "Ender Dragon",  EnumGenes.REGENERATION, 50));

		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.BLAZE_POWDER, "EntityPig",  EnumGenes.MEATY, 50));

		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.ENDER_EYE, "EntityBat",  EnumGenes.INVISIBLE, 50));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.ENDER_EYE, "EntitySkeleton",  EnumGenes.INVISIBLE, 50));
		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.GOLDEN_APPLE, "EntityEnderman",  EnumGenes.MORE_HEARTS, 50));

		//Bottle of Viral Agents
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(MUTATION_POTION, Items.CHORUS_FRUIT, null,  
				PotionUtils.addPotionToItemStack(new ItemStack(GRItems.GRPotion), VIRAL_POTION)));
		
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.POISON_PROOF,  EnumGenes.POISON, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.WITHER_HIT,  EnumGenes.POISON_4, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.WITHER_PROOF,  EnumGenes.WITHER, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.STRENGTH,  EnumGenes.WEAKNESS, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.NIGHT_VISION,  EnumGenes.BLINDNESS, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.SPEED,  EnumGenes.SLOWNESS, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.SPEED_2,  EnumGenes.SLOWNESS_4, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.SPEED_4,  EnumGenes.SLOWNESS_6, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.MILKY,  EnumGenes.NAUSEA, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.MEATY,  EnumGenes.NAUSEA, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.LAY_EGG,  EnumGenes.NAUSEA, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.NO_HUNGER,  EnumGenes.HUNGER, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.FIRE_PROOF,  EnumGenes.FLAME, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.LUCK,  EnumGenes.CURSED, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.HASTE,  EnumGenes.MINING_WEAKNESS, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.SCARE_CREEPERS,  EnumGenes.DEAD_CREEPERS, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.SCARE_ZOMBIES,  EnumGenes.DEAD_UNDEAD, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.SCARE_SKELETONS,  EnumGenes.DEAD_UNDEAD, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.RESISTANCE,  EnumGenes.DEAD_OLD_AGE, 100));
		BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.DNAHelix, EnumGenes.DRAGONS_BREATH,  EnumGenes.DEAD_HOSTILE, 100));

		if (GeneticsReborn.enableBlackDeath) {
			BrewingRecipeRegistry.addRecipe(new ComplexBrew(VIRAL_POTION, GRItems.GlassSyringe, EnumGenes.DEAD_ALL,  EnumGenes.DEAD_ALL, 100));
		}
		
}
	
	
	@SubscribeEvent
	public void handleCurePotion(WorldTickEvent event) {
		for (Entity ent : event.world.loadedEntityList) {
			if (ent instanceof EntityLivingBase) {
				EntityLivingBase e = (EntityLivingBase)ent;
				if (e.isPotionActive(CURE_EFFECT)) {
					if (e instanceof EntityPlayer) cureEntity(e);
					else if (e instanceof EntityVillager) cureEntity(e);
					else if (e instanceof EntityZombieVillager) cureZombieVillager((EntityZombieVillager)e);
				}
			}
		}		
	}
	

	
	@SuppressWarnings("rawtypes")
	private void cureZombieVillager(EntityZombieVillager e) {
		if (e.isConverting()) return;
		Class[] args = {UUID.class, int.class};
		try {
			Method m = e.getClass().getDeclaredMethod("startConverting", args);
			m.setAccessible(true);
			m.invoke(e, null, 20);
		} catch (Exception e1) { 
			
		}		
	}


	private void cureEntity(EntityLivingBase e) {
		IGenes genes = ModUtils.getIGenes(e);
		for (int i = genes.getGeneNumber()-1; i >= 0 ; i--) {
			EnumGenes gene = genes.getGeneList().get(i);
			if (gene.isNegative()) genes.removeGene(gene);
		}
			
			
	}


	@SubscribeEvent
    @SideOnly(Side.CLIENT)
	public void handleTooltipEvent(ItemTooltipEvent event) {
		if (event.getItemStack().getItem() == Items.POTIONITEM || event.getItemStack().getItem() == GRItems.GRPotion) {
			NBTTagCompound tag = event.getItemStack().getTagCompound();
			if (tag == null || tag.getString("Potion") == null) return;
			if (tag.getString("Potion").equals(GROWTH_POTION.getRegistryName().toString())) {
				if (tag.hasKey("entityName"))
					event.getToolTip().add("("+tag.getString("entityName")+ ")");
			}
			if (tag.getString("Potion").equals(MUTATION_POTION.getRegistryName().toString())) {
				if (tag.hasKey("entityName"))
					event.getToolTip().add("("+tag.getString("entityName")+ ")");
			}
			if (tag.getString("Potion").equals(VIRAL_POTION.getRegistryName().toString())) {
				if (tag.hasKey("gene")) {
					EnumGenes gene = EnumGenes.fromGeneName(tag.getString("gene"));
					if (gene != null) event.getToolTip().add("("+gene.getDescription()+ ")");
				}
			}
		}
	}	
}
