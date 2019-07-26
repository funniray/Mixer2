package com.funniray.mixer.interactiveListeners;

import com.funniray.mixer.MixerPaper;
import com.funniray.mixer.interactive.ButtonPressEvent;
import com.google.gson.JsonObject;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;
import org.bukkit.Bukkit;

@Listener(references = References.Strong)
public class RunAsServerListener {

    @Handler
    public void onPress(ButtonPressEvent event) {
        JsonObject meta = event.getInteractiveControl().getMeta();

        if (meta.get("runCommandAsServer") == null || event.isCancelled())
            return;

        String command = event.replaceString(meta.get("runCommandAsServer").getAsJsonObject().get("value").getAsString());

        Bukkit.getScheduler().runTask(MixerPaper.instance,() -> Bukkit.getServer()
                .dispatchCommand(Bukkit.getConsoleSender(), command));
    }
}
