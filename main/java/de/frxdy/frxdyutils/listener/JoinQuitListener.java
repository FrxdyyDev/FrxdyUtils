package de.frxdy.frxdyutils.listener;

import de.frxdy.frxdyutils.FrxdyUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (FrxdyUtils.getSettings().isSendJoinMessage()) {
            event.setJoinMessage(FrxdyUtils.getPrefix() + ChatColor.GREEN + ">> " + ChatColor.DARK_GRAY + event.getPlayer().getName());
        }else {
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (FrxdyUtils.getSettings().isSendJoinMessage()) {
            event.setQuitMessage(FrxdyUtils.getPrefix() + ChatColor.DARK_RED + "<< " + ChatColor.DARK_GRAY + event.getPlayer().getName());
        }else {
            event.setQuitMessage("");
        }
    }

}
