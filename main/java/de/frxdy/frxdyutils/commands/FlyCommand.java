package de.frxdy.frxdyutils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.isFlying() && player.getAllowFlight()) {
                player.setFlying(false);
                player.setAllowFlight(false);
            }else {
                player.setAllowFlight(true);
                player.setFlying(true);
            }
        }
        return false;
    }
}
