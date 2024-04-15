import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrashPlugin extends JavaPlugin implements Listener, CommandExecutor {

    private Connection connection;
    private String host, database, username, password, table;

    private List<Location> trashCanLocations = new ArrayList<>();

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        host = getConfig().getString("database.host");
        database = getConfig().getString("database.database");
        username = getConfig().getString("database.username");
        password = getConfig().getString("database.password");
        table = "trashcans"; // Name der Tabelle in der Datenbank

        connectToDatabase();

        loadTrashCanLocations();

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("trash").setExecutor(this);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, username, password);
            Bukkit.getLogger().info("Verbunden mit der MySQL-Datenbank!");
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Fehler beim Verbinden mit der MySQL-Datenbank: " + e.getMessage());
        }
    }

    private void loadTrashCanLocations() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                String worldName = resultSet.getString("world");
                Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
                trashCanLocations.add(location);
            }

            statement.close();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Fehler beim Laden der Mülleimer-Positionen: " + e.getMessage());
        }
    }

    private void saveTrashCanLocation(Location location) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + table + " (x, y, z, world) VALUES (?, ?, ?, ?)"
            );
            statement.setDouble(1, location.getX());
            statement.setDouble(2, location.getY());
            statement.setDouble(3, location.getZ());
            statement.setString(4, location.getWorld().getName());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Fehler beim Speichern der Mülleimer-Position: " + e.getMessage());
        }
    }

    private void removeTrashCanLocation(Location location) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM " + table + " WHERE x = ? AND y = ? AND z = ? AND world = ?"
            );
            statement.setDouble(1, location.getX());
            statement.setDouble(2, location.getY());
            statement.setDouble(3, location.getZ());
            statement.setString(4, location.getWorld().getName());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Fehler beim Entfernen der Mülleimer-Position: " + e.getMessage());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("trash") && sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack trashCan = createTrashCan();
            player.getInventory().addItem(trashCan);
            player.sendMessage(ChatColor.GREEN + "Du hast einen Mülleimer erhalten!");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item != null && item.getType() == Material.CAULDRON && item.hasItemMeta() &&
                item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Mülleimer") &&
                item.getItemMeta().getLore().contains(ChatColor.GRAY + "Platziere es zum aktivieren")) {

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Location location = event.getClickedBlock().getLocation();
                trashCanLocations.add(location);
                saveTrashCanLocation(location);
                player.sendMessage(ChatColor.GREEN + "Mülleimer platziert!");
            }

            event.setCancelled(true);
        }
    }

    private ItemStack createTrashCan() {
        ItemStack trashCan = new ItemStack(Material.CAULDRON);
        ItemMeta meta = trashCan.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Mülleimer");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Platziere es zum aktivieren");
        meta.setLore(lore);
        trashCan.setItemMeta(meta);
        return trashCan;
    }

    // Hier implementierst du noch die Funktionen zum Öffnen des Inventars beim Rechtsklick auf den Mülleimer
    // und zum Behandeln des Abbaus des Mülleimers

    @Override
    public void onDisable() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Fehler beim Schließen der Verbindung zur Datenbank: " + e.getMessage());
        }
    }
}


Dieser Code verwendet eine MySQL-Datenbank, um die Positionen der Mülleimer zu speichern und zu laden. Bitte stelle sicher, dass du die MySQL-Datenbank richtig eingerichtet hast und die erforderlichen Berechtigungen für den Zugriff auf die Datenbank hast. Außerdem solltest du die Konfigurationsdatei des Plugins entsprechend anpassen, um die Datenbankverbindungsinformationen zu speichern.