package com.example.deathlogplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PlayerDeathListener implements Listener {

    private final DeathLogPlugin plugin;
    private final Map<UUID, LinkedList<DeathRecord>> playerDeathLogs = new HashMap<>();
    private static final int MAX_LOGS_PER_PLAYER = 10;

    public PlayerDeathListener(DeathLogPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        UUID playerUUID = event.getEntity().getUniqueId();
        LinkedList<DeathRecord> deathRecords = playerDeathLogs.getOrDefault(playerUUID, new LinkedList<>());

        DeathRecord deathRecord = new DeathRecord(event);
        if (deathRecords.size() >= MAX_LOGS_PER_PLAYER) {
            deathRecords.removeFirst(); // Xoá kỷ lục cũ nhất nếu vượt qua số lượng kỷ lục tối đa
        }
        deathRecords.add(deathRecord);
        playerDeathLogs.put(playerUUID, deathRecords);

        // Broadcast báo chết với màu tuỳ chỉnh
        String deathMessage = ChatColor.RED + "Death of " + ChatColor.GREEN + deathRecord.getPlayerName() +
                ChatColor.RED + ": " + ChatColor.GOLD + deathRecord.getDeathMessage();
        Bukkit.getServer().broadcastMessage(deathMessage);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (playerDeathLogs.containsKey(playerUUID)) {
            LinkedList<DeathRecord> deathRecords = playerDeathLogs.get(playerUUID);
            for (DeathRecord record : deathRecords) {
                event.getPlayer().sendMessage(record.toString()); // Sử dụng phương thức toString() để hiển thị kỷ lục
            }
        }
    }

    public LinkedList<DeathRecord> getDeathRecords(UUID playerUUID) {
        return playerDeathLogs.get(playerUUID);
    }

    public void saveToConfig(FileConfiguration config) {
        config.set("deathLogs", null); // Xoá các log cũ trước
        for (Map.Entry<UUID, LinkedList<DeathRecord>> entry : playerDeathLogs.entrySet()) {
            UUID playerId = entry.getKey();
            LinkedList<DeathRecord> deathRecords = entry.getValue();

            List<Map<String, Object>> logList = new ArrayList<>();
            for (DeathRecord record : deathRecords) {
                logList.add(record.serialize()); // Serialize DeathRecord
            }
            config.set("deathLogs." + playerId.toString(), logList);
        }
    }

    public void loadFromConfig(FileConfiguration config) {
        playerDeathLogs.clear();  // Xoá log hiện tại trước khi loading cái mới
        if (config.contains("deathLogs")) {
            for (String key : config.getConfigurationSection("deathLogs").getKeys(false)) {
                LinkedList<DeathRecord> deathRecords = new LinkedList<>();
                List<Map<?, ?>> logs = config.getMapList("deathLogs." + key);

                for (Map<?, ?> log : logs) {
                    deathRecords.add(DeathRecord.deserialize((Map<String, Object>) log)); // Deserialize DeathRecord
                }
                playerDeathLogs.put(UUID.fromString(key), deathRecords);
            }
        }
    }
}