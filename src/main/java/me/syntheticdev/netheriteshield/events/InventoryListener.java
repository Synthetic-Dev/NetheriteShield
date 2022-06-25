package me.syntheticdev.netheriteshield.events;

import me.syntheticdev.netheriteshield.NetheriteShield;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.Map;

public class InventoryListener implements Listener {
    public InventoryListener() {}

    @EventHandler
    public void onPrepareSmith(PrepareSmithingEvent event) {
        SmithingInventory inventory = event.getInventory();

        ItemStack base = inventory.getItem(0);
        ItemStack addition = inventory.getItem(1);

        if (base != null && addition != null) {
            if (base.getType().equals(Material.SHIELD) && !NetheriteShield.is(base)
                && addition.getType().equals(Material.NETHERITE_INGOT)) {

                ItemStack result = NetheriteShield.create(base.getItemMeta());
                inventory.setResult(result);
                event.setResult(result);
                return;
            }
        }

        inventory.setResult(null);
        event.setResult(null);
    }

//    @EventHandler
//    public void onSmith(SmithItemEvent event) {
//
//    }

    @EventHandler
    public void onRepair(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();

        ItemStack item = anvil.getItem(0);
        ItemStack modifier = anvil.getItem(1);

        if (item == null || modifier == null) return;
        if (!NetheriteShield.is(item) || !modifier.getType().equals(Material.NETHERITE_INGOT)) return;

        ItemStack shield = item.clone();
        Damageable meta = (Damageable)shield.getItemMeta();
        if (!meta.hasDamage()) return;

        anvil.setRepairCost(3);

        int maxDurability = shield.getType().getMaxDurability();
        meta.setDamage(meta.getDamage() - (int)Math.round(maxDurability * 0.2));
        shield.setItemMeta(meta);

        event.setResult(shield);
    }

    @EventHandler
    public void onCombine(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();

        ArrayList<ItemStack> shields = new ArrayList<>();
        for (ItemStack item : inventory.getMatrix()) {
            if (item == null || item.getType().equals(Material.AIR)) continue;

            boolean is = NetheriteShield.is(item);
            if (is) shields.add(item);

            if (shields.size() > 2 || (!is && shields.size() > 0)) {
                inventory.setResult(null);
                return;
            }
        }

        if (shields.size() == 2) {
            ItemStack result = NetheriteShield.create();
            Damageable resultMeta = (Damageable)result.getItemMeta();

            ItemStack first = shields.get(0);
            Damageable firstMeta = (Damageable)first.getItemMeta();

            ItemStack second = shields.get(0);
            Damageable secondMeta = (Damageable)second.getItemMeta();

            int maxDurability = Material.SHIELD.getMaxDurability();
            resultMeta.setDamage(firstMeta.getDamage() - (maxDurability - secondMeta.getDamage()));
            result.setItemMeta(resultMeta);

            inventory.setResult(result);
        }
    }
}
