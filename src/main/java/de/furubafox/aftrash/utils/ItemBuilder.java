package de.furubafox.aftrash.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * @author ThisNightFox
 * @since 15.04.2024
 */

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setLocalizedName(String s) {
        meta.setLocalizedName(s);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        for (String string : lore) {
            string.replaceAll("&", "§");
        }
        meta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... s){
        meta.addItemFlags(s);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment em, int level) {
        meta.addEnchant(em, level, true);
        return this;
    }

    public ItemBuilder setAmount(int Amount) {
        item.setAmount(Amount);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        meta.setUnbreakable(true);
        return this;
    }

    public ItemBuilder setHideEnchantment() {
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}

