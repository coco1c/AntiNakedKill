package com.coco.Events;

import com.coco.AntiNakedKill;
import com.coco.Checkers.NakedChecker;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlockBreakEventHandler extends NakedChecker implements Listener {
    AntiNakedKill main;

    FileConfiguration config;

    public BlockBreakEventHandler(AntiNakedKill main) {
        super(main);
        this.main = main;
        config = main.getConfig();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (isNaked(player)) {
            if (e.isDropItems()) {
                if (!e.isCancelled()) {
                    e.setDropItems(false);

                    int emptySlots = 0;
                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                        if (player.getInventory().getItem(i) == null) {
                            emptySlots++;
                        }
                    }
                    if (emptySlots > 0) {
                        player.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                    }
                }
            }
        } else if (isNaked(player) && isValidBlockToBreak(e.getBlock().getType())) {
            if (e.isDropItems()) {
                if (!e.isCancelled()) {
                    e.setDropItems(false);

                    int emptySlots = 0;
                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                        if (player.getInventory().getItem(i) == null) {
                            emptySlots++;
                        }
                    }
                    if (emptySlots > 0) {
                        player.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                    }
                }
            }
        }
    }
    private boolean isValidBlockToBreak(Material material) {
        List<String> ignoredBlocks = config.getStringList("blocks.ignored_break");
        String materialName = material.toString().toUpperCase();
        if (ignoredBlocks.contains(materialName)){
            return true;
        }else {
            return false;
        }
    }

}

// isValidBlockToBreak(e.getBlock().getType())