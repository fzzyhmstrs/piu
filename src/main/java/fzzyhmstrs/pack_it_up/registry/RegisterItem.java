package fzzyhmstrs.pack_it_up.registry;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.item.BagItem;
import fzzyhmstrs.pack_it_up.item.PackItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unused")
public class RegisterItem {

    //basic items
    public static final Item CLOTH = register("cloth",new Item(new Item.Settings().group(ItemGroup.MATERIALS)));
    public static final Item FRAME = register("frame",new Item(new Item.Settings().group(ItemGroup.TRANSPORTATION)));

    public static final Item BACKPACK = register("backpack",new PackItem(new Item.Settings().group(ItemGroup.TRANSPORTATION), PackItem.ModuleTier.PACK, PackItem.StackPredicate.ANY));
    public static final Item LARGE_BACKPACK = register("large_backpack",new PackItem(new Item.Settings().group(ItemGroup.TRANSPORTATION), PackItem.ModuleTier.BIG_PACK, PackItem.StackPredicate.ANY));
    public static final Item EXTREME_BACKPACK = register("extreme_backpack",new PackItem(new Item.Settings().group(ItemGroup.TRANSPORTATION), PackItem.ModuleTier.NETHERITE, PackItem.StackPredicate.ANY));
    public static final Item BLOCKPACK = register("blockpack",new PackItem(new Item.Settings().group(ItemGroup.TRANSPORTATION), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.BLOCK));
    public static final Item OREPACK = register("orepack",new PackItem(new Item.Settings().group(ItemGroup.TRANSPORTATION), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.ORE));
    public static final Item PLANTPACK = register("plantpack",new PackItem(new Item.Settings().group(ItemGroup.TRANSPORTATION), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.PLANTS));
    public static final Item TOOLPACK = register("toolpack",new PackItem(new Item.Settings().group(ItemGroup.TRANSPORTATION), PackItem.ModuleTier.TOOL, PackItem.StackPredicate.TOOL));
    public static final Item MAGICPACK = register("magicpack",new PackItem(new Item.Settings().group(ItemGroup.TRANSPORTATION), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.MAGIC));
    public static final Item LUNCHPACK = register("lunchpack",new PackItem(new Item.Settings().group(ItemGroup.TRANSPORTATION), PackItem.ModuleTier.SPECIAL, PackItem.StackPredicate.FOOD));
    public static final Item ENDERPACK = register("enderpack",new PackItem(new Item.Settings().group(ItemGroup.TRANSPORTATION), PackItem.ModuleTier.ENDER, PackItem.StackPredicate.BLOCK));

    public static final Item BACKPACK_BAG = register("backpack_bag",new BagItem(new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item LARGE_BACKPACK_BAG = register("large_backpack_bag",new BagItem(new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item EXTREME_BACKPACK_BAG = register("extreme_backpack_bag",new BagItem(new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item BLOCKPACK_BAG = register("blockpack_bag",new BagItem(new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item OREPACK_BAG = register("orepack_bag",new BagItem(new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item PLANTPACK_BAG = register("plantpack_bag",new BagItem(new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item TOOLPACK_BAG = register("toolpack_bag",new BagItem(new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item MAGICPACK_BAG = register("magicpack_bag",new BagItem(new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item LUNCHPACK_BAG = register("lunchpack_bag",new BagItem(new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item ENDERPACK_BAG = register("enderpack_bag",new BagItem(new Item.Settings().group(ItemGroup.TRANSPORTATION)));

    private static Item register(String path, Item item){
        return Registry.register(Registry.ITEM,new Identifier(PIU.MOD_ID,path),item);
    }

    public static void init(){}
}
