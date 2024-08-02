package de.frxdy.frxdyutils.timer;

import de.frxdy.frxdyutils.FrxdyUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TimerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
            switch (strings[0]) {
                case "resume":
                    Timer.resume();
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(FrxdyUtils.getPrefix() + " Timer started!");
                    }
                    break;
                case "reset":
                    Timer.reset();
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(FrxdyUtils.getPrefix() + " Timer resetted!");
                    }
                    break;
                case "pause":
                    Timer.pause();
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(FrxdyUtils.getPrefix() + " Timer paused!");
                    }
                    break;
                case "set":
                    Timer.set(Integer.parseInt(strings[1]));
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(FrxdyUtils.getPrefix() + " Timer set to " + Integer.parseInt(strings[1]) + "!");
                    }
                    break;
                case "reverse":
                    Timer.reverse();
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(FrxdyUtils.getPrefix() + " Timer reversed!");
                    }
                    break;
            }
        return false;
    }
}
