package com.coco.Events;

import com.coco.AntiNakedKill;
import com.coco.Checkers.NakedChecker;
import com.coco.Helpers.PlayerMessageWithColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NakedPlayerPickupEvent extends NakedChecker implements Listener {
    private final AntiNakedKill main;
    private final FileConfiguration config;
    private final PlayerMessageWithColor msg = new PlayerMessageWithColor();

    private final Map<Player, Long> messageCooldown = new HashMap<>();

    public NakedPlayerPickupEvent(AntiNakedKill main) {
        super(main);
        this.main = main;
        config = main.getConfig();
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        Material itemMaterial = e.getItem().getItemStack().getType();

        if (isNaked(player) && !isValidItemToPickup(itemMaterial)) {
            e.setCancelled(true);
            if (canSendMessage(player)) {
                msg.sendMessage(player, String.valueOf(config.get("Messages.naked_pickup_blacklisted_items")));
                setMessageCooldown(player);
            }
        }
    }

    private boolean isValidItemToPickup(Material material) {
        List<String> ignoredDrops = config.getStringList("Drops.ignored");
        String materialName = material.toString().toUpperCase();
        return ignoredDrops.contains(materialName);
    }

    private boolean canSendMessage(Player player) {
        long currentTime = System.currentTimeMillis();
        long lastMessageTime = messageCooldown.getOrDefault(player, 0L);
        long cooldownTime = config.getLong("Messages.cooldown_time", 2000);

        return currentTime - lastMessageTime >= cooldownTime;
    }

    private void setMessageCooldown(Player player) {
        messageCooldown.put(player, System.currentTimeMillis());
    }
}
