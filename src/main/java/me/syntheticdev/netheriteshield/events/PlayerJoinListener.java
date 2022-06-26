package me.syntheticdev.netheriteshield.events;

import me.syntheticdev.netheriteshield.NetheriteShield;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;

public class PlayerJoinListener implements Listener {
    public PlayerJoinListener() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setResourcePack("http://192.248.166.29:3000/spigot-resource/netherite-shield", null, true);
    }
}
