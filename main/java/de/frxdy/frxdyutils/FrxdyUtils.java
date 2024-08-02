package de.frxdy.frxdyutils;

import de.frxdy.frxdyutils.commands.FlyCommand;
import de.frxdy.frxdyutils.commands.GMCommand;
import de.frxdy.frxdyutils.commands.Invsee;
import de.frxdy.frxdyutils.commands.WorldResetCommand;
import de.frxdy.frxdyutils.commands.positionCMD.PositionCommand;
import de.frxdy.frxdyutils.listener.DamageListener;
import de.frxdy.frxdyutils.listener.JoinQuitListener;
import de.frxdy.frxdyutils.settings.PluginSettings;
import de.frxdy.frxdyutils.settings.SettingsInventoryHandler;
import de.frxdy.frxdyutils.timer.Timer;
import de.frxdy.frxdyutils.timer.TimerCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class FrxdyUtils extends JavaPlugin {

    public static PluginSettings getSettings() {
        return settings;
    }

    private static Plugin plugin;
    public static Plugin getPlugin() {
        return plugin;
    }

    public static Connection DBConnection;

    private static PluginSettings settings;

    private static Config pluginSettingsConfig;

    @Override
    public void onEnable() {
        settings = new PluginSettings();
        commandRegistration();
        listenerRegistration();
        try {
            setupDataBase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        plugin = this;
        pluginSettingsConfig = new Config("pluginSettings");
        Timer.updateTimer();
        Bukkit.getLogger().fine("FrxdyUtils enabled");
    }

    @Override
    public void onDisable() {

        Bukkit.getLogger().fine("FrxdyUtils disabled");
    }

    private void setupDataBase() throws IOException, SQLException {
        File database = new File("./plugins/FrxdyUtils/ChallengeDatabase.db");
        if (!database.exists()) {
            database.createNewFile();
        }
        DBConnection = DriverManager.getConnection("jdbc:sqlite:" + database.getAbsolutePath());
        DBConnection.createStatement().execute("create table if not exists POSITIONS("
                + "posName TEXT PRIMARY KEY NOT NULL,"
                + "world TEXT NOT NULL,"
                + "x INTEGER NOT NULL,"
                + "y INTEGER NOT NULL,"
                + "z INTEGER NOT NULL"
                + ");");
        DBConnection.createStatement().execute("create table if not exists CHALLENGES(" +
                "name TEXT PRIMARY KEY NOT NULL," +
                "activated boolean NOT NULL," +
                "item TEXT NOT NULL," +
                "settings TEXT NOT NULL" +
                ");");
        DBConnection.createStatement().execute("create table if not exists GAMERULES(" +
                "name TEXT PRIMARY KEY NOT NULL," +
                "activated boolean NOT NULL," +
                "item TEXT NOT NULL," +
                "settings TEXT NOT NULL" +
                ");");
    }

    private void commandRegistration() {
        getCommand("gm").setExecutor(new GMCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("timer").setExecutor(new TimerCommand());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("settings").setExecutor(new SettingsInventoryHandler());
        getCommand("position").setExecutor(new PositionCommand());
        getCommand("reset").setExecutor(new WorldResetCommand());
    }

    private void listenerRegistration() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinQuitListener(), this);
        pluginManager.registerEvents(new DamageListener(), this);
    }

    public static String getPrefix() {
        return ChatColor.DARK_PURPLE + "[" + ChatColor.DARK_BLUE + "Utils" + ChatColor.DARK_PURPLE + "] " + ChatColor.BLACK;
    }

}
