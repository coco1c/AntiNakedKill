package com.coco.Commands;

import com.coco.AntiNakedKill;
import com.coco.Helpers.PlayerMessageWithColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SetItemName implements CommandExecutor {
    AntiNakedKill main;
    FileConfiguration config;
    PlayerMessageWithColor msg = new PlayerMessageWithColor();

    public SetItemName(AntiNakedKill main){
        this.main = main;
        config = main.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("nonakedkill.setname")) {
                if (player.getItemInHand().getItemMeta().hasDisplayName()) {
                    String materialName = player.getItemInHand().getType().toString();
                    ItemMeta itemMeta = player.getItemInHand().getItemMeta();
                    String configKey = "items.ignored." + materialName.toUpperCase();
                    String displayName = player.getItemInHand().getItemMeta().getDisplayName();
                    List<String> itemLore = itemMeta.getLore();
                    config.set(configKey + ".display_name", displayName);
                    if (player.getItemInHand().getItemMeta().hasLore()){
                        config.set(configKey + ".lore", itemLore);
                    }
                    main.saveConfig();
                    msg.sendMessage(player, String.valueOf(config.get("Messages.item_name_set"))
                            .replace("{item_name}", displayName)
                            .replace("{item_type}", materialName));
                    return true;
                } else {
                    msg.sendMessage(player, String.valueOf(config.get("Messages.item_name_set_no_display_name")));
                    return false;
                }
            } else {
                msg.sendMessage(player, String.valueOf(config.get("Messages.no_perms")));
                return false;
            }
        }
        return false;
    }
}
