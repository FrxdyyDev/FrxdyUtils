package de.frxdy.frxdyutils.settings;

import de.frxdy.frxdyutils.settings.SettingsEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.SQLException;

public class SettingsInventoryHandler implements CommandExecutor {

    public static Inventory challengeSettingsGUI = null;
    public static Inventory challengeGUI = null;

    public static Inventory gamerulesGUI = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            challengeSettingsGUI = createSettingsGUI();
            challengeGUI = createGUI();
            gamerulesGUI = createGUI();
            try {
                SettingsEventHandler.setSettingsPages();
                SettingsEventHandler.setGamerulesPages();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            player.openInventory(challengeSettingsGUI);
        }else {
            sender.sendMessage(ChatColor.DARK_GRAY + "[+] " + ChatColor.RED + "Du musst ein Spieler sein!");
        }
        return false;
    }

    private static Inventory createGUI() {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.DARK_RED + "Settings");
        inventory = fillInventory(inventory, "full", Material.BLACK_STAINED_GLASS_PANE, " ");
        return inventory;
    }

    private static Inventory createSettingsGUI() {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.DARK_RED + "Settings");
        inventory = fillInventory(inventory, "full", Material.BLACK_STAINED_GLASS_PANE, " ");
        inventory = addSettingsButtonsToGUI(inventory);
        return inventory;
    }

    private static Inventory addSettingsButtonsToGUI(Inventory inventory) {
        ItemStack challengeStack = new ItemStack(Material.NETHER_STAR);
        ItemMeta challengeMeta = challengeStack.getItemMeta();
        challengeMeta.setDisplayName(ChatColor.GOLD + "Challenges");
        challengeStack.setItemMeta(challengeMeta);

        ItemStack clockStack = new ItemStack(Material.CLOCK);
        ItemMeta clockMeta = clockStack.getItemMeta();
        clockMeta.setDisplayName(ChatColor.GOLD + "Timer");
        clockStack.setItemMeta(clockMeta);

        ItemStack gameRuleStack = new ItemStack(Material.REDSTONE);
        ItemMeta gameRuleMeta = gameRuleStack.getItemMeta();
        gameRuleMeta.setDisplayName(ChatColor.GOLD + "Gamerules");
        gameRuleStack.setItemMeta(gameRuleMeta);

        inventory.setItem(10, challengeStack);
        inventory.setItem(13, clockStack);
        inventory.setItem(16, gameRuleStack);
        return inventory;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack createSkull(Material material, String materialName, String Skullowner, int amount) {
        ItemStack itemStack = new ItemStack(material);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(Skullowner);
        meta.setDisplayName(materialName);
        itemStack.setAmount(amount);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack createItem(Material material, String materialName, int amount) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        itemStack.setAmount(amount);
        meta.setDisplayName(materialName);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static Inventory fillInventory(Inventory inventory, String mode, Material material, String materialName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(materialName);
        itemStack.setItemMeta(meta);
        return fillInventory(inventory, mode, itemStack);
    }

    private static Inventory fillInventory(Inventory inventory, String mode, ItemStack itemStack) {
        switch (mode) {
            case "full":
                for (int i = 0; i <= 26; i++) {
                    inventory.setItem(i, itemStack);
                }
            case "topRow":
                for (int i = 0; i < 9; i++) {
                    inventory.setItem(i, itemStack);
                }
            case "bottomRow":
                for (int i = (inventory.getSize() - 9); i < inventory.getSize(); i++) {
                    inventory.setItem(i, itemStack);
                }
        }

        return inventory;
    }

}
