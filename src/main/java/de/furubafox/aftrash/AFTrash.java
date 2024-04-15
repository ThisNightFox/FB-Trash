package de.furubafox.aftrash;

import org.bukkit.plugin.java.JavaPlugin;

public final class AFTrash extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Das Plugin wurde geladen");

    }

    @Override
    public void onDisable() {
        System.out.println("DAs Plugin wurde gestopt");
    }
}
