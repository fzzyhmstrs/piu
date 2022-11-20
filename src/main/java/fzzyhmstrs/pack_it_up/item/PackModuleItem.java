package fzzyhmstrs.pack_it_up.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class PackModuleItem extends Item {
    public PackModuleItem(ModuleSettings moduleSettings,Settings settings) {
        super(settings);
        this.moduleSettings = moduleSettings;
    }

    private final ModuleSettings moduleSettings;




    public record ModuleSettings(int size, int width, int height, int maxStack, @Nullable Predicate<ItemStack> stackPredicate) {

        public static ModuleSettings simple(int size, int dim){
            return new ModuleSettings(size, dim, dim, 64, null);
        }
        public static ModuleSettings simple(int size, int width, int height){
            return new ModuleSettings(size, width, height, 64, null);
        }
    }
}
