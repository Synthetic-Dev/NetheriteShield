package me.syntheticdev.netheriteshield;

import me.syntheticdev.netheriteshield.events.DamageListener;
import me.syntheticdev.netheriteshield.events.InventoryListener;
import me.syntheticdev.netheriteshield.events.PlayerJoinListener;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.UUID;

public final class NetheriteShield extends JavaPlugin {
    private static NetheriteShield plugin;

    public static boolean is(ItemStack item) {
        if (!item.getType().equals(Material.SHIELD) || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer nbt = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "netherite-shield");
        return nbt.has(key, PersistentDataType.BYTE);
    }

    public static ItemStack create() {
        return create(null);
    }

    public static ItemStack create(@Nullable ItemMeta initialMeta) {
        ItemStack shield = new ItemStack(Material.SHIELD, 1);
        ItemMeta meta = initialMeta == null ? shield.getItemMeta() : initialMeta.clone();

        if (!meta.hasDisplayName()) {
            meta.setDisplayName(ChatColor.RESET + "Netherite Shield");
        }
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.setCustomModelData(539272);

        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH,
                new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_MAX_HEALTH.name(),
                        4, AttributeModifier.Operation.ADD_NUMBER)
        );
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED,
                new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_MOVEMENT_SPEED.name(),
                        -0.02, AttributeModifier.Operation.ADD_NUMBER)
        );

        PersistentDataContainer nbt = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "netherite-shield");
        nbt.set(key, PersistentDataType.BYTE, (byte)1);

        shield.setItemMeta(meta);
        return shield;
    }

    public static void handleBreak(ItemStack shield, Player player) {
        if (!NetheriteShield.is(shield)) return;

        Damageable meta = (Damageable)shield.getItemMeta();
        int maxDurability = shield.getType().getMaxDurability();

        if (meta.getDamage() > maxDurability) {
            World world = player.getWorld();
            PlayerInventory inventory = player.getInventory();
            inventory.removeItem(shield);

            world.spawnParticle(Particle.BLOCK_CRACK, player.getLocation().add(0, 1.25, 0), 15, Material.NETHERITE_BLOCK.createBlockData());
            world.playSound(player, Sound.ITEM_SHIELD_BREAK, 1f, 1f);
        }
    }

    @Override
    public void onEnable() {
        plugin = this;

        this.registerEvents();
        Recipes.register();
    }

    public static NetheriteShield getPlugin() {
        return plugin;
    }

    private void registerEvents() {
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new PlayerJoinListener(), this);
        manager.registerEvents(new DamageListener(), this);
        manager.registerEvents(new InventoryListener(), this);
    }
}
