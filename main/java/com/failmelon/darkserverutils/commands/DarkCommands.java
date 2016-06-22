package com.failmelon.darkserverutils.commands;

import java.io.File;

import com.failmelon.darkserverutils.DarkServerUtils;
import com.failmelon.darkserverutils.Logger;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.config.Configuration;

public class DarkCommands extends CommandBase
{

	@Override
	public String getCommandName() {

		return "darkserver";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) 
	{
		return "commands.darkserver.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params) 
	{
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        
		// Need to give a player in the arguments
        if (params.length < 2)
        {
            throw new net.minecraft.command.WrongUsageException("commands.darkserver.usage", new Object[0]);
        }   
                
        if (params[0].equalsIgnoreCase("config") && params[1].equalsIgnoreCase("reload"))
        {
    		Configuration oldconfig = DarkServerUtils.Config;

    		try
    		{
    			if (DarkServerUtils.Config != null)
    			{
    				player.addChatMessage(new ChatComponentText("Loading Common.cfg"));
    				
    				DarkServerUtils.Config = new Configuration(new File(oldconfig.getConfigFile().getAbsolutePath()));
        			// Attempt to load it up
        			DarkServerUtils.Config.load();
    			}
    		} catch (Exception e)
    		{
    			// Cannot load report error
    			Logger.WriteLog("Error while trying to load Common.cfg");
    			Logger.WriteLog(e.getMessage());
    			DarkServerUtils.Config = oldconfig;
    		}
    		finally
    		{
				player.addChatMessage(new ChatComponentText("Successfully loaded Common.cfg"));
    		}        		
        }
	}

}
