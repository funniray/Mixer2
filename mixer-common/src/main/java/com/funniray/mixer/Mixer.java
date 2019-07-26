package com.funniray.mixer;

import com.funniray.mixer.config.Config;
import com.funniray.mixer.interactive.Interactive;
import net.engio.mbassy.bus.MBassador;

import java.util.HashSet;

public class Mixer {

    private MBassador<Object> eventBus;
    private iPlayer player;
    private static HashSet<Class> handlersToRegister = new HashSet<>();
    private Interactive interactive;

    public Mixer(Config config, iPlayer player){
        this.player = player;
        eventBus = new MBassador<>();
        initStreamer(config,player);

        //Initialize all the handlers
        for(Class clazz : handlersToRegister) {
            try {
                eventBus.subscribe(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addListener(Class clazz) {
        handlersToRegister.add(clazz);
    }

    public void initStreamer(Config config, iPlayer player){
        new Thread(()-> interactive = new Interactive(config,config.token,player, this)).start();
    }

    public void end(){
        interactive.disconnect();
    }

    public MBassador<Object> getEventBus() {
        return eventBus;
    }

    public iPlayer getPlayer() {
        return player;
    }
}
