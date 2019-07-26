package com.funniray.mixer.interactive.listeners;

import com.funniray.mixer.interactive.ButtonPressEvent;
import com.google.gson.JsonObject;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

@Listener(references = References.Strong)
public class SwitchWindowListener {

    @Handler
    public void onPress(ButtonPressEvent event) {
        JsonObject meta = event.getInteractiveControl().getMeta();

        if (meta.get("switchWindow") == null)
            return;

        event.getInteractive().switchSceneForParticipant(event.getUserPressed(),
                meta.get("switchWindow").getAsJsonObject().get("value").getAsString());
    }
}
