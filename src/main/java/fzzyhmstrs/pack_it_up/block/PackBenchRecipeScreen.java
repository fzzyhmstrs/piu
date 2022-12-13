package fzzyhmstrs.pack_it_up.block;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.recipe.PackBenchRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;

import java.util.*;

public class PackBenchRecipeScreen extends Screen {

    protected PackBenchRecipeScreen(Screen oldScreen) {
        super(Text.translatable("pack_it_up.recipes"));
        this.oldScreen = oldScreen;
    }

    private final Screen oldScreen;
    private static final RecipeHolder recipeHolder = new RecipeHolder();

    @Override
    protected void init() {
        super.init();
        if (!recipeHolder.initialized && this.client != null){
            recipeHolder.init(this.client);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(oldScreen);
        }
    }

    private static class RecipeHolder{

        boolean initialized = false;
        List<ItemEntry> craftingRecipes = new LinkedList<>();
        List<ItemEntry> packingRecipes = new LinkedList<>();
        int currentCraftingPage = 0;
        int currentPackingPage = 0;
        int maxCraftingPage = 0;
        int maxPackingPage = 0;

        void init(MinecraftClient client){
            if (client.world != null) {
                RecipeManager manager = client.world.getRecipeManager();
                List<PackBenchRecipe> list = manager.listAllOfType(PackBenchRecipe.TYPE);
                for (PackBenchRecipe pbr : list){
                    ItemStack output = pbr.getOutput();
                    packingRecipes.add(new ItemEntry(output,pbr));
                }
                List<CraftingRecipe> list2 = manager.listAllOfType(RecipeType.CRAFTING);
                for (CraftingRecipe cr : list2){
                    if (!cr.getId().getNamespace().equals(PIU.MOD_ID)) continue;
                    craftingRecipes.add(new ItemEntry(cr.getOutput(),cr));
                }
                initialized = true;
            }
        }

    }

    private static record ItemEntry(ItemStack stack, Recipe<?> recipe){

    }
}
