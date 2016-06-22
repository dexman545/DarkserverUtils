package com.failmelon.darkserverutils;

import java.io.File;

import com.failmelon.darkserverutils.commands.BackCommand;
import com.failmelon.darkserverutils.commands.DarkCommands;
import com.failmelon.darkserverutils.commands.TPACommand;
import com.failmelon.darkserverutils.commands.AcceptCommand;
import com.failmelon.darkserverutils.events.BlockEvents;
import com.failmelon.darkserverutils.events.PlayerEvents;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import cpw.mods.fml.common.registry.GameData;

@Mod(modid=DarkServerUtils.MODID, name=DarkServerUtils.MODName, version=DarkServerUtils.VERSION, acceptableRemoteVersions = "*", dependencies = "before:TerraFirmaCraft")
public class DarkServerUtils
{
	@Instance("darkserverutils")
	public static DarkServerUtils instance;
	
    public static final String MODID = "darkserverutils";
    public static final String MODName = "Dark Server Utilities";
    public static final String VERSION = "0.2.1";
	public static final String CHANNEL = "darkserverutils";

	public static Configuration Config;
	
	public static int NoobTime = 576000;
	
	public static boolean NoobProtection = false;
	
	public static boolean GetBlockBlacklist(String cat, Block block)
	{		
		if (Config == null) return true;
		
		return Config.get(cat, GameData.getBlockRegistry().getNameForObject(block), true).getBoolean();
	}
	
	public static boolean GetItemBlacklist(String cat, Item item)
	{			
		if (Config == null) return true;
		
		return Config.get(cat, GameData.getItemRegistry().getNameForObject(item), true).getBoolean();
	}
	
	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{		
		LoadCommonConfig(event.getModConfigurationDirectory());
				
		ConfigCategory oredictionys = Config.getCategory("oredictionary");
		for(String block : oredictionys.getValues().keySet())
		{
			Block mcblock = GameData.getBlockRegistry().getObject(block);
			Item mcitem = GameData.getItemRegistry().getObject(block);
			
			if (mcblock != null && mcblock != Blocks.air)
			{
				OreDictionary.registerOre(oredictionys.get(block).getString(), new ItemStack(mcblock));
				continue;
			}
			
			if (mcitem != null)
				OreDictionary.registerOre(oredictionys.get(block).getString(), new ItemStack(mcitem));
		}
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{

	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{				
		MinecraftForge.EVENT_BUS.register(new BlockEvents());
		
		event.registerServerCommand(new DarkCommands());
		
		if (NoobProtection)
		{
			// Register our noob protection system commands
			event.registerServerCommand(new AcceptCommand());
			event.registerServerCommand(new TPACommand());
			event.registerServerCommand(new BackCommand());
			
			// Register our events
			MinecraftForge.EVENT_BUS.register(new PlayerEvents());
			
			// Register our event buses
			FMLCommonHandler.instance().bus().register(new PlayerEvents());
		}

		if (Config != null)
			Config.save();
	}
	
	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{		
		if (Config != null)
		{
			ConfigCategory harvestlevels = Config.getCategory("harvestlevel");
			for(String block : harvestlevels.getValues().keySet())
			{
				Item item = GameData.getItemRegistry().getObject(block);
				String harvestlevel = harvestlevels.get(block).getString();
				String[] harvestleveldata = harvestlevel.split(":");

				if (item != null && harvestleveldata.length == 2)
					item.setHarvestLevel(harvestleveldata[0], Integer.parseInt(harvestleveldata[1]));
			}
			
			ConfigCategory durabilities = Config.getCategory("durability");
			for(String block : durabilities.getValues().keySet())
			{
				Item item = GameData.getItemRegistry().getObject(block);
				if (item != null)
					item.setMaxDamage(durabilities.get(block).getInt());
			}
			
			ConfigCategory hardness = Config.getCategory("hardness");
			for(String block : hardness.getValues().keySet())
			{
				Block mcblock = GameData.getBlockRegistry().getObject(block);
				if (mcblock != null)
					mcblock.setHardness(hardness.get(block).getInt());
			}
			
			ConfigCategory lightlevel = Config.getCategory("lightlevel");
			for(String block : lightlevel.getValues().keySet())
			{
				Block mcblock = GameData.getBlockRegistry().getObject(block);
				if (mcblock != null)
					mcblock.setLightLevel((float)lightlevel.get(block).getDouble());
			}
			
			ConfigCategory resistance = Config.getCategory("blastresistance");
			for(String block : resistance.getValues().keySet())
			{
				Block mcblock = GameData.getBlockRegistry().getObject(block);
				if (mcblock != null)
					mcblock.setResistance((int)resistance.get(block).getDouble());
			}
			
			ConfigCategory fireinfos = Config.getCategory("fireinfo");
			for(String block : fireinfos.getValues().keySet())
			{
				Block mcblock = GameData.getBlockRegistry().getObject(block);
				String fireinfo = fireinfos.get(block).getString();
				String[] fireinfodata = fireinfo.split(":");
				
				if (mcblock != null && fireinfodata.length == 2)
					Blocks.fire.setFireInfo(mcblock, Integer.parseInt(fireinfodata[0]), Integer.parseInt(fireinfodata[1]));
			}
	
		}
	}
	
	public void LoadCommonConfig(File location)
	{
		// Construct our configuration file
		Config = new Configuration(new File(location, "/DarkServer/Common.cfg"));
		
		try
		{
			// Attempt to load it up
			Config.load();
		} catch (Exception e)
		{
			// Cannot load report error
			Logger.WriteLog("Error while trying to load Common.cfg");
			Logger.WriteLog(e.getMessage());
			Config = null;
		}

		if (Config != null)
		{
			NoobProtection = Config.get("Common", "noobprotection", false).getBoolean();
			Config.get("canplaceblock", "minecraft:chest", false);
			Config.get("canplaceblock", "BiblioCraft:BiblioIronLanter", false);
			Config.get("canplaceblock", "BiblioCraft:BiblioLantern", false);
			Config.get("canplaceblock", "Thaumcraft:blockAiry", false);
			Config.get("canplaceblock", "Thaumcraft:blockCandle", false);
			Config.get("canplaceblock", "minecraft:redstone_torch", false);
				
			Config.get("canuseonblock", "minecraft:redstone_torch", false);
			Config.get("canuseonblock", "minecraft:chest", false);
			Config.get("canuseonblock", "BiblioCraft:BiblioIronLanter", false);
			Config.get("canuseonblock", "BiblioCraft:BiblioLantern", false);
			Config.get("canuseonblock", "Thaumcraft:blockCandle", false);
			Config.get("canuseonblock", "Thaumcraft:ItemResource", false);
			
			Config.save();
		}		
	}
}
