package fzzyhmstrs.pack_it_up.item;

import net.minecraft.item.Item;

public class PackFrameItem extends Item {

    public PackFrameItem(PackModuleItem.ModuleTier maxTier,int maxMainModules, Settings settings) {
        super(settings);
        this.maxTier = maxTier;
        this.maxMainModules = maxMainModules;
    }

    private final PackModuleItem.ModuleTier maxTier;
    private final int maxMainModules;

    public PackModuleItem.ModuleTier getMaxTier(){
        return maxTier;
    }

    public int getMaxMainModules() {
        return maxMainModules;
    }
}
