package com.coco.Commands;

import com.coco.AntiNakedKill;
import com.coco.Checkers.NakedChecker;
import com.coco.Helpers.PlayerMessageWithColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MainCommand extends NakedChecker implements CommandExecutor, TabCompleter {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private int cooldownDuration = 5;

    FileConfiguration config;
    AntiNakedKill main;
    PlayerMessageWithColor msg = new PlayerMessageWithColor();

    public MainCommand(AntiNakedKill main){
        super(main);
        this.main = main;
        config = main.getConfig();
        cooldownDuration = config.getInt("cooldown_duration", 5);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("protection")) {
                if (player.hasPermission("nonakedkill.onoff")) {
                    if (args.length == 1) {
                        if (isOnCooldown(player)) {
                            long remainingTime = getRemainingCooldown(player);
                            msg.sendMessage(player, String.valueOf(config.get("Messages.cooldown")).replace("{time}", String.valueOf(remainingTime)));
                            return false;
                        } else {
                            if (args[0].equalsIgnoreCase("off")) {
                                setProtection("no");
                                playerNoProtection.add(player.getName());
                                msg.sendMessage(player, String.valueOf(config.get("Messages.protection_off")));
                                setCooldown(player);
                                return true;
                            } else if (args[0].equalsIgnoreCase("on")) {
                                setProtection("yes");
                                playerNoProtection.remove(player.getName());
                                msg.sendMessage(player, String.valueOf(config.get("Messages.protection_on")));
                                setCooldown(player);
                                return true;
                            }
                        }
                    }
                } else {
                    msg.sendMessage(player, String.valueOf(config.get("Messages.no_perms")));
                    return false;
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("protection")) {
            if (args.length == 1) {
                completions.add("on");
                completions.add("off");
            }
        }
        List<String> filteredCompletions = new ArrayList<>();
        String input = args[args.length - 1].toLowerCase();
        for (String completion : completions) {
            if (completion.toLowerCase().startsWith(input)) {
                filteredCompletions.add(completion);
            }
        }
        return filteredCompletions;
    }

    private boolean isOnCooldown(Player player) {
        if (cooldowns.containsKey(player.getUniqueId())) {
            long lastExecutionTime = cooldowns.get(player.getUniqueId());
            long currentTime = System.currentTimeMillis() / 1000;
            return (currentTime - lastExecutionTime) < cooldownDuration;
        }
        return false;
    }

    private long getRemainingCooldown(Player player) {
        long lastExecutionTime = cooldowns.get(player.getUniqueId());
        long currentTime = System.currentTimeMillis() / 1000;
        long elapsedTime = currentTime - lastExecutionTime;
        return Math.max(0, cooldownDuration - elapsedTime);
    }

    private void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() / 1000);
    }

}
