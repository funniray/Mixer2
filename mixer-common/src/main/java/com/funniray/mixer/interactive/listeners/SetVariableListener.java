package com.funniray.mixer.interactive.listeners;

import com.funniray.mixer.interactive.ButtonPressEvent;
import com.google.gson.JsonObject;
import net.engio.mbassy.listener.Handler;

public class SetVariableListener {

    @Handler(priority = 90)
    public void onPress(ButtonPressEvent event) {
        JsonObject meta = event.getInteractiveControl().getMeta();
        String variableType = "local";

        if (meta.get("setVariable") == null || meta.get("variable") == null)
            return;

        String variableName = meta.get("setVariable").getAsJsonObject().get("value").getAsString();
        String variable = meta.get("variable").getAsJsonObject().get("value").getAsString();

        if (meta.get("variableType") != null) {
            variableType = meta.get("variableType").getAsJsonObject().get("value").getAsString();
        }

        if (variableType.equals("global")) {
            event.getGlobalVars().put(variableName,event.replaceString(variable));
        } else if (variableType.equals("local")) {
            event.setVariableForPresser(event.getUserPressed(),variableName,event.replaceString(variable));
        }
    }
}
