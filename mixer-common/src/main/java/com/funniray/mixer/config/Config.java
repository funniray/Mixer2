package com.funniray.mixer.config;

import com.funniray.mixer.chat.ChatMode;
import com.funniray.mixer.interactive.CommandMode;
import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.List;

public class Config {

    public ChatMode chatMode = ChatMode.STREAMER_ONLY;
    public String chatModeNode = "mixer.interactive.global_chat";
    public boolean multiStreamerMode = false;
    public CommandMode commandMode = CommandMode.STREAMER_ONLY;
    public String commandModeNode = "mixer.interactive.global_commands";
    public String token = "That's your job.";
    //public String clientID = "d04e85fd1cb06e4eb9891fc118fe75893eca399955189926";
    public String shareCode = "dbzktlsk";
    public int projectID = 191773;
    public boolean showAlerts = true;
    public String followCommand = "";
    public String subCommand = "";
    public String resubCommand = "";
    public List<String> bannedWords = Arrays.asList("Put bad words", "here");

    @Expose(deserialize = false, serialize = false)
    public boolean shouldSaveToFile = true; //For custom plugins that edit the config

}
