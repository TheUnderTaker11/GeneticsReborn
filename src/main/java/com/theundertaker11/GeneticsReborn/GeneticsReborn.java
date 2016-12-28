package com.theundertaker11.GeneticsReborn;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import com.theundertaker11.GeneticsReborn.crafting.CraftingManager;
import com.theundertaker11.GeneticsReborn.proxy.CommonProxy;

@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.NAME)

public class GeneticsReborn {

	
	public static CreativeTabs GRtab = new CreativeTabGR(CreativeTabs.getNextID(), "GRtab");
	@SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.SERVERPROXY)
	public static CommonProxy proxy;
	
	public static boolean debugMode = false;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		config.addCustomCategoryComment("Items", "Set any values to false to disable the recipe(s) for this item.");
		//QuarryRadius = config.getInt("Radius of Quarry", "Quarry", 8, 3, 50, "Changes the radius of the Quarry(Mines to bedrock in that radius)");
		
		config.save();

		

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		CraftingManager.RegisterRecipes();
		proxy.registerRenders();
		MinecraftForge.EVENT_BUS.register(new GREventHandler());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	

}
