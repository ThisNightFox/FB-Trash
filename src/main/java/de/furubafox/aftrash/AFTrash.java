package de.furubafox.aftrash;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class AFTrash extends JavaPlugin implements Listener, CommandExecutor {


    @Override
    public void onEnable() {
        System.out.println("Das plugin wurde gestartet");

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("trash").setExecutor(this);
    }


    @Override
    public void onDisable() {
    }
}
