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

    public boolean checkTier(ModuleTier chk){
        return moduleSettings.tier.checkTier(chk);
    }

    public Predicate<ItemStack> getPredicate(){return moduleSettings.stackPredicate;}

    public int getModuleCapacity(boolean side){
        return side ? moduleSettings.tier.slots : moduleSettings.tier.sideSlots;
    }

    public int getMaxStackCount(){
        return moduleSettings.maxStack;
    }

    public ModuleTier getTier(){return moduleSettings.tier;}

    public record ModuleSettings(ModuleTier tier, int maxStack, Predicate<ItemStack> stackPredicate) {
        public ModuleSettings(ModuleTier tier, int maxStack){
            this(tier,maxStack,(stack)-> true);
        }

        public static ModuleSettings simple(ModuleTier tier){
            return new ModuleSettings(tier, 64);
        }
    }

    public enum ModuleTier{
        CLOTH(0,9,-1),
        IRON(1, 18, -1),
        TOOL(1,27,9),
        GOLD(2,27,9),
        DIAMOND(3,36,18),
        BIG_TOOL(3,54,27),
        ENDER(4,27,27),
        NETHERITE(5,54,27);

        private final int tier;
        private final int slots;
        private final int sideSlots;

        ModuleTier(int tier, int slots, int sideSlots){
            this.tier = tier;
            this.slots = slots;
            this.sideSlots = sideSlots;
        }

        public boolean suitableForSideSlot(){
            return sideSlots > 0;
        }

        public boolean checkTier(ModuleTier chk){
            return this.tier >= chk.tier;
        }

        public int getSlots(){
            return slots;
        }

        public int getSideSlots(){
            return sideSlots;
        }
    }
}
