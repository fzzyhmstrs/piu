package fzzyhmstrs.pack_it_up;


import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;

public class PIU implements ModInitializer {

    public static boolean DEBUG = false;
    public static String MOD_ID = "pack_it_up";
    public static Random piuRandom = new LocalRandom(System.currentTimeMillis());
    public static TagKey<Item> PLANT_ITEMS = TagKey.of(Registry.ITEM_KEY,new Identifier(MOD_ID,"plant_items"));
    public static TagKey<Item> MINING_ITEMS = TagKey.of(Registry.ITEM_KEY,new Identifier(MOD_ID,"mining_items"));
    public static TagKey<Item> GEMS = TagKey.of(Registry.ITEM_KEY,new Identifier("c","gems"));
    public static TagKey<Item> SEEDS = TagKey.of(Registry.ITEM_KEY,new Identifier("c","seeds"));

    @Override
    public void onInitialize() {



    }
}
