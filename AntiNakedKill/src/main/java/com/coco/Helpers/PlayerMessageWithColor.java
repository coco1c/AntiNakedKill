package com.coco.Helpers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerMessageWithColor {
    public void sendMessage(Player player, String txt){
        txt = ChatColor.translateAlternateColorCodes('&', txt)
                .replace("{player_name}", player.getName())
                .replace("{player_health}", String.valueOf(player.getHealth()))
                .replace("{player_UUID}", String.valueOf(player.getUniqueId()))
                .replace("{player_world}", (player.getWorld().getName()))
                .replace("{player_walkspeed}", String.valueOf(player.getWalkSpeed()))
                .replace("{player_biome}", String.valueOf(player.getWorld().getBiome(player.getLocation())))
                .replace("{player_mainHandItem}", String.valueOf(player.getInventory().getItemInMainHand()))
                .replace("{player_gamemode}", String.valueOf(player.getGameMode()))
                .replace("{player_gamemode_no_caps}", String.valueOf(player.getGameMode()).toLowerCase());
        player.sendMessage(txt);
    }
}
