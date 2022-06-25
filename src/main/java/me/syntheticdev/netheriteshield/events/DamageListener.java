package me.syntheticdev.netheriteshield.events;

import me.syntheticdev.netheriteshield.NetheriteShield;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;

public class DamageListener implements Listener {
    public DamageListener() {}

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        DamageCause cause = event.getCause();

        switch (event.getEntityType()) {
            case DROPPED_ITEM:
                if (!(event.getEntity() instanceof Item)) break;
                if (!(cause.equals(DamageCause.LAVA) || cause.equals(DamageCause.FIRE)
                    || cause.equals(DamageCause.FIRE_TICK) || cause.equals(DamageCause.HOT_FLOOR)
                    || cause.equals(DamageCause.LIGHTNING))) break;

                Item item = (Item)event.getEntity();
                if (!NetheriteShield.is(item.getItemStack())) break;
                item.setFireTicks(0);
                item.setVisualFire(false);
                event.setCancelled(true);
                break;
            case PLAYER:
                if (!cause.equals(DamageCause.FALL)) break;
                Player player = (Player)event.getEntity();
                PlayerInventory inventory = player.getInventory();

                ItemStack mainHand = inventory.getItemInMainHand();
                ItemStack offHand = inventory.getItemInOffHand();

                ItemStack shield = NetheriteShield.is(mainHand) ? mainHand
                        : (NetheriteShield.is(offHand) ? offHand : null);
                if (shield == null) break;

                Damageable meta = (Damageable)shield.getItemMeta();

                int damageToReduce = (int)Math.ceil(event.getDamage() / 2);
                meta.setDamage(meta.getDamage() + damageToReduce);
                event.setDamage(event.getDamage() - damageToReduce);
                shield.setItemMeta(meta);
                break;
        }
    }
}
