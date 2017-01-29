package com.theundertaker11.GeneticsReborn;

import com.theundertaker11.GeneticsReborn.api.capability.CapabilityHandler;
import com.theundertaker11.GeneticsReborn.api.capability.genes.MobToGeneRegistry;
import com.theundertaker11.GeneticsReborn.blocks.GRBlocks;
import com.theundertaker11.GeneticsReborn.crafting.CraftingManager;
import com.theundertaker11.GeneticsReborn.event.GREventHandler;
import com.theundertaker11.GeneticsReborn.items.GRItems;
import com.theundertaker11.GeneticsReborn.keybinds.KeybindHandler;
import com.theundertaker11.GeneticsReborn.packets.GeneticsRebornPacketHandler;
import com.theundertaker11.GeneticsReborn.proxy.CommonProxy;
import com.theundertaker11.GeneticsReborn.proxy.GuiProxy;
import com.theundertaker11.GeneticsReborn.tile.GRTileEntity;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.NAME)

public class GeneticsReborn {

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
	
	public static CreativeTabs GRtab = new CreativeTabGR(CreativeTabs.getNextID(), "GRtab");
	@SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.SERVERPROXY)
	public static CommonProxy proxy;
	
	@Mod.Instance
    public static GeneticsReborn instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		loadConfig(config);
		
		MobToGeneRegistry.init();
		GRItems.init();
		GRBlocks.init();
		GRTileEntity.regTileEntitys();
		GeneticsRebornPacketHandler.init();
		
		if(event.getSide()==Side.CLIENT) KeybindHandler.init();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		CraftingManager.RegisterRecipes();
		proxy.registerRenders();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
		CapabilityHandler.init();
		GREventHandler.init();
		
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	public void loadConfig(Configuration config)
	{
		config.load();
		config.addCustomCategoryComment("General Config", "");
		config.addCustomCategoryComment("Genes", "Set any values to false to disable that gene.");

		playerGeneSharing = config.getBoolean("Enable Gene Sharing", "General Config", false, "Setting this to true will enable players being able to take the blood of other players and get all the genes from it.");
		keepGenesOnDeath = config.getBoolean("Keep genes on death", "General Config", true, "Better keep some back up syringes if this is set to false.");
		allowGivingEntityGenes = config.getBoolean("Allow giving other entities genes", "General Config", false, "If this is enabled players can give animals such as horses genes with the metal syringe");
		
		enableDragonsBreath = config.getBoolean("Dragon's Breath", "Genes", true, "");
		enableEatGrass = config.getBoolean("Eat Grass", "Genes", true, "");
		enableEmeraldHeart = config.getBoolean("Emerald Heart", "Genes", true, "");
		enableEnderDragonHealth = config.getBoolean("Ender Dragon Health", "Genes", true, "");
		enableFireProof = config.getBoolean("Fire-Proof", "Genes", true, "");
		enableFlight = config.getBoolean("Flight", "Genes", true, "");
		enableJumpBoost = config.getBoolean("Jump Boost", "Genes", true, "");
		enableMilky = config.getBoolean("Milky", "Genes", true, "");
		enableMoreHearts = config.getBoolean("More Hearts", "Genes", true, "");
		enableNightVision = config.getBoolean("Night Vision", "Genes", true, "");
		enableNoFallDamage = config.getBoolean("No Fall Damage", "Genes", true, "");
		enablePoisonProof = config.getBoolean("Poison Proof", "Genes", true, "");
		enableResistance = config.getBoolean("Resistance", "Genes", true, "");
		enableSaveInventory = config.getBoolean("Keep Inventory", "Genes", true, "");
		enableScareCreepers = config.getBoolean("Scare Creepers", "Genes", true, "");
		enableShootFireballs = config.getBoolean("Shoot Fireballs", "Genes", true, "");
		enableSlimy = config.getBoolean("Slimy", "Genes", true, "");
		enableSpeed = config.getBoolean("Speed", "Genes", true, "");
		enableStrength = config.getBoolean("Strength", "Genes", true, "");
		enableTeleporter = config.getBoolean("Teleporter", "Genes", true, "");
		enableWaterBreathing = config.getBoolean("Water Breathing", "Genes", true, "");
		enableWooly = config.getBoolean("Wooly", "Genes", true, "");
		enableWitherHit = config.getBoolean("Wither Hit", "Genes", true, "");
		enableWitherProof = config.getBoolean("Wither Proof", "Genes", true, "");
		enableItemMagnet = config.getBoolean("Item Attraction Field", "Genes", true, "");
		enableXPMagnet = config.getBoolean("XP Attraction Field", "Genes", true, "");
		enableExplosiveExit = config.getBoolean("Explosive Exit", "Genes", true, "");


		config.save();
	}
}
