package de.frxdy.frxdyutils.commands;

import de.frxdy.frxdyutils.FrxdyUtils;
import de.frxdy.frxdyutils.commands.positionCMD.PositionCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.SQLException;

public class WorldResetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("confirm")) {
                    if (player.isOp()) {
                        resetWorlds();
                        try {
                            PositionCommand.clearPositionsInDatabase();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
            }else {

            }
        }else {
            try {
                resetChallenges();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            resetWorlds();
        }
        return false;
    }

    public void resetChallenges() throws SQLException {
        FrxdyUtils.DBConnection.createStatement().executeQuery("drop table POSITIONS");
        FrxdyUtils.DBConnection.createStatement().executeQuery("drop table CHALLENGES");
    }

    public static void resetWorlds() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.kickPlayer(ChatColor.DARK_GRAY + "Welt wird zurückgesetzt...!\nBitte habe etwas geduld!");
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
            World world = Bukkit.getWorlds().get(i);
            Bukkit.unloadWorld(world, true);
            File worldFile = new File(world.getWorldFolder().getPath());
            deleteWorld(worldFile);
        }
    }

    private static boolean deleteWorld(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();

            for(int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory()) {
                    WorldResetCommand.deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }

        return path.delete();
    }

}
