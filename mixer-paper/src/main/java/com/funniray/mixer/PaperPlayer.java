package com.funniray.mixer;

import com.funniray.mixer.commands.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class PaperPlayer implements iPlayer {

    private Player player;

    public PaperPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    @Override
    public void sendMessage(String string) {
        this.player.sendMessage(ChatColor.translateAlternateColorCodes('&',string));
    }

    @Override
    public Collection<iPlayer> getAllPlayers() {
        HashSet<PaperPlayer> players = new HashSet<>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            players.add(new PaperPlayer(player));
        }
        return new ArrayList<>(players);
    }

    @Override
    public boolean hasPermission(PermissionNode node) {
        return true; //TODO:
    }
}
