package de.frxdy.frxdyutils.commands.positionCMD;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class PositionEventHandler implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        try {
            if (PositionCommand.positionsPages.contains(event.getClickedInventory())) {
                event.setCancelled(true);
                if (event.getSlot() == event.getClickedInventory().getSize() - 1) {
                    event.getWhoClicked().openInventory(PositionCommand.positionsPages.get(event.getCurrentItem().getAmount()));
                }else if (event.getSlot() == event.getClickedInventory().getSize() - 9) {
                    event.getWhoClicked().openInventory(PositionCommand.positionsPages.get(event.getCurrentItem().getAmount()));
                }
            }
        }catch (NullPointerException e) {
        }
    }
    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        if (PositionCommand.positionsPages.contains(event.getSource())) {
            event.setCancelled(true);
        }
    }

}
