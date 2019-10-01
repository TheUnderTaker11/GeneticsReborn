package com.theundertaker11.geneticsreborn.api.capability.genes;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.JsonHandler;
import com.theundertaker11.geneticsreborn.util.MobToGeneObject;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

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
			registerMob(new MobToGeneObject("EntityVillager", EnumGenes.EMERALD_HEART));
			registerMob(new MobToGeneObject("EntitySheep", new EnumGenes[] {EnumGenes.EAT_GRASS, EnumGenes.WOOLY}));
			registerMob(new MobToGeneObject("EntityCow", new EnumGenes[] {EnumGenes.EAT_GRASS, EnumGenes.MILKY}));
			registerMob(new MobToGeneObject("EntityHorse", new EnumGenes[] {EnumGenes.JUMP_BOOST, EnumGenes.STEP_ASSIST, EnumGenes.SPEED}));
			registerMob(new MobToGeneObject("EntityChicken", EnumGenes.NO_FALL_DAMAGE));
			registerMob(new MobToGeneObject("EntityBat", new EnumGenes[] {EnumGenes.NIGHT_VISION}));
			registerMob(new MobToGeneObject("EntityOcelot", new EnumGenes[] {EnumGenes.SPEED, EnumGenes.SCARE_CREEPERS}));
			registerMob(new MobToGeneObject("EntityPolarBear", new EnumGenes[] {EnumGenes.STRENGTH, EnumGenes.STEP_ASSIST}));
			registerMob(new MobToGeneObject("EntityRabbit", new EnumGenes[] {EnumGenes.JUMP_BOOST, EnumGenes.SPEED}));
			registerMob(new MobToGeneObject("EntitySquid", EnumGenes.WATER_BREATHING));
			registerMob(new MobToGeneObject("EntityIronGolem", EnumGenes.MORE_HEARTS));
	
			//Overworld Hostiles
			registerMob(new MobToGeneObject("EntityZombie", EnumGenes.RESISTANCE));
			registerMob(new MobToGeneObject("EntityCreeper", EnumGenes.EXPLOSIVE_EXIT));
			registerMob(new MobToGeneObject("EntitySpider", EnumGenes.NIGHT_VISION));
			registerMob(new MobToGeneObject("EntityCaveSpider", new EnumGenes[] {EnumGenes.NIGHT_VISION, EnumGenes.POISON_PROOF}));
			registerMob(new MobToGeneObject("EntitySlime", EnumGenes.NO_FALL_DAMAGE));
			registerMob(new MobToGeneObject("EntityGuardian", EnumGenes.WATER_BREATHING));
			registerMob(new MobToGeneObject("EntitySkeleton", EnumGenes.INFINITY));
	
			//End hostiles
			registerMob(new MobToGeneObject("EntityEndermite", new EnumGenes[] {EnumGenes.SAVE_INVENTORY, EnumGenes.ITEM_MAGNET, EnumGenes.XP_MAGNET}));
			registerMob(new MobToGeneObject("EntityEnderman", new EnumGenes[] {EnumGenes.TELEPORTER, EnumGenes.MORE_HEARTS}));
			registerMob(new MobToGeneObject("EntityShulker", EnumGenes.RESISTANCE));
	
			//Nether hostiles
			registerMob(new MobToGeneObject("Wither Skeleton", new EnumGenes[] {EnumGenes.WITHER_HIT, EnumGenes.INFINITY} ));
			registerMob(new MobToGeneObject("EntityBlaze", new EnumGenes[] {EnumGenes.FLY, EnumGenes.SHOOT_FIREBALLS, EnumGenes.FIRE_PROOF}));
			registerMob(new MobToGeneObject("EntityGhast", new EnumGenes[] {EnumGenes.FLY, EnumGenes.SHOOT_FIREBALLS}));
			registerMob(new MobToGeneObject("EntityPigZombie", EnumGenes.FIRE_PROOF));
			registerMob(new MobToGeneObject("EntityMagmaCube", EnumGenes.FIRE_PROOF));
	
			//Bosses
			registerMob(new MobToGeneObject("EntityWither", new EnumGenes[] {EnumGenes.WITHER_PROOF, EnumGenes.FLY}));
			registerMob(new MobToGeneObject("Ender Dragon", new EnumGenes[] {EnumGenes.DRAGONS_BREATH, EnumGenes.ENDER_DRAGON_HEALTH}));
		}

		if (GeneticsReborn.registerModGenes) {
			try {
				if (Loader.isModLoaded("arsmagica2")) registerArsMagica();
				if (Loader.isModLoaded("grimoireofgaia")) registerGrimoireOfGaia();
				if (Loader.isModLoaded("mocreatures")) registerMoCreatures();
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
		String genename = "BasicGene";
		String g = "GeneticsReborn";
		int numb = ThreadLocalRandom.current().nextInt(1, 101);
		if (numb > 40)
			return g + genename;

		MobToGeneObject object = null;
		for (MobToGeneObject obj : list) {
			if (obj.MobCodeName.equals(entityCodeName)) {
				object = obj;
				break;
			}
		}
		if (object == null || object.getValidGenesNum() == 0)
			return g + genename;


		genename = object.getRandomGene();
		String fullname = g + genename;

		if (!ModUtils.isGeneEnabled(fullname)) {
			fullname = g + "BasicGene";
		}

		return fullname;
	}


	@Optional.Method(modid = "arsmagica2")
	public static void registerArsMagica() {
		registerMob(new MobToGeneObject("EntityDryad", EnumGenes.PHOTOSYNTHESIS));
		registerMob(new MobToGeneObject("EntityNatureGuardian", EnumGenes.PHOTOSYNTHESIS));
		registerMob(new MobToGeneObject("EntityWaterGuardian", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("EntityAirGuardian", EnumGenes.NO_FALL_DAMAGE));
		registerMob(new MobToGeneObject("EntityFireGuardian", EnumGenes.FIRE_PROOF));
	}

	@Optional.Method(modid = "grimoireofgaia")
	public static void registerGrimoireOfGaia() {
		registerMob(new MobToGeneObject("EntityGaiaDryad", EnumGenes.PHOTOSYNTHESIS));
	}

	@Optional.Method(modid = "mocreatures")
	public static void registerMoCreatures() {
		//Mo'Creatures mod mobs
		registerMob(new MobToGeneObject("MoCEntityFly", EnumGenes.FLY));
		registerMob(new MobToGeneObject("MoCEntityAnchovy", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityAngelFish", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityAngler", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityBass", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityClownFish", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityCod", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityFishy", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityGoldFish", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityHippoTang", EnumGenes.STRENGTH));
		registerMob(new MobToGeneObject("MoCEntityMantaRay", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityMediumFish", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityPiranha", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityRay", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntitySalmon", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityShark", new EnumGenes[] {EnumGenes.WATER_BREATHING, EnumGenes.STRENGTH}));
		registerMob(new MobToGeneObject("MoCEntitySmallFish", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("MoCEntityStringRay", EnumGenes.WATER_BREATHING));

		registerMob(new MobToGeneObject("MoCEntityFireOgre", EnumGenes.FIRE_PROOF));
		registerMob(new MobToGeneObject("MoCEntityOgre", EnumGenes.STRENGTH));
		registerMob(new MobToGeneObject("MoCEntityWerewolf", EnumGenes.STRENGTH));
		registerMob(new MobToGeneObject("MoCEntityBear", EnumGenes.STRENGTH));
		registerMob(new MobToGeneObject("MoCEntityBlackBear", EnumGenes.STRENGTH));
		registerMob(new MobToGeneObject("MoCEntityBigCat", EnumGenes.SCARE_CREEPERS));
		registerMob(new MobToGeneObject("MoCEntityBunny", EnumGenes.JUMP_BOOST));
		registerMob(new MobToGeneObject("MoCEntityKitty", EnumGenes.SCARE_CREEPERS));
		registerMob(new MobToGeneObject("MoCEntityLion", EnumGenes.STRENGTH));
		registerMob(new MobToGeneObject("MoCEntityScorpion", EnumGenes.POISON_PROOF));
		registerMob(new MobToGeneObject("MoCEntityTurtle", EnumGenes.RESISTANCE));
	}
}
