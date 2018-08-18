package com.theundertaker11.geneticsreborn;

import com.theundertaker11.geneticsreborn.api.capability.CapabilityHandler;
import com.theundertaker11.geneticsreborn.api.capability.genes.MobToGeneRegistry;
import com.theundertaker11.geneticsreborn.blocks.GRBlocks;
import com.theundertaker11.geneticsreborn.crafting.CraftingManager;
import com.theundertaker11.geneticsreborn.event.GREventHandler;
import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.keybinds.KeybindHandler;
import com.theundertaker11.geneticsreborn.packets.GeneticsRebornPacketHandler;
import com.theundertaker11.geneticsreborn.proxy.CommonProxy;
import com.theundertaker11.geneticsreborn.proxy.GuiProxy;
import com.theundertaker11.geneticsreborn.tile.GRTileEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.NAME, acceptedMinecraftVersions = Reference.ACCEPTED_MINECRAFT)
public class GeneticsReborn {

	@Mod.Instance
	public static GeneticsReborn instance;

	public static final CreativeTabs GRtab = new CreativeTabGR(CreativeTabs.getNextID(), "GRtab");

	@SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.SERVERPROXY)
	public static CommonProxy proxy;

	public static final Logger log = FMLLog.getLogger();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "GeneticsReborn/GeneticsReborn.cfg"));
		loadConfig(config);
		JsonHandler.handleJson(event);

		MobToGeneRegistry.init();
		GRItems.init();
		GRBlocks.init();
		GRTileEntity.regTileEntitys();
		GeneticsRebornPacketHandler.init();

		if (event.getSide() == Side.CLIENT)
			KeybindHandler.init();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		CraftingManager.RegisterRecipes();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
		CapabilityHandler.init();
		GREventHandler.init();

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	public static boolean playerGeneSharing;
	public static boolean keepGenesOnDeath;
	public static boolean allowGivingEntityGenes;

	public static boolean enableDragonsBreath;
	public static boolean enableEatGrass;
	public static boolean enableEmeraldHeart;
	public static boolean enableEnderDragonHealth;
	public static boolean enableFireProof;
	public static boolean enableFlight;
	public static boolean enableJumpBoost;
	public static boolean enableMilky;
	public static boolean enableMoreHearts;
	public static boolean enableNightVision;
	public static boolean enableNoFallDamage;
	public static boolean enablePoisonProof;
	public static boolean enableResistance;
	public static boolean enableSaveInventory;
	public static boolean enableScareCreepers;
	public static boolean enableShootFireballs;
	public static boolean enableSlimy;
	public static boolean enableSpeed;
	public static boolean enableStrength;
	public static boolean enableTeleporter;
	public static boolean enableWaterBreathing;
	public static boolean enableWooly;
	public static boolean enableWitherHit;
	public static boolean enableWitherProof;
	public static boolean enableItemMagnet;
	public static boolean enableXPMagnet;
	public static boolean enableExplosiveExit;
	public static boolean enablePhotosynthesis;
	public static String[] CloningBlacklist;

	public static int maxEnergyStored;

	public static int baseRfPerTickBloodPurifier;
	public static int baseRfPerTickCellAnalyser;
	public static int baseRfPerTickCloningMachine;
	public static int baseRfPerTickDNADecrypter;
	public static int baseRfPerTickDNAExtractor;
	public static int baseRfPerTickPlasmidInfuser;
	public static int baseRfPerTickPlasmidInjector;

	public static int baseTickBloodPurifier;
	public static int baseTickCellAnalyser;
	public static int baseTickCloningMachine;
	public static int baseTickDNADecrypter;
	public static int baseTickDNAExtractor;
	public static int baseTickPlasmidInfuser;
	public static int baseTickPlasmidInjector;

	public static int ocBloodPurifier;
	public static int ocCellAnalyser;
	public static int ocCloningMachine;
	public static int ocDNADecrypter;
	public static int ocDNAExtractor;
	public static int ocPlasmidInfuser;
	public static int ocPlasmidInjector;
	public static int ocCoalGenerator;

	public static int CoalGeneratorBaseRF;

	public static void loadConfig(Configuration config) {
		config.load();
		final String general = "General Config";
		final String genes = "Genes";
		final String baseticks = "baseticks";
		final String baserf = "baserfpertick";
		final String oclockers = "Change max amount of Overclockers for each machine";

		config.addCustomCategoryComment(general, "");
		config.addCustomCategoryComment(genes, "Set any values to false to disable that gene.");
		config.addCustomCategoryComment(oclockers, "Changes max overclockers for each machine");
		config.addCustomCategoryComment(baseticks, "Changes base ticks needed for each machine, max overclockers will always make the machine take 1-2 ticks. Reducing/Increasing this also increases total rf used by the machine.");
		config.addCustomCategoryComment(baserf, "Base rf/t use of each machine.");

		playerGeneSharing = config.getBoolean("Enable Gene Sharing", general, false, "Setting this to true will enable players being able to take the blood of other players and get all the genes from it.");
		keepGenesOnDeath = config.getBoolean("Keep genes on death", general, true, "Better keep some back up syringes if this is set to false.");
		allowGivingEntityGenes = config.getBoolean("Allow giving other entities genes", general, false, "If this is enabled players can give animals such as horses genes with the metal syringe");

		enableDragonsBreath = config.getBoolean("Dragon's Breath", genes, true, "");
		enableEatGrass = config.getBoolean("Eat Grass", genes, true, "");
		enableEmeraldHeart = config.getBoolean("Emerald Heart", genes, true, "");
		enableEnderDragonHealth = config.getBoolean("Ender Dragon Health", genes, true, "");
		enableFireProof = config.getBoolean("Fire-Proof", genes, true, "");
		enableFlight = config.getBoolean("Flight", genes, true, "");
		enableJumpBoost = config.getBoolean("Jump Boost", genes, true, "");
		enableMilky = config.getBoolean("Milky", genes, true, "");
		enableMoreHearts = config.getBoolean("More Hearts", genes, true, "");
		enableNightVision = config.getBoolean("Night Vision", genes, true, "");
		enableNoFallDamage = config.getBoolean("No Fall Damage", genes, true, "");
		enablePoisonProof = config.getBoolean("Poison Proof", genes, true, "");
		enableResistance = config.getBoolean("Resistance", genes, true, "");
		enableSaveInventory = config.getBoolean("Keep Inventory", genes, true, "");
		enableScareCreepers = config.getBoolean("Scare Creepers", genes, true, "");
		enableShootFireballs = config.getBoolean("Shoot Fireballs", genes, true, "");
		enableSlimy = config.getBoolean("Slimy", genes, true, "");
		enableSpeed = config.getBoolean("Speed", genes, true, "");
		enableStrength = config.getBoolean("Strength", genes, true, "");
		enableTeleporter = config.getBoolean("Teleporter", genes, true, "");
		enableWaterBreathing = config.getBoolean("Water Breathing", genes, true, "");
		enableWooly = config.getBoolean("Wooly", genes, true, "");
		enableWitherHit = config.getBoolean("Wither Hit", genes, true, "");
		enableWitherProof = config.getBoolean("Wither Proof", genes, true, "");
		enableItemMagnet = config.getBoolean("Item Attraction Field", genes, true, "");
		enableXPMagnet = config.getBoolean("XP Attraction Field", genes, true, "");
		enableExplosiveExit = config.getBoolean("Explosive Exit", genes, true, "");
		enablePhotosynthesis = config.getBoolean("Photosynthesis", genes, true, "");
		CloningBlacklist = config.getStringList("Cloning Blacklist", general, new String[]{"EntityWither"}, "Add the name of the Entity's class you want blacklisted. (The ender dragon will always be hardcode blacklisted.)");

		maxEnergyStored = config.getInt("Max", general, 20000, 10000, 1000000000, "Changes max RF stored by all machines");

		baseTickBloodPurifier = config.getInt("Blood Purifier", baseticks, 200, 2, 5000, "");
		baseTickCellAnalyser = config.getInt("Cell Analyser", baseticks, 400, 2, 5000, "");
		baseTickCloningMachine = config.getInt("Cloning Machine", baseticks, 200, 2, 5000, "");
		baseTickDNADecrypter = config.getInt("DNA Decrypter", baseticks, 200, 2, 5000, "");
		baseTickDNAExtractor = config.getInt("DNA Extractor", baseticks, 200, 2, 5000, "");
		baseTickPlasmidInfuser = config.getInt("Plasmid Infuser", baseticks, 400, 2, 5000, "");
		baseTickPlasmidInjector = config.getInt("Plasmid Injector", baseticks, 400, 2, 5000, "");

		baseRfPerTickBloodPurifier = config.getInt("Blood Purifier", baserf, 20, 1, 100000, "");
		baseRfPerTickCellAnalyser = config.getInt("Cell Analyser", baserf, 20, 1, 100000, "");
		baseRfPerTickCloningMachine = config.getInt("Cloning Machine", baserf, 500, 1, 100000, "");
		baseRfPerTickDNADecrypter = config.getInt("DNA Decrypter", baserf, 20, 1, 100000, "");
		baseRfPerTickDNAExtractor = config.getInt("DNA Extractor", baserf, 20, 1, 100000, "");
		baseRfPerTickPlasmidInfuser = config.getInt("Plasmid Infuser", baserf, 20, 1, 100000, "");
		baseRfPerTickPlasmidInjector = config.getInt("Plasmid Injector", baserf, 20, 1, 100000, "");

		ocBloodPurifier = config.getInt("Blood Purifier", oclockers, 5, 0, 5, "");
		ocCellAnalyser = config.getInt("Cell Analyser", oclockers, 10, 0, 10, "");
		ocCloningMachine = config.getInt("Cloning Machine", oclockers, 3, 0, 3, "");
		ocDNADecrypter = config.getInt("DNA Decrypter", oclockers, 5, 0, 5, "");
		ocDNAExtractor = config.getInt("DNA Extractor", oclockers, 5, 0, 5, "");
		ocPlasmidInfuser = config.getInt("Plasmid Infuser", oclockers, 10, 0, 10, "");
		ocPlasmidInjector = config.getInt("Plasmid Injector", oclockers, 10, 0, 10, "");
		ocCoalGenerator = config.getInt("Coal Generator", oclockers, 10, 0, 10, "");

		CoalGeneratorBaseRF = config.getInt("Base rf/t production", "Power Gen", 10, 5, 1000000, "");
		config.save();
	}
}
