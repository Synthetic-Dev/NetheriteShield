package me.syntheticdev.netheriteshield;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.SmithingRecipe;

public class Recipes {

    public static void register() {
        ItemStack shield = NetheriteShield.create();

        {
            NamespacedKey recipeKey = new NamespacedKey(NetheriteShield.getPlugin(), "smith_netherite_shield");
            SmithingRecipe recipe = new SmithingRecipe(recipeKey, shield,
                    new RecipeChoice.MaterialChoice(Material.SHIELD),
                    new RecipeChoice.MaterialChoice(Material.NETHERITE_INGOT));
            Bukkit.addRecipe(recipe);
        }
        {
            NamespacedKey recipeKey = new NamespacedKey(NetheriteShield.getPlugin(), "netherite_shield");
            ShapedRecipe recipe = new ShapedRecipe(recipeKey, shield);
            recipe.shape(
                    "WIW",
                    "WWW",
                    " W "
            );
            recipe.setIngredient('W', new RecipeChoice.MaterialChoice(Tag.PLANKS));
            recipe.setIngredient('I', Material.NETHERITE_INGOT);
            Bukkit.addRecipe(recipe);
        }
    }
}
