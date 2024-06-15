package com.example.deathlogplugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.LinkedList;
import java.util.UUID;

public class DeathLogPlugin extends JavaPlugin {

    private PlayerDeathListener playerDeathListener;
    private File deathLogFile;
    private FileConfiguration deathLogConfig;

    @Override
    public void onEnable() {
        this.playerDeathListener = new PlayerDeathListener(this);
        
        getServer().getPluginManager().registerEvents(this.playerDeathListener, this);
        this.getCommand("deathlog").setExecutor(new DeathLogCommandExecutor(this));
        this.loadDeathLogs();
        
        getLogger().info("DeathLogPlugin has been enabled.");
        
        // Add credit
        getLogger().info("Plugin is created by tuan");
    }

    @Override
    public void onDisable() {
        this.saveDeathLogs();
        getLogger().info("DeathLogPlugin has been disabled.");
    }

    public LinkedList<DeathRecord> getDeathLogs(UUID playerUUID) {
        return playerDeathListener.getDeathRecords(playerUUID);
    }
    
    public PlayerDeathListener getDeathListener() {
        return playerDeathListener;
    }
    
    private void loadDeathLogs(){
        deathLogFile = new File(getDataFolder(), "deathlogs.yml");
        if(!deathLogFile.exists()){
            deathLogFile.getParentFile().mkdirs();
            saveResource("deathlogs.yml", false);
        }
        
        deathLogConfig = new YamlConfiguration();
        try {
            deathLogConfig.load(deathLogFile);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Could not load death logs", e);
        }
        
        // Parse death log data and load into the listener
        this.playerDeathListener.loadFromConfig(deathLogConfig);
    }

    private void saveDeathLogs(){
        // Save death logs to the file
        this.playerDeathListener.saveToConfig(deathLogConfig);
        try {
            deathLogConfig.save(deathLogFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save death logs", e);
        }
    }
}