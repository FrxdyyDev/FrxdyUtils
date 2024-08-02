package de.frxdy.frxdyutils.commands.positionCMD;

import de.frxdy.frxdyutils.FrxdyUtils;
import de.frxdy.frxdyutils.settings.SettingsInventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PositionCommand implements CommandExecutor {

    public static List<ResultSet> data = new ArrayList<>();
    public static ConcurrentHashMap<Integer, Inventory> positionsPages = new ConcurrentHashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (data.size() == 0) {
            try {
                data.add(FrxdyUtils.DBConnection.createStatement().executeQuery("select * from POSITIONS"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage(ChatColor.DARK_GRAY + "[+] " + ChatColor.DARK_GREEN + "Possible Positions: ");
                ResultSet resultSet = data.get(0);
                try {
                    while (resultSet.next()) {
                        player.sendMessage(ChatColor.DARK_GRAY + "  [+] " + ChatColor.DARK_GREEN + resultSet.getString("posName"));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                data.remove(0);
                try {
                    data.add(FrxdyUtils.DBConnection.createStatement().executeQuery("select * from POSITIONS"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else if (args[0].equalsIgnoreCase("inv")) {
                ResultSet resultSet = data.get(0);
                try {
                    ResultSet count = null;
                    try {
                        count = FrxdyUtils.DBConnection.createStatement().executeQuery("select count(*) from CHALLENGES");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    for (int i = 1; i <= (int) Math.ceil(count.getDouble(1) / 9); i++) {
                        Inventory inv = Bukkit.createInventory(null, 9*6, ChatColor.DARK_RED + "Positions");
                        SettingsInventoryHandler.fillInventory(inv, "topRow", Material.BLACK_STAINED_GLASS_PANE, " ");
                        SettingsInventoryHandler.fillInventory(inv, "bottomRow", Material.BLACK_STAINED_GLASS_PANE, " ");

                        if (i != (int)Math.ceil((count.getDouble(1) / 9))) {
                            inv.setItem(inv.getSize() - 1, SettingsInventoryHandler.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "Next page", i+1));
                        }
                        if (i > 1) {
                            inv.setItem(inv.getSize() - 9, SettingsInventoryHandler.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "Last page", i-1));
                        }
                        for (int i2 = 9; i2 < inv.getSize() - 10; i2++) {
                            if (resultSet.next()) {
                                String posName = resultSet.getString("posName");
                                String world = resultSet.getString("world");
                                Double x = resultSet.getDouble("x");
                                Double y = resultSet.getDouble("y");
                                Double z = resultSet.getDouble("z");
                                ItemStack stack = new ItemStack(Material.BARRIER);
                                if (world.contains("Overworld")) {
                                    stack.setType(Material.GRASS_BLOCK);
                                }else if (world.contains("Nether")) {
                                    stack.setType(Material.NETHERRACK);
                                }else {
                                    stack.setType(Material.END_STONE);
                                }
                                ItemMeta meta = stack.getItemMeta();
                                meta.setDisplayName(ChatColor.GREEN + posName);
                                List<String> lore = new ArrayList<>();
                                lore.add(ChatColor.GREEN + "x: " + x);
                                lore.add(ChatColor.GREEN + "y: " + y);
                                lore.add(ChatColor.GREEN + "z: " + z);
                                meta.setLore(lore);
                                stack.setItemMeta(meta);
                                inv.setItem(i2, stack);
                            }
                        }
                        positionsPages.put(i, inv);
                    }
                }catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                player.openInventory(positionsPages.get(1));
                data.remove(0);
                try {
                    data.add(FrxdyUtils.DBConnection.createStatement().executeQuery("select * from POSITIONS"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else {
                boolean existsInData = false;
                ResultSet resultSet = data.get(0);
                try {
                    while (resultSet.next()) {
                        if (resultSet.getString("posName").equalsIgnoreCase(args[0])) {
                            existsInData = true;
                        }
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                data.remove(0);
                try {
                    data.add(FrxdyUtils.DBConnection.createStatement().executeQuery("select * from POSITIONS"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                ResultSet dataToOutput = data.get(0);
                if (existsInData) {
                    try {
                        String posName = "";
                        String world = "";
                        Double x;
                        Double y;
                        Double z;
                        while (dataToOutput.next()) {
                            if (dataToOutput.getString("posName").equalsIgnoreCase(args[0])) {
                                posName = dataToOutput.getString("posName");
                                world = dataToOutput.getString("world");
                                x = dataToOutput.getDouble("x");
                                y = dataToOutput.getDouble("y");
                                z = dataToOutput.getDouble("z");
                                player.sendMessage(ChatColor.DARK_GRAY + "[+] " + ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + posName + ChatColor.DARK_GREEN + "]\n"
                                        + ChatColor.DARK_GRAY + "[+] " + ChatColor.GREEN + world + ChatColor.DARK_GREEN + " | " + ChatColor.GREEN + x + ChatColor.DARK_GREEN + " | " + ChatColor.GREEN + y + ChatColor.DARK_GREEN
                                        + " | " + ChatColor.GREEN + z);
                            }
                        }
                        data.remove(0);
                        try {
                            data.add(FrxdyUtils.DBConnection.createStatement().executeQuery("select * from POSITIONS"));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        String world = player.getWorld().getName();
                        if (world.contains("nether")) {
                            world = "Nether";
                        }else if (world.contains("end")) {
                            world = "End";
                        }else {
                            world = "Overworld";
                        }
                        PreparedStatement preparedStatement = FrxdyUtils.DBConnection.prepareStatement("insert into POSITIONS(posName, world, x, y, z) values(?,?,?,?,?)");
                        preparedStatement.setString(1, args[0]);
                        preparedStatement.setString(2, world);
                        preparedStatement.setDouble(3, player.getLocation().getBlockX());
                        preparedStatement.setDouble(4, player.getLocation().getBlockY());
                        preparedStatement.setDouble(5, player.getLocation().getBlockZ());
                        preparedStatement.execute();
                        data.remove(0);
                        try {
                            data.add(FrxdyUtils.DBConnection.createStatement().executeQuery("select * from POSITIONS"));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    player.sendMessage(ChatColor.DARK_GRAY + "[+] " + ChatColor.DARK_GREEN + "Position [" + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + "] erfolgreich erstellt!");
                }
            }
        }else if (args.length == 2){
            if (args[0].equalsIgnoreCase("delete")) {
                boolean existsInData = false;
                ResultSet resultSet = data.get(0);
                try {
                    while (resultSet.next()) {
                        if (resultSet.getString("posName").equalsIgnoreCase(args[1])) {
                            existsInData = true;
                        }
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                if (existsInData) {
                    try {
                        PreparedStatement preparedStatement = FrxdyUtils.DBConnection.prepareStatement("delete from POSITIONS where posName = ?");
                        preparedStatement.setString(1, args[1]);
                        preparedStatement.execute();
                        data.remove(0);
                        try {
                            data.add(FrxdyUtils.DBConnection.createStatement().executeQuery("select * from POSITIONS"));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    player.sendMessage(ChatColor.DARK_GRAY + "[+] " + ChatColor.DARK_GREEN + "Position [" + ChatColor.GREEN + args[1] + ChatColor.DARK_GREEN + "] erfolgreich gelöscht!");
                }else {
                    player.sendMessage(ChatColor.DARK_GRAY + "[+] " + ChatColor.RED + "Position existiert nicht!");
                }
            }
        }else {

        }
        return false;
    }

    public static void clearPositionsInDatabase() throws SQLException {
        PreparedStatement preparedStatement = FrxdyUtils.DBConnection.prepareStatement("delete from POSITIONS");
        preparedStatement.execute();
    }

}
