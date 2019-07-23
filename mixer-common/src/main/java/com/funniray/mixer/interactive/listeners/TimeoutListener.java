package com.funniray.mixer.interactive.listeners;

import com.funniray.mixer.interactive.ButtonPressEvent;
import com.google.gson.JsonObject;
import net.engio.mbassy.listener.Handler;

import java.util.Date;

public class TimeoutListener {

    @Handler
    public void onPress(ButtonPressEvent event) {
        JsonObject meta = event.getInteractiveControl().getMeta();

        if (meta.get("timeout") == null)
            return;

        event.getInteractiveControl().setCooldown(new Date().getTime() + 1000 * meta.get("timeout")
                .getAsJsonObject().get("value").getAsInt());
        event.setShouldUpdate(true);
    }
}
