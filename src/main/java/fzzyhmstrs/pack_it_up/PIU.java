package fzzyhmstrs.pack_it_up;


import fzzyhmstrs.pack_it_up.block.PackBenchScreenHandler;
import fzzyhmstrs.pack_it_up.item.ArmoredPackItem;
import fzzyhmstrs.pack_it_up.item.PackItem;
import fzzyhmstrs.pack_it_up.item.PackScreenHandler;
import fzzyhmstrs.pack_it_up.item.Packable;
import fzzyhmstrs.pack_it_up.recipe.PackBenchRecipe;
import fzzyhmstrs.pack_it_up.recipe.PackBenchRecipeSerializer;
import fzzyhmstrs.pack_it_up.registry.RegisterBlock;
import fzzyhmstrs.pack_it_up.registry.RegisterItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PIU implements ModInitializer {

    public static String MOD_ID = "pack_it_up";
    public static Identifier OPEN_BACKPACK = new Identifier(MOD_ID,"open_backpack");
    public static Identifier SOUND_BACKPACK = new Identifier(MOD_ID,"sound_backpack");

    public static TagKey<Item> PLANT_ITEMS = TagKey.of(Registry.ITEM_KEY,new Identifier(MOD_ID,"plant_items"));
    public static TagKey<Item> BACKPACKS = TagKey.of(Registry.ITEM_KEY,new Identifier(MOD_ID,"backpacks"));
    public static TagKey<Item> LARGE_BACKPACKS = TagKey.of(Registry.ITEM_KEY,new Identifier(MOD_ID,"large_backpacks"));
    public static TagKey<Item> SMALL_BACKPACKS = TagKey.of(Registry.ITEM_KEY,new Identifier(MOD_ID,"small_backpacks"));
    public static TagKey<Item> GEMS = TagKey.of(Registry.ITEM_KEY,new Identifier("c","gems"));
    public static TagKey<Item> SEEDS = TagKey.of(Registry.ITEM_KEY,new Identifier("c","seeds"));
    public static TagKey<Item> RAW_ORES = TagKey.of(Registry.ITEM_KEY,new Identifier("c","raw_ores"));

    public static ScreenHandlerType<PackScreenHandler> PACK_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MOD_ID,"pack"),new ExtendedScreenHandlerType<>(PackScreenHandler::new));
    public static ScreenHandlerType<PackBenchScreenHandler> PACK_BENCH_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER,new Identifier(MOD_ID,"pack_bench"),new ScreenHandlerType<>(PackBenchScreenHandler::new));


    public static RecipeType<PackBenchRecipe> PACK_BENCH_RECIPE = Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, PackBenchRecipe.ID),PackBenchRecipe.TYPE);

    @Override
    public void onInitialize() {
        RegisterItem.init();
        RegisterBlock.init();
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, PackBenchRecipe.ID), new PackBenchRecipeSerializer());
        ServerPlayNetworking.registerGlobalReceiver(OPEN_BACKPACK,(server,player,handler,buf,responseSender)-> {
            if (player.currentScreenHandler == player.playerScreenHandler){
                PlayerInventory inventory = player.getInventory();
                ItemStack chest = inventory.armor.get(EquipmentSlot.CHEST.getEntitySlotId());
                if (chest.getItem() instanceof ArmoredPackItem armoredPackItem){
                    PacketByteBuf buf2 = PacketByteBufs.create();
                    buf2.writeBoolean(true);
                    ServerPlayNetworking.send(player,SOUND_BACKPACK,buf2);
                    armoredPackItem.openPackScreenHandler(player, chest);
                }  else {
                    for (ItemStack stack : inventory.main){
                        if (stack.getItem() instanceof Packable packableItem){
                            PacketByteBuf buf2 = PacketByteBufs.create();
                            buf2.writeBoolean(true);
                            ServerPlayNetworking.send(player,SOUND_BACKPACK,buf2);
                            packableItem.openPackScreenHandler(player,stack);
                        }
                    }
                }
            }
        });
    }
}
