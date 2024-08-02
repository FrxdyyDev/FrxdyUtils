package de.frxdy.frxdyutils.timer;

import de.frxdy.frxdyutils.FrxdyUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Timer {

    private static int time = 0;

    public static boolean isRunning() {
        return running;
    }

    private static boolean running = false;

    private enum countDirection {
        up,
        down
    }

    public static countDirection countDirection = Timer.countDirection.up;

    public static void updateTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(FrxdyUtils.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (running) {
                    if (countDirection.equals(Timer.countDirection.up)) {
                        time += 45;
                    }else {
                        if (time >= 45) {
                            time -= 45;
                        }else {
                            running = false;
                        }
                    }
                    int hours = time / 3600000;
                    int minutes = time / 60000 % 60;
                    int second = time / 1000 % 60;
                    StringBuilder message = new StringBuilder(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD);
                    message.append(String.format("%02d", hours)).append(":");
                    message.append(String.format("%02d", minutes)).append(":");
                    message.append(String.format("%02d", second));

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message.toString()));
                    }
                }else {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(new StringBuilder(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Timer paused...").toString()));
                    }
                }
            }
        }, 0, 1);
    }

    public static void set(int pTime) {
        Bukkit.broadcastMessage("" + pTime);
        if (running) {
            pause();
            time = pTime * 1000;
            resume();
        }else {
            time = pTime * 1000;
        }
    }

    public static void reset() {
        if (running) {
            running = false;
            time = 0;
            if (countDirection.equals(Timer.countDirection.down)) {
                countDirection = Timer.countDirection.up;
            }
        }
    }

    public static void pause() {
        if (running) {
            running = false;
        }
    }

    public static void resume() {
        if (!running) {
            running = true;
        }
    }

    public static void reverse() {
        boolean lol = running;
        if (lol) {
            pause();
        }
        if (countDirection.equals(Timer.countDirection.up)) {
            countDirection = Timer.countDirection.down;
        }else {
            countDirection = Timer.countDirection.up;
        }
        if (lol) {
            resume();
        }
    }

}
