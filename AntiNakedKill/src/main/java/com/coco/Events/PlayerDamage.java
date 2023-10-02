package com.coco.Events;

import com.coco.AntiNakedKill;
import com.coco.Checkers.NakedChecker;
import com.coco.Helpers.PlayerMessageWithColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage extends NakedChecker implements Listener {
    AntiNakedKill main;
    FileConfiguration config;
    PlayerMessageWithColor msg = new PlayerMessageWithColor();
    public PlayerDamage(AntiNakedKill main){
        super(main);
        this.main = main;
        config = main.getConfig();
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player player = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            if (isNaked(player) && isNaked(damager)) {

            } else if (isNaked(player)) {
                e.setCancelled(true);
                if (config.get("Messages.player_is_naked").equals("{no_msg}")) {

                } else {
                    msg.sendMessage(damager, String.valueOf(config.get("Messages.player_is_naked"))
                            .replace("{naked_player}", player.getName()));
                }
            } else if (!isNaked(player) && isNaked(damager)) {
                e.setCancelled(true);
                msg.sendMessage(damager, String.valueOf(config.get("Messages.player_is_not_naked"))
                        .replace("{not_naked_player}", damager.getName()));
            }
        }
    }
}
