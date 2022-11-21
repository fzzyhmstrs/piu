package fzzyhmstrs.pack_it_up.registry;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.item.PackFrameItem;
import fzzyhmstrs.pack_it_up.item.PackModuleItem;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.item.*;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegisterItem {

    //frames
    public static final Item BASIC_FRAME = register("basic_frame", new PackFrameItem(PackModuleItem.ModuleTier.IRON,1, new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item TALL_BASIC_FRAME = register("tall_basic_frame", new PackFrameItem(PackModuleItem.ModuleTier.IRON,2, new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item ADVANCED_FRAME = register("advanced_frame", new PackFrameItem(PackModuleItem.ModuleTier.DIAMOND,1, new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item HIKING_FRAME = register("hiking_frame", new PackFrameItem(PackModuleItem.ModuleTier.GOLD,2, new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item EXTREME_FRAME = register("extreme_frame", new PackFrameItem(PackModuleItem.ModuleTier.NETHERITE,1, new Item.Settings().group(ItemGroup.TRANSPORTATION)));

    //cloth tier modules
    public static final Item CLOTH_POUCH = register("cloth_pouch",new PackModuleItem(PackModuleItem.ModuleSettings.simple(PackModuleItem.ModuleTier.CLOTH),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item GARDENING_POUCH = register("gardening_pouch",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.CLOTH,256,(stack)-> stack.isIn(PIU.PLANT_ITEMS)),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item FOOD_POUCH = register("food_pouch",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.CLOTH,256,(stack)-> stack.isIn(ConventionalItemTags.FOODS)),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item BLOCK_POUCH = register("block_pouch",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.CLOTH,256,(stack)-> stack.getItem() instanceof BlockItem),new Item.Settings().group(ItemGroup.TRANSPORTATION)));

    //iron tier modules
    public static final Item LARGE_POUCH = register("large_pouch",new PackModuleItem(PackModuleItem.ModuleSettings.simple(PackModuleItem.ModuleTier.IRON),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item LARGE_GARDENING_POUCH = register("large_gardening_pouch",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.IRON,256,(stack)-> stack.isIn(PIU.PLANT_ITEMS)),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item LARGE_FOOD_POUCH = register("large_food_pouch",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.IRON,256,(stack)-> stack.isIn(ConventionalItemTags.FOODS)),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item LARGE_BLOCK_POUCH = register("large_block_pouch",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.IRON,256,(stack)-> stack.getItem() instanceof BlockItem),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item ORE_POUCH = register("ore_pouch",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.IRON,256,(stack)-> stack.isIn(ConventionalItemTags.ORES)),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item TOOL_POUCH = register("tool_pouch",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.TOOL,1, ItemStack::isDamageable),new Item.Settings().group(ItemGroup.TRANSPORTATION)));

    //gold tier modules
    public static final Item SATCHEL = register("satchel",new PackModuleItem(PackModuleItem.ModuleSettings.simple(PackModuleItem.ModuleTier.GOLD),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item POTION_SATCHEL = register("potion_satchel",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.GOLD,4, (stack)-> stack.isIn(ConventionalItemTags.POTIONS)),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item ENCHANTERS_SATCHEL = register("enchanters_satchel",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.GOLD,4, (stack)-> stack.getItem() instanceof EnchantedBookItem),new Item.Settings().group(ItemGroup.TRANSPORTATION)));

    //diamond tier modules
    public static final Item BAG = register("bag",new PackModuleItem(PackModuleItem.ModuleSettings.simple(PackModuleItem.ModuleTier.DIAMOND),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item GARDENING_BAG = register("gardening_bag",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.DIAMOND,256,(stack)-> stack.isIn(PIU.PLANT_ITEMS)),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item FOOD_BAG = register("food_bag",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.DIAMOND,256,(stack)-> stack.isIn(ConventionalItemTags.FOODS)),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item BLOCK_BAG = register("block_bag",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.DIAMOND,256,(stack)-> stack.getItem() instanceof BlockItem),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item ORE_BAG = register("ore_bag",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.DIAMOND,256,(stack)-> stack.isIn(ConventionalItemTags.ORES)),new Item.Settings().group(ItemGroup.TRANSPORTATION)));
    public static final Item TOOL_BAG = register("tool_bag",new PackModuleItem(new PackModuleItem.ModuleSettings(PackModuleItem.ModuleTier.BIG_TOOL,1, ItemStack::isDamageable),new Item.Settings().group(ItemGroup.TRANSPORTATION)));

    //ender tier modules
    public static final Item ENDER_BAG = register("ender_bag",new PackModuleItem(PackModuleItem.ModuleSettings.simple(PackModuleItem.ModuleTier.ENDER),new Item.Settings().group(ItemGroup.TRANSPORTATION)));

    //netherite tier modules
    public static final Item LARGE_BAG = register("large_bag",new PackModuleItem(PackModuleItem.ModuleSettings.simple(PackModuleItem.ModuleTier.NETHERITE),new Item.Settings().group(ItemGroup.TRANSPORTATION)));

    private static Item register(String path, Item item){
        return Registry.register(Registry.ITEM,new Identifier(PIU.MOD_ID,path),item);
    }

}
