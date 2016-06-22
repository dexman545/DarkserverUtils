package com.failmelon.darkserverutils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.ChatComponentText;

import com.failmelon.darkserverutils.DarkServerUtils;
import com.failmelon.darkserverutils.TempPlayerData;

public class BackCommand extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "back";
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
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        // Get our stats file from the player class
		StatisticsFile stats = player.func_147099_x();
		
		// Get our minutes played stat as int
		int minstatint = stats.writeStat(StatList.minutesPlayedStat);
		
		// Check against noob time to see if they are still a noob
		if (minstatint <= DarkServerUtils.NoobTime)
		{
			// Gets our old position from temp storage
			Object oldpos = TempPlayerData.GetData(player.getPersistentID(), "noobprotect");
			
			// Checks to see if we have stored data
			if (oldpos != null)
			{
				double[] deathpos = (double[])oldpos;
				
				// Sets our position to last stored death position
				player.setPositionAndUpdate(deathpos[0], deathpos[1], deathpos[2]);
				
				// Removes our old position from temp storage
				TempPlayerData.RemoveData(player.getPersistentID(), "noobprotect");
			}
			else
			{
				// No deaths have been recorded by player events
				player.addChatMessage(new ChatComponentText("No recorded deaths."));
			}
		}
		else
		{
			// No longer a noob tell me about it
			player.addChatMessage(new ChatComponentText("No longer under noob protection."));
		}
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "commands.back.usage";
	}

}
