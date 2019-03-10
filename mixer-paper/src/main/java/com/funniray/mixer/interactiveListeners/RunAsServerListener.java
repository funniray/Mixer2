package com.funniray.mixer.interactiveListeners;

import com.funniray.mixer.interactive.ButtonPressEvent;
import com.google.gson.JsonObject;
import net.engio.mbassy.listener.Handler;
import org.bukkit.Bukkit;

public class RunAsServerListener {

    @Handler
    public void onPress(ButtonPressEvent event) {
        JsonObject meta = event.getInteractiveControl().getMeta();

        if (meta.get("runCommandAsServer") == null)
            return;

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),event.replaceString(meta.get("runCommandAsServer").getAsJsonObject().get("value").getAsString()));
    }
}
