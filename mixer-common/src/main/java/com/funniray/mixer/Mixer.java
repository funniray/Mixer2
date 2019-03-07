package com.funniray.mixer;

import com.funniray.mixer.config.Config;
import com.funniray.mixer.interactive.Interactive;
import com.google.common.eventbus.EventBus;

public class Mixer {

    private EventBus eventBus;

    public Mixer(Config config, iPlayer player){
        eventBus = new EventBus();
        initStreamer(config,player);
    }

    public void initStreamer(Config config, iPlayer player){
        new Thread(()-> new Interactive(config,config.token,player, this)).start();
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
