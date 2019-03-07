package com.funniray.mixer.interactive;

public enum CommandMode {


    //Everyone with the node set in the
    //config will run commands on buttons with command meta
    GLOBAL_WITH_PERMISSION,
    //Everyone will run commands on buttons with command meta
    GLOBAL,
    //Only the streamer will run commands
    STREAMER_ONLY,

}
