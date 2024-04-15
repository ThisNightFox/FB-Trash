package de.furubafox.aftrash.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TrashCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (sender instanceof Player) {
            Player plr = (Player) sender;
            Inventory inv = Bukkit.createInventory(null, 2*9, "&aMülleimer");
            plr.openInventory(inv);
        } else sender.sendMessage("§cDas dürfen nur Spieler!");
        return false;
    }
}

