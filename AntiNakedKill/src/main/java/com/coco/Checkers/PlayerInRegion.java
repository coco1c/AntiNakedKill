package com.coco.Checkers;

import com.coco.AntiNakedKill;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PlayerInRegion {
    private AntiNakedKill main;
    private WorldGuardPlugin worldGuard;
    private FileConfiguration config;
    static String name;


    public PlayerInRegion(AntiNakedKill main) {
        this.main = main;
        config = main.getConfig();
        this.worldGuard = getWorldGuardPlugin();
    }

    private WorldGuardPlugin getWorldGuardPlugin() {
        Plugin plugin = main.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin instanceof WorldGuardPlugin) {
            return (WorldGuardPlugin) plugin;
        }
        return null;
    }

    public static boolean isInRegion(Player p, String region) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));

        if (set.size() == 0) {
            return false;
        } else {
            for (ProtectedRegion pr : set) {
                if (pr.getId().equalsIgnoreCase(region)) {
                    return true;
                }
            }
            return false;
        }
    }
}
