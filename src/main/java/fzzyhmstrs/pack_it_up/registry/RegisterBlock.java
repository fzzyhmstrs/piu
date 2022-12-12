package fzzyhmstrs.pack_it_up.registry;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.block.PackBenchBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegisterBlock {

    public static Block PACK_BENCH = register("pack_bench",new PackBenchBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5f).sounds(BlockSoundGroup.WOOD)));

    public static Block register(String path, Block block){
        Registry.register(Registry.ITEM,new Identifier(PIU.MOD_ID,path),new BlockItem(block, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        return Registry.register(Registry.BLOCK,new Identifier(PIU.MOD_ID,path),block);
    }

    public static void init(){}
}
