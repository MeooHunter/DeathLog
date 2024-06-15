package com.example.deathlogplugin;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DeathRecord implements ConfigurationSerializable {

    private String playerName;
    private String deathMessage;
    private Location deathLocation;
    private Date deathDate;           // Ngày tử vong
    private String causeOfDeath;      // Nguyên nhân tử vong

    public DeathRecord(PlayerDeathEvent event) {
        Player player = event.getEntity();
        this.playerName = player.getName();
        this.deathMessage = event.getDeathMessage();
        this.deathLocation = player.getLocation();
        this.deathDate = new Date();  // Lấy ngày hiện tại là ngày tử vong
        this.causeOfDeath = event.getDeathMessage(); // Giả định nguyên nhân tử vong từ tin nhắn tử vong
    }

    public DeathRecord(Map<String, Object> map) {
        this.playerName = (String) map.get("playerName");
        this.deathMessage = (String) map.get("deathMessage");
        this.deathLocation = (Location) map.get("deathLocation");
        this.deathDate = (Date) map.get("deathDate");
        this.causeOfDeath = (String) map.get("causeOfDeath");
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getDeathMessage() {
        return deathMessage;
    }

    public Location getDeathLocation() {
        return deathLocation;
    }

    public Date getDate() {
        return deathDate;
    }

    public String getCause() {
        return causeOfDeath;
    }

    public Location getLocation() {
        return deathLocation;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("playerName", playerName);
        map.put("deathMessage", deathMessage);
        map.put("deathLocation", deathLocation);
        map.put("deathDate", deathDate);
        map.put("causeOfDeath", causeOfDeath);
        return map;
    }

    public static DeathRecord deserialize(Map<String, Object> map) {
        return new DeathRecord(map);
    }

    @Override
    public String toString() {
        return "DeathRecord{" +
                "playerName='" + playerName + '\'' +
                ", deathMessage='" + deathMessage + '\'' +
                ", deathLocation=" + deathLocation +
                ", deathDate=" + deathDate +
                ", causeOfDeath='" + causeOfDeath + '\'' +
                '}';
    }
}