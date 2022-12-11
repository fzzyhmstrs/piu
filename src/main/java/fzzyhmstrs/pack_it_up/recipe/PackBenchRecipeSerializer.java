package fzzyhmstrs.pack_it_up.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class PackBenchRecipeSerializer implements RecipeSerializer<PackBenchRecipe> {
    @Override
    public PackBenchRecipe read(Identifier id, JsonObject jsonObject) {
        Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base"));
        Ingredient ingredient2 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "addition"));
        ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
        return new PackBenchRecipe(id,ingredient, ingredient2, itemStack);
    }

    @Override
    public PackBenchRecipe read(Identifier id, PacketByteBuf packetByteBuf) {
        Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
        Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
        ItemStack itemStack = packetByteBuf.readItemStack();
        return new PackBenchRecipe(id,ingredient, ingredient2, itemStack);
    }

    @Override
    public void write(PacketByteBuf buf, PackBenchRecipe recipe) {
        recipe.base.write(buf);
        recipe.addition.write(buf);
        buf.writeItemStack(recipe.result);
    }
}
