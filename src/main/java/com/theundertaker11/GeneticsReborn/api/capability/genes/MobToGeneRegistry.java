package com.theundertaker11.GeneticsReborn.api.capability.genes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraftforge.fml.common.Optional;

import com.theundertaker11.GeneticsReborn.util.MobToGeneObject;
/**
 * This is used by the DNA Decrypter to tell if a mob should drop a given gene. 
 * DO NOT TRY TO ADD GENES TO MOBS I'VE ALREADY REGISTERED GENES TO. It is simply not made to handle that.
 * It should work perfect for adding existing genes to mobs I haven't registered though.
 * 
 * @author TheUnderTaker11
 *
 */
public class MobToGeneRegistry {

	private static HashSet<MobToGeneObject> list = new HashSet<MobToGeneObject>();
	
	public static void init()
	{
		//Peacefuls
		registerMob(new MobToGeneObject("EntityVillager", EnumGenes.EMERALD_HEART));
		registerMob(new MobToGeneObject("EntitySheep", EnumGenes.EAT_GRASS, EnumGenes.WOOLY));
		registerMob(new MobToGeneObject("EntityCow", EnumGenes.EAT_GRASS, EnumGenes.MILKY));
		registerMob(new MobToGeneObject("EntityHorse", EnumGenes.JUMP_BOOST));
		registerMob(new MobToGeneObject("EntityChicken", EnumGenes.NO_FALL_DAMAGE));
		registerMob(new MobToGeneObject("EntityBat", EnumGenes.FLY, EnumGenes.NIGHT_VISION));
		registerMob(new MobToGeneObject("EntityOcelot", EnumGenes.SPEED, EnumGenes.SCARE_CREEPERS));
		registerMob(new MobToGeneObject("EntityPolarBear", EnumGenes.STRENGTH));
		registerMob(new MobToGeneObject("EntityRabbit", EnumGenes.JUMP_BOOST, EnumGenes.SPEED));
		registerMob(new MobToGeneObject("EntitySquid", EnumGenes.WATER_BREATHING));
		registerMob(new MobToGeneObject("EntityIronGolem", EnumGenes.MORE_HEARTS));
		
		//Overworld Hostiles
		registerMob(new MobToGeneObject("EntityZombie", EnumGenes.RESISTANCE));
		registerMob(new MobToGeneObject("EntityCreeper", EnumGenes.EXPLOSIVE_EXIT));
		registerMob(new MobToGeneObject("EntitySpider", EnumGenes.NIGHT_VISION));
		registerMob(new MobToGeneObject("EntityCaveSpider", EnumGenes.NIGHT_VISION, EnumGenes.POISON_PROOF));
		registerMob(new MobToGeneObject("EntitySlime", EnumGenes.SLIMY));
		registerMob(new MobToGeneObject("EntityGuardian", EnumGenes.WATER_BREATHING));
		
		//End hostiles
		registerMob(new MobToGeneObject("EntityEndermite", EnumGenes.SAVE_INVENTORY, EnumGenes.ITEM_MAGNET, EnumGenes.XP_MAGNET));
		registerMob(new MobToGeneObject("EntityEnderman", EnumGenes.TELEPORTER, EnumGenes.MORE_HEARTS));
		registerMob(new MobToGeneObject("EntityShulker", EnumGenes.RESISTANCE));
		
		//Nether hostiles
		registerMob(new MobToGeneObject("Wither Skeleton", EnumGenes.WITHER_HIT));
		registerMob(new MobToGeneObject("EntityBlaze", EnumGenes.SHOOT_FIREBALLS, EnumGenes.FIRE_PROOF));
		registerMob(new MobToGeneObject("EntityGhast", EnumGenes.FLY, EnumGenes.SHOOT_FIREBALLS));
		registerMob(new MobToGeneObject("EntityPigZombie", EnumGenes.FIRE_PROOF));
		registerMob(new MobToGeneObject("EntityMagmaCube", EnumGenes.FIRE_PROOF));
		
		//Bosses
		registerMob(new MobToGeneObject("EntityWither", EnumGenes.WITHER_PROOF));
		registerMob(new MobToGeneObject("Ender Dragon", EnumGenes.DRAGONS_BREATH, EnumGenes.ENDER_DRAGON_HEALTH));
		
		try{
			registerMoCreatures();
		}catch(NoSuchMethodError e){
			
		}
		//Unused entities below
		/*
		 registerMob(new MobToGeneObject("EntitySkeleton", EnumGenes.None));
		 registerMob(new MobToGeneObject("EntitySilverfish", EnumGenes.None));
		 registerMob(new MobToGeneObject("EntityPig", EnumGenes.None));
		 registerMob(new MobToGeneObject("EntityWitch", EnumGenes.None));
		 registerMob(new MobToGeneObject("EntityPolarBear", EnumGenes.None));
		 registerMob(new MobToGeneObject("EntityMooshroom", EnumGenes.None));
		 registerMob(new MobToGeneObject("EntityWolf", EnumGenes.None));
		 registerMob(new MobToGeneObject("EntitySnowman", EnumGenes.None));
		 */
	}
	public static void registerMob(MobToGeneObject obj)
	{
		list.add(obj);
	}
	/**
	 * Returns the gene in String form, prefixed with GeneticsReborn
	 * 
	 * @param entityCodeName name gotten from #getClass()#getSimpleName() on an EntityLivingBase
	 * @return Said above.
	 */
	public static String getGene(String entityCodeName)
	{
		String genename = "BasicGene";
		int numb = ThreadLocalRandom.current().nextInt(1, 101);
		if(numb>40) return "GeneticsReborn"+genename;
		
		MobToGeneObject object = null;
		for(MobToGeneObject obj : list)
		{
			if(obj.MobCodeName.equals(entityCodeName))
			{
				object = obj;
				break;
			}
		}
		if(object==null||object.getValidGenesNum()==0) return "GeneticsReborn"+genename;
		
		
		List<EnumGenes> genes = new ArrayList<EnumGenes>();
		genes.add(object.Gene1);
		if(object.Gene2!=null) genes.add(object.Gene2);
		if(object.Gene3!=null) genes.add(object.Gene3);
		if(genes.size()==1) 
			genename = genes.get(0).toString();
		else if(genes.size()==2) 
			genename = genes.get(ThreadLocalRandom.current().nextInt(2)).toString();
		else if(genes.size()==3) 
			genename = genes.get(ThreadLocalRandom.current().nextInt(3)).toString();
		
		return "GeneticsReborn"+genename;
	}
	
	@Optional.Method(modid = "mocreatures")
	public static void registerMoCreatures()
	{
		//Mo'Creatures mod mobs
		System.out.println("Registering Mo'Creatures' mob genes");
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
		registerMob(new MobToGeneObject("MoCEntityShark", EnumGenes.WATER_BREATHING, EnumGenes.STRENGTH));
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
