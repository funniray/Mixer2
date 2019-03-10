package com.funniray.mixer;

import com.funniray.mixer.config.Config;
import com.funniray.mixer.config.ConfigParser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class MixerPaper extends JavaPlugin implements Listener {

    public Config config = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        //TODO: Start with command instead
        try {
            config = ConfigParser.loadConfig(this.getDataFolder().getPath() + "/config.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onJoin(PlayerGameModeChangeEvent event) { //TODO: Make a command for this
        new Mixer(this.config, new PaperPlayer(event.getPlayer()));
    }
}
