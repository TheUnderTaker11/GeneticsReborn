package com.theundertaker11.geneticsreborn.api.capability.genes;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.JsonHandler;
import com.theundertaker11.geneticsreborn.util.MobToGeneObject;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;

/**
 * This is used by the DNA Decrypter to tell if a mob should drop a given gene. 
 * DO NOT TRY TO ADD GENES TO MOBS I'VE ALREADY REGISTERED GENES TO. It is simply not made to handle that.
 * It should work perfect for adding existing genes to mobs I haven't registered though.
 * 
 * @author TheUnderTaker11
 *
 */
public class MobToGeneRegistry {

	private static HashSet<MobToGeneObject> list = new HashSet<>();

	public static void init() {
		if (GeneticsReborn.registerModGenes) {
			//Peacefuls
			registerMob(new MobToGeneObject("EntityVillager", new EnumGenes[] {EnumGenes.MOB_SIGHT, EnumGenes.EMERALD_HEART, EnumGenes.REGENERATION}));
			registerMob(new MobToGeneObject("EntitySheep", new EnumGenes[] {EnumGenes.EAT_GRASS, EnumGenes.WOOLY}));
			registerMob(new MobToGeneObject("EntityCow", new EnumGenes[] {EnumGenes.EAT_GRASS, EnumGenes.MILKY}));
			registerMob(new MobToGeneObject("EntityPig", new EnumGenes[] {EnumGenes.MEATY}));
			registerMob(new MobToGeneObject("EntityHorse", new EnumGenes[] {EnumGenes.JUMP_BOOST, EnumGenes.STEP_ASSIST, EnumGenes.SPEED}));
			registerMob(new MobToGeneObject("EntityChicken", new EnumGenes[] {EnumGenes.NO_FALL_DAMAGE, EnumGenes.LAY_EGG}));
			registerMob(new MobToGeneObject("EntityBat", new EnumGenes[] {EnumGenes.MOB_SIGHT, EnumGenes.NIGHT_VISION}));
			registerMob(new MobToGeneObject("EntityParrot", new EnumGenes[] {EnumGenes.NO_FALL_DAMAGE}));
			registerMob(new MobToGeneObject("EntityOcelot", new EnumGenes[] {EnumGenes.SPEED, EnumGenes.SCARE_CREEPERS}));
			registerMob(new MobToGeneObject("EntityPolarBear", new EnumGenes[] {EnumGenes.STRENGTH, EnumGenes.CLAWS, EnumGenes.STEP_ASSIST}));
			registerMob(new MobToGeneObject("EntityLlama", new EnumGenes[] {EnumGenes.STRENGTH, EnumGenes.STEP_ASSIST}));
			registerMob(new MobToGeneObject("EntityWolf", new EnumGenes[] {EnumGenes.SCARE_SKELETONS, EnumGenes.NIGHT_VISION, EnumGenes.NO_HUNGER}));
			registerMob(new MobToGeneObject("EntityRabbit", new EnumGenes[] {EnumGenes.JUMP_BOOST, EnumGenes.SPEED}));
			registerMob(new MobToGeneObject("EntitySquid", EnumGenes.WATER_BREATHING));
			registerMob(new MobToGeneObject("EntityIronGolem", new EnumGenes[] {EnumGenes.MORE_HEARTS, EnumGenes.REGENERATION}));
	
			//Overworld Hostiles
			registerMob(new MobToGeneObject("EntityZombie", EnumGenes.RESISTANCE));
			registerMob(new MobToGeneObject("EntityCreeper", new EnumGenes[] {EnumGenes.EXPLOSIVE_EXIT}));
			registerMob(new MobToGeneObject("EntitySpider", new EnumGenes[] {EnumGenes.NIGHT_VISION, EnumGenes.CLIMB_WALLS}));
			registerMob(new MobToGeneObject("EntityCaveSpider", new EnumGenes[] {EnumGenes.NIGHT_VISION, EnumGenes.CLIMB_WALLS, EnumGenes.POISON_PROOF}));
			registerMob(new MobToGeneObject("EntitySlime", new EnumGenes[] {EnumGenes.NO_FALL_DAMAGE, EnumGenes.SLIMY}));
			registerMob(new MobToGeneObject("EntityGuardian", new EnumGenes[] {EnumGenes.MOB_SIGHT, EnumGenes.WATER_BREATHING}));
			registerMob(new MobToGeneObject("EntitySkeleton", EnumGenes.INFINITY));
			registerMob(new MobToGeneObject("EntitySilverfish", new EnumGenes[] {EnumGenes.HASTE, EnumGenes.EFFICIENCY}));
	
			//End hostiles
			registerMob(new MobToGeneObject("EntityEndermite", new EnumGenes[] {EnumGenes.SAVE_INVENTORY, EnumGenes.ITEM_MAGNET, EnumGenes.XP_MAGNET}));
			registerMob(new MobToGeneObject("EntityEnderman", new EnumGenes[] {EnumGenes.TELEPORTER, EnumGenes.MORE_HEARTS}));
			registerMob(new MobToGeneObject("EntityShulker", new EnumGenes[] {EnumGenes.RESISTANCE, EnumGenes.REGENERATION}));
	
			//Nether hostiles
			registerMob(new MobToGeneObject("Wither Skeleton", new EnumGenes[] {EnumGenes.WITHER_HIT, EnumGenes.INFINITY} ));
			registerMob(new MobToGeneObject("EntityBlaze", new EnumGenes[] {EnumGenes.SHOOT_FIREBALLS, EnumGenes.FIRE_PROOF, EnumGenes.BIOLUMIN}));
			registerMob(new MobToGeneObject("EntityGhast", new EnumGenes[] {EnumGenes.MOB_SIGHT, EnumGenes.SHOOT_FIREBALLS}));
			registerMob(new MobToGeneObject("EntityPigZombie", new EnumGenes[] {EnumGenes.FIRE_PROOF, EnumGenes.MEATY}));
			registerMob(new MobToGeneObject("EntityMagmaCube", new EnumGenes[] {EnumGenes.FIRE_PROOF, EnumGenes.BIOLUMIN}));
	
			//Bosses
			registerMob(new MobToGeneObject("EntityWither", new EnumGenes[] {EnumGenes.WITHER_PROOF}));
			registerMob(new MobToGeneObject("Ender Dragon", new EnumGenes[] {EnumGenes.DRAGONS_BREATH, EnumGenes.ENDER_DRAGON_HEALTH, EnumGenes.FLY}));
		}

		if (GeneticsReborn.registerModGenes) {
			try {
				if (Loader.isModLoaded("arsmagica2")) registerArsMagica();
				if (Loader.isModLoaded("grimoireofgaia")) registerGrimoireOfGaia();
				if (Loader.isModLoaded("mocreatures")) registerMoCreatures();
				if (Loader.isModLoaded("netherex")) registerNEXMobs();
				if (Loader.isModLoaded("botania")) registerBotaniaMobs();
				if (Loader.isModLoaded("cyberware")) registerCyberMobs();
			} catch (NoSuchMethodError e) {
				e.printStackTrace();
			}
		}

		JsonHandler.registerMobsFromJson();
	}

	public static void registerMob(MobToGeneObject obj) {
		list.add(obj);
	}


	/**
	 * Returns the gene in String form, prefixed with GeneticsReborn. For use in decrypter. Should only be called serverside.
	 *
	 * @param entityCodeName name gotten from #getClass()#getSimpleName() on an EntityLivingBase
	 * @return Random gene based on mob and registry.
	 */
	public static String getGene(String entityCodeName) {
		int numb = ThreadLocalRandom.current().nextInt(1, 101);
		if (numb > 40)
			return "GeneticsRebornBasicGene";

		MobToGeneObject object = null;
		for (MobToGeneObject obj : list) {
			if (obj.MobCodeName.equals(entityCodeName)) {
				object = obj;
				break;
			}
		}
		if (object == null || object.getValidGenesNum() == 0)
			return "GeneticsRebornBasicGene";


		EnumGenes gene = object.getRandomGene();
		if (!gene.isActive()) return "GeneticsRebornBasicGene";
		return gene.toGeneName();
	}

	/**
	 * Checks if the original item has been "forced" by a cell growth/mutation potion, if so
	 * the RNG will process the new chances, otherwise, default to standard logic.
	 *
	 */
	public static String getGene(ItemStack stack, String entityCodeName) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null || !tag.hasKey("forceGene")) return getGene(entityCodeName);
		
		if (tag.getBoolean("mutation")) {
			int chance = tag.getInteger("chance");
			//uh oh, they used overclockers w/o chorus fruit
			if (tag.hasKey("overclocked")) chance -= tag.getInteger("overclocked") * 13;			
			//uh oh, they used high-temp... 
			if (!tag.getBoolean("lowTemp")) chance = 3;
			
			EnumGenes gene = EnumGenes.fromGeneName(tag.getString("forceGene"));
			if (ThreadLocalRandom.current().nextInt(1, 101) <= chance) 
				return gene.getMutation().toGeneName();
			if (gene != gene.getMutation()) return gene.toGeneName();
			return "GeneticsRebornMutatedGene";			
		} else {
			if (ThreadLocalRandom.current().nextInt(1, 101) < tag.getInteger("chance")) 
				return EnumGenes.fromGeneName(tag.getString("forceGene")).toGeneName();
			return "GeneticsRebornBasicGene";
		}
		
	}


	
	public static void registerArsMagica() {
		registerMob(new MobToGeneObject("EntityDryad", EnumGenes.PHOTOSYNTHESIS));
		registerMob(new MobToGeneObject("EntityNatureGuardian", EnumGenes.PHOTOSYNTHESIS));
		registerMob(new MobToGeneObject("EntityWaterGuardian", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("EntityAirGuardian", EnumGenes.NO_FALL_DAMAGE));
		registerMob(new MobToGeneObject("EntityFireGuardian", new EnumGenes[] {EnumGenes.FIRE_PROOF, EnumGenes.BIOLUMIN}));
	}

	public static void registerGrimoireOfGaia() {
		registerMob(new MobToGeneObject("EntityGaiaDryad", EnumGenes.PHOTOSYNTHESIS));
	}
	
	public static void registerCyberMobs() {
		registerMob(new MobToGeneObject("EntityCyberZombie", EnumGenes.CYBERNETIC));
	}
	
	public static void registerBotaniaMobs() {
		registerMob(new MobToGeneObject("EntityPinkWither", new EnumGenes[] {EnumGenes.WITHER_HIT, EnumGenes.WITHER_PROOF, EnumGenes.FLY}));		
		registerMob(new MobToGeneObject("EntityPixie", new EnumGenes[] {EnumGenes.PHOTOSYNTHESIS, EnumGenes.NO_FALL_DAMAGE}));
		registerMob(new MobToGeneObject("EntityDoppleganger", new EnumGenes[] {EnumGenes.INVISIBLE, EnumGenes.LUCK}));		 
	}

	public static void registerNEXMobs() {
		registerMob(new MobToGeneObject("EntitySpinout", new EnumGenes[] {EnumGenes.FIRE_PROOF, EnumGenes.THORNS}));
		registerMob(new MobToGeneObject("EntityWight", new EnumGenes[] {EnumGenes.FIRE_PROOF, EnumGenes.NO_FALL_DAMAGE, EnumGenes.INVISIBLE}));
		registerMob(new MobToGeneObject("EntityCoolmarSpider", new EnumGenes[] {EnumGenes.NIGHT_VISION, EnumGenes.POISON_PROOF}));
		registerMob(new MobToGeneObject("EntityBrute", new EnumGenes[] {EnumGenes.STRENGTH, EnumGenes.MORE_HEARTS}));
	}

	public static void registerMoCreatures() {
		//Mo'Creatures mod mobs
		registerMob(new MobToGeneObject("MoCEntityFly", EnumGenes.FLY));
		registerMob(new MobToGeneObject("MoCEntityAnchovy", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityAngelFish", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityAngler", new EnumGenes[] {EnumGenes.WATER_BREATHING, EnumGenes.BIOLUMIN}));
		registerMob(new MobToGeneObject("MoCEntityBass", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityClownFish", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityCod", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityFishy", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityGoldFish", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityHippoTang", EnumGenes.STRENGTH));
		registerMob(new MobToGeneObject("MoCEntityMantaRay", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityMediumFish", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityPiranha", new EnumGenes[] {EnumGenes.WATER_BREATHING, EnumGenes.CLAWS}));
		registerMob(new MobToGeneObject("MoCEntityRay", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntitySalmon", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityShark", new EnumGenes[] {EnumGenes.WATER_BREATHING, EnumGenes.STRENGTH, EnumGenes.CLAWS}));
		registerMob(new MobToGeneObject("MoCEntitySmallFish", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityStringRay", EnumGenes.WATER_BREATHING));

		registerMob(new MobToGeneObject("MoCEntityFireOgre", new EnumGenes[] {EnumGenes.FIRE_PROOF, EnumGenes.BIOLUMIN}));
		registerMob(new MobToGeneObject("MoCEntityOgre", EnumGenes.STRENGTH));
		registerMob(new MobToGeneObject("MoCEntityWerewolf", new EnumGenes[] {EnumGenes.STRENGTH, EnumGenes.CLAWS, EnumGenes.EMERALD_HEART}));
		registerMob(new MobToGeneObject("MoCEntityBear", new EnumGenes[] {EnumGenes.STRENGTH, EnumGenes.CLAWS}));
		registerMob(new MobToGeneObject("MoCEntityBlackBear", new EnumGenes[] {EnumGenes.STRENGTH, EnumGenes.CLAWS}));
		registerMob(new MobToGeneObject("MoCEntityBigCat", new EnumGenes[] {EnumGenes.SCARE_CREEPERS, EnumGenes.STEP_ASSIST}));
		registerMob(new MobToGeneObject("MoCEntityBunny",  new EnumGenes[] {EnumGenes.JUMP_BOOST, EnumGenes.SPEED}));
		registerMob(new MobToGeneObject("MoCEntityKitty", EnumGenes.SCARE_CREEPERS));
		registerMob(new MobToGeneObject("MoCEntityLion", EnumGenes.STRENGTH));
		registerMob(new MobToGeneObject("MoCEntityScorpion", EnumGenes.POISON_PROOF));
		registerMob(new MobToGeneObject("MoCEntityTurtle", EnumGenes.RESISTANCE));
	}
}
