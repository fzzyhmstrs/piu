package fzzyhmstrs.pack_it_up.compat;

import dev.emi.emi.api.EmiApi;
import fzzyhmstrs.pack_it_up.compat.emi.EmiClientPlugin;
import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModCompatHelper {

    private static final Map<String,Integer> viewerHierarchy;

    static{
        Map<String,Integer> map = new HashMap<>();
        map.put("emi",1);
        map.put("roughlyenoughitems",2);
        map.put("jei",3);
        viewerHierarchy = map;
    }

    public static void handleClick(int offset){
        if (offset == 1){
            EmiApi.displayRecipeCategory(EmiClientPlugin.PACK_CATEGORY);
        }
    }

    public static int getScreenHandlerOffset(){
        for (Map.Entry<String,Integer> entry: viewerHierarchy.entrySet()){
            if(FabricLoader.getInstance().isModLoaded(entry.getKey())){
                return entry.getValue();
            }
        }
        return 0;
    }

    public static boolean isViewerSuperceded(String viewer){
        int ranking = viewerHierarchy.getOrDefault(viewer,-1);
        for (Map.Entry<String,Integer> entry: viewerHierarchy.entrySet()){
            if (!Objects.equals(entry.getKey(), viewer) && entry.getValue() < ranking && FabricLoader.getInstance().isModLoaded(entry.getKey())) return true;
        }
        return false;
    }

}
