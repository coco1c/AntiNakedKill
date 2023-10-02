package com.coco.Commands;

import org.bukkit.command.Command;
import com.coco.Helpers.PlayerMessageWithColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
    private PlayerMessageWithColor msg = new PlayerMessageWithColor();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("nonakedkill.help")){
                msg.sendMessage(player, "&ePermission: &cnonakedkill.bypass");
                msg.sendMessage(player, "&7This if you want to &cbypass &7the checker");
                msg.sendMessage(player, "&7To enable protection: &c/protection on");
                msg.sendMessage(player, "&7To disable protection: &c/protection off");
                msg.sendMessage(player, "&7This plugin was created by coco");
                return true;
            }
            msg.sendMessage(player, "&cYou don't have permission to use this command!");
            return false;
        }
        return false;
    }
}
