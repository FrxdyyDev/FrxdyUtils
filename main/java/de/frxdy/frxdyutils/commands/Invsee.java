package de.frxdy.frxdyutils.commands;

import de.frxdy.frxdyutils.FrxdyUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Invsee implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 1) {
                Player target = Bukkit.getPlayer(strings[0]);
                if (target != null) {
                    player.openInventory(target.getInventory());
                }
            }else {
                player.sendMessage(FrxdyUtils.getPrefix() + ChatColor.RED + "Wrong Usage! Use /invsee <playername>");
            }
        }else {
            commandSender.sendMessage("You have to be a player!");
        }
        return false;
    }
}
