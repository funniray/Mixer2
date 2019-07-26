package com.funniray.mixer;

import com.funniray.mixer.commands.istart;
import com.funniray.mixer.commands.istop;
import com.funniray.mixer.config.Config;
import com.funniray.mixer.config.ConfigParser;
import com.funniray.mixer.interactiveListeners.RunAsServerListener;
import com.funniray.mixer.interactiveListeners.RunCommandListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class MixerPaper extends JavaPlugin {

    public Config config = null;
    public static MixerPaper instance;
    public Mixer mixer = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        try {
            config = ConfigParser.loadConfig(this.getDataFolder().getPath() + "/config.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mixer.addListener(RunAsServerListener.class);
        Mixer.addListener(RunCommandListener.class);
        this.getCommand("istart").setExecutor(new istart());
        this.getCommand("istop").setExecutor(new istop());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
