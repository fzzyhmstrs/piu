package fzzyhmstrs.pack_it_up.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

public class PackBenchRecipe implements Recipe<Inventory> {

    public final Ingredient base;
    public final Ingredient addition;
    public final ItemStack result;
    private final Identifier id;
    public static final String ID = "packing";
    public static final RecipeType<PackBenchRecipe> TYPE = new RecipeType<>() {
    };

    public PackBenchRecipe(Identifier id, Ingredient base, Ingredient addition, ItemStack result) {
        this.id = id;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.base.test(inventory.getStack(0)) && this.addition.test(inventory.getStack(1));
    }

    public boolean testAddition(ItemStack stack) {
        return this.addition.test(stack);
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        ItemStack itemStack = this.result.copy();
        NbtCompound nbtCompound = inventory.getStack(0).getNbt();
        if (nbtCompound != null) {
            itemStack.setNbt(nbtCompound.copy());
        }
        return itemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getOutput() {
        return result.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return new PackBenchRecipeSerializer();
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }



}
