package de.frxdy.frxdyutils.listener;

import de.frxdy.frxdyutils.FrxdyUtils;
import de.frxdy.frxdyutils.settings.PluginSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (FrxdyUtils.getSettings().isSendDamageNotification()) {
                if (!(event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK))) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(FrxdyUtils.getPrefix() + player.getName() + " lost " + (event.getFinalDamage() / 2) + " hearts because of " + event.getCause().toString().toLowerCase());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageByEntitiy(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (FrxdyUtils.getSettings().isSendDamageNotification()) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage(FrxdyUtils.getPrefix() + player.getName() + " lost " + (event.getFinalDamage() / 2) + " hearts because of " + event.getDamager().getType().getName().toLowerCase());
                }
            }
        }
    }

}
