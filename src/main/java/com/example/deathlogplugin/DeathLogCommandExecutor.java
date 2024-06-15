package com.example.deathlogplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.LinkedList;
import java.util.UUID;

public class DeathLogCommandExecutor implements CommandExecutor {

    private final DeathLogPlugin plugin;

    public DeathLogCommandExecutor(DeathLogPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            
            LinkedList<DeathRecord> deathRecords = plugin.getDeathLogs(playerUUID);
            
            if (deathRecords.isEmpty()) {
                player.sendMessage(ChatColor.RED + "You haven't died yet");
                return true;
            }
            
            player.sendMessage(ChatColor.GOLD + "---- " + ChatColor.DARK_RED + "Your Death Diary" + ChatColor.GOLD + " ----");

            for (DeathRecord record : deathRecords) {
                String message = ChatColor.YELLOW + "Day: " + ChatColor.GREEN + record.getDate() + "\n" +
                                 ChatColor.YELLOW + "Reason: " + ChatColor.GREEN + record.getCause() + "\n" +
                                 ChatColor.YELLOW + "Location: " + ChatColor.GREEN + "X: " + record.getLocation().getBlockX() + 
                                 ", Y: " + record.getLocation().getBlockY() + 
                                 ", Z: " + record.getLocation().getBlockZ();
                player.sendMessage(message);
            }
        }
        return true;
    }
}
