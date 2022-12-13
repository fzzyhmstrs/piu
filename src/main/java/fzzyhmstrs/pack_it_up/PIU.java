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
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class PIU implements ModInitializer {

    public static String MOD_ID = "pack_it_up";

    public static TagKey<Item> PLANT_ITEMS = TagKey.of(RegistryKeys.ITEM,new Identifier(MOD_ID,"plant_items"));
    public static TagKey<Item> BACKPACKS = TagKey.of(RegistryKeys.ITEM,new Identifier(MOD_ID,"backpacks"));
    public static TagKey<Item> LARGE_BACKPACKS = TagKey.of(RegistryKeys.ITEM,new Identifier(MOD_ID,"large_backpacks"));
    public static TagKey<Item> SMALL_BACKPACKS = TagKey.of(RegistryKeys.ITEM,new Identifier(MOD_ID,"small_backpacks"));
    public static TagKey<Item> GEMS = TagKey.of(RegistryKeys.ITEM,new Identifier("c","gems"));
    public static TagKey<Item> SEEDS = TagKey.of(RegistryKeys.ITEM,new Identifier("c","seeds"));

    public static ScreenHandlerType<PackScreenHandler> PACK_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER,new Identifier(MOD_ID,"pack"),new ExtendedScreenHandlerType<>(PackScreenHandler::new));
    public static ScreenHandlerType<PackBenchScreenHandler> PACK_BENCH_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER,new Identifier(MOD_ID,"pack_bench"),new ScreenHandlerType<>(PackBenchScreenHandler::new));


    public static RecipeType<PackBenchRecipe> PACK_BENCH_RECIPE = Registry.register(Registries.RECIPE_TYPE, new Identifier(MOD_ID, PackBenchRecipe.ID),PackBenchRecipe.TYPE);

    @Override
    public void onInitialize() {
        RegisterItem.init();
        RegisterBlock.init();
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_ID, PackBenchRecipe.ID), new PackBenchRecipeSerializer());
    }
}
