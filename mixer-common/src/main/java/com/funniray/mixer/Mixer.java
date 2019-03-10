package com.funniray.mixer;

import com.funniray.mixer.config.Config;
import com.funniray.mixer.interactive.Interactive;
import net.engio.mbassy.bus.MBassador;

public class Mixer {

    private MBassador<Object> eventBus;
    private iPlayer player;

    public Mixer(Config config, iPlayer player){
        this.player = player;
        eventBus = new MBassador<>();
        initStreamer(config,player);
    }

    public void initStreamer(Config config, iPlayer player){
        new Thread(()-> new Interactive(config,config.token,player, this)).start();
    }

    public MBassador<Object> getEventBus() {
        return eventBus;
    }

    public iPlayer getPlayer() {
        return player;
    }
}
