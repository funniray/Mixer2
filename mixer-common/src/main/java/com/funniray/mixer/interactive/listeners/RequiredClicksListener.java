package com.funniray.mixer.interactive.listeners;

import com.funniray.mixer.interactive.ButtonPressEvent;
import com.google.gson.JsonObject;
import net.engio.mbassy.listener.Handler;

public class RequiredClicksListener {

    @Handler(priority = 10)
    public void onPress(ButtonPressEvent event) {
        JsonObject meta = event.getInteractiveControl().getMeta();

        if (meta.get("requiredClicks") == null)
            return;

        double clickedPercent = meta.get("requiredClicks").getAsJsonObject().get("value").getAsInt()/(double)event.getPreviouslyPressed().size();

        if (clickedPercent < 1) {
            event.setCancelled(true);
            event.setShouldReset(false);
        }
    }
}
