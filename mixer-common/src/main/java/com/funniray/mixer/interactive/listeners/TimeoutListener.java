package com.funniray.mixer.interactive.listeners;

import com.funniray.mixer.interactive.ButtonPressEvent;
import com.google.gson.JsonObject;
import com.mixer.interactive.resources.control.ButtonControl;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

import java.util.Date;

@Listener(references = References.Strong)
public class TimeoutListener {

    @Handler
    public void onPress(ButtonPressEvent event) {
        JsonObject meta = event.getInteractiveControl().getMeta();

        if (!(event.getInteractiveControl() instanceof ButtonControl))
            return;

        ButtonControl control = (ButtonControl) event.getInteractiveControl();

        if (meta.get("timeout") == null)
            return;

        control.setCooldown(new Date().getTime() + 1000 * meta.get("timeout").getAsJsonObject().get("value").getAsInt());
        event.setShouldUpdate(true);
    }
}
