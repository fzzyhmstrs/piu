package fzzyhmstrs.pack_it_up.registry;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.item.ArmoredPackItem;
import fzzyhmstrs.pack_it_up.item.BagItem;
import fzzyhmstrs.pack_it_up.item.FrameItem;
import fzzyhmstrs.pack_it_up.item.PackItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@SuppressWarnings("unused")
public class RegisterItem {




    //basic items
    public static final Item CLOTH = register("cloth",new Item(new Item.Settings()));
    public static final Item FRAME = register("frame",new FrameItem(new Item.Settings()));

    public static final Item BACKPACK_BAG = register("backpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item LARGE_BACKPACK_BAG = register("large_backpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item EXTREME_BACKPACK_BAG = register("extreme_backpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item BLOCKPACK_BAG = register("blockpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item OREPACK_BAG = register("orepack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item PLANTPACK_BAG = register("plantpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item TOOLPACK_BAG = register("toolpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item MAGICPACK_BAG = register("magicpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item LUNCHPACK_BAG = register("lunchpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item ENDERPACK_BAG = register("enderpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item IRON_ARMORPACK_BAG = register("iron_armorpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item GOLDEN_ARMORPACK_BAG = register("golden_armorpack_bag",new BagItem(new Item.Settings().maxCount(1)));
    public static final Item NETHERITE_ARMORPACK_BAG = register("netherite_armorpack_bag",new BagItem(new Item.Settings().maxCount(1)));

    public static final Item BACKPACK = register("backpack",new PackItem(new Item.Settings().maxCount(1).recipeRemainder(BACKPACK_BAG), PackItem.ModuleTier.PACK, PackItem.StackPredicate.ANY));
    public static final Item LARGE_BACKPACK = register("large_backpack",new PackItem(new Item.Settings().maxCount(1).recipeRemainder(LARGE_BACKPACK_BAG), PackItem.ModuleTier.BIG_PACK, PackItem.StackPredicate.ANY));
    public static final Item EXTREME_BACKPACK = register("extreme_backpack",new PackItem(new Item.Settings().maxCount(1).recipeRemainder(EXTREME_BACKPACK_BAG), PackItem.ModuleTier.NETHERITE, PackItem.StackPredicate.ANY));
    public static final Item BLOCKPACK = register("blockpack",new PackItem(new Item.Settings().maxCount(1).recipeRemainder(BLOCKPACK_BAG), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.BLOCK));
    public static final Item OREPACK = register("orepack",new PackItem(new Item.Settings().maxCount(1).recipeRemainder(OREPACK_BAG), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.ORE));
    public static final Item PLANTPACK = register("plantpack",new PackItem(new Item.Settings().maxCount(1).recipeRemainder(PLANTPACK_BAG), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.PLANTS));
    public static final Item TOOLPACK = register("toolpack",new PackItem(new Item.Settings().maxCount(1).recipeRemainder(TOOLPACK_BAG), PackItem.ModuleTier.TOOL, PackItem.StackPredicate.TOOL));
    public static final Item MAGICPACK = register("magicpack",new PackItem(new Item.Settings().maxCount(1).recipeRemainder(MAGICPACK_BAG), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.MAGIC));
    public static final Item LUNCHPACK = register("lunchpack",new PackItem(new Item.Settings().maxCount(1).recipeRemainder(LUNCHPACK_BAG), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.FOOD));
    public static final Item ENDERPACK = register("enderpack",new PackItem(new Item.Settings().maxCount(1).recipeRemainder(ENDERPACK_BAG), PackItem.ModuleTier.ENDER, PackItem.StackPredicate.BLOCK));
    public static final Item IRON_ARMORPACK = register("iron_armorpack",new ArmoredPackItem(ArmoredPackItem.Materials.IRON,new Item.Settings().maxCount(1).recipeRemainder(IRON_ARMORPACK_BAG), PackItem.ModuleTier.PACK, PackItem.StackPredicate.ANY));
    public static final Item GOLDEN_ARMORPACK = register("golden_armorpack",new ArmoredPackItem(ArmoredPackItem.Materials.GOLD,new Item.Settings().maxCount(1).recipeRemainder(GOLDEN_ARMORPACK_BAG), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.ANY));
    public static final Item NETHERITE_ARMORPACK = register("netherite_armorpack",new ArmoredPackItem(ArmoredPackItem.Materials.NETHERITE,new Item.Settings().maxCount(1).recipeRemainder(NETHERITE_ARMORPACK_BAG), PackItem.ModuleTier.BIG_PACK, PackItem.StackPredicate.ANY));

    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier(PIU.MOD_ID, "pack_group"))
            .displayName(Text.translatable("pack_it_up.name"))
            .icon(() -> new ItemStack(BACKPACK))
            .entries((enabledFeatures, entries, operatorEnabled) -> {
                entries.addAll(List.of(new ItemStack(CLOTH),
                        new ItemStack(FRAME),
                        new ItemStack(RegisterBlock.PACK_BENCH.asItem()),
                        new ItemStack(BACKPACK),
                        new ItemStack(BACKPACK_BAG),
                        new ItemStack(LARGE_BACKPACK),
                        new ItemStack(LARGE_BACKPACK_BAG),
                        new ItemStack(EXTREME_BACKPACK),
                        new ItemStack(EXTREME_BACKPACK_BAG),
                        new ItemStack(IRON_ARMORPACK),
                        new ItemStack(IRON_ARMORPACK_BAG),
                        new ItemStack(GOLDEN_ARMORPACK),
                        new ItemStack(GOLDEN_ARMORPACK_BAG),
                        new ItemStack(NETHERITE_ARMORPACK),
                        new ItemStack(NETHERITE_ARMORPACK_BAG),
                        new ItemStack(LUNCHPACK),
                        new ItemStack(LUNCHPACK_BAG),
                        new ItemStack(PLANTPACK),
                        new ItemStack(PLANTPACK_BAG),
                        new ItemStack(OREPACK),
                        new ItemStack(OREPACK_BAG),
                        new ItemStack(MAGICPACK),
                        new ItemStack(MAGICPACK_BAG),
                        new ItemStack(BLOCKPACK),
                        new ItemStack(BLOCKPACK_BAG),
                        new ItemStack(TOOLPACK),
                        new ItemStack(TOOLPACK_BAG),
                        new ItemStack(ENDERPACK),
                        new ItemStack(ENDERPACK_BAG)));
            })
            .build();

    private static Item register(String path, Item item){
        return Registry.register(Registries.ITEM,new Identifier(PIU.MOD_ID,path),item);
    }

    public static void init(){}
}
