package com.coco;

import com.coco.Commands.HelpCommand;
import com.coco.Commands.MainCommand;
import com.coco.Commands.SetItemName;
import com.coco.Events.BlockBreakEventHandler;
import com.coco.Checkers.NakedChecker;
import com.coco.Events.NakedPlayerPickupEvent;
import com.coco.Events.PlayerDamage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiNakedKill extends JavaPlugin {
    private FileConfiguration config;
    private static AntiNakedKill instance;
    private NakedChecker nakedChecker;
    private BlockBreakEventHandler blockBreakEventHandler;
    private MainCommand commands;
    private PlayerDamage damageEvent;
    private NakedPlayerPickupEvent pickup;
    private HelpCommand helpCmd;
    private SetItemName itemName;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        config = this.getConfig();
        nakedChecker = new NakedChecker(this);
        blockBreakEventHandler = new BlockBreakEventHandler(this);
        commands = new MainCommand(this);
        damageEvent = new PlayerDamage(this);
        pickup = new NakedPlayerPickupEvent(this);
        helpCmd = new HelpCommand();
        itemName = new SetItemName(this);

        getCommand("protection").setExecutor(commands);
        getCommand("protection").setTabCompleter(commands);
        getCommand("nonakedkillhelp").setExecutor(helpCmd);
        getCommand("whitelistitem").setExecutor(itemName);

        getServer().getPluginManager().registerEvents(nakedChecker, this);
        getServer().getPluginManager().registerEvents(blockBreakEventHandler, this);
        getServer().getPluginManager().registerEvents(damageEvent, this);
        getServer().getPluginManager().registerEvents(pickup, this);
    }

    @Override
    public void onDisable() {

    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("nonakedkillreload")) {
            if (sender.hasPermission("nonakedkill.reload")) {
                HandlerList.unregisterAll(this);


                this.reloadConfig();
                this.config = this.getConfig();

                this.damageEvent = new PlayerDamage(this);
                this.nakedChecker = new NakedChecker(this);
                this.blockBreakEventHandler = new BlockBreakEventHandler(this);
                this.commands = new MainCommand(this);
                this.pickup = new NakedPlayerPickupEvent(this);
                this.helpCmd = new HelpCommand();
                this.itemName = new SetItemName(this);

                getServer().getPluginManager().registerEvents(nakedChecker, this);
                getServer().getPluginManager().registerEvents(blockBreakEventHandler, this);
                getServer().getPluginManager().registerEvents(damageEvent, this);
                getServer().getPluginManager().registerEvents(pickup, this);


                getCommand("protection").setExecutor(commands);
                getCommand("protection").setTabCompleter(commands);
                getCommand("nonakedkillhelp").setExecutor(helpCmd);
                getCommand("whitelistitem").setExecutor(itemName);

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.get("Messages.reload_msg"))));
                return true;
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(config.get("Messages.no_perms"))));
            }
        } else {
            if (sender instanceof Player && cmd.getName().equalsIgnoreCase("getname")) {
                Player player = (Player) sender;
                ItemStack item = new ItemStack(player.getItemInHand());
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName()) {
                    meta.getDisplayName();
                    player.sendMessage(meta.getDisplayName());
                }
            }
            return true;
        }
        return false;
    }
}
