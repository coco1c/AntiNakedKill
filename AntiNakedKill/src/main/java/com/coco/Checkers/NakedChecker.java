package com.coco.Checkers;

import com.coco.AntiNakedKill;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class NakedChecker implements Listener {
    private final AntiNakedKill main;
    private final FileConfiguration config;
    public static String protection = "yes";
    public static List<String> playerNoProtection = new ArrayList<>();

    public NakedChecker(AntiNakedKill main) {
        this.main = main;
        this.config = main.getConfig();
    }


    public boolean isNaked(Player player) {
        String configKey = "Region";
        List<String> regions = config.getStringList(configKey);
        if (!player.getName().contains(String.valueOf(config.get("NPC.contains")))) {
            if (player.getGameMode() == GameMode.SURVIVAL && !player.hasPermission("nonakedkill.bypass")) {
                if (!playerNoProtection.contains(player.getName())) {
                    for (String region : regions) {
                        if (PlayerInRegion.isInRegion(player, region)) {
                            return false;
                        }
                    }

                    boolean fullyNaked = isFullyNaked(player);
                    boolean hasValidItem = hasValidItem(player);

                    return (fullyNaked || hasValidItem);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean isFullyNaked(Player player) {
        ItemStack[] mainInventory = player.getInventory().getContents();
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        for (ItemStack itemStack : mainInventory) {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                return false;
            }
        }

        for (ItemStack armorItem : armorContents) {
            if (armorItem != null && !armorItem.getType().equals(Material.AIR)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasValidItem(Player player) {
        Inventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack itemStack : contents) {
            if (!isNullOrAir(itemStack)) {
                if (itemStack.getType().isBlock()) {
                    Material block = itemStack.getType();
                    if (!isValidBlock(block)) {
                        return false;
                    }
                } else if (!isValidItem(itemStack)) {
                    return false;
                }
            }
        }
        for (ItemStack armorPiece : armor) {
            if (!isNullOrAir(armorPiece)) {
                if (!isValidItem(armorPiece)) {
                    return false;
                }
            }
        }

        ItemStack offhand = player.getInventory().getItemInOffHand();
        if (!isNullOrAir(offhand) && !isValidItem(offhand)) {
            return false;
        }

        return true;
    }


    private boolean isNullOrAir(ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }


    private boolean isValidItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        String itemName = itemMeta.getDisplayName();
        if (itemMeta != null) {
            String materialName = itemStack.getType().toString().toUpperCase();
            String configKey = "items.ignored." + materialName.toUpperCase();
//            List<String> loreList = config.getStringList(configKey + ".lore");
            if (config.contains(configKey)) {
                if (config.contains(configKey + ".display_name")) {
                    String displayName = config.getString(configKey + ".display_name");
                    if (config.contains(configKey + ".lore")) {
                        List<String> itemLore = itemMeta.getLore();
                        List<String> loreList = config.getStringList(configKey + ".lore");
                        if (loreList.contains("null")){
                            if (itemStack.getItemMeta().hasDisplayName()) {
                                boolean displayNameMatches = displayName == null || displayName.isEmpty() ||
                                        (itemMeta.hasDisplayName() && displayName.equalsIgnoreCase(itemMeta.getDisplayName()));
                                boolean materialMatches = materialName.equals(itemStack.getType().toString().toUpperCase());
                                if (displayNameMatches && materialMatches) {
                                    return true;
                                }
                            }
                        } else if (itemStack.getItemMeta().hasDisplayName()) {
                                boolean displayNameMatches = displayName == null || displayName.isEmpty() ||
                                        (itemMeta.hasDisplayName() && displayName.equalsIgnoreCase(itemMeta.getDisplayName()));

                                boolean loreMatches = loreList.isEmpty() || (itemLore != null && itemLore.containsAll(loreList));

                                boolean materialMatches = materialName.equals(itemStack.getType().toString().toUpperCase());

                                if (displayNameMatches && loreMatches && materialMatches) {
                                    return true;
                            }
                        }

                    }
                } else if (config.contains(configKey + ".lore")) {
                    List<String> itemLore = itemMeta.getLore();
                    List<String> loreList = config.getStringList(configKey + ".lore");
                    if (loreList.contains("null")) {
                        if (!itemStack.getItemMeta().hasLore()) {

                            boolean materialMatches = materialName.equals(itemStack.getType().toString().toUpperCase());

                            if (materialMatches) {
                                return true;
                            }
                        }
                    } else {
                        if (itemStack.getItemMeta().hasLore()) {

                            boolean loreMatches = loreList.isEmpty() || (itemLore != null && itemLore.containsAll(loreList));

                            boolean materialMatches = materialName.equals(itemStack.getType().toString().toUpperCase());

                            if (loreMatches && materialMatches) {
                                return true;
                            }
                        }
                    }
                } else {
                    boolean materialMatches = materialName.equals(itemStack.getType().toString().toUpperCase());
                    if (materialMatches) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
    private boolean isValidBlock(Material material) {
        List<String> ignoredBlocks = config.getStringList("blocks.ignored_take");

        String materialName = material.toString().toUpperCase();

        if (ignoredBlocks.contains(materialName)) {
            return true;
        } else {
            return false;
        }
    }
    public String isProtectionOn(){
        return protection;
    }
    public void setProtection(String enable){
        this.protection = enable;
    }
}
