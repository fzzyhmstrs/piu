package fzzyhmstrs.pack_it_up.item;

import net.minecraft.item.Item;

public class PackFrameItem extends Item {

    public PackFrameItem(int mainModules, int sideModules, int bottomModules,Settings settings) {
        super(settings);
        this.mainModules = mainModules;
        this.sideModules = sideModules;
        this.bottomModules = bottomModules;
    }

    private final int mainModules, sideModules, bottomModules;



    public int mainModulesSupported(){
        return mainModules;
    };

    public int sideModulesSupported(){
        return sideModules;
    };

    public int bottomModulesSupported(){
        return bottomModules;
    };

}
