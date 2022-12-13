package fzzyhmstrs.pack_it_up;


import fzzyhmstrs.pack_it_up.block.PackBenchScreenHandler;
import fzzyhmstrs.pack_it_up.item.PackScreenHandler;
import fzzyhmstrs.pack_it_up.recipe.PackBenchRecipe;
import fzzyhmstrs.pack_it_up.recipe.PackBenchRecipeSerializer;
import fzzyhmstrs.pack_it_up.registry.RegisterBlock;
import fzzyhmstrs.pack_it_up.registry.RegisterItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PIU implements ModInitializer {

    public static String MOD_ID = "pack_it_up";

    public static TagKey<Item> PLANT_ITEMS = TagKey.of(Registry.ITEM_KEY,new Identifier(MOD_ID,"plant_items"));
    public static TagKey<Item> BACKPACKS = TagKey.of(Registry.ITEM_KEY,new Identifier(MOD_ID,"backpacks"));
    public static TagKey<Item> LARGE_BACKPACKS = TagKey.of(Registry.ITEM_KEY,new Identifier(MOD_ID,"large_backpacks"));
    public static TagKey<Item> SMALL_BACKPACKS = TagKey.of(Registry.ITEM_KEY,new Identifier(MOD_ID,"small_backpacks"));
    public static TagKey<Item> GEMS = TagKey.of(Registry.ITEM_KEY,new Identifier("c","gems"));
    public static TagKey<Item> SEEDS = TagKey.of(Registry.ITEM_KEY,new Identifier("c","seeds"));

    public static ScreenHandlerType<PackScreenHandler> PACK_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MOD_ID,"pack"),new ExtendedScreenHandlerType<>(PackScreenHandler::new));
    public static ScreenHandlerType<PackBenchScreenHandler> PACK_BENCH_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MOD_ID,"pack_bench"),new ScreenHandlerType<>(PackBenchScreenHandler::new));


    public static RecipeType<PackBenchRecipe> PACK_BENCH_RECIPE = Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, PackBenchRecipe.ID),PackBenchRecipe.TYPE);

    @Override
    public void onInitialize() {
        RegisterItem.init();
        RegisterBlock.init();
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, PackBenchRecipe.ID), new PackBenchRecipeSerializer());
    }
}
