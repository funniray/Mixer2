package com.funniray.mixer;

import com.funniray.mixer.commands.PermissionNode;

import java.util.Collection;
import java.util.UUID;

public interface iPlayer {

    String getName();
    UUID getUUID();
    void sendMessage(String string);
    Collection<iPlayer> getAllPlayers();
    boolean hasPermission(PermissionNode node);

}
