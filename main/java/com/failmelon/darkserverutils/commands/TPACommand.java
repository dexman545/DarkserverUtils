package com.failmelon.darkserverutils.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.ChatComponentText;

import com.failmelon.darkserverutils.DarkServerUtils;
import com.failmelon.darkserverutils.TempPlayerData;

public class TPACommand extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "tpa";
	}

    /**
     * Return the required permission level for this command.
     */
	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
	
    /**
     * Returns true if the given command sender is allowed to use this command.
     */
	@Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
    {
    	return true;
    }

	@Override
	public void processCommand(ICommandSender sender, String[] params)
	{
		// Need to give a player in the arguments
        if (params.length < 1)
        {
            throw new net.minecraft.command.WrongUsageException("commands.tpa.usage", new Object[0]);
        }
		
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        // Get our players stats file
		StatisticsFile stats = player.func_147099_x();
		
		// Get our minutes played stat from our players stat file
		int minstatint = stats.writeStat(StatList.minutesPlayedStat);
		
		// Check against it to see if the are a noob
		if (minstatint <= DarkServerUtils.NoobTime)
		{
            EntityPlayerMP otherplayer = getPlayer(sender, params[0]);
         
            // Sets temp data for the teleport request to another player
            TempPlayerData.SetData(otherplayer.getPersistentID(), "teleportrequest", player.getUniqueID());
            
            // Tell the other player about the teleport request
            otherplayer.addChatMessage(new ChatComponentText(player.getDisplayName() + " wants to teleport to you use /accept to accept their teleport."));
		}
		else
		{
			// No longer a noob tell them about it
			player.addChatMessage(new ChatComponentText("No longer under noob protection."));
		}
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "commands.tpa.usage";
	}
	
    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
    	// If we are currently on the first argument then try to attempt tab completion against player names
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getPlayers()) : null;
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }
}
