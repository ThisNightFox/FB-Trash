package de.furubafox.fbtrash;

import de.furubafox.fbtrash.command.TrashCommand;
import de.furubafox.fbtrash.utils.UpdateChecker;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class FBTrash extends JavaPlugin implements Listener, CommandExecutor {


    @Override
    public void onEnable() {
        this.getLogger().info("Das Plugin wurde gestartet");
        this.getCommand("trash").setExecutor(new TrashCommand());

        new UpdateChecker(this, 116517).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else {
                getLogger().info("There is a new update available.");
            }
        });

    }


    @Override
    public void onDisable() {
        this.getLogger().info("Das Plugin ist gestoppt");
    }
}
