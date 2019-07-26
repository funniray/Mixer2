package com.funniray.mixer.commands;

import com.funniray.mixer.MixerPaper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class istop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MixerPaper.instance.mixer.end();
        return true;
    }
}
