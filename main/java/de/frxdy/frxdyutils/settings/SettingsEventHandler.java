package de.frxdy.frxdyutils.settings;

import de.frxdy.frxdyutils.FrxdyUtils;
import de.frxdy.frxdyutils.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SettingsEventHandler implements Listener {

    private static ConcurrentHashMap<Integer, Inventory> settingsPages = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, Inventory> gamerulesPages = new ConcurrentHashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        try {
            if (event.getClickedInventory().equals(SettingsInventoryHandler.challengeSettingsGUI) || event.getClickedInventory().equals(SettingsInventoryHandler.challengeGUI) || settingsPages.contains(event.getClickedInventory())) {
                event.setCancelled(true);
                if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Challenges")) {
                    player.openInventory(settingsPages.get(1));
                }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Timer")) {

                }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Gamerules")) {
                    player.openInventory(gamerulesPages.get(1));
                }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GRAY + "Next page")) {
                    if (settingsPages.contains(player.getOpenInventory())) {
                        player.openInventory(settingsPages.get(item.getAmount()));
                    }else if (gamerulesPages.contains(player.getOpenInventory())) {
                        player.openInventory(gamerulesPages.get(item.getAmount()));
                    }
                }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GRAY + "Last page")) {
                    if (settingsPages.contains(player.getOpenInventory())) {
                        player.openInventory(settingsPages.get(item.getAmount()));
                    }else if (gamerulesPages.contains(player.getOpenInventory())) {
                        player.openInventory(gamerulesPages.get(item.getAmount()));
                    }
                }else {
                    if (event.getSlot() > 10 && event.getSlot() < 17) {
                        if (event.getAction().equals(InventoryAction.PICKUP_HALF)) {
                            PreparedStatement statement = FrxdyUtils.DBConnection.prepareStatement("select settings from CHALLENGES where name = ?");
                            statement.setString(1, event.getCurrentItem().getItemMeta().getDisplayName());
                            ResultSet challengeSettings = statement.executeQuery();
                            List<String> settings = Arrays.asList(challengeSettings.getString(1).split(";"));
                            System.out.println(settings);
                            Inventory settingInv = Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.DARK_RED + event.getCurrentItem().getItemMeta().getDisplayName());
                            SettingsInventoryHandler.fillInventory(settingInv, "full", Material.BLACK_STAINED_GLASS_PANE, " ");
                            for (int i = 0; i < settings.size(); i++) {
                                switch (settings.size()) {
                                    case 1:
                                        if (settings.get(0).contains("Delay")) {
                                            settingInv.setItem(4, SettingsInventoryHandler.createSkull(Material.PLAYER_HEAD, ChatColor.GOLD + "+1", "LordRazen", 1));
                                        }
                                }
                            }
                            player.openInventory(settingInv);
                        }else {
                            if (Timer.isRunning()) {
                                Timer.pause();
                            }
                            String challenge = event.getCurrentItem().getItemMeta().getDisplayName();
                            if (challenge.equalsIgnoreCase("ForceBlock")) {
                            }
                            changeChallengeStatus(challenge);
                        }
                    }
                }
            }
        }catch (NullPointerException | SQLException e) {
        }
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        if (event.getSource().equals(SettingsInventoryHandler.challengeSettingsGUI) || event.getSource().equals(SettingsInventoryHandler.challengeGUI)) {
            event.setCancelled(true);
        }
    }

    public static void changeChallengeStatus(String challenge) throws SQLException {
        boolean activated;
        PreparedStatement statement = FrxdyUtils.DBConnection.prepareStatement("select activated from CHALLENGES where name = ?");
        statement.setString(1, challenge);
        ResultSet challengeSettings = statement.executeQuery();
        if (challengeSettings.getBoolean(1)) {
            activated = false;
        }else {
            activated = true;
        }
        System.out.println(activated);
        PreparedStatement preparedStatement = FrxdyUtils.DBConnection.prepareStatement("update CHALLENGES set activated = ? where name = ?");
        preparedStatement.setBoolean(1, activated);
        preparedStatement.setString(2, challenge);
        preparedStatement.execute();
    }

    public static void setSettingsPages() throws SQLException {
        ResultSet data = FrxdyUtils.DBConnection.createStatement().executeQuery("select * from CHALLENGES");
        ResultSet count = FrxdyUtils.DBConnection.createStatement().executeQuery("select count(*) from CHALLENGES");
        for (int i = 1; i <= (int)Math.ceil((count.getDouble(1) / 9)); i++) {
            Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.DARK_RED + "Challenges");
            inventory.setContents(SettingsInventoryHandler.challengeGUI.getContents());
            if (i != (int)Math.ceil((count.getDouble(1) / 9))) {
                inventory.setItem(inventory.getSize() - 1, SettingsInventoryHandler.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "Next page", i+1));
            }
            if (i > 1) {
                inventory.setItem(inventory.getSize() - 9, SettingsInventoryHandler.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "Last page", i-1));
            }
            for (int i2 = 10; i2 < 17; i2++) {
                if (data.next()) {
                    inventory.setItem(i2, SettingsInventoryHandler.createItem(Material.matchMaterial(data.getString("item")), data.getString("name"), 1));
                }
            }
            settingsPages.put(i, inventory);
        }
    }
    public static void setGamerulesPages() throws SQLException {
        ResultSet data = FrxdyUtils.DBConnection.createStatement().executeQuery("select * from GAMERULES");
        ResultSet count = FrxdyUtils.DBConnection.createStatement().executeQuery("select count(*) from GAMERULES");
        for (int i = 1; i <= (int)Math.ceil((count.getDouble(1) / 9)); i++) {
            Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.DARK_RED + "Gamerules");
            inventory.setContents(SettingsInventoryHandler.gamerulesGUI.getContents());
            if (i != (int)Math.ceil((count.getDouble(1) / 9))) {
                inventory.setItem(inventory.getSize() - 1, SettingsInventoryHandler.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "Next page", i+1));
            }
            if (i > 1) {
                inventory.setItem(inventory.getSize() - 9, SettingsInventoryHandler.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "Last page", i-1));
            }
            for (int i2 = 10; i2 < 17; i2++) {
                if (data.next()) {
                    inventory.setItem(i2, SettingsInventoryHandler.createItem(Material.matchMaterial(data.getString("item")), data.getString("name"), 1));
                }
            }
            gamerulesPages.put(i, inventory);
        }
    }

}
