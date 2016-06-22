package com.failmelon.darkserverutils.events;

import com.failmelon.darkserverutils.DarkServerUtils;
import com.failmelon.darkserverutils.TempPlayerData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class PlayerEvents 
{
	@SubscribeEvent
	public void OnPlayerJoined(PlayerLoggedInEvent event)
	{		
		// Check if we are the server and if the entity is EntityPlayerMP
		if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP)
		{			
			EntityPlayerMP player = ((EntityPlayerMP)event.player);
			
			// Get the players stats file
			StatisticsFile stats = player.func_147099_x();
			
			// Find our minutes played stat
			int minstatint = stats.writeStat(StatList.minutesPlayedStat);
											
			// Check against it to see if we are under noob protection and report it to the player
			if (minstatint <= DarkServerUtils.NoobTime)
				player.addChatMessage(new ChatComponentText("You are under noob protection."));
		}
	}
	
	@SubscribeEvent
	public void OnPlayerRespawn(PlayerRespawnEvent event)
	{		
		// Check if we are the server and if the entity is EntityPlayerMP
		if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP)
		{			
			EntityPlayerMP player = ((EntityPlayerMP)event.player);
			
			// Get the players stats file
			StatisticsFile stats = player.func_147099_x();
			
			// Find our minutes played stat
			int minstatint = stats.writeStat(StatList.minutesPlayedStat);
		
			// Check against noob time to see if we are still a noob
			if (minstatint <= DarkServerUtils.NoobTime)
			{
				// Remove any heal potion effects and readds op one
				player.removePotionEffect(Potion.heal.id);			
				player.addPotionEffect(new PotionEffect(Potion.heal.id, 600, 10));
				
				// Tell the player that they are under protection
				player.addChatMessage(new ChatComponentText("You are under noob protection."));
				player.addChatMessage(new ChatComponentText("/back to go to your lastest death."));
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		// Check if we are the server and if the entity is EntityPlayerMP
		if (!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayerMP)
		{			
			EntityPlayerMP player = ((EntityPlayerMP)event.entityLiving);
			
			// Get the players stats file
			StatisticsFile stats = player.func_147099_x();
			
			// Find our minutes played stat
			int minstatint = stats.writeStat(StatList.minutesPlayedStat);
			
			// Check against noob time to see if we are a noob and sets our position as we die
			if (minstatint <= DarkServerUtils.NoobTime)
	            TempPlayerData.SetData(player.getPersistentID(), "noobprotect", new double[] { player.posX, player.posY, player.posZ });
		}
	}
}
