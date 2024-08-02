package de.frxdy.frxdyutils.commands;

import de.frxdy.frxdyutils.FrxdyUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GMCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (strings.length == 2) {
            Player target = Bukkit.getPlayer(strings[1]);
            if (player.isOp()) {
                if (target != null) {
                    switch (strings[0]) {
                        case "0":
                            target.setGameMode(GameMode.SURVIVAL);
                            target.setFlying(false);
                            target.setAllowFlight(true);
                            break;
                        case "1":
                            target.setGameMode(GameMode.CREATIVE);
                            target.setFlying(false);
                            target.setAllowFlight(true);
                            break;
                        case "2":
                            target.setGameMode(GameMode.ADVENTURE);
                            target.setFlying(false);
                            target.setAllowFlight(true);
                            break;
                        case "3":
                            target.setGameMode(GameMode.SPECTATOR);
                            target.setFlying(false);
                            target.setAllowFlight(true);
                            break;
                        default:
                            break;
                    }
                    player.sendMessage(FrxdyUtils.getPrefix() + "Gamemode of " + target.getName() + " changed to " + target.getGameMode());
                    target.sendMessage(FrxdyUtils.getPrefix() + "Your gamemode has been changed to " + target.getGameMode());
                }else {
                    player.sendMessage(FrxdyUtils.getPrefix() + strings[1] + ChatColor.RED + " is not online.");
                }
            }else {
                player.sendMessage(FrxdyUtils.getPrefix() + ChatColor.RED + "You don´t have the permission to use this command.");
            }
        }else {
            if (player.isOp()) {
                switch (strings[0]) {
                    case "0":
                        player.setGameMode(GameMode.SURVIVAL);
                        player.setFlying(false);
                        player.setAllowFlight(true);
                        break;
                    case "1":
                        player.setGameMode(GameMode.CREATIVE);
                        player.setFlying(false);
                        player.setAllowFlight(true);
                        break;
                    case "2":
                        player.setGameMode(GameMode.ADVENTURE);
                        player.setFlying(false);
                        player.setAllowFlight(true);
                        break;
                    case "3":
                        player.setGameMode(GameMode.SPECTATOR);
                        player.setFlying(false);
                        player.setAllowFlight(true);
                        break;
                    default:
                        break;
                }
                player.sendMessage(FrxdyUtils.getPrefix() + "Your gamemode has been changed to " + player.getGameMode());
            }else {
                player.sendMessage(FrxdyUtils.getPrefix() + ChatColor.RED + "You don´t have the permission to use this command.");
            }
        }
        return false;
    }
}
