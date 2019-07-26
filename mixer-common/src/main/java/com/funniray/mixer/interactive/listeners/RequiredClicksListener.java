package com.funniray.mixer.interactive.listeners;

import com.funniray.mixer.interactive.ButtonPressEvent;
import com.google.gson.JsonObject;
import com.mixer.interactive.resources.control.ButtonControl;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

@Listener(references = References.Strong)
public class RequiredClicksListener {

    @Handler(priority = 10)
    public void onPress(ButtonPressEvent event) {
        JsonObject meta = event.getInteractiveControl().getMeta();

        if (meta.get("requiredClicks") == null)
            return;

        float clickedPercent = (float) event.getPreviouslyPressed().size()/meta.get("requiredClicks").getAsJsonObject().get("value").getAsInt();

        if (!(event.getInteractiveControl() instanceof ButtonControl))
            return;

        ButtonControl control = (ButtonControl) event.getInteractiveControl();

        control.setProgress(clickedPercent);

        if (clickedPercent < 1) {
            event.setCancelled(true);
            event.setShouldReset(false);
            event.setShouldUpdate(true);
        } else {
            control.setProgress(0F);
            event.setCancelled(false);
            event.setShouldReset(true);
            event.setShouldUpdate(true);
        }
    }
}
