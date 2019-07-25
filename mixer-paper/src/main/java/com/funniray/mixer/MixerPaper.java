package com.funniray.mixer;

import com.funniray.mixer.config.Config;
import com.funniray.mixer.config.ConfigParser;
import com.funniray.mixer.interactiveListeners.RunAsServerListener;
import com.funniray.mixer.interactiveListeners.RunCommandListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class MixerPaper extends JavaPlugin implements Listener {

    public Config config = null;
    public static MixerPaper instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        //TODO: Start with command instead
        instance = this;
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
    public void onJoin(PlayerJoinEvent event) { //TODO: Make a command for this
        new Thread(()-> {
            Mixer mixer = new Mixer(this.config, new PaperPlayer(event.getPlayer()));
            mixer.getEventBus().subscribe(new RunAsServerListener());
            mixer.getEventBus().subscribe(new RunCommandListener());
        }).start();
    }
}
