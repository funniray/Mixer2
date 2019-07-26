package com.funniray.mixer.commands;

import com.funniray.mixer.Mixer;
import com.funniray.mixer.MixerPaper;
import com.funniray.mixer.PaperPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class istart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            new Thread(()-> MixerPaper.instance.mixer = new Mixer(MixerPaper.instance.config, new PaperPlayer(player))).start();
        } else {
            sender.sendMessage("You must be a player to use this command!");
        }

        return true;
    }
}
