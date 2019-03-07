package com.funniray.mixer.chat;

public enum ChatMode {

    //Only the streamer can see the chat
    STREAMER_ONLY,
    //Everyone on the server can see the chat
    GLOBAL,
    //Only people with the permission node can see the chat
    GLOBAL_WITH_NODE
    //TODO: SYNCCHAT (Syncs the chat between the mixer chat and server)
}
