package com.failmelon.darkserverutils.commands;

import java.util.UUID;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import net.minecraft.world.WorldServer;

import com.failmelon.darkserverutils.TempPlayerData;

public class AcceptCommand extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "accept";
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
		MinecraftServer server = MinecraftServer.getServer();
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		WorldServer world = server.worldServerForDimension(player.getEntityWorld().provider.dimensionId);

		// Get our stored uuid data saved in temp data
		Object otheruuid = TempPlayerData.GetData(player.getPersistentID(), "teleportrequest");
		
		if (otheruuid != null)
		{
			// Find our other player by UUID
			EntityPlayer other = world.func_152378_a(UUID.class.cast(otheruuid));

			// Set our other players position to our players position and network update
			other.setPositionAndUpdate(player.posX, player.posY, player.posZ);

			// Remove our stored uuid data
			TempPlayerData.RemoveData(player.getPersistentID(), "teleportrequest");
		}
	}
	
	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "commands.accept.usage";
	}

}
