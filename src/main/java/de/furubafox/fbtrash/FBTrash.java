package de.furubafox.fbtrash;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class FBTrash extends JavaPlugin implements Listener, CommandExecutor {


    @Override
    public void onEnable() {
        System.out.println("Das Plugin wurde gestartet");
        this.getLogger().info("Das Plugin wurde gestartet");

        getCommand("trash").setExecutor(this);
    }


    @Override
    public void onDisable() {
        this.getLogger().info("Das Plugin ist gestoppt");
    }
}
