package com.theundertaker11.GeneticsReborn;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import com.theundertaker11.GeneticsReborn.api.capability.CapabilityHandler;
import com.theundertaker11.GeneticsReborn.api.capability.Genes;
import com.theundertaker11.GeneticsReborn.api.capability.GenesStorage;
import com.theundertaker11.GeneticsReborn.api.capability.IGenes;
import com.theundertaker11.GeneticsReborn.blocks.GRBlocks;
import com.theundertaker11.GeneticsReborn.crafting.CraftingManager;
import com.theundertaker11.GeneticsReborn.items.GRItems;
import com.theundertaker11.GeneticsReborn.proxy.CommonProxy;
import com.theundertaker11.GeneticsReborn.proxy.GuiProxy;
import com.theundertaker11.GeneticsReborn.tile.GRTileEntity;

@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.NAME)

public class GeneticsReborn {

	public static boolean playerGeneSharing;
	public static boolean keepGenesOnDeath;
	
	public static boolean enableEatGrass;
	public static boolean enableEnderDragonHealth;
	public static boolean enableFlight;
	public static boolean enableFireProof;
	
	public static CreativeTabs GRtab = new CreativeTabGR(CreativeTabs.getNextID(), "GRtab");
	@SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.SERVERPROXY)
	public static CommonProxy proxy;
	
	@Mod.Instance
    public static GeneticsReborn instance;
	
	public static boolean debugMode = false;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		config.addCustomCategoryComment("General Config", "");
		config.addCustomCategoryComment("Genes", "Set any values to false to disable that gene.");
		//QuarryRadius = config.getInt("Radius of Quarry", "Quarry", 8, 3, 50, "Changes the radius of the Quarry(Mines to bedrock in that radius)");
		playerGeneSharing = config.getBoolean("Enable Gene Sharing", "General Config", false, "Setting this to true will enable players being able to take the blood of other players and get all the genes from it.");
		keepGenesOnDeath = config.getBoolean("Keep genes on death", "General Config", true, "Better keep some back up syringes if this is set to false.");
		
		enableEatGrass = config.getBoolean("Eat Grass", "Genes", true, "");
		enableEnderDragonHealth = config.getBoolean("Ender Dragon Health", "Genes", true, "");
		enableFlight = config.getBoolean("Flight", "Genes", true, "");
		enableFireProof = config.getBoolean("Fire Proof", "Genes", true, "");
		
		config.save();

		GRItems.init();
		GRBlocks.init();
		GRTileEntity.regTileEntitys();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		CraftingManager.RegisterRecipes();
		proxy.registerRenders();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
		MinecraftForge.EVENT_BUS.register(new GREventHandler());
		CapabilityManager.INSTANCE.register(IGenes.class, new GenesStorage(), Genes.class);
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	

}
