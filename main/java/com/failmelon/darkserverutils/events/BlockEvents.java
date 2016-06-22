package com.failmelon.darkserverutils.events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

import com.failmelon.darkserverutils.DarkServerUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockEvents 
{
	@SubscribeEvent
	public void OnPlaceBlockEvent(PlaceEvent event)
	{
		// If we are in creative ignore blacklist
		if (event.player.capabilities.isCreativeMode)
			return;
		
		// Check the config to see if we can place block
		if (!DarkServerUtils.GetBlockBlacklist("canplaceblock", event.block))
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void OnUseItem(PlayerInteractEvent event)
	{
		ItemStack itemstack = event.entityPlayer.getHeldItem();
		
		if (itemstack != null && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			// If we are in creative ignore blacklist
			if (event.entityPlayer.capabilities.isCreativeMode)
				return;			

			// Check the config to see if we can use item
			if (!DarkServerUtils.GetItemBlacklist("canuseonblock", itemstack.getItem()))
				event.setCanceled(true);
		}
	}

}
