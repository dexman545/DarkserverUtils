package com.failmelon.darkserverutils;

import java.util.LinkedHashMap;
import java.util.UUID;

// Holds temporary data for players on the server
public class TempPlayerData 
{
	private static LinkedHashMap<UUID, LinkedHashMap<String, Object>> PlayersData = new LinkedHashMap<UUID, LinkedHashMap<String, Object>>();
	
	public static void SetData(UUID playerid, String key, Object value)
	{
		// Define our player data variable
		LinkedHashMap<String, Object> playerdata = null;
		
		// Check to see if the player id is inside the map
		if (PlayersData.containsKey(playerid))
		{
			// Find it inside the map and set the data
			playerdata = PlayersData.get(playerid);
			playerdata.put(key, value);
		}
		else
		{
			// Create a new map a new map and set the data
			playerdata = new LinkedHashMap<String, Object>();
			playerdata.put(key, value);			
		}
		
		// Put the player data variable into PlayersData map
		PlayersData.put(playerid, playerdata);
	}
	
	public static void RemoveData(UUID playerid, String key)
	{
		// Check to see if the player id is inside the map
		if (PlayersData.containsKey(playerid))
		{
			// get the player data from the map
			LinkedHashMap<String, Object> playerdata = PlayersData.get(playerid);
			
			// Removes our player data value by key
			playerdata.remove(key);
			PlayersData.put(playerid, playerdata);
		}
	}
	
	public static Object GetData(UUID playerid, String key)
	{		
		// Check to see if the player id is inside the map
		if (PlayersData.containsKey(playerid))
		{
			// get the player data from the map
			LinkedHashMap<String, Object> playerdata = PlayersData.get(playerid);		
			
			// Check to see if it contains our player data key and gets it
			if (playerdata.containsKey(key))				
				return playerdata.get(key);
		}
		
		return null;
	}
}
